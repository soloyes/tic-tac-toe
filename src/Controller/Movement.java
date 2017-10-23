package Controller;

import Model.*;

/**
 * Created by sol on 05.06.17 12:38.
 */

public class Movement {

    public static void nextMovement () {
        for (;;) {
            Integer in = ProgramAI.programMovement(1);
            if (Game.getCurrentMovement().equals(Players.PLAYER)) {
                in = UserAI.userMovement();
            }
            if (GameField.isEmptyPosition(in)){
                GameField.getField().set(in, (Game.getCurrentMovement().equals(Players.PLAYER) ?
                        Election.getPlayerChip() : Election.getProgramChip()));
                break;
            }
            if(Game.getCurrentMovement().equals(Players.PLAYER)) {
                System.out.println("Exist's value!");
            }
        }
    }

    public static void nextGUIMovement(){
        for (;;) {
            Game.isGameFieldFull();
            if (!Game.isGame()) {
                break;
            }
            Integer in = ProgramAI.programMovement(1);
            if (GameField.isEmptyPosition(in)){
                GameField.getField().set(in, Election.getProgramChip());
                break;
            }
        }
    }
}