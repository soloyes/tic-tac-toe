package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.controller.GameController;
import io.github.soloyes.tictactoe.controller.Score;
import io.github.soloyes.tictactoe.model.Mark;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/** Bottom bar: whose turn it is or how the game ended, plus the session score. */
final class StatusBar extends JComponent {
    private static final int HEIGHT = 52;

    private final GameController game;

    StatusBar(GameController game) {
        this.game = game;
        game.addListener(g -> repaint());
    }

    /** The main status message, also used by tests. */
    String message() {
        if (game.humanWon()) {
            return "You win!";
        }
        if (game.computerWon()) {
            return "Computer wins";
        }
        if (game.outcome().isDraw()) {
            return "Draw";
        }
        return game.isHumanTurn() ? "Your turn" : "Computer is thinking…";
    }

    private Mark iconMark() {
        if (game.outcome().isWin()) {
            return game.outcome().winner();
        }
        if (game.outcome().isDraw()) {
            return null;
        }
        return game.currentMark();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        Theme.antialias(g);
        int w = getWidth();
        int h = getHeight();
        g.setColor(Theme.PLATE);
        g.fillRect(0, 0, w, h);
        g.setColor(Theme.BORDER);
        g.draw(new Line2D.Double(0, 0.5, w, 0.5));

        int x = 22;
        double cy = h / 2.0;
        Mark icon = iconMark();
        if (icon != null) {
            MarkPainter.paint(g, icon, x + 10, cy, 26, Theme.colorOf(icon));
        } else {
            MarkPainter.paint(g, Mark.X, x + 4, cy, 20, Theme.X_COLOR);
            MarkPainter.paint(g, Mark.O, x + 18, cy, 20, Theme.O_COLOR);
        }
        x += 34;

        boolean over = game.isOver();
        g.setFont(Theme.font(Font.BOLD, over ? 16f : 15f));
        FontMetrics fm = g.getFontMetrics();
        float baseline = (float) (cy - fm.getHeight() / 2.0 + fm.getAscent());
        String msg = message();
        g.setColor(over && icon != null ? Theme.colorOf(icon) : Theme.TEXT);
        g.drawString(msg, x, baseline);
        x += fm.stringWidth(msg);

        if (!over) {
            g.setFont(Theme.font(Font.PLAIN, 13f));
            FontMetrics small = g.getFontMetrics();
            g.setColor(Theme.TEXT_MUTED);
            String hint = "  ·  you are " + game.humanMark();
            g.drawString(hint, x, (float) (cy - small.getHeight() / 2.0 + small.getAscent()));
        }

        Score s = game.score();
        drawScore(g, w - 22, cy, s);
        g.dispose();
    }

    private void drawScore(Graphics2D g, int right, double cy, Score s) {
        String[][] parts = {
                {"You", String.valueOf(s.humanWins())},
                {"Draw", String.valueOf(s.draws())},
                {"CPU", String.valueOf(s.computerWins())},
        };
        Font labelFont = Theme.font(Font.PLAIN, 12f);
        Font numFont = Theme.font(Font.BOLD, 14f);
        int x = right;
        for (int i = parts.length - 1; i >= 0; i--) {
            g.setFont(numFont);
            FontMetrics nfm = g.getFontMetrics();
            int nw = nfm.stringWidth(parts[i][1]);
            x -= nw;
            g.setColor(Theme.TEXT);
            g.drawString(parts[i][1], x, (float) (cy - nfm.getHeight() / 2.0 + nfm.getAscent()));
            g.setFont(labelFont);
            FontMetrics lfm = g.getFontMetrics();
            String lbl = parts[i][0] + " ";
            x -= lfm.stringWidth(lbl);
            g.setColor(Theme.TEXT_MUTED);
            g.drawString(lbl, x, (float) (cy - lfm.getHeight() / 2.0 + lfm.getAscent()));
            x -= 16;
        }
    }
}
