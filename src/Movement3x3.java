import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by sol
 * on 05.06.17 12:38.
 */
public class Movement3x3 {
    void setMovement(Game game){
            if (game.gameField.currentMovement == 0) {
                if (game.election.program){
                    game.gameField.data.set(4, 'X');
                    System.out.println("I am first..");
                    game.gameField.draw(game.gameField.data, game.N);
                }
                else {
                    System.out.println("What is your's..?");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    try {
                        reader.readLine();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                game.gameField.currentMovement ++;
            }
            else {
                System.out.println("Stop Game!");
                game.game = false;
            }
    }
}

