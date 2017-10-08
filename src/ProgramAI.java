import java.util.*;
/**
 * Created by sol
 * on 07.06.17 17:21.
 * programMovement() - should be AI. If no AI and i = 0 => return 4, other case  return random.
 * gameFieldAnalyse() - check raw and column and diagonale. Here will be check whether use AI or not.
 * Now - just check the standard N*N field with N-win combination.
 */

public class ProgramAI {
    static Random randomBack() {
        return new Random();
    }

    static int programMovement(int i) {
        if (i == 0) return 4;
        else if (i == 1) return randomBack().nextInt(GameField.N*GameField.M);
        else return 666; //Here going to be AI return
    }

    static void gameFieldAnalyse() {
        //Check valid movement
        Game.game = false;
        for (int i = 0; i < GameField.N*GameField.M; i ++){
            Game.game |= Input.isEmptyPosition(i);
        }

        //Check raw and column
        char currentChip = GameField.currentMovement ? Election.programChip : Election.userChip;

        for (int i = 0; i < GameField.N; i ++){
            boolean rawStatus = true;
            boolean columnStatus = true;
            for (int j = 0; j < GameField.N; j ++){
                if (!(GameField.data.get(GameField.getRealValue(i, j)) == currentChip)) rawStatus = false;
                if (!(GameField.data.get(GameField.getRealValue(j, i)) == currentChip)) columnStatus = false ;
            }
            if (rawStatus || columnStatus){
                Game.game = false;
                Game.result = !GameField.currentMovement;
            }
        }

        //Check Diagonale
        boolean diag1 = true;
        boolean diag2 = true;
        for (int i = 0; i < GameField.N; i++) {
            if (!(GameField.data.get(GameField.getRealValue(i, i)) == currentChip))
                diag1 = false;
            if (!(GameField.data.get(GameField.getRealValue(i,GameField.N - i - 1)) == currentChip))
                diag2 = false;
        }
        if (diag1 || diag2){
            Game.game = false;
            Game.result = !GameField.currentMovement;
        }
    }
}