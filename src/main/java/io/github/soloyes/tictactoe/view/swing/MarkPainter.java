package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.model.Mark;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/** Draws X as two round-capped strokes and O as a ring. */
final class MarkPainter {
    private MarkPainter() {
    }

    /**
     * Paints {@code mark} centred at {@code (cx, cy)} inside a box of side {@code size}.
     */
    static void paint(Graphics2D g, Mark mark, double cx, double cy, double size, Color color) {
        float stroke = (float) Math.max(2, size * 0.13);
        g.setColor(color);
        g.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (mark == Mark.X) {
            double h = size * 0.30;
            g.draw(new Line2D.Double(cx - h, cy - h, cx + h, cy + h));
            g.draw(new Line2D.Double(cx - h, cy + h, cx + h, cy - h));
        } else {
            double r = size * 0.30;
            g.draw(new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r));
        }
    }
}
