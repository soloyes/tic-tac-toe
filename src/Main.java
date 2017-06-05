import java.io.IOException;

/**
 * Created by sol
 * on 05.06.17 11:05.
 */
public class Main {
    public static void main(String[] args) throws IOException{
        Game game = new Game(true);
        for (; game.game;){
            Movement3x3 movement3x3 = new Movement3x3();
            movement3x3.setMovement(game);
        }
    }
}
