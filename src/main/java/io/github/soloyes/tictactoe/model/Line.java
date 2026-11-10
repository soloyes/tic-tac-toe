package io.github.soloyes.tictactoe.model;

import java.util.Arrays;

/**
 * A straight line of cells (row, column or diagonal) that wins the game
 * when one player fills all of it. Cells are flat board indexes.
 */
public final class Line {
    private final int[] cells;

    Line(int... cells) {
        this.cells = cells.clone();
    }

    public int length() {
        return cells.length;
    }

    public int cell(int i) {
        return cells[i];
    }

    public int first() {
        return cells[0];
    }

    public int last() {
        return cells[cells.length - 1];
    }

    public boolean contains(int index) {
        for (int c : cells) {
            if (c == index) {
                return true;
            }
        }
        return false;
    }

    /** Returns a copy of the cell indexes. */
    public int[] cells() {
        return cells.clone();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Line other && Arrays.equals(cells, other.cells);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cells);
    }

    @Override
    public String toString() {
        return "Line" + Arrays.toString(cells);
    }
}
