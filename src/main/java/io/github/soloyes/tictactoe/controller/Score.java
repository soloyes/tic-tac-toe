package io.github.soloyes.tictactoe.controller;

/** Running tally of finished games in this session. */
public record Score(int humanWins, int computerWins, int draws) {
    public static final Score ZERO = new Score(0, 0, 0);

    Score plusHumanWin() {
        return new Score(humanWins + 1, computerWins, draws);
    }

    Score plusComputerWin() {
        return new Score(humanWins, computerWins + 1, draws);
    }

    Score plusDraw() {
        return new Score(humanWins, computerWins, draws + 1);
    }
}
