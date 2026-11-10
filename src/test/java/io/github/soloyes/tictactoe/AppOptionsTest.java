package io.github.soloyes.tictactoe;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.controller.FirstPlayer;
import io.github.soloyes.tictactoe.controller.GameSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppOptionsTest {

    @Test
    void defaultsToGuiWithDefaultSettings() {
        App.Options o = App.Options.parse();
        assertFalse(o.console());
        assertEquals(GameSettings.DEFAULT, o.settings());
    }

    @Test
    void parsesAllOptions() {
        App.Options o = App.Options.parse("--console", "--size=4", "--first=computer", "--level=hard");
        assertTrue(o.console());
        assertEquals(new GameSettings(4, FirstPlayer.COMPUTER, Difficulty.HARD), o.settings());
    }

    @Test
    void rejectsUnknownOptions() {
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("--size=5"));
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("--level=godlike"));
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("--fullscreen"));
    }
}
