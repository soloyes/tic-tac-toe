package io.github.soloyes.tictactoe.model;

/** A mark a player puts on the board. X always moves first. */
public enum Mark {
    X, O;

    /** Returns the other mark. */
    public Mark opponent() {
        return this == X ? O : X;
    }
}
