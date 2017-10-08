/**
 * Created by sol
 * on 05.06.17 11:08.
 * Election by random. Who will be first? 0 - program, 1 - user
 * Player chip depend's from Election's results.
 */

public class Election {
    static boolean program = ProgramAI.randomBack().nextBoolean();
    static char userChip;
    static char programChip;
    static {
        userChip = 'X'; programChip = 'O';
        if (program){
            userChip = 'O'; programChip = 'X';
        }
    }
}
