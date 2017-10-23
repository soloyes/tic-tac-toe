package View;

import Controller.*;
import Main.Main;
import Model.*;

import java.io.IOException;

/**
 * Created by sol on 21.10.17 10:00.
 */

public class Console {
    public static void gameConsole() {
        Game.gameRestart();
        try {
            do {
                System.out.println("Game field size is " + GameField.N + "\n");
                for (; Game.isGame() ;) {
                    Movement.nextMovement();
                    GameField.drawField();
                    Game.isGameFieldFull();
                    Game.gameFieldAnalyse(Election.PLAYER_CHARACTER1);
                    Game.gameFieldAnalyse(Election.PLAYER_CHARACTER2);
                    Game.nextPlayer();
                }

                if (Game.getResult() == null) {
                    System.out.println("Nobody win's");
                }
                else if (Game.getResult().equals(Players.PLAYER)) {
                    System.out.println(Players.PLAYER + " win's");
                }
                else if (Game.getResult().equals(Players.PROGRAM)) {
                    System.out.println(Players.PROGRAM + " win's");
                }

                System.out.println("End game!");
                System.out.println("\nPlay again?\n1 - Yes\nOther - No");
                Game.gameRestart();

            }while (Main.reader.readLine().equals("1"));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
