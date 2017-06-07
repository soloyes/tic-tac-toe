import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sol
 * on 05.06.17 14:56.
 */
public class Input {

    Integer checkCoordinate(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer in;
        for (; ;) {
            try{
                System.out.println("Example: 11, 12, 13, 21, 22, 23, 31, 32, 33");
                System.out.println("What is your's..?");
                in = Integer.parseInt(reader.readLine());
                if (in == 11 || in == 12 || in == 13 ||
                        in == 21 || in == 22 || in == 23 ||
                        in == 31 || in == 32 || in == 33) {
                    break;
                }
            } catch (Exception e){}
        }
    return in;
    }

    Boolean checkExist(Integer in){
        return GameField.data.get(in) != GameField.fieldCharacter;
    }

    Integer translate(Integer in){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(11, 0);     map.put(12, 1);     map.put(13, 2);
        map.put(21, 3);     map.put(22, 4);     map.put(23, 5);
        map.put(31, 6);     map.put(32, 7);     map.put(33, 8);
        return map.get(in);
    }
}
