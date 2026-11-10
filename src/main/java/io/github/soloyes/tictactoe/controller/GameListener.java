package io.github.soloyes.tictactoe.controller;

/** Notified after every change of the game: a move, a new game or new settings. */
@FunctionalInterface
public interface GameListener {
    void gameChanged(GameController game);
}
