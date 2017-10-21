package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sol on 21.10.17 10:00.
 */

public class GUI extends JFrame{
    private GUI(){
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout());

        JButton reStart = new JButton("Restart");
        JButton end = new JButton("End");

        reStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

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

        setVisible(true);
    }
    public static GUI gameGUI(){
       return new GUI();
    }
}
