package Controller;

import java.util.Random;

import Model.*;

/**
 * Created by sol on 05.06.17 14:02.
 */

public class Game {
    public static Random random = new Random();
    private static boolean game;
    //Nobody wins by default (if there are no any free movements)
    private static Players result;
    private static Players currentMovement;

    public static boolean isGame() {
        return game;
    }
    public static void setGame(boolean game) {
        Game.game = game;
    }

    public static Players getResult() {
        return result;
    }
    public static void setResult(Players result) {
        Game.result = result;
    }

    public static Players getCurrentMovement() { return currentMovement; }

    public static void nextPlayer(){
        currentMovement = (currentMovement.equals(Players.PLAYER)) ? Players.PROGRAM : Players.PLAYER;
    }

    public static void gameRestart(){
        Game.setGame(true);
        Game.setResult(null);
        GameField.refreshField();
        Election.chipElection();
        currentMovement = Election.currentPlayer();
    }

    public static void isGameFieldFull(){
        Game.setGame(false);
        for (int i = 0; i < GameField.getField().size(); i ++)
            Game.setGame(Game.isGame() | GameField.isEmptyPosition(i));
    }

    public static void gameFieldAnalyse(char x) {
        //Check raw and column
        for (int i = 0; i < GameField.N; i ++){
            boolean rawStatus = true;
            boolean columnStatus = true;
            for (int j = 0; j < GameField.N; j ++){
                if (GameField.getField().get(GameField.getRealValue(i, j)) != x)
                    rawStatus = false;
                if (GameField.getField().get(GameField.getRealValue(j, i)) != x)
                    columnStatus = false ;
            }
            if (rawStatus || columnStatus) {
                Game.setGame(false);
                Game.setResult(currentMovement);
            }
        }

        //Check Diagonale
        boolean diag1 = true;
        boolean diag2 = true;
        for (int i = 0; i < GameField.N; i ++) {
            if (GameField.getField().get(GameField.getRealValue(i, i)) != x)
                diag1 = false;
            if (GameField.getField().get(GameField.getRealValue(i, GameField.N - i - 1)) != x)
                diag2 = false;
        }
        if (diag1 || diag2) {
            Game.setGame(false);
            Game.setResult(currentMovement);
        }
    }
}