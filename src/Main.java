/**
 * Created by sol
 * on 05.06.17 11:05.
 */
public class Main {
    public static void main(String[] args){
        Movement movement = new Movement();
        System.out.println("Game field size is " + GameField.N);
        for (; Game.game ;){
            movement.nextMovement();
            GameField.drawField();
        }

        if (Game.result == null)System.out.println("Nobody win's");
        else if (Game.result) System.out.println("User win's");
        else if (!Game.result) System.out.println("Program win's");

        System.out.println("End game!");
    }
}
