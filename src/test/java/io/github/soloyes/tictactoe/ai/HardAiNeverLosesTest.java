package io.github.soloyes.tictactoe.ai;

import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Mark;
import io.github.soloyes.tictactoe.model.Outcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Proof by exhaustion that the Hard AI is unbeatable on 3x3: the opponent tries every possible
 * sequence of moves, and on its own turns the AI is allowed any of the moves it considers best
 * (it picks one of them at random in real games). No branch may end in an AI loss.
 */
class HardAiNeverLosesTest {

    private final Minimax minimax = new Minimax(Difficulty.HARD.searchDepth(3));
    private int games;
    private int aiWins;

    @ParameterizedTest(name = "AI plays {0}")
    @EnumSource(Mark.class)
    @DisplayName("Hard AI never loses on 3x3 against any sequence of moves")
    void neverLoses(Mark ai) {
        explore(new Board(3), ai);
        assertTrue(games > 100, "explored " + games + " games");
        assertTrue(aiWins > 0, "the AI also punishes mistakes");
    }

    private void explore(Board board, Mark ai) {
        Outcome outcome = board.outcome();
        if (outcome.isOver()) {
            games++;
            if (outcome.isWin()) {
                if (outcome.winner() != ai) {
                    fail("AI (" + ai + ") lost:\n" + board);
                }
                aiWins++;
            }
            return;
        }
        Mark toMove = board.nextMark();
        if (toMove == ai) {
            List<ScoredMove> scored = minimax.scoreMoves(board, ai);
            int best = scored.stream().mapToInt(ScoredMove::score).max().orElseThrow();
            for (ScoredMove m : scored) {
                if (m.score() == best) {
                    board.place(m.cell(), ai);
                    explore(board, ai);
                    board.clear(m.cell());
                }
            }
        } else {
            for (int cell : board.emptyCells()) {
                board.place(cell, toMove);
                explore(board, ai);
                board.clear(cell);
            }
        }
    }

    @ParameterizedTest(name = "Hard vs Hard on {0}x{0}")
    @org.junit.jupiter.params.provider.ValueSource(ints = {3})
    void perfectPlayIsADraw(int size) {
        for (long seed = 0; seed < 20; seed++) {
            ComputerPlayer player = new ComputerPlayer(Difficulty.HARD, new java.util.Random(seed));
            Board board = new Board(size);
            while (!board.outcome().isOver()) {
                board.place(player.chooseMove(board, board.nextMark()), board.nextMark());
            }
            assertEquals(Outcome.Status.DRAW, board.outcome().status(), board::toString);
        }
    }
}
