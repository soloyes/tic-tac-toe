/**
 * Created by sol
 * on 05.06.17 12:38.
 */
public class Movement3x3 {
    private Input input = new Input();
    void firstMovement() {
        if (GameField.currentMovement == -1) {
            if (Election.program) {
                GameField.data.set(4, Election.programChip);
                GameField.nextUser();
                System.out.println("I am first..");
            } else {
                GameField.data.set(input.translate(ProgramAI.map, input.checkCoordinate()), Election.userChip);
                GameField.nextProgram();
            }
        }
    }

    void nextMovement (){
        Integer in;
        if (GameField.currentMovement == 1){
            for (; ;){
                GameField.drawField(GameField.data);
                in = input.translate(ProgramAI.map, input.checkCoordinate());
                if (!input.checkExist(in)) {
                    GameField.data.set(in, Election.userChip);
                    GameField.currentMovement = 0;
                    break;
                }
                System.out.println("Exist's value!");
            }
        }
        else {
            for (; ;){
                in = Game.programai.randomBack();
                if (!input.checkExist(in)) {
                    GameField.data.set(in, Election.programChip);
                    GameField.currentMovement = 1;
                    break;
                }
            }
        }
    }
}

