package io.github.soloyes.tictactoe.view.swing;

import io.github.soloyes.tictactoe.ai.Difficulty;
import io.github.soloyes.tictactoe.controller.FirstPlayer;
import io.github.soloyes.tictactoe.controller.GameController;
import io.github.soloyes.tictactoe.controller.GameSettings;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Everything inside the window: header with settings, the board and the status bar.
 * It is a plain lightweight panel, so it can also be painted offscreen for screenshots.
 */
public final class GamePanel extends JPanel {
    /** Pause before the computer answers, so its move is easy to follow. */
    static final int COMPUTER_DELAY_MS = 380;

    private final GameController game;
    private final BoardPanel board;
    private final StatusBar status;
    private final SegmentedControl<Integer> sizeControl;
    private final SegmentedControl<FirstPlayer> firstControl;
    private final SegmentedControl<Difficulty> levelControl;
    private final Timer computerTimer;

    public GamePanel(GameController game) {
        super(new BorderLayout());
        this.game = game;
        setBackground(Theme.BACKGROUND);

        GameSettings s = game.settings();
        sizeControl = new SegmentedControl<>(List.of(3, 4), n -> n + "×" + n, s.boardSize());
        firstControl = new SegmentedControl<>(List.of(FirstPlayer.values()), FirstPlayer::label, s.firstPlayer());
        levelControl = new SegmentedControl<>(List.of(Difficulty.values()), Difficulty::label, s.difficulty());

        sizeControl.onSelect(n -> game.newGame(game.settings().withBoardSize(n)));
        firstControl.onSelect(f -> game.newGame(game.settings().withFirstPlayer(f)));
        levelControl.onSelect(game::setDifficulty);

        board = new BoardPanel(game);
        status = new StatusBar(game);

        add(buildHeader(), BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        computerTimer = new Timer(COMPUTER_DELAY_MS, e -> {
            if (game.isComputerTurn()) {
                game.playComputerMove();
            }
        });
        computerTimer.setRepeats(false);
        game.addListener(g -> onGameChanged());

        bindNewGameKey();
        onGameChanged();
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(18, 22, 4, 22));

        JPanel titleRow = row();
        JLabel title = new JLabel("Tic-Tac-Toe");
        title.setFont(Theme.font(Font.BOLD, 24f));
        title.setForeground(Theme.TEXT);
        titleRow.add(title);
        titleRow.add(Box.createHorizontalGlue());
        titleRow.add(new PillButton("New game", game::newGame));
        header.add(titleRow);
        header.add(Box.createVerticalStrut(14));

        JPanel controls = row();
        controls.add(labelled("Board", sizeControl));
        controls.add(Box.createHorizontalStrut(16));
        controls.add(labelled("First move", firstControl));
        controls.add(Box.createHorizontalStrut(16));
        controls.add(labelled("Level", levelControl));
        controls.add(Box.createHorizontalGlue());
        header.add(controls);
        return header;
    }

    private static JPanel row() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private static JComponent labelled(String caption, JComponent control) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(caption.toUpperCase(java.util.Locale.ROOT));
        l.setFont(Theme.font(Font.BOLD, 10.5f));
        l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 4, 6, 0));
        control.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(control);
        p.setMaximumSize(p.getPreferredSize());
        return p;
    }

    private void bindNewGameKey() {
        int menuMask = 0;
        try {
            menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        } catch (RuntimeException | Error ignored) {
            // headless toolkit: no shortcut modifier
        }
        var inputs = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        inputs.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "newGame");
        if (menuMask != 0) {
            inputs.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, menuMask), "newGame");
        }
        getActionMap().put("newGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.newGame();
            }
        });
    }

    private void onGameChanged() {
        sizeControl.setSelected(game.settings().boardSize());
        firstControl.setSelected(game.settings().firstPlayer());
        levelControl.setSelected(game.settings().difficulty());
        if (game.isComputerTurn()) {
            computerTimer.restart();
        } else {
            computerTimer.stop();
        }
    }

    /** Stops the pending computer move; used when rendering a static screenshot. */
    void stopTimers() {
        computerTimer.stop();
    }

    BoardPanel boardPanel() {
        return board;
    }

    StatusBar statusBar() {
        return status;
    }
}
