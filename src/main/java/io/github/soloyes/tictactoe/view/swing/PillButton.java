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

/** A flat accent-coloured button with rounded ends. */
final class PillButton extends JComponent {
    private final String text;
    private final Runnable action;
    private boolean hover;
    private boolean pressed;

    PillButton(String text, Runnable action) {
        this.text = text;
        this.action = action;
        setFont(Theme.font(Font.BOLD, 13f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boolean fire = pressed && contains(e.getPoint());
                pressed = false;
                repaint();
                if (fire) {
                    action.run();
                }
            }
        };
        addMouseListener(mouse);
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(text) + 36, 34);
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
        int w = getWidth();
        int h = getHeight();
        g.setColor(pressed ? Theme.ACCENT.darker() : hover ? Theme.ACCENT.brighter() : Theme.ACCENT);
        g.fill(new RoundRectangle2D.Double(0, 0, w, h, h, h));
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Theme.BACKGROUND);
        g.drawString(text, (w - fm.stringWidth(text)) / 2f, (h - fm.getHeight()) / 2f + fm.getAscent());
        g.dispose();
    }
}
