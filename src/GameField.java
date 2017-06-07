import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sol
 * on 05.06.17 11:45.
 */
public class GameField {
    static char fieldCharacter = '_';
    static ArrayList<Character> data = new ArrayList<>();
    //-1 - this is the first step
    // 0 - this is program step
    // 1 - this is user step
    static int currentMovement = -1;
    static int fullField = 0;

    GameField(int N, int M){
        for (int i = 0; i < N*M; i ++){   data.add(fieldCharacter);   }
    }

    static void drawField(ArrayList<Character> list, int N) {
        Iterator iterator = list.iterator();
        int j = 0;
        //Here we draw array as a lines, depends from field length
            for(;iterator.hasNext();){
                for (int i = j; i < N + j; i ++) {
                    System.out.print(iterator.next() + " ");
                }
                System.out.println();
                j += N;
            }
        }

    static void nextProgram(){
        currentMovement = 0;
    }

    static void nextUser(){
        currentMovement = 1;
    }
}
