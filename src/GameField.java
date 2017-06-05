import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sol
 * on 05.06.17 11:45.
 */
public class GameField {
    private static char fieldCharacter = '_';
    static ArrayList<Character> data = new ArrayList<>();
    static int currentMovement;

    GameField(int N, int M){
        currentMovement = 0;
        for (int i = 0; i < N*M; i ++){   data.add(fieldCharacter);   }
    }

    void draw(ArrayList<Character> list, int N) {
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
}
