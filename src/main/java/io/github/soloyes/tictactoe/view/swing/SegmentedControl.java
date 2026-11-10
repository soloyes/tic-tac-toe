package io.github.soloyes.tictactoe.view.swing;

import javax.swing.JComponent;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/** A row of mutually exclusive options drawn as one rounded pill, like a native segmented control. */
final class SegmentedControl<T> extends JComponent {
    private static final int PAD_X = 13;
    private static final int HEIGHT = 32;

    private final List<T> options;
    private final Function<T, String> label;
    private Consumer<T> onSelect = t -> { };
    private int selected;
    private int hover = -1;

    SegmentedControl(List<T> options, Function<T, String> label, T initial) {
        this.options = List.copyOf(options);
        this.label = label;
        this.selected = Math.max(0, this.options.indexOf(initial));
        setFont(Theme.font(Font.PLAIN, 13f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                setHover(segmentAt(e.getX()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHover(-1);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int i = segmentAt(e.getX());
                if (i >= 0 && i != selected) {
                    selected = i;
                    repaint();
                    onSelect.accept(options.get(i));
                }
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    void onSelect(Consumer<T> handler) {
        this.onSelect = Objects.requireNonNull(handler);
    }

    T selected() {
        return options.get(selected);
    }

    void setSelected(T value) {
        int i = options.indexOf(value);
        if (i >= 0 && i != selected) {
            selected = i;
            repaint();
        }
    }

    private void setHover(int i) {
        if (i != hover) {
            hover = i;
            repaint();
        }
    }

    /** Natural width of each segment: its bold label plus padding. */
    private int[] naturalWidths() {
        FontMetrics fm = getFontMetrics(getFont().deriveFont(Font.BOLD));
        int[] w = new int[options.size()];
        for (int i = 0; i < w.length; i++) {
            w[i] = fm.stringWidth(label.apply(options.get(i))) + 2 * PAD_X;
        }
        return w;
    }

    /** Left edges of the segments plus the right edge of the last one, scaled to the current width. */
    private double[] edges() {
        int[] natural = naturalWidths();
        int total = 0;
        for (int w : natural) {
            total += w;
        }
        double k = (getWidth() - 4) / (double) Math.max(1, total);
        double[] edges = new double[natural.length + 1];
        edges[0] = 2;
        for (int i = 0; i < natural.length; i++) {
            edges[i + 1] = edges[i] + natural[i] * k;
        }
        return edges;
    }

    private int segmentAt(int x) {
        double[] e = edges();
        for (int i = 0; i < options.size(); i++) {
            if (x >= e[i] && x < e[i + 1]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Dimension getPreferredSize() {
        int total = 4;
        for (int w : naturalWidths()) {
            total += w;
        }
        return new Dimension(total, HEIGHT);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        Theme.antialias(g);
        int h = getHeight();
        int w = getWidth();
        g.setColor(Theme.SEGMENT_BG);
        g.fill(new RoundRectangle2D.Double(0, 0, w, h, h, h));
        g.setColor(Theme.BORDER);
        g.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, h, h));

        double[] e = edges();
        for (int i = 0; i < options.size(); i++) {
            double x = e[i];
            double sw = e[i + 1] - e[i];
            if (i == selected) {
                g.setColor(Theme.SEGMENT_SELECTED);
                g.fill(new RoundRectangle2D.Double(x, 2, sw, h - 4, h - 4, h - 4));
            } else if (i == hover) {
                g.setColor(Theme.withAlpha(Theme.SEGMENT_SELECTED, 0.45f));
                g.fill(new RoundRectangle2D.Double(x, 2, sw, h - 4, h - 4, h - 4));
            }
            Font f = i == selected ? getFont().deriveFont(Font.BOLD) : getFont();
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            String text = label.apply(options.get(i));
            float tx = (float) (x + (sw - fm.stringWidth(text)) / 2);
            float ty = (h - fm.getHeight()) / 2f + fm.getAscent();
            g.setColor(i == selected ? Theme.TEXT : Theme.TEXT_MUTED);
            g.drawString(text, tx, ty);
        }
        g.dispose();
    }
}
