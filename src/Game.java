/**
 * Created by sol
 * on 05.06.17 14:02.
 */
public class Game {
    //Field power. Version 1.0 is only about 3x3
    static boolean game = true;
    static int N = 3;
    static int M = 3;
    static {
        Election.setChip();
    }
    static ProgramAI programai = new ProgramAI();
}
