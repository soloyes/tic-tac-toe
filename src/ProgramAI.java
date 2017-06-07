import java.util.*;

/**
 * Created by sol
 * on 07.06.17 17:21.
 */
public class ProgramAI {
    static Map<Integer,Integer> map;
    static {
        map = new HashMap<>();
        map.put(11, 0);     map.put(12, 1);     map.put(13, 2);
        map.put(21, 3);     map.put(22, 4);     map.put(23, 5);
        map.put(31, 6);     map.put(32, 7);     map.put(33, 8);
    }
    Integer randomBack(){
        List<Integer> keysAsArray = new ArrayList<>(map.keySet());
        Random r = new Random();
        return map.get(keysAsArray.get(r.nextInt(keysAsArray.size())));
    }

    void aiBack(){

    }

    void gameFieldAnalyse(){
        for (int j = 0; j <= GameField.data.size(); j+= Game.N ) {
            for (int i = j; i < Game.N + j; i++) {

            }
        }
    }
}
