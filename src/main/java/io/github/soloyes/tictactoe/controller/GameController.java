package io.github.soloyes.tictactoe.controller;

import io.github.soloyes.tictactoe.ai.ComputerPlayer;
import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.ai.MoveStrategy;
import io.github.soloyes.tictactoe.model.Board;
import io.github.soloyes.tictactoe.model.Mark;
import io.github.soloyes.tictactoe.model.Outcome;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * One game session between a human and the computer. Holds the board, whose turn it is
 * and the score; views read from it and send moves to it. Contains no UI code, so the
 * same controller drives both the Swing and the console front end.
 *
 * <p>The computer does not move by itself: the view calls {@link #playComputerMove()} when
 * {@link #isComputerTurn()} is true. That lets the Swing UI add a short "thinking" pause
 * while the console plays immediately.
 */
public final class GameController {
    private final MoveStrategy computer;
    private final List<GameListener> listeners = new CopyOnWriteArrayList<>();

    private GameSettings settings;
    private Board board;
    private Mark humanMark;
    private Outcome outcome;
    private Score score = Score.ZERO;
    private int lastMove = -1;

    public GameController(GameSettings settings) {
        this(settings, new Random());
    }

    public GameController(GameSettings settings, Random random) {
        this(settings, new ComputerPlayer(settings.difficulty(), random));
    }

    /** Uses a custom move source for the computer, e.g. scripted moves in tests. */
    public GameController(GameSettings settings, MoveStrategy computer) {
        this.computer = Objects.requireNonNull(computer, "computer");
        start(settings);
    }

    public void addListener(GameListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    public void removeListener(GameListener listener) {
        listeners.remove(listener);
    }

    /** Starts a new game with the current settings. */
    public void newGame() {
        newGame(settings);
    }

    /** Starts a new game with new settings. The score is kept. */
    public void newGame(GameSettings newSettings) {
        start(newSettings);
        fire();
    }

    /** Changes the computer's strength without restarting the game. */
    public void setDifficulty(Difficulty difficulty) {
        settings = settings.withDifficulty(difficulty);
        computer.setDifficulty(difficulty);
        fire();
    }

    private void start(GameSettings newSettings) {
        settings = Objects.requireNonNull(newSettings);
        computer.setDifficulty(newSettings.difficulty());
        board = new Board(newSettings.boardSize());
        humanMark = newSettings.firstPlayer() == FirstPlayer.HUMAN ? Mark.X : Mark.O;
        outcome = Outcome.inProgress();
        lastMove = -1;
    }

    /**
     * Plays the human's move. Returns {@code false} and changes nothing if the game is over,
     * it is not the human's turn, or the cell is taken.
     */
    public boolean playHumanMove(int row, int col) {
        if (!isHumanTurn() || row < 0 || col < 0 || row >= board.size() || col >= board.size()) {
            return false;
        }
        int index = board.index(row, col);
        if (!board.isEmpty(index)) {
            return false;
        }
        apply(index, humanMark);
        return true;
    }

    /** Lets the computer move. Returns the flat index it played. */
    public int playComputerMove() {
        if (!isComputerTurn()) {
            throw new IllegalStateException("It is not the computer's turn");
        }
        int index = computer.chooseMove(board, computerMark());
        apply(index, computerMark());
        return index;
    }

    private void apply(int index, Mark mark) {
        board.place(index, mark);
        lastMove = index;
        outcome = board.outcome();
        if (outcome.isWin()) {
            score = outcome.winner() == humanMark ? score.plusHumanWin() : score.plusComputerWin();
        } else if (outcome.isDraw()) {
            score = score.plusDraw();
        }
        fire();
    }

    private void fire() {
        for (GameListener l : listeners) {
            l.gameChanged(this);
        }
    }

    /** A copy of the board; changing it does not affect the game. */
    public Board board() {
        return board.copy();
    }

    public int boardSize() {
        return board.size();
    }

    public Mark markAt(int row, int col) {
        return board.get(row, col);
    }

    public boolean isEmptyCell(int row, int col) {
        return board.get(row, col) == null;
    }

    public GameSettings settings() {
        return settings;
    }

    public Outcome outcome() {
        return outcome;
    }

    public boolean isOver() {
        return outcome.isOver();
    }

    public Mark humanMark() {
        return humanMark;
    }

    public Mark computerMark() {
        return humanMark.opponent();
    }

    public Mark currentMark() {
        return board.nextMark();
    }

    public boolean isHumanTurn() {
        return !isOver() && currentMark() == humanMark;
    }

    public boolean isComputerTurn() {
        return !isOver() && currentMark() != humanMark;
    }

    public boolean humanWon() {
        return outcome.isWin() && outcome.winner() == humanMark;
    }

    public boolean computerWon() {
        return outcome.isWin() && outcome.winner() != humanMark;
    }

    /** Flat index of the last move, or -1 at the start of a game. */
    public int lastMove() {
        return lastMove;
    }

    public Score score() {
        return score;
    }
}
