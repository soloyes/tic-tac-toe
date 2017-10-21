package Controller;

import Model.*;

/**
 * Created by sol
 * on 07.06.17 17:21.
 * programMovement() - should be AI. If no AI and i = 0 => return 4, other case  return random.
 * gameFieldAnalyse() - check raw and column and diagonale. Here will be check whether use AI or not.
 * Now - just check the standard N*N field with N-win combination.
 */

public class ProgramAI {

    static Integer programMovement(int i) {
        if (i == 0) return 4;
        else if (i == 1) return Game.random.nextInt(GameField.N * GameField.M);
        else return 666; //Here going to be AI return. Do i later.
    }
}