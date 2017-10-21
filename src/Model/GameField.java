package Model;

import java.util.LinkedList;
import java.util.List;

import Controller.*;

/**
 * Created by sol
 * on 05.06.17 11:45.
 * - getRealValue() - returns number of item in the ArrayList from 2-dimension coordinate.
 * - currentPlayer() - if current step is first, check Election results. Otherwise - check who is operate no by
 * value of currentMovement
 */

public class GameField {
    public static final int N = Game.random.nextInt(2) + 3;
    public static final int M = N;
    private static final Character FIELD_CHARACTER = '_';

    private static List<Character> field;

    static {
        field = new LinkedList<>();
        for (int i = 0; i < N*M; i ++){ field.add(FIELD_CHARACTER); }
    }

    public static List<Character> getField() {
        return field;
    }

    public static void drawField() {
        System.out.println(Game.getCurrentMovement().equals(Players.PLAYER) ? "User:" : "Program:");
        for (int i = 0; i < field.size(); i += N ) {
            for (int j = i; j < N + i; j ++) {
                System.out.print(field.get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static Integer getRealValue(int i, int j){
        return N*i + j;
    }

    public static Boolean isEmptyPosition(Integer in){
        return GameField.getField().get(in) == FIELD_CHARACTER;
    }
}