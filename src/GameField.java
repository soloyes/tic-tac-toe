import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sol
 * on 05.06.17 11:45.
 */
public class GameField {
    static ArrayList<Character> data;
    static char fieldCharacter = '_';
    //-1 - this is the first step
    // 0 - this is program step
    // 1 - this is user step
    static int currentMovement = -1;

    static {
        data = new ArrayList<>();
        for (int i = 0; i < Game.N*Game.M; i ++){   data.add(fieldCharacter);   }
    }

    static void drawField(ArrayList<Character> list) {
        for (int j = 0; j < GameField.data.size(); j+= Game.N ) {
            for (int i = j; i < Game.N + j; i++) {
                System.out.print(GameField.data.get(i) + " ");
            }
            System.out.println();
        }
    }

    static void nextProgram(){
        currentMovement = 0;
    }

    static void nextUser(){
        currentMovement = 1;
    }

}
