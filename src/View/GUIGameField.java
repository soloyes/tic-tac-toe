package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Controller.Game;
import Controller.Movement;
import Model.Election;
import Model.GameField;
import Model.Players;

public class GUIGameField extends JPanel{

    private int cellWidth;
    private int cellHeight;

    public GUIGameField(){
        if (Election.currentPlayer().equals(Players.PROGRAM)) Movement.nextGUIMovement();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int clX = mouseEvent.getX()/cellWidth;
                int clY = mouseEvent.getY()/cellHeight;
                int position = GameField.getRealValue(clX, clY);
                if (Game.isGame()) {
                    if (GameField.isEmptyPosition(position)) {
                        GameField.getField().set(position, Election.getPlayerChip());
                        Movement.nextGUIMovement();
                    }
                    Game.isGameFieldFull();
                    Game.gameFieldAnalyse(Election.PLAYER_CHARACTER1);
                    Game.gameFieldAnalyse(Election.PLAYER_CHARACTER2);
                }
                repaint();
                System.out.println("Game status: " + Game.isGame());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        cellWidth = width / GameField.N;
        cellHeight = height / GameField.M;
        for (int i = 0; i < GameField.N; i ++) {
            graphics.drawLine(i*cellWidth, 0, i*cellWidth, height);
            graphics.drawLine(0, i*cellHeight, width, i*cellHeight);
        }

        for(int i = 0; i < GameField.N; i ++) {
            for (int j = 0; j < GameField.M; j ++) {
                if (GameField.getField().get(GameField.getRealValue(i ,j)) == Election.PLAYER_CHARACTER2) {
                    graphics.fillOval(i * cellWidth + 10, j * cellHeight + 10, cellWidth - 20, cellHeight - 20);
                }
            }
        }

        for(int i = 0; i < GameField.N; i ++) {
            for (int j = 0; j < GameField.M; j ++) {
                if (GameField.getField().get(GameField.getRealValue(i ,j)) == Election.PLAYER_CHARACTER1) {
                    graphics.drawOval(i * cellWidth + 10, j * cellHeight + 10, cellWidth - 20, cellHeight - 20);
                }
            }
        }
    }
}