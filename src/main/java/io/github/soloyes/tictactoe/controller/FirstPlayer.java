package io.github.soloyes.tictactoe.controller;

/** Who opens the game. The opener plays X. */
public enum FirstPlayer {
    HUMAN("You"),
    COMPUTER("Computer");

    private final String label;

    FirstPlayer(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
