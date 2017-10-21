package View;

import Controller.*;
import Model.*;

/**
 * Created by sol on 21.10.17 10:00.
 */

public class Console {
    public static void gameConsole() {
        System.out.println("Game field size is " + GameField.N);
        for (; Game.isGame() ;) {
            Movement.nextMovement();
            GameField.drawField();
            Game.gameFieldAnalyse();
            Game.nextPlayer();
        }

        if (Game.getResult() == null) System.out.println("Nobody win's");
        else if (Game.getResult().equals(Players.PLAYER)) System.out.println(Players.PLAYER + " win's");
        else if (Game.getResult().equals(Players.PROGRAM)) System.out.println(Players.PROGRAM + " win's");

        System.out.println("End game!");
    }
}
