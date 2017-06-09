import java.util.*;

/**
 * Created by sol
 * on 07.06.17 17:21.
 */
public class ProgramAI {
    static Integer randomBack(int N) {
        Random r = new Random();
        return r.nextInt(N);
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
        int intUserChip = (int) Election.userChip;
        int intProgramChip = (int) Election.programChip;

        for (int i = 0; i < GameField.N; i ++){
            int rawStatus = 0;
            int columnStatus = 0;
            for (int j = 0; j < GameField.N; j ++){
                rawStatus += GameField.data.get(GameField.getRealValue(i, j)) %
                        (GameField.currentMovement ?
                                intProgramChip :
                                intUserChip);
                columnStatus += GameField.data.get(GameField.getRealValue(j, i)) %
                        (GameField.currentMovement ?
                                intProgramChip :
                                intUserChip);
            }
            if (rawStatus == 0 || columnStatus == 0) Game.game = false;
        }

        //Check Diagonale
        int diag1 = 0;
        int diag2 = 0;
        for (int i = 0; i < GameField.N; i++) {
            diag1 += GameField.data.get(GameField.getRealValue(i, i));
            diag2 += GameField.data.get(GameField.getRealValue(i,GameField.N - i - 1));
        }
        diag1 = diag1 % (GameField.currentMovement ?
                intProgramChip : intUserChip);
        diag2 = diag2 % (GameField.currentMovement ?
                intProgramChip : intUserChip);
        if (diag1 == 0 || diag2 == 0) Game.game = false;
    }
}
