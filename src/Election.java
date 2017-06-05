import java.util.Random;

/**
 * Created by sol
 * on 05.06.17 11:08.
 */
public class Election {
    //Who will be first? 0 - user, 1 - program
    Random random = new Random();
    boolean user = random.nextBoolean();
    boolean program = !user;
}
