package Controller;

import java.util.Random;

import Model.*;

/**
 * Created by sol on 05.06.17 14:02.
 * Default game status is true.
 * Default result is unknown (null).
 */

public class Game {
    public static Random random = new Random();
    private static boolean game = true;
    //Nobody wins by default (if there are no any free movements)
    private static Players result = null;
    private static Players currentMovement = Election.currentPlayer();

    public static boolean isGame() {
        return game;
    }
    static void setGame(boolean game) {
        Game.game = game;
    }

    public static Players getResult() {
        return result;
    }
    static void setResult(Players result) {
        Game.result = result;
    }

    public static Players getCurrentMovement() {
        return currentMovement;
    }

    public static void nextPlayer(){
        if (currentMovement.equals(Players.PLAYER)) currentMovement = Players.PROGRAM;
        else if (currentMovement.equals(Players.PROGRAM)) currentMovement = Players.PLAYER;
    }

    static char currentChip(){
        return Game.getCurrentMovement().equals(Players.PLAYER) ?
                Election.getPlayerChip() : Election.getProgramChip();
    }

    public static void gameFieldAnalyse() {
        //Check valid movement
        Game.setGame(false);
        for (int i = 0; i < GameField.getField().size(); i ++){
            Game.setGame(Game.isGame() | GameField.isEmptyPosition(i));
        }

        char currentChip = currentChip();

        //Check raw and column
        for (int i = 0; i < GameField.N; i ++){
            boolean rawStatus = true;
            boolean columnStatus = true;
            for (int j = 0; j < GameField.N; j ++){
                if (GameField.getField().get(GameField.getRealValue(i, j)) != currentChip) rawStatus = false;
                if (GameField.getField().get(GameField.getRealValue(j, i)) != currentChip) columnStatus = false ;
            }
            if (rawStatus || columnStatus){
                Game.setGame(false);
                Game.setResult(Game.getCurrentMovement());
            }
        }

        //Check Diagonale
        boolean diag1 = true;
        boolean diag2 = true;
        for (int i = 0; i < GameField.N; i++) {
            if (GameField.getField().get(GameField.getRealValue(i, i)) != currentChip)
                diag1 = false;
            if (GameField.getField().get(GameField.getRealValue(i, GameField.N - i - 1)) != currentChip)
                diag2 = false;
        }
        if (diag1 || diag2){
            Game.setGame(false);
            Game.setResult(Game.getCurrentMovement());
        }
    }
}