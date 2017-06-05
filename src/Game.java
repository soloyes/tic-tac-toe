/**
 * Created by sol
 * on 05.06.17 14:02.
 */
public class Game {
    //Field power. Version 1.0 is only about 3x3
    static int N = 3;
    static int M = 3;

    static boolean game;
    static GameField gameField;
    static Election election;

    Game(boolean game){
        this.game = game;
        gameField = new GameField(N, M);
        election = new Election();
    }
}
