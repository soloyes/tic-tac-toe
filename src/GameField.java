import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sol
 * on 05.06.17 11:45.
 */
public class GameField {
    private static char fieldCharacter = '_';
    static ArrayList<Character> data = new ArrayList<>();

    GameField(int N){
        for (int i = 0; i < N; i ++){   data.add(fieldCharacter);   }
    }

    void draw(ArrayList<Character> list, int n) {
        Iterator iterator = list.iterator();
        int j = 0;
        //Here we draw array as a lines, depends from field length
            for(;iterator.hasNext();){
                for (int i = j; i < n + j; i ++) {
                    System.out.print(iterator.next() + " ");
                }
                System.out.println();
                j += n;
            }
        }
}
