import java.io.BufferedReader;
import java.io.InputStreamReader;

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
                if (in == 11 ||
                        in == 12 ||
                        in == 13 ||
                        in == 21 ||
                        in == 22 ||
                        in == 23 ||
                        in == 31 ||
                        in == 32 ||
                        in == 33) {
                    break;
                }
            } catch (Exception e){}
        }
    return in;
    }

    void checkExist(){

    }

    Integer translate(Integer in){
        //This is empirical calculated formula to convert the index of matrix 3*3 to index in ArrayList
        //Just belive it :)
        return (in/10)*(in/10-1) + in%10 - 1;
    }
}
