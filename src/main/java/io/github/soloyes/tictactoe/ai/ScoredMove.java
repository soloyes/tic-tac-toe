package io.github.soloyes.tictactoe.ai;

/**
 * A legal move and its minimax value for the player who makes it.
 *
 * @param cell  flat board index
 * @param score positive is good for the mover, {@code > Minimax.WIN_THRESHOLD} is a forced win
 */
public record ScoredMove(int cell, int score) {
}
