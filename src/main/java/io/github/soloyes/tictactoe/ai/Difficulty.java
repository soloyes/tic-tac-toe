package io.github.soloyes.tictactoe.ai;

/**
 * How strong the computer plays.
 *
 * <p>On every move the computer first rolls a die: with probability {@link #mistakeRate()}
 * it plays a random legal move. Otherwise it runs minimax to the given depth and plays
 * one of the best moves. On 3x3 the search always goes to the end of the game; on 4x4 it is
 * depth-limited so that moves stay instant.
 */
public enum Difficulty {
    /** Mostly random, but still grabs a win now and then. */
    EASY("Easy", 0.6, 2, 2),
    /** Plays well, but blunders on roughly one move in five. */
    NORMAL("Normal", 0.2, Minimax.UNLIMITED, 4),
    /** Never blunders. Perfect on 3x3, depth 6 lookahead on 4x4. */
    HARD("Hard", 0.0, Minimax.UNLIMITED, 6);

    private final String label;
    private final double mistakeRate;
    private final int depth3x3;
    private final int depthLarge;

    Difficulty(String label, double mistakeRate, int depth3x3, int depthLarge) {
        this.label = label;
        this.mistakeRate = mistakeRate;
        this.depth3x3 = depth3x3;
        this.depthLarge = depthLarge;
    }

    public String label() {
        return label;
    }

    public double mistakeRate() {
        return mistakeRate;
    }

    public int searchDepth(int boardSize) {
        return boardSize <= 3 ? depth3x3 : depthLarge;
    }

    /** Case-insensitive lookup by name, e.g. {@code "hard"}. */
    public static Difficulty parse(String text) {
        return valueOf(text.trim().toUpperCase(java.util.Locale.ROOT));
    }

    @Override
    public String toString() {
        return label;
    }
}
