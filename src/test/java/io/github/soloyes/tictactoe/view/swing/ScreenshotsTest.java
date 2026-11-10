package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.ai.MoveStrategy;
import io.github.soloyes.tictactoe.controller.FirstPlayer;
import io.github.soloyes.tictactoe.controller.GameController;
import io.github.soloyes.tictactoe.controller.GameSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Renders the real game panel into the PNGs used by the README. Skipped in normal builds; run with
 * {@code mvn test -Dtest=ScreenshotsTest -Dscreenshots=docs}.
 */
@EnabledIfSystemProperty(named = "screenshots", matches = ".+")
class ScreenshotsTest {
    private static final int W = 580;
    private static final int H = 700;
    private static final int SCALE = 2;

    private final Path dir = Path.of(System.getProperty("screenshots", "docs"));

    /** 3x3, human X to move, hovering a free cell. */
    @Test
    void midGame() throws IOException {
        GameController g = scripted(new GameSettings(3, FirstPlayer.HUMAN, Difficulty.HARD),
                new int[][]{{1, 1}, {0, 1}});
        human(g, 0, 0);
        computer(g, 1, 1);
        human(g, 2, 2);
        computer(g, 0, 1);
        GamePanel panel = new GamePanel(g);
        panel.stopTimers();
        panel.boardPanel().setHover(new int[]{2, 1});
        save(panel, "screenshot-3x3-playing.png");
    }

    /** 4x4 won by the human along the anti-diagonal. */
    @Test
    void wonGame() throws IOException {
        int[][] human = {{0, 3}, {1, 2}, {2, 1}, {3, 0}};
        int[][] cpu = {{1, 1}, {0, 0}, {2, 2}};
        GameController g = scripted(new GameSettings(4, FirstPlayer.HUMAN, Difficulty.NORMAL), cpu);
        for (int i = 0; i < 4; i++) {
            human(g, human[i][0], human[i][1]);
            if (i < 3) {
                computer(g, cpu[i][0], cpu[i][1]);
            }
        }
        assertTrue(g.humanWon());
        GamePanel panel = new GamePanel(g);
        panel.stopTimers();
        save(panel, "screenshot-4x4-won.png");
    }

    private static void human(GameController g, int r, int c) {
        assertTrue(g.playHumanMove(r, c));
    }

    private static void computer(GameController g, int r, int c) {
        g.playComputerMove();
        assertEquals(r * g.boardSize() + c, g.lastMove());
    }

    /** Computer that plays the given cells in order, so the screenshots show a fixed position. */
    private static GameController scripted(GameSettings settings, int[][] cells) {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int[] rc : cells) {
            queue.add(rc[0] * settings.boardSize() + rc[1]);
        }
        MoveStrategy script = (board, me) -> queue.removeFirst();
        return new GameController(settings, script);
    }

    private void save(GamePanel panel, String name) throws IOException {
        Files.createDirectories(dir);
        File out = dir.resolve(name).toFile();
        ImageIO.write(PanelRenderer.render(panel, W, H, SCALE), "png", out);
        assertTrue(out.length() > 0);
    }
}
