package Model;

import Controller.*;

/**
 * Created by sol
 * on 05.06.17 11:08.
 * Election by random. Who will be first? 0 - program, 1 - user
 * Player chip depend's from Model.Election's results.
 */

public class Election {
    private static boolean program = Game.random.nextBoolean();
    private static char playerChip;
    private static char programChip;

    public static char getPlayerChip() {
        return playerChip;
    }

    public static char getProgramChip() {
        return programChip;
    }

    public static Players currentPlayer(){
        if (program) return Players.PROGRAM;
        return Players.PLAYER;
    }

    static {
        playerChip = 'X'; programChip = 'O';
        if (program){
            playerChip = 'O'; programChip = 'X';
        }
    }
}