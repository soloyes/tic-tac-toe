package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.controller.FirstPlayer;
import io.github.soloyes.tictactoe.controller.GameController;
import io.github.soloyes.tictactoe.controller.GameSettings;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Runs headless: the panels are lightweight and can be laid out and painted offscreen. */
class BoardPanelTest {

    private static GameController game(int size) {
        return new GameController(new GameSettings(size, FirstPlayer.HUMAN, Difficulty.HARD), new Random(1));
    }

    @Test
    void mapsMouseToRowAndColumn() {
        BoardPanel panel = new BoardPanel(game(3));
        panel.setSize(340, 340); // board = 300x300 at (20, 20), cells of 100px
        assertArrayEquals(new int[]{0, 0}, panel.cellAt(new Point(25, 25)));
        assertArrayEquals(new int[]{0, 2}, panel.cellAt(new Point(310, 30)), "x picks the column");
        assertArrayEquals(new int[]{2, 0}, panel.cellAt(new Point(30, 310)), "y picks the row");
        assertArrayEquals(new int[]{1, 2}, panel.cellAt(new Point(250, 150)));
        assertNull(panel.cellAt(new Point(5, 100)));
        assertNull(panel.cellAt(new Point(100, 330)));
    }

    @Test
    void mapsMouseOnWideComponentWith4x4() {
        BoardPanel panel = new BoardPanel(game(4));
        panel.setSize(600, 440); // board = 400x400 centred at (100, 20), cells of 100px
        assertArrayEquals(new int[]{0, 0}, panel.cellAt(new Point(101, 21)));
        assertArrayEquals(new int[]{3, 1}, panel.cellAt(new Point(250, 390)));
        assertNull(panel.cellAt(new Point(50, 200)));
    }

    @Test
    void statusFollowsTheGame() {
        GameController g = game(3);
        GamePanel panel = new GamePanel(g);
        panel.stopTimers();
        StatusBar status = panel.statusBar();
        assertEquals("Your turn", status.message());
        g.playHumanMove(0, 0);
        panel.stopTimers();
        assertEquals("Computer is thinking…", status.message());
    }

    @Test
    void paintsWithoutErrors() {
        GameController g = game(3);
        GamePanel panel = new GamePanel(g);
        panel.stopTimers();
        BufferedImage img = PanelRenderer.render(panel, 560, 680, 1);
        assertEquals(560, img.getWidth());
        assertEquals(Theme.BACKGROUND.getRGB(), img.getRGB(2, 2), "dark background");
    }
}
