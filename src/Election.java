import java.util.Random;

/**
 * Created by sol
 * on 05.06.17 11:08.
 */
public class Election {
    //Who will be first? 0 - program, 1 - user
    private static Random random = new Random();
    static boolean program = random.nextBoolean();
    //Default chips
    static char userChip = 'X';
    static char programChip = 'O';

    static void setChip(){
        if (program){
            userChip = 'O';
            programChip = 'X';
        }
    }
}
