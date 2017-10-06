/**
 * Created by sol
 * on 05.06.17 12:38.
 */
public class Movement {

    void nextMovement () {
        //Nobody moved. So, check elections result
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
                    if (!Input.checkExist(in)) {
                        GameField.data.set(in, Election.userChip);
                        break;
                    }
                    System.out.println("Exist's value!");
                }
            } else {
                for (;;) {
                    in = ProgramAI.programMovement(1);
                    if (!Input.checkExist(in)) {
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

