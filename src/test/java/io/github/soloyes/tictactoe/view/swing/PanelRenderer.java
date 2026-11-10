package io.github.soloyes.tictactoe.view.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/** Paints a lightweight Swing component tree into an image, without a window or a display. */
final class PanelRenderer {
    private PanelRenderer() {
    }

    static BufferedImage render(Container root, int width, int height, int scale) {
        root.setSize(width, height);
        layout(root);
        BufferedImage image = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            g.scale(scale, scale);
            root.paint(g);
        } finally {
            g.dispose();
        }
        return image;
    }

    /** {@code validate()} needs a native peer, so lay the tree out by hand. */
    private static void layout(Component c) {
        if (c instanceof Container container) {
            container.doLayout();
            for (Component child : container.getComponents()) {
                layout(child);
            }
        }
    }
}
