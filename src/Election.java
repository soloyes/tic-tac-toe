import java.util.Random;

/**
 * Created by sol
 * on 05.06.17 11:08.
 */
public class Election {
    //Who will be first? 0 - program, 1 - user
    static boolean program = ProgramAI.randomBack().nextBoolean();
    //Default chips
    static char userChip;
    static char programChip;
    static {
        userChip = 'X'; programChip = 'O';
        if (program){
            userChip = 'O'; programChip = 'X';
        }
    }
}
