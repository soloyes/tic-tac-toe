package io.github.soloyes.tictactoe.ai;

import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Mark;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerPlayerTest {

    @ParameterizedTest
    @EnumSource(Difficulty.class)
    void alwaysPlaysALegalMove(Difficulty level) {
        Random random = new Random(42);
        for (int game = 0; game < 30; game++) {
            ComputerPlayer player = new ComputerPlayer(level, random);
            Board board = new Board(3);
            while (!board.outcome().isOver()) {
                int move = player.chooseMove(board, board.nextMark());
                assertTrue(board.isEmpty(move));
                board.place(move, board.nextMark());
            }
        }
    }

    @Test
    void easyIsBeatableAndVaried() {
        // Easy as O against a fixed opening: the replies must vary, unlike Hard.
        Set<Integer> replies = new HashSet<>();
        Random random = new Random(3);
        for (int i = 0; i < 50; i++) {
            Board board = Board.parse("X..", "...", "...");
            replies.add(new ComputerPlayer(Difficulty.EASY, random).chooseMove(board, Mark.O));
        }
        assertTrue(replies.size() > 3, "replies: " + replies);
    }

    @Test
    void hardAnswersCornerOpeningWithCentre() {
        // Any reply other than the centre loses against a corner opening.
        for (long seed = 0; seed < 10; seed++) {
            Board board = Board.parse("X..", "...", "...");
            assertEquals(4, new ComputerPlayer(Difficulty.HARD, new Random(seed)).chooseMove(board, Mark.O));
        }
    }

    @Test
    void refusesFinishedGame() {
        Board board = Board.parse("XXX", "OO.", "...");
        ComputerPlayer player = new ComputerPlayer(Difficulty.HARD, new Random());
        assertThrows(IllegalStateException.class, () -> player.chooseMove(board, Mark.O));
    }
}
