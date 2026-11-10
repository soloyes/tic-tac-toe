package io.github.soloyes.tictactoe;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.controller.FirstPlayer;
import io.github.soloyes.tictactoe.controller.GameController;
import io.github.soloyes.tictactoe.controller.GameSettings;
import io.github.soloyes.tictactoe.view.console.ConsoleGame;
import io.github.soloyes.tictactoe.view.swing.GameWindow;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

/** Entry point. Opens the Swing window by default, or plays in the terminal with {@code --console}. */
public final class App {
    static final String USAGE = """
            Usage: java -jar tic-tac-toe.jar [options]

              --console                 play in the terminal instead of the window
              --size=3|4                board size (default 3)
              --first=you|computer      who moves first (default you)
              --level=easy|normal|hard  computer strength (default normal)
              -h, --help                show this help
            """;

    private App() {
    }

    public static void main(String[] args) {
        Options options;
        try {
            options = Options.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.print(USAGE);
            System.exit(2);
            return;
        }
        if (options.help()) {
            System.out.print(USAGE);
            return;
        }
        if (options.console() || GraphicsEnvironment.isHeadless()) {
            if (!options.console()) {
                System.out.println("No display found, starting in console mode.");
            }
            var in = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
            new ConsoleGame(in, new PrintStream(System.out, true), new Random()).run(options.settings());
        } else {
            GameWindow.launch(new GameController(options.settings()));
        }
    }

    /** Parsed command line. */
    record Options(boolean console, boolean help, GameSettings settings) {

        static Options parse(String... args) {
            boolean console = false;
            boolean help = false;
            GameSettings s = GameSettings.DEFAULT;
            for (String arg : args) {
                String a = arg.trim();
                String value = a.contains("=") ? a.substring(a.indexOf('=') + 1).toLowerCase(Locale.ROOT) : "";
                if (a.equals("--console") || a.equals("-c")) {
                    console = true;
                } else if (a.equals("--help") || a.equals("-h")) {
                    help = true;
                } else if (a.startsWith("--size=")) {
                    if (!value.equals("3") && !value.equals("4")) {
                        throw new IllegalArgumentException("Board size must be 3 or 4: " + value);
                    }
                    s = s.withBoardSize(Integer.parseInt(value));
                } else if (a.startsWith("--first=")) {
                    s = s.withFirstPlayer(switch (value) {
                        case "you", "human", "me" -> FirstPlayer.HUMAN;
                        case "computer", "cpu", "ai" -> FirstPlayer.COMPUTER;
                        default -> throw new IllegalArgumentException("Unknown first player: " + value);
                    });
                } else if (a.startsWith("--level=") || a.startsWith("--difficulty=")) {
                    try {
                        s = s.withDifficulty(Difficulty.parse(value));
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Unknown level: " + value);
                    }
                } else {
                    throw new IllegalArgumentException("Unknown option: " + a);
                }
            }
            return new Options(console, help, s);
        }
    }
}
