package io.github.soloyes.tictactoe.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    /** Every row, column and diagonal of 3x3 and 4x4 boards, for both marks. */
    static Stream<Arguments> allLines() {
        List<Arguments> args = new ArrayList<>();
        for (int n : new int[]{3, 4}) {
            for (Mark mark : Mark.values()) {
                for (int r = 0; r < n; r++) {
                    int[] cells = new int[n];
                    for (int c = 0; c < n; c++) {
                        cells[c] = r * n + c;
                    }
                    args.add(Arguments.of(n, mark, "row " + r, cells));
                }
                for (int c = 0; c < n; c++) {
                    int[] cells = new int[n];
                    for (int r = 0; r < n; r++) {
                        cells[r] = r * n + c;
                    }
                    args.add(Arguments.of(n, mark, "column " + c, cells));
                }
                int[] diag = new int[n];
                int[] anti = new int[n];
                for (int i = 0; i < n; i++) {
                    diag[i] = i * n + i;
                    anti[i] = i * n + n - 1 - i;
                }
                args.add(Arguments.of(n, mark, "main diagonal", diag));
                args.add(Arguments.of(n, mark, "anti-diagonal", anti));
            }
        }
        return args.stream();
    }

    @ParameterizedTest(name = "{0}x{0}: {1} wins on {2}")
    @MethodSource("allLines")
    void detectsWinOnEveryLine(int n, Mark mark, String name, int[] cells) {
        Board board = new Board(n);
        for (int cell : cells) {
            assertFalse(board.outcome().isOver(), "no win before the line is complete");
            board.place(cell, mark);
        }
        Outcome outcome = board.outcome();
        assertTrue(outcome.isWin());
        assertEquals(mark, outcome.winner());
        assertArrayEquals(cells, outcome.winningLine().cells());
    }

    @ParameterizedTest(name = "{0}x{0}: {1} with one cell taken by the opponent is not a win on {2}")
    @MethodSource("allLines")
    void blockedLineIsNotAWin(int n, Mark mark, String name, int[] cells) {
        Board board = new Board(n);
        for (int i = 0; i < cells.length; i++) {
            board.place(cells[i], i == 1 ? mark.opponent() : mark);
        }
        assertFalse(board.outcome().isWin(), board::toString);
    }

    @Test
    @DisplayName("full 3x3 board without a line is a draw")
    void draw3x3() {
        Board board = Board.parse(
                "XOX",
                "XOO",
                "OXX");
        assertTrue(board.isFull());
        assertTrue(board.outcome().isDraw());
        assertNull(board.outcome().winner());
    }

    @Test
    @DisplayName("full 4x4 board without a line is a draw")
    void draw4x4() {
        Board board = Board.parse(
                "XXOO",
                "OOXX",
                "XXOO",
                "OOXX");
        assertTrue(board.outcome().isDraw());
    }

    @Test
    void winOnLastCellIsAWinNotADraw() {
        Board board = Board.parse(
                "XOX",
                "OXO",
                "OXX");
        assertTrue(board.isFull());
        assertTrue(board.outcome().isWin());
        assertEquals(Mark.X, board.outcome().winner());
    }

    @Test
    void partialBoardIsInProgress() {
        Board board = Board.parse(
                "XO.",
                "...",
                "...");
        assertEquals(Outcome.Status.IN_PROGRESS, board.outcome().status());
        assertFalse(board.outcome().isOver());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4})
    void rowAndColumnMapping(int n) {
        Board board = new Board(n);
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int index = board.index(r, c);
                assertEquals(r * n + c, index);
                assertEquals(r, board.row(index));
                assertEquals(c, board.col(index));
            }
        }
        board.place(0, n - 1, Mark.X); // top-right corner
        assertEquals(Mark.X, board.get(n - 1));
        assertEquals(2 * n + 2, board.lines().size());
    }

    @Test
    void cannotPlaceOnTakenCell() {
        Board board = new Board(3);
        board.place(4, Mark.X);
        assertThrows(IllegalStateException.class, () -> board.place(4, Mark.O));
    }

    @Test
    void rejectsUnsupportedSizes() {
        assertThrows(IllegalArgumentException.class, () -> new Board(2));
        assertThrows(IllegalArgumentException.class, () -> new Board(Board.MAX_SIZE + 1));
    }

    @Test
    void copyIsIndependent() {
        Board board = new Board(3);
        Board copy = board.copy();
        copy.place(0, Mark.X);
        assertTrue(board.isEmpty(0));
        assertEquals(1, copy.filledCount());
    }

    @Test
    void clearUndoesAMove() {
        Board board = new Board(3);
        board.place(4, Mark.X);
        board.clear(4);
        assertTrue(board.isEmpty(4));
        assertEquals(0, board.filledCount());
        assertEquals(9, board.emptyCells().length);
    }

    @Test
    void nextMarkAlternatesStartingWithX() {
        Board board = new Board(3);
        assertEquals(Mark.X, board.nextMark());
        board.place(0, Mark.X);
        assertEquals(Mark.O, board.nextMark());
    }

    @Test
    void parseAndToStringRoundTrip() {
        Board board = Board.parse("X.O", ".X.", "O..");
        assertEquals("X.O\n.X.\nO..", board.toString());
    }
}
