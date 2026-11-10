package io.github.soloyes.tictactoe.ai;

import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Mark;

/** Chooses a move for the computer. {@link ComputerPlayer} is the real one; tests can script moves. */
public interface MoveStrategy {

    /** Returns the flat index of an empty cell to play for {@code me}. Must not modify the board. */
    int chooseMove(Board board, Mark me);

    /** Called when the player changes the level. Strategies without levels ignore it. */
    default void setDifficulty(Difficulty difficulty) {
    }
}
