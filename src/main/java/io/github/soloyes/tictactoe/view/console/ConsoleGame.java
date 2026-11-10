package io.github.soloyes.tictactoe.view.console;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.controller.FirstPlayer;
import io.github.soloyes.tictactoe.controller.GameController;
import io.github.soloyes.tictactoe.controller.GameSettings;
import io.github.soloyes.tictactoe.controller.Score;
import io.github.soloyes.tictactoe.model.Mark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

/** Text front end: reads moves as "row col" and prints the board after every move. */
public final class ConsoleGame {
    private final BufferedReader in;
    private final PrintStream out;
    private final Random random;

    public ConsoleGame(BufferedReader in, PrintStream out, Random random) {
        this.in = in;
        this.out = out;
        this.random = random;
    }

    /** Runs games until the user quits or input ends. */
    public void run(GameSettings defaults) {
        out.println("Tic-Tac-Toe (console mode). Type q at any prompt to quit.");
        Optional<GameSettings> settings = askSettings(defaults);
        if (settings.isEmpty()) {
            return;
        }
        GameController game = new GameController(settings.get(), random);
        while (true) {
            if (!playOne(game)) {
                return;
            }
            Score s = game.score();
            out.printf("Score: you %d, computer %d, draws %d%n", s.humanWins(), s.computerWins(), s.draws());
            Optional<String> again = prompt("Play again? [Y/n] ");
            if (again.isEmpty() || again.get().toLowerCase(Locale.ROOT).startsWith("n")) {
                out.println("Bye!");
                return;
            }
            game.newGame();
        }
    }

    /** Plays one game to the end. Returns false if the user quit. */
    private boolean playOne(GameController game) {
        out.printf("%nNew %dx%d game, %s. You play %s.%n", game.boardSize(), game.boardSize(),
                game.settings().difficulty().label().toLowerCase(Locale.ROOT), game.humanMark());
        printBoard(game);
        while (!game.isOver()) {
            if (game.isComputerTurn()) {
                int cell = game.playComputerMove();
                int n = game.boardSize();
                out.printf("Computer plays %d %d%n", cell / n + 1, cell % n + 1);
                printBoard(game);
                continue;
            }
            Optional<String> line = prompt("Your move (row col): ");
            if (line.isEmpty()) {
                return false;
            }
            int[] rc = parseMove(line.get(), game.boardSize());
            if (rc == null) {
                out.printf("Enter row and column from 1 to %d, e.g. \"2 3\".%n", game.boardSize());
            } else if (!game.playHumanMove(rc[0], rc[1])) {
                out.println("That cell is taken.");
            } else {
                printBoard(game);
            }
        }
        if (game.humanWon()) {
            out.println("You win!");
        } else if (game.computerWon()) {
            out.println("Computer wins.");
        } else {
            out.println("Draw.");
        }
        return true;
    }

    private Optional<GameSettings> askSettings(GameSettings defaults) {
        GameSettings s = defaults;
        while (true) {
            Optional<String> a = prompt("Board size, 3 or 4 [" + s.boardSize() + "]: ");
            if (a.isEmpty()) {
                return Optional.empty();
            }
            String v = a.get();
            if (v.isEmpty()) {
                break;
            }
            if (v.equals("3") || v.equals("4")) {
                s = s.withBoardSize(Integer.parseInt(v));
                break;
            }
        }
        while (true) {
            String def = s.firstPlayer() == FirstPlayer.HUMAN ? "you" : "computer";
            Optional<String> a = prompt("Who moves first, you or computer [" + def + "]: ");
            if (a.isEmpty()) {
                return Optional.empty();
            }
            String v = a.get().toLowerCase(Locale.ROOT);
            if (v.isEmpty()) {
                break;
            }
            if (v.startsWith("y")) {
                s = s.withFirstPlayer(FirstPlayer.HUMAN);
                break;
            }
            if (v.startsWith("c")) {
                s = s.withFirstPlayer(FirstPlayer.COMPUTER);
                break;
            }
        }
        while (true) {
            Optional<String> a = prompt("Difficulty, easy/normal/hard ["
                    + s.difficulty().label().toLowerCase(Locale.ROOT) + "]: ");
            if (a.isEmpty()) {
                return Optional.empty();
            }
            String v = a.get().toLowerCase(Locale.ROOT);
            if (v.isEmpty()) {
                break;
            }
            Difficulty d = switch (v.charAt(0)) {
                case 'e' -> Difficulty.EASY;
                case 'n' -> Difficulty.NORMAL;
                case 'h' -> Difficulty.HARD;
                default -> null;
            };
            if (d != null) {
                s = s.withDifficulty(d);
                break;
            }
        }
        return Optional.of(s);
    }

    /**
     * Parses "2 3", "2,3" or "23" into zero-based {row, col}; returns null if invalid.
     */
    static int[] parseMove(String text, int size) {
        String t = text.trim();
        String[] parts = t.split("[\\s,;]+");
        int row;
        int col;
        try {
            if (parts.length == 2) {
                row = Integer.parseInt(parts[0]);
                col = Integer.parseInt(parts[1]);
            } else if (parts.length == 1 && t.length() == 2) {
                row = Character.digit(t.charAt(0), 10);
                col = Character.digit(t.charAt(1), 10);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        if (row < 1 || row > size || col < 1 || col > size) {
            return null;
        }
        return new int[]{row - 1, col - 1};
    }

    private void printBoard(GameController game) {
        int n = game.boardSize();
        StringBuilder sb = new StringBuilder("\n    ");
        for (int c = 1; c <= n; c++) {
            sb.append(' ').append(c).append("  ");
        }
        sb.append('\n');
        for (int r = 0; r < n; r++) {
            sb.append("  ").append(r + 1).append(' ');
            for (int c = 0; c < n; c++) {
                Mark m = game.markAt(r, c);
                sb.append(' ').append(m == null ? ' ' : m.name().charAt(0)).append(' ');
                if (c < n - 1) {
                    sb.append('|');
                }
            }
            sb.append('\n');
            if (r < n - 1) {
                sb.append("    ").append("---+".repeat(n - 1)).append("---\n");
            }
        }
        out.print(sb);
        out.println();
    }

    /** Prints a prompt and reads a trimmed line; empty Optional on end of input or "q". */
    private Optional<String> prompt(String text) {
        out.print(text);
        out.flush();
        try {
            String line = in.readLine();
            if (line == null) {
                out.println();
                return Optional.empty();
            }
            line = line.trim();
            if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit")) {
                return Optional.empty();
            }
            return Optional.of(line);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
