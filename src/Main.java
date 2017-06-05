import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by sol
 * on 05.06.17 11:05.
 */
public class Main {
    static int N = 9;
    //Who will be first? 0 - user, 1 - program
    static Election election = new Election();
    static int elected = election.getFirst(2);
    //

    public static void main(String[] args){
        GameField gameField = new GameField(N);
        gameField.draw(gameField.data, 3);


    }
}
