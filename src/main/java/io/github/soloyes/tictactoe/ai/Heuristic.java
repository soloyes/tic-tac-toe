package io.github.soloyes.tictactoe.ai;

import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Line;
import io.github.soloyes.tictactoe.model.Mark;

/**
 * Static evaluation used when the search stops before the game is over (4x4 boards).
 *
 * <p>Every line that still can be won by only one side counts for that side,
 * and the more marks it already has, the more it is worth (1, 10, 100, ...).
 * Lines that contain both marks are dead and count for nothing.
 */
final class Heuristic {
    private static final int[] WEIGHTS = {0, 1, 10, 100, 1_000, 10_000};

    private Heuristic() {
    }

    /** Score of {@code board} from the point of view of {@code player}. */
    static int evaluate(Board board, Mark player) {
        int score = 0;
        for (Line line : board.lines()) {
            int mine = 0;
            int theirs = 0;
            for (int i = 0; i < line.length(); i++) {
                Mark m = board.get(line.cell(i));
                if (m == player) {
                    mine++;
                } else if (m != null) {
                    theirs++;
                }
            }
            if (theirs == 0) {
                score += WEIGHTS[mine];
            } else if (mine == 0) {
                score -= WEIGHTS[theirs];
            }
        }
        return score;
    }
}
