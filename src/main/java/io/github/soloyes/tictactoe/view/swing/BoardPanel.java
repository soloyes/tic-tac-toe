package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.controller.GameController;
import io.github.soloyes.tictactoe.model.Line;
import io.github.soloyes.tictactoe.model.Mark;
import io.github.soloyes.tictactoe.model.Outcome;

import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

/**
 * The board itself: square tiles centred in the component, marks, hover highlight and
 * the winning line. Clicks are translated to (row, col) and sent to the controller.
 */
final class BoardPanel extends JComponent {
    private static final int MARGIN = 20;

    private final GameController game;
    private int hoverRow = -1;
    private int hoverCol = -1;

    BoardPanel(GameController game) {
        this.game = game;
        setOpaque(true);
        setPreferredSize(new Dimension(480, 480));
        setMinimumSize(new Dimension(260, 260));
        game.addListener(g -> repaint());

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                setHover(cellAt(e.getPoint()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHover(null);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int[] rc = cellAt(e.getPoint());
                if (rc != null) {
                    game.playHumanMove(rc[0], rc[1]);
                }
                setHover(cellAt(e.getPoint()));
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    /** Sets the hovered cell; {@code null} clears it. Package-private for the screenshot tool. */
    void setHover(int[] rc) {
        int r = rc == null ? -1 : rc[0];
        int c = rc == null ? -1 : rc[1];
        if (r != hoverRow || c != hoverCol) {
            hoverRow = r;
            hoverCol = c;
            repaint();
        }
        boolean clickable = rc != null && game.isHumanTurn() && game.isEmptyCell(r, c);
        setCursor(Cursor.getPredefinedCursor(clickable ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
    }

    /** Geometry of the board inside the component, recomputed from the current size. */
    private record Geometry(double x, double y, double side, double cell) {
    }

    private Geometry geometry() {
        int n = game.boardSize();
        double side = Math.max(0, Math.min(getWidth(), getHeight()) - 2.0 * MARGIN);
        double x = (getWidth() - side) / 2;
        double y = (getHeight() - side) / 2;
        return new Geometry(x, y, side, side / n);
    }

    /**
     * Maps a point to {row, col}: x selects the column, y selects the row.
     * Returns {@code null} outside the board.
     */
    int[] cellAt(Point p) {
        Geometry g = geometry();
        if (g.cell() <= 0) {
            return null;
        }
        double dx = p.x - g.x();
        double dy = p.y - g.y();
        if (dx < 0 || dy < 0 || dx >= g.side() || dy >= g.side()) {
            return null;
        }
        int n = game.boardSize();
        int col = Math.min(n - 1, (int) (dx / g.cell()));
        int row = Math.min(n - 1, (int) (dy / g.cell()));
        return new int[]{row, col};
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            Theme.antialias(g);
            g.setColor(Theme.BACKGROUND);
            g.fillRect(0, 0, getWidth(), getHeight());
            paintBoard(g);
        } finally {
            g.dispose();
        }
    }

    private void paintBoard(Graphics2D g) {
        Geometry geo = geometry();
        int n = game.boardSize();
        double cell = geo.cell();
        double gap = Math.max(4, cell * 0.07);
        double arc = cell * 0.22;
        Outcome outcome = game.outcome();
        Line winLine = outcome.isWin() ? outcome.winningLine() : null;

        // plate behind the tiles
        double pad = gap;
        g.setColor(Theme.PLATE);
        g.fill(new RoundRectangle2D.Double(geo.x() - pad, geo.y() - pad,
                geo.side() + 2 * pad, geo.side() + 2 * pad, arc * 1.2, arc * 1.2));

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int index = r * n + c;
                double tx = geo.x() + c * cell + gap / 2;
                double ty = geo.y() + r * cell + gap / 2;
                double ts = cell - gap;
                RoundRectangle2D tile = new RoundRectangle2D.Double(tx, ty, ts, ts, arc, arc);

                Mark mark = game.markAt(r, c);
                boolean hovered = r == hoverRow && c == hoverCol;
                boolean canPlay = mark == null && game.isHumanTurn();

                g.setColor(hovered && canPlay ? Theme.TILE_HOVER : Theme.TILE);
                g.fill(tile);
                if (winLine != null && winLine.contains(index)) {
                    g.setColor(Theme.withAlpha(Theme.colorOf(outcome.winner()), 0.16f));
                    g.fill(tile);
                }

                double cx = tx + ts / 2;
                double cy = ty + ts / 2;
                if (mark != null) {
                    float alpha = 1f;
                    if (winLine != null && !winLine.contains(index)) {
                        alpha = 0.32f;
                    } else if (outcome.isDraw()) {
                        alpha = 0.6f;
                    }
                    MarkPainter.paint(g, mark, cx, cy, ts, Theme.blend(Theme.colorOf(mark), Theme.TILE, alpha));
                } else if (hovered && canPlay) {
                    Color ghost = Theme.blend(Theme.colorOf(game.humanMark()), Theme.TILE_HOVER, 0.25f);
                    MarkPainter.paint(g, game.humanMark(), cx, cy, ts, ghost);
                }
            }
        }

        if (winLine != null) {
            paintWinLine(g, geo, winLine, Theme.colorOf(outcome.winner()));
        }
    }

    private void paintWinLine(Graphics2D g, Geometry geo, Line line, Color color) {
        int n = game.boardSize();
        double cell = geo.cell();
        double x1 = geo.x() + (line.first() % n + 0.5) * cell;
        double y1 = geo.y() + (line.first() / n + 0.5) * cell;
        double x2 = geo.x() + (line.last() % n + 0.5) * cell;
        double y2 = geo.y() + (line.last() / n + 0.5) * cell;
        double len = Math.hypot(x2 - x1, y2 - y1);
        double ext = cell * 0.32;
        double ux = (x2 - x1) / len;
        double uy = (y2 - y1) / len;
        Line2D seg = new Line2D.Double(x1 - ux * ext, y1 - uy * ext, x2 + ux * ext, y2 + uy * ext);

        float w = (float) Math.max(3, cell * 0.06);
        g.setColor(Theme.withAlpha(color, 0.18f));
        g.setStroke(new BasicStroke(w * 3.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(seg);
        g.setColor(Theme.withAlpha(color, 0.30f));
        g.setStroke(new BasicStroke(w * 2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(seg);
        g.setColor(Theme.withAlpha(Color.WHITE, 0.92f));
        g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(seg);
    }
}
