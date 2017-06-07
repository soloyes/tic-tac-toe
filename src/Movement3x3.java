/**
 * Created by sol
 * on 05.06.17 12:38.
 */
public class Movement3x3 {
    private Input input = new Input();
    private ProgramAI programai = new ProgramAI();
    void firstMovement() {
        if (GameField.currentMovement == -1) {
            if (Game.election.program) {
                GameField.data.set(4, Game.election.program ? 'X' : 'O');
                GameField.nextUser();
                System.out.println("I am first..");
            } else {
                GameField.data.set(input.translate(input.checkCoordinate()), Game.election.user ? 'X' : 'O');
                GameField.nextProgram();
            }
        }
    }

    void nextMovement (){
        Integer in;
        if (GameField.currentMovement == 1){
            for (; ;){
                GameField.drawField(GameField.data, Game.N);
                in = input.translate(input.checkCoordinate());
                if (!input.checkExist(in)) {
                    GameField.data.set(in, GameField.currentMovement == 1 ? 'X' : 'O');
                    GameField.currentMovement = 0;
                    break;
                }
                System.out.println("Exist's value!");
            }
        }
        else {
            for (; ;){
                in = programai.randomBack();
                if (!input.checkExist(in)) {
                    GameField.data.set(in, GameField.currentMovement == 0 ? 'O' : 'X');
                    GameField.currentMovement = 1;
                    System.out.println(GameField.fullField);
                    break;
                }
            }
        }
    }
}

