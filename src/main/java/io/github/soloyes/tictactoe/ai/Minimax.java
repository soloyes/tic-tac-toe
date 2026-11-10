package io.github.soloyes.tictactoe.ai;

import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Mark;
import io.github.soloyes.tictactoe.model.Outcome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Minimax search with alpha-beta pruning, written in the negamax form:
 * the value of a position for one player is minus its value for the other.
 *
 * <ul>
 *     <li>A win is worth {@code WIN_SCORE - ply}, so the AI prefers quick wins
 *     and, when it is lost anyway, the longest defence.</li>
 *     <li>With a depth limit, positions at the horizon are scored by {@link Heuristic}.</li>
 *     <li>Moves are tried centre-first, which makes cut-offs happen early.</li>
 * </ul>
 */
public final class Minimax {
    public static final int WIN_SCORE = 1_000_000;
    /** Any score above this is a forced win, below its negation a forced loss. */
    public static final int WIN_THRESHOLD = WIN_SCORE - 100;
    /** Depth that is never reached on supported boards, i.e. search to the end. */
    public static final int UNLIMITED = Integer.MAX_VALUE;

    private static final int INF = Integer.MAX_VALUE;

    private final int maxDepth;
    private long nodes;

    public Minimax(int maxDepth) {
        if (maxDepth < 1) {
            throw new IllegalArgumentException("maxDepth must be >= 1");
        }
        this.maxDepth = maxDepth;
    }

    /**
     * Scores every legal move for {@code me}. Each root move gets an exact value
     * (searched with a full window) so callers can pick randomly among equal moves;
     * alpha-beta pruning works inside each subtree.
     */
    public List<ScoredMove> scoreMoves(Board board, Mark me) {
        if (board.outcome().isOver()) {
            throw new IllegalStateException("Game is already over");
        }
        Board work = board.copy();
        List<ScoredMove> result = new ArrayList<>();
        for (int cell : orderedMoves(work)) {
            work.place(cell, me);
            int score = -negamax(work, me.opponent(), maxDepth - 1, 1, -INF, INF);
            work.clear(cell);
            result.add(new ScoredMove(cell, score));
        }
        return result;
    }

    /** Number of positions visited since this instance was created (for diagnostics and tests). */
    public long nodes() {
        return nodes;
    }

    private int negamax(Board board, Mark toMove, int depthLeft, int ply, int alpha, int beta) {
        nodes++;
        Outcome outcome = board.outcome();
        if (outcome.isWin()) {
            // The previous move won, so the side to move has lost.
            return -(WIN_SCORE - ply);
        }
        if (outcome.isDraw()) {
            return 0;
        }
        if (depthLeft <= 0) {
            return Heuristic.evaluate(board, toMove);
        }
        int best = -INF;
        for (int cell : orderedMoves(board)) {
            board.place(cell, toMove);
            int value = -negamax(board, toMove.opponent(), depthLeft - 1, ply + 1, -beta, -alpha);
            board.clear(cell);
            if (value > best) {
                best = value;
            }
            if (best > alpha) {
                alpha = best;
            }
            if (alpha >= beta) {
                break; // the opponent will never allow this position
            }
        }
        return best;
    }

    private static final int[][] ORDER = new int[Board.MAX_SIZE + 1][];

    static {
        for (int n = Board.MIN_SIZE; n <= Board.MAX_SIZE; n++) {
            final int size = n;
            double mid = (size - 1) / 2.0;
            ORDER[n] = IntStream.range(0, size * size)
                    .boxed()
                    .sorted(Comparator.comparingDouble(i -> {
                        double dr = i / size - mid;
                        double dc = i % size - mid;
                        // centre first, then corners (they sit on diagonals), then edges
                        boolean onDiagonal = Math.abs(dr) == Math.abs(dc);
                        return Math.max(Math.abs(dr), Math.abs(dc)) * 2 + (onDiagonal ? 0 : 1);
                    }))
                    .mapToInt(Integer::intValue)
                    .toArray();
        }
    }

    private static int[] orderedMoves(Board board) {
        int[] order = ORDER[board.size()];
        int[] moves = new int[board.cellCount() - board.filledCount()];
        int k = 0;
        for (int cell : order) {
            if (board.isEmpty(cell)) {
                moves[k++] = cell;
            }
        }
        return k == moves.length ? moves : Arrays.copyOf(moves, k);
    }
}
