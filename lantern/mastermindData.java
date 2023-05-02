package lantern;
/*
 *  Copyright (C) 2010 Michael Ronald Adams.
 *  All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 *  This code is distributed in the hope that it will
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  General Public License for more details.
 */

import java.util.Random;
//import javax.jnlp.*;


public class mastermindData {
    int[][] guessGrid;
    int[] masterBoard;
    int[][] scoreGrid;
    int guessTop = 0;
    int guessMax = 10;
    int guessLimit = 4;

    int RED = 1; // guess Colors
    int BLUE = 2;
    int GREEN = 3;
    int YELLOW = 4;
    int PURPLE = 5;
    int ORANGE = 6;
    int BLACK = 1; // scores
    int WHITE = 2;

    int state;
    int ONGOING = 0;
    int DONE = 1;


    mastermindData() {
        guessGrid = new int[guessMax][guessLimit];
        scoreGrid = new int[guessMax][guessLimit];

        masterBoard = new int[4];
        resetGame();
        state = ONGOING;

    }//end constructor

    void resetGame() {
        for (int a = 0; a < guessMax; a++)
            for (int b = 0; b < guessLimit; b++) {
                guessGrid[a][b] = 0;
                scoreGrid[a][b] = 0;

            }
        guessTop = 0;
        generateMasterBoard();

    }

    boolean makeGuess(int[] guess) {
        if (guessTop >= guessMax)
            return false;
        for (int a = 0; a < guessLimit; a++) {
            guessGrid[guessTop][a] = guess[a];

        }
        return false;

    }

    boolean scoreGuess(int[] guess) {
        int[] scoreguess = new int[guessLimit];
        int[] scoremaster = new int[guessLimit];
        int scores = 0;
        for (int a = 0; a < guessLimit; a++) {
            scoreguess[a] = guess[a];
            scoremaster[a] = masterBoard[a];
            if (scoreguess[a] == scoremaster[a]) {
                scoreguess[a] = scoremaster[a] = 0;
                scoreGrid[guessTop][scores++] = BLACK;
            }
        }// end for

        // now right color wrong square;

        for (int b = 0; b < guessLimit; b++) {
            if (scoreguess[b] == 0)
                continue;
            for (int c = 0; c < guessLimit; c++) {
                if (scoreguess[b] == scoremaster[c]) {
                    scoremaster[c] = 0;
                    scoreGrid[guessTop][scores++] = WHITE;
                    break;

                }
            }
        }
        guessTop++;
        boolean win = false;
        if (scores == guessLimit) {
            win = true;
            for (int z = 0; z < guessLimit; z++)
                if (scoreGrid[guessTop - 1][z] != BLACK)
                    win = false;

            if (win == true)
                state = DONE;
        }

        if (guessTop == guessMax)
            state = DONE;
        return win;


    }

    void generateMasterBoard() {
        Random generator = new Random(System.currentTimeMillis());
        for (int a = 0; a < 4; a++) {

            int randomIndex = generator.nextInt(ORANGE) + 1; // ORANGE being current top
            masterBoard[a] = randomIndex;
        }
    }// end method generate master board


}// end class mastermind data

