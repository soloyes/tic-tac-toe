import java.util.*;

/**
 * Created by sol
 * on 07.06.17 17:21.
 */
public class ProgramAI {
    Integer randomBack() {
        Random r = new Random();
        return r.nextInt(GameField.data.size());
    }

    void aiBack() {

    }

    static void gameFieldAnalyse() {
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
            System.out.println(rawStatus + " " + columnStatus);
            if (rawStatus == 0 || columnStatus == 0) Game.game = false;
        }

        //Check Diagonale
        for (int i = 0; i < GameField.N; i++) {
            int diagonale1 = 0;
            int diagonale2 = 0;
            diagonale1 += GameField.data.get(GameField.getRealValue(i, i)) %
                    (GameField.currentMovement ?
                            intProgramChip :
                            intUserChip);
            diagonale2 += GameField.data.get(GameField.getRealValue(i,GameField.N -i -1)) %
                    (GameField.currentMovement ?
                            intProgramChip :
                            intUserChip);
        }
            if (diagonale1 == 0 || diagonale2 ==0) {
                Game.game = false;
            }
    }
}


