package io.github.soloyes.tictactoe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A square N x N board. A player wins by filling a whole row, column or diagonal.
 *
 * <p>Cells are addressed either by {@code (row, col)} or by a flat index
 * {@code row * size + col}. Empty cells hold {@code null}.
 */
public final class Board {
    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 5;

    private final int size;
    private final Mark[] cells;
    private final List<Line> lines;
    private int filled;

    public Board(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE) {
            throw new IllegalArgumentException(
                    "Board size must be between " + MIN_SIZE + " and " + MAX_SIZE + ": " + size);
        }
        this.size = size;
        this.cells = new Mark[size * size];
        this.lines = buildLines(size);
    }

    private Board(Board other) {
        this.size = other.size;
        this.cells = other.cells.clone();
        this.lines = other.lines;
        this.filled = other.filled;
    }

    /**
     * Builds a board from text rows, e.g. {@code Board.parse("X.O", ".X.", "O..")}.
     * {@code X}/{@code O} are marks; {@code .}, {@code _} or a space mean empty.
     */
    public static Board parse(String... rows) {
        Board board = new Board(rows.length);
        for (int r = 0; r < rows.length; r++) {
            if (rows[r].length() != rows.length) {
                throw new IllegalArgumentException("Row " + r + " must have " + rows.length + " cells");
            }
            for (int c = 0; c < rows.length; c++) {
                char ch = Character.toUpperCase(rows[r].charAt(c));
                switch (ch) {
                    case 'X' -> board.place(r, c, Mark.X);
                    case 'O' -> board.place(r, c, Mark.O);
                    case '.', '_', ' ' -> { }
                    default -> throw new IllegalArgumentException("Unknown cell '" + ch + "'");
                }
            }
        }
        return board;
    }

    private static List<Line> buildLines(int n) {
        List<Line> result = new ArrayList<>(2 * n + 2);
        for (int r = 0; r < n; r++) {
            int[] row = new int[n];
            for (int c = 0; c < n; c++) {
                row[c] = r * n + c;
            }
            result.add(new Line(row));
        }
        for (int c = 0; c < n; c++) {
            int[] col = new int[n];
            for (int r = 0; r < n; r++) {
                col[r] = r * n + c;
            }
            result.add(new Line(col));
        }
        int[] diag = new int[n];
        int[] anti = new int[n];
        for (int i = 0; i < n; i++) {
            diag[i] = i * n + i;
            anti[i] = i * n + (n - 1 - i);
        }
        result.add(new Line(diag));
        result.add(new Line(anti));
        return Collections.unmodifiableList(result);
    }

    public int size() {
        return size;
    }

    public int cellCount() {
        return cells.length;
    }

    public int index(int row, int col) {
        checkCoordinate(row);
        checkCoordinate(col);
        return row * size + col;
    }

    public int row(int index) {
        return index / size;
    }

    public int col(int index) {
        return index % size;
    }

    /** Returns the mark at {@code index}, or {@code null} if the cell is empty. */
    public Mark get(int index) {
        return cells[index];
    }

    /** Returns the mark at {@code (row, col)}, or {@code null} if the cell is empty. */
    public Mark get(int row, int col) {
        return cells[index(row, col)];
    }

    public boolean isEmpty(int index) {
        return cells[index] == null;
    }

    public boolean isFull() {
        return filled == cells.length;
    }

    public int filledCount() {
        return filled;
    }

    /** Puts {@code mark} on an empty cell. */
    public void place(int index, Mark mark) {
        if (mark == null) {
            throw new IllegalArgumentException("mark is null");
        }
        if (cells[index] != null) {
            throw new IllegalStateException("Cell " + index + " is already taken");
        }
        cells[index] = mark;
        filled++;
    }

    public void place(int row, int col, Mark mark) {
        place(index(row, col), mark);
    }

    /** Empties a cell. Used by the AI to undo a trial move. */
    public void clear(int index) {
        if (cells[index] != null) {
            cells[index] = null;
            filled--;
        }
    }

    /** Indexes of all empty cells, in ascending order. */
    public int[] emptyCells() {
        int[] result = new int[cells.length - filled];
        int k = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == null) {
                result[k++] = i;
            }
        }
        return result;
    }

    /** All winning lines of this board: rows, columns, then the two diagonals. */
    public List<Line> lines() {
        return lines;
    }

    /** Whose turn it is, assuming X moved first and players alternated. */
    public Mark nextMark() {
        return filled % 2 == 0 ? Mark.X : Mark.O;
    }

    public Outcome outcome() {
        for (Line line : lines) {
            Mark first = cells[line.first()];
            if (first == null) {
                continue;
            }
            boolean complete = true;
            for (int i = 1; i < line.length(); i++) {
                if (cells[line.cell(i)] != first) {
                    complete = false;
                    break;
                }
            }
            if (complete) {
                return Outcome.win(first, line);
            }
        }
        return isFull() ? Outcome.draw() : Outcome.inProgress();
    }

    public Board copy() {
        return new Board(this);
    }

    private void checkCoordinate(int v) {
        if (v < 0 || v >= size) {
            throw new IndexOutOfBoundsException("Coordinate " + v + " is outside 0.." + (size - 1));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Mark m = cells[r * size + c];
                sb.append(m == null ? '.' : m.name().charAt(0));
            }
            if (r < size - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
