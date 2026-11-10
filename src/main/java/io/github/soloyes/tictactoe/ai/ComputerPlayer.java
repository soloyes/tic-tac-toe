package io.github.soloyes.tictactoe.ai;

import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/** Picks the computer's move according to a {@link Difficulty}. */
public final class ComputerPlayer implements MoveStrategy {
    private final Random random;
    private Difficulty difficulty;

    public ComputerPlayer(Difficulty difficulty, Random random) {
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty");
        this.random = Objects.requireNonNull(random, "random");
    }

    public Difficulty difficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty");
    }

    @Override
    public int chooseMove(Board board, Mark me) {
        int[] empty = board.emptyCells();
        if (empty.length == 0 || board.outcome().isOver()) {
            throw new IllegalStateException("No moves left");
        }
        if (difficulty.mistakeRate() > 0 && random.nextDouble() < difficulty.mistakeRate()) {
            return empty[random.nextInt(empty.length)];
        }
        List<ScoredMove> scored = new Minimax(difficulty.searchDepth(board.size())).scoreMoves(board, me);
        int best = scored.stream().mapToInt(ScoredMove::score).max().orElseThrow();
        List<Integer> ties = new ArrayList<>();
        for (ScoredMove m : scored) {
            if (m.score() == best) {
                ties.add(m.cell());
            }
        }
        return ties.get(random.nextInt(ties.size()));
    }
}
