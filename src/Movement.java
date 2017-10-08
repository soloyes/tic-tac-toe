/**
 * Created by sol
 * on 05.06.17 12:38.
 * nextMovement() - if first step, check Election results. Other - check who is operate no by
 * GameField.currentMovement
 */

public class Movement {

    void nextMovement () {
        if (GameField.currentMovement == null) {
                if (Election.program) {
                GameField.data.set(ProgramAI.programMovement(0), Election.programChip);
                GameField.currentMovement = true;
                System.out.println("I am first..");
            } else {
                GameField.data.set(Input.translate(Input.checkCoordinate()), Election.userChip);
                GameField.currentMovement = false;
            }

        } else {
            Integer in;
            if (!GameField.currentMovement) {
                for (;;) {
                    in = Input.translate(Input.checkCoordinate());
                    if (Input.isEmptyPosition(in)) {
                        GameField.data.set(in, Election.userChip);
                        break;
                    }
                    System.out.println("Exist's value!");
                }
            } else {
                for (;;) {
                    in = ProgramAI.programMovement(1);
                    if (Input.isEmptyPosition(in)) {
                        GameField.data.set(in, Election.programChip);
                        break;
                    }
                }
            }
        }
        ProgramAI.gameFieldAnalyse();
        GameField.nextGamer();
    }
}