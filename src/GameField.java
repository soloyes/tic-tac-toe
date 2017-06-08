import java.util.LinkedList;
import java.util.List;

/**
 * Created by sol
 * on 05.06.17 11:45.
 */

public class GameField {
    static List<Character> data;
    static int N = 3;
    private static int M = N;
    static Character fieldCharacter = '_';
    //null - this is the first step
    //false - this is program step
    //true - this is user step
    static Boolean currentMovement = null;

    static {
        data = new LinkedList<>();
        for (int i = 0; i < N*M; i ++){   data.add(fieldCharacter);   }
    }

    static void drawField() {
        if (currentMovement) System.out.println("User:");
        else System.out.println("Program:");
        for (int j = 0; j < data.size(); j+= N ) {
            for (int i = j; i < N + j; i++) {
                System.out.print(data.get(i) + " ");
            }
            System.out.println();
        }
    }

    static void nextGamer(){
        currentMovement = !currentMovement;
    }

    static Integer getRealValue(int i, int j){
        return GameField.N*i + j;
    }
}
