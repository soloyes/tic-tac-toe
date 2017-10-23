package Model;

import Controller.*;

/**
 * Created by sol
 * on 05.06.17 11:08.
 * Election by random.
 * Player chip depend's from Election's results.
 */

public class Election {
    private static boolean program = Game.random.nextBoolean();
    private static char playerChip;
    private static char programChip;
    public static final Character PLAYER_CHARACTER1 = 'X';
    public static final Character PLAYER_CHARACTER2 = 'O';

    public static char getPlayerChip() { return playerChip; }

    public static char getProgramChip() {
        return programChip;
    }

    public static Players currentPlayer() {
        if (program) return Players.PROGRAM;
        return Players.PLAYER;
    }

    public static void chipElection() {
        program = Game.random.nextBoolean();
        programChip = (program) ? PLAYER_CHARACTER1 : PLAYER_CHARACTER2;
        playerChip = (!program) ? PLAYER_CHARACTER1 : PLAYER_CHARACTER2;
    }
}