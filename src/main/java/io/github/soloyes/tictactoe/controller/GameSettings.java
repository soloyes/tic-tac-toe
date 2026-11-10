package io.github.soloyes.tictactoe.controller;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.model.Board;

import java.util.Objects;

/** Options for a new game. */
public record GameSettings(int boardSize, FirstPlayer firstPlayer, Difficulty difficulty) {

    public static final GameSettings DEFAULT = new GameSettings(3, FirstPlayer.HUMAN, Difficulty.NORMAL);

    public GameSettings {
        if (boardSize < Board.MIN_SIZE || boardSize > Board.MAX_SIZE) {
            throw new IllegalArgumentException("Unsupported board size: " + boardSize);
        }
        Objects.requireNonNull(firstPlayer, "firstPlayer");
        Objects.requireNonNull(difficulty, "difficulty");
    }

    public GameSettings withBoardSize(int size) {
        return new GameSettings(size, firstPlayer, difficulty);
    }

    public GameSettings withFirstPlayer(FirstPlayer first) {
        return new GameSettings(boardSize, first, difficulty);
    }

    public GameSettings withDifficulty(Difficulty level) {
        return new GameSettings(boardSize, firstPlayer, level);
    }
}
