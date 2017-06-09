import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * Created by sol
 * on 05.06.17 14:56.
 */
public class Input {

    static Integer checkCoordinate(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer in;
        for (; ;) {
            try{
                System.out.println("Movement example: raw and column (" +
                        (ProgramAI.randomBack().nextInt(GameField.N) + 1) +
                        (ProgramAI.randomBack().nextInt(GameField.N) + 1) +
                        ")");
                System.out.println("What is your's..?");
                in = Integer.parseInt(reader.readLine());
                if (in/10 > 0 &&
                        in/10 <= GameField.N &&
                        in % 10 > 0 &&
                        in % 10 <= GameField.N) break;
            } catch (Exception e){}
        }
    return in;
    }

    static Boolean checkExist(Integer in){
        return GameField.data.get(in) != GameField.fieldCharacter;
    }

    static Integer translate(Integer in){
        //Input translate to Array index
        return GameField.N*(in/10 - 1) + in%10 - 1;
    }
}
