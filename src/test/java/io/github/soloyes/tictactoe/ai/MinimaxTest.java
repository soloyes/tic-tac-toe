package io.github.soloyes.tictactoe.ai;

import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Mark;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinimaxTest {

    private static ComputerPlayer hard() {
        return new ComputerPlayer(Difficulty.HARD, new Random(1));
    }

    @Test
    void takesImmediateWin() {
        Board board = Board.parse(
                "XX.",
                "OO.",
                "...");
        assertEquals(2, hard().chooseMove(board, Mark.X));
    }

    @Test
    void blocksImmediateLoss() {
        Board board = Board.parse(
                "XX.",
                ".O.",
                "...");
        assertEquals(2, hard().chooseMove(board, Mark.O));
    }

    @Test
    void prefersWinOverBlock() {
        Board board = Board.parse(
                "OO.",
                "XX.",
                "X..");
        assertEquals(2, hard().chooseMove(board, Mark.O));
    }

    @Test
    void emptyThreeByThreeIsADraw() {
        List<ScoredMove> moves = new Minimax(Minimax.UNLIMITED).scoreMoves(new Board(3), Mark.X);
        assertEquals(9, moves.size());
        assertTrue(moves.stream().allMatch(m -> m.score() <= 0), "no opening wins by force");
        assertTrue(moves.stream().anyMatch(m -> m.score() == 0), "some opening keeps the draw");
    }

    @Test
    void createsAFork() {
        // X to move. Taking the bottom-right corner threatens the right column and the
        // bottom row at once, and O can only block one of them.
        Board board = Board.parse(
                "X.X",
                ".O.",
                "O..");
        // X can win right away on the top row; a quick win beats a slower forced one.
        assertEquals(1, hard().chooseMove(board, Mark.X));

        Board fork = Board.parse(
                "X..",
                ".O.",
                "..X");
        // O to move must not take a corner: X would then fork. An edge holds the draw.
        int reply = hard().chooseMove(fork, Mark.O);
        assertTrue(reply == 1 || reply == 3 || reply == 5 || reply == 7, "played " + reply);
    }

    @Test
    void fourByFourTakesWinAndBlocks() {
        Board win = Board.parse(
                "XXX.",
                "OOO.",
                "....",
                "....");
        assertEquals(3, hard().chooseMove(win, Mark.X));

        Board block = Board.parse(
                "OOO.",
                "XX..",
                "X...",
                "....");
        assertEquals(3, hard().chooseMove(block, Mark.X));
    }

    @ParameterizedTest
    @EnumSource(Difficulty.class)
    void fourByFourMovesAreInstant(Difficulty level) {
        // The opening move on an empty 4x4 board is the most expensive search.
        ComputerPlayer player = new ComputerPlayer(level, new Random(7));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            Board board = new Board(4);
            while (!board.outcome().isOver()) {
                board.place(player.chooseMove(board, board.nextMark()), board.nextMark());
            }
        });
    }

    @Test
    void searchPrunesBranches() {
        // Plain minimax visits 549,945 positions from the empty 3x3 board; alpha-beta needs far fewer.
        Minimax m = new Minimax(Minimax.UNLIMITED);
        m.scoreMoves(new Board(3), Mark.X);
        assertTrue(m.nodes() < 549_945 / 2, "visited " + m.nodes());
    }

    @Test
    void heuristicFavoursOpenLines() {
        Board board = Board.parse(
                "....",
                ".X..",
                "....",
                "....");
        assertTrue(Heuristic.evaluate(board, Mark.X) > 0);
        assertTrue(Heuristic.evaluate(board, Mark.O) < 0);
    }
}
