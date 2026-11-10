package io.github.soloyes.tictactoe.model;

import java.util.Objects;

/**
 * State of a board: still in progress, won by a mark along a line, or drawn.
 *
 * @param status      what happened
 * @param winner      the winning mark, or {@code null} unless {@code status == WIN}
 * @param winningLine the completed line, or {@code null} unless {@code status == WIN}
 */
public record Outcome(Status status, Mark winner, Line winningLine) {

    public enum Status { IN_PROGRESS, WIN, DRAW }

    private static final Outcome IN_PROGRESS = new Outcome(Status.IN_PROGRESS, null, null);
    private static final Outcome DRAW = new Outcome(Status.DRAW, null, null);

    public Outcome {
        Objects.requireNonNull(status, "status");
        if ((status == Status.WIN) != (winner != null && winningLine != null)) {
            throw new IllegalArgumentException("winner and line are required for WIN only");
        }
    }

    public static Outcome inProgress() {
        return IN_PROGRESS;
    }

    public static Outcome draw() {
        return DRAW;
    }

    public static Outcome win(Mark winner, Line line) {
        return new Outcome(Status.WIN, winner, line);
    }

    public boolean isOver() {
        return status != Status.IN_PROGRESS;
    }

    public boolean isWin() {
        return status == Status.WIN;
    }

    public boolean isDraw() {
        return status == Status.DRAW;
    }
}
