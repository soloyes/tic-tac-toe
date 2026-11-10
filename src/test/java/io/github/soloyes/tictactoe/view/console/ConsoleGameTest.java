package io.github.soloyes.tictactoe.view.console;

import io.github.soloyes.tictactoe.controller.GameSettings;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleGameTest {

    @Test
    void parsesMoves() {
        assertArrayEquals(new int[]{1, 2}, ConsoleGame.parseMove("2 3", 3));
        assertArrayEquals(new int[]{1, 2}, ConsoleGame.parseMove(" 2,3 ", 3));
        assertArrayEquals(new int[]{3, 0}, ConsoleGame.parseMove("41", 4));
        assertNull(ConsoleGame.parseMove("4 1", 3));
        assertNull(ConsoleGame.parseMove("0 1", 3));
        assertNull(ConsoleGame.parseMove("a b", 3));
        assertNull(ConsoleGame.parseMove("", 3));
    }

    @Test
    void playsAFullGameAgainstHardAndEndsInDraw() {
        // Settings: 3x3, computer first, hard. Human then tries every cell in order;
        // taken cells are rejected and the next line is read. Hard vs anything never loses.
        StringBuilder script = new StringBuilder("3\ncomputer\nhard\n");
        for (int round = 0; round < 5; round++) {
            for (int r = 1; r <= 3; r++) {
                for (int c = 1; c <= 3; c++) {
                    script.append(r).append(' ').append(c).append('\n');
                }
            }
        }
        String output = run(script.toString());
        assertTrue(output.contains("Computer plays"), output);
        assertTrue(output.contains("Computer wins.") || output.contains("Draw."), output);
        assertTrue(output.contains("That cell is taken."), output);
    }

    @Test
    void quitsOnQ() {
        String output = run("q\n");
        assertTrue(output.contains("console mode"));
    }

    private static String run(String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        new ConsoleGame(new BufferedReader(new StringReader(input)), out, new Random(1)).run(GameSettings.DEFAULT);
        return bytes.toString(StandardCharsets.UTF_8);
    }
}
