package Controller;

import java.io.IOException;

import Model.*;
import static Main.Main.reader;

/**
 * Created by sol on 05.06.17 14:56.
 * - checkCoordinate() - check input coordinate's until well inputted.
 * - translate() - translate user's input to LinkedList position. Example: User input = 11 => 3*(0) + 1 -1 = 0.
 * Therefore 0 - is the array[0] item.
 */

class UserAI {
    private static Integer checkCoordinate(){
        Integer in;
        for (;;) {
            try {
                System.out.println("Movement example: raw and column (" +
                        (Game.random.nextInt(GameField.N) + 1) +
                        (Game.random.nextInt(GameField.N) + 1) +
                        ")");
                System.out.println("What is your's..?");
                in = Integer.parseInt(reader.readLine());
                if (in / 10 > 0 &&
                        in / 10 <= GameField.N &&
                        in % 10 > 0 &&
                        in % 10 <= GameField.N) break;
            } catch (NumberFormatException | IOException e) { }
        }
        return in;
    }

    private static Integer translate(Integer in){
        return GameField.N*(in/10 - 1) + in%10 - 1;
    }

    static Integer userMovement(){
        return translate(checkCoordinate());
    }
}