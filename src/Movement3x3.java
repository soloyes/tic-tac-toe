/**
 * Created by sol
 * on 05.06.17 12:38.
 */
public class Movement3x3 {
    void setMovement(Game game) {
        Input input = new Input();
        if (GameField.currentMovement == 0) {
            if (Game.election.program) {
                GameField.data.set(4, 'X');
                System.out.println("I am first..");
                Game.gameField.draw(GameField.data, Game.N);
            } else {
                System.out.print(input.translate(input.checkCoordinate()));
                GameField.data.set(input.translate(input.checkCoordinate()), 'X');
                Game.gameField.draw(GameField.data, Game.N);
            }
        } else{
                System.out.println("Stop Game!");
                Game.game = false;
            }
            GameField.currentMovement++;
        }
    }
