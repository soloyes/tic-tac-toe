package View;

import Controller.Game;
import Controller.Movement;
import Model.Election;
import Model.Players;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sol on 21.10.17 10:00.
 */

public class GUI extends JFrame{
    private GUI(){
        Game.gameRestart();

        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300,300);
        setLocationRelativeTo(null);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout());

        JButton reStart = new JButton("Restart");
        JButton end = new JButton("End");

        GUIGameField guiGameField = new GUIGameField();
        guiGameField.setLayout(new GridLayout());

        reStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Game.gameRestart();
                if (Election.currentPlayer().equals(Players.PROGRAM)) {
                    Movement.nextGUIMovement();
                }
                System.out.println("Restart game");
                repaint();
            }
        });

        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Exit game");
                System.exit(0);
            }
        });

        bottomPanel.add(reStart);
        bottomPanel.add(end);

        add(bottomPanel, BorderLayout.SOUTH);
        add(guiGameField, BorderLayout.CENTER);

        setVisible(true);
    }
    public static GUI gameGUI(){
       return new GUI();
    }
}
