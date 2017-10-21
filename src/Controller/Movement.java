package Controller;

import Model.*;

/**
 * Created by sol on 05.06.17 12:38.
 */

public class Movement {

    public static void nextMovement () {
        for (;;) {
            Integer in = ProgramAI.programMovement(1);
            if (Game.getCurrentMovement().equals(Players.PLAYER)) in = UserAI.userMovement();

            if (GameField.isEmptyPosition(in)){
                GameField.getField().set(in, Game.currentChip());
                break;
            }
            if(Game.getCurrentMovement().equals(Players.PLAYER)) System.out.println("Exist's value!");
        }
    }
}