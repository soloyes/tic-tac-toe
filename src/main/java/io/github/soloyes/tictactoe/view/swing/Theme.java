package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.model.Mark;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Colours, fonts and rendering hints of the dark UI. */
final class Theme {
    static final Color BACKGROUND = new Color(0x12141A);
    static final Color PLATE = new Color(0x191C24);
    static final Color TILE = new Color(0x222631);
    static final Color TILE_HOVER = new Color(0x2C3241);
    static final Color BORDER = new Color(0x2A2F3B);
    static final Color TEXT = new Color(0xE7E9EF);
    static final Color TEXT_MUTED = new Color(0x8B91A1);
    static final Color ACCENT = new Color(0x7C8CFF);
    static final Color SEGMENT_BG = new Color(0x1C1F28);
    static final Color SEGMENT_SELECTED = new Color(0x343A4C);

    static final Color X_COLOR = new Color(0x4CC9F0);
    static final Color O_COLOR = new Color(0xF7A859);

    private static final String FAMILY = pickFamily();

    private Theme() {
    }

    static Color colorOf(Mark mark) {
        return mark == Mark.X ? X_COLOR : O_COLOR;
    }

    static Color withAlpha(Color c, float alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.round(alpha * 255));
    }

    /** Solid colour {@code amount} of the way from {@code base} to {@code c}; avoids alpha overlap in crossing strokes. */
    static Color blend(Color c, Color base, float amount) {
        return new Color(
                Math.round(base.getRed() + (c.getRed() - base.getRed()) * amount),
                Math.round(base.getGreen() + (c.getGreen() - base.getGreen()) * amount),
                Math.round(base.getBlue() + (c.getBlue() - base.getBlue()) * amount));
    }

    static Font font(int style, float size) {
        return new Font(FAMILY, style, 12).deriveFont(size);
    }

    static void antialias(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private static String pickFamily() {
        try {
            Set<String> available = new HashSet<>(Arrays.asList(
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
            for (String f : new String[]{"Inter", "Segoe UI", "Helvetica Neue", "Ubuntu", "Cantarell", "DejaVu Sans"}) {
                if (available.contains(f)) {
                    return f;
                }
            }
        } catch (RuntimeException ignored) {
            // fall through to the logical font
        }
        return Font.SANS_SERIF;
    }
}
