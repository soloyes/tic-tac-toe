/**
 * Created by sol
 * on 05.06.17 12:38.
 */
public class Movement3x3 {

    void nextMovement () {
        if (GameField.currentMovement == null) {
            if (Election.program) {
                GameField.data.set(4, Election.programChip);
                GameField.currentMovement = true;
                System.out.println("I am first..");
            } else {
                GameField.data.set(Input.translate(Input.checkCoordinate()), Election.userChip);
                GameField.currentMovement = false;
            }
        } else {
            Integer in;
            if (!GameField.currentMovement) {
                for (; ;) {
                    in = Input.translate(Input.checkCoordinate());
                    if (!Input.checkExist(in)) {
                        GameField.data.set(in, Election.userChip);
                        break;
                    }
                    System.out.println("Exist's value!");
                }
            } else {
                for (; ;) {
                    in = ProgramAI.randomBack(GameField.N*GameField.M);
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

