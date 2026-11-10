package io.github.soloyes.tictactoe.controller;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.model.Mark;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameControllerTest {

    private static GameController game(int size, FirstPlayer first, Difficulty level) {
        return new GameController(new GameSettings(size, first, level), new Random(5));
    }

    @Test
    void humanOpensWithX() {
        GameController g = game(3, FirstPlayer.HUMAN, Difficulty.HARD);
        assertEquals(Mark.X, g.humanMark());
        assertTrue(g.isHumanTurn());
        assertTrue(g.playHumanMove(1, 2));
        assertEquals(Mark.X, g.markAt(1, 2));
        assertTrue(g.isComputerTurn());
    }

    @Test
    void computerOpensWithX() {
        GameController g = game(4, FirstPlayer.COMPUTER, Difficulty.HARD);
        assertEquals(Mark.O, g.humanMark());
        assertTrue(g.isComputerTurn());
        assertFalse(g.playHumanMove(0, 0), "human must wait for the computer");
        int cell = g.playComputerMove();
        assertEquals(Mark.X, g.board().get(cell));
        assertTrue(g.isHumanTurn());
    }

    @Test
    void rejectsTakenCellsAndOutOfRange() {
        GameController g = game(3, FirstPlayer.HUMAN, Difficulty.EASY);
        g.playHumanMove(0, 0);
        g.playComputerMove();
        int computerCell = g.lastMove();
        assertFalse(g.playHumanMove(computerCell / 3, computerCell % 3));
        assertFalse(g.playHumanMove(3, 0));
        assertFalse(g.playHumanMove(-1, 0));
        assertTrue(g.isHumanTurn());
    }

    @Test
    void humanWinIsCreditedToHumanAndStopsTheGame() {
        // Easy, seeded so that the computer never blocks the top row.
        GameController g = game(3, FirstPlayer.HUMAN, Difficulty.EASY);
        // Play row 0 left to right; if the computer blocks, restart with another seed.
        for (long seed = 0; seed < 200; seed++) {
            g = new GameController(new GameSettings(3, FirstPlayer.HUMAN, Difficulty.EASY), new Random(seed));
            boolean blocked = false;
            for (int c = 0; c < 3 && !g.isOver(); c++) {
                if (!g.playHumanMove(0, c)) {
                    blocked = true;
                    break;
                }
                if (g.isComputerTurn()) {
                    g.playComputerMove();
                }
            }
            if (!blocked && g.humanWon()) {
                break;
            }
        }
        assertTrue(g.humanWon());
        assertFalse(g.computerWon());
        assertTrue(g.isOver());
        assertFalse(g.isComputerTurn(), "computer must not move after the game is won");
        assertFalse(g.isHumanTurn());
        assertEquals(new Score(1, 0, 0), g.score());
        GameController finished = g;
        assertThrows(IllegalStateException.class, finished::playComputerMove);
        assertFalse(g.playHumanMove(2, 2));
    }

    @Test
    void computerWinIsCreditedToComputer() {
        GameController g = game(3, FirstPlayer.COMPUTER, Difficulty.HARD);
        // Human plays the weakest possible moves: always the first free cell.
        while (!g.isOver()) {
            if (g.isComputerTurn()) {
                g.playComputerMove();
            } else {
                int cell = g.board().emptyCells()[0];
                g.playHumanMove(cell / 3, cell % 3);
            }
        }
        assertTrue(g.computerWon());
        assertEquals(Mark.X, g.outcome().winner());
        assertEquals(new Score(0, 1, 0), g.score());
    }

    @Test
    void newGameKeepsScoreAndAppliesSettings() {
        GameController g = game(3, FirstPlayer.COMPUTER, Difficulty.HARD);
        while (!g.isOver()) {
            if (g.isComputerTurn()) {
                g.playComputerMove();
            } else {
                int cell = g.board().emptyCells()[0];
                g.playHumanMove(cell / 3, cell % 3);
            }
        }
        g.newGame(g.settings().withBoardSize(4).withFirstPlayer(FirstPlayer.HUMAN));
        assertEquals(4, g.boardSize());
        assertTrue(g.isHumanTurn());
        assertEquals(0, g.board().filledCount());
        assertEquals(1, g.score().computerWins());
    }

    @Test
    void listenersAreNotified() {
        GameController g = game(3, FirstPlayer.HUMAN, Difficulty.NORMAL);
        AtomicInteger calls = new AtomicInteger();
        g.addListener(x -> calls.incrementAndGet());
        g.playHumanMove(1, 1);
        g.playComputerMove();
        g.setDifficulty(Difficulty.EASY);
        g.newGame();
        assertEquals(4, calls.get());
        assertEquals(Difficulty.EASY, g.settings().difficulty());
    }

    @Test
    void boardAccessorReturnsACopy() {
        GameController g = game(3, FirstPlayer.HUMAN, Difficulty.NORMAL);
        g.board().place(0, Mark.O);
        assertTrue(g.isEmptyCell(0, 0));
    }
}
