package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.controller.GameController;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;

/** The application window. */
public final class GameWindow extends JFrame {

    public GameWindow(GameController game) {
        super("Tic-Tac-Toe");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GamePanel panel = new GamePanel(game);
        setContentPane(panel);
        getContentPane().setBackground(Theme.BACKGROUND);
        setMinimumSize(new Dimension(580, 620));
        setSize(580, 700);
        setLocationRelativeTo(null);
    }

    /** Creates and shows the window on the Swing event thread. */
    public static void launch(GameController game) {
        if (System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT).contains("mac")) {
            System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAqua");
            System.setProperty("apple.awt.application.name", "Tic-Tac-Toe");
        }
        SwingUtilities.invokeLater(() -> new GameWindow(game).setVisible(true));
    }
}
