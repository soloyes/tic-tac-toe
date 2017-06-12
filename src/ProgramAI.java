import java.util.*;

/**
 * Created by sol
 * on 07.06.17 17:21.
 */
public class ProgramAI {
    static Random randomBack() {
        return new Random();
    }

    void aiBack() {

    }

    static void gameFieldAnalyse() {
        //Check valid movement
        Game.game = false;
        for (int i = 0; i < GameField.N*GameField.M; i ++){
            Game.game |= !Input.checkExist(i);
        }

        //Here i check the sum of values of all positions.
        //Check raw and column
        char currentChip = GameField.currentMovement ? Election.programChip : Election.userChip;

        for (int i = 0; i < GameField.N; i ++){
            boolean rawStatus = true;
            boolean columnStatus = true;
            for (int j = 0; j < GameField.N; j ++){
                if (!GameField.data.get(GameField.getRealValue(i, j)).equals(currentChip)) rawStatus = false;
                if (!GameField.data.get(GameField.getRealValue(j, i)).equals(currentChip)) columnStatus = false ;
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
            if (!GameField.data.get(GameField.getRealValue(i, i)).equals(currentChip)) diag1 = false;
            if (!GameField.data.get(GameField.getRealValue(i,GameField.N - i - 1)).equals(currentChip)) diag2 = false;
        }
        if (diag1 || diag2){
            Game.game = false;
            Game.result = !GameField.currentMovement;
        }
    }
}
