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

class moveGenerator {
/*
from is where it places from moves, and to same.
board is current board to generate from, this gets called by current postion but also engine lines
*/

    int generatePawnMoves(int[] from, int[] to, int[] board, int top, int color, int type, int iflipped) {
        // this method only does pseudo legality and is only used to check legality
        for (int a = 0; a < 64; a++) {

            if (board[a] == type) // a pawn
            {

                if ((color == 1 && iflipped == 0) || (color == 0 && iflipped == 1)) {
                    // up 2
                    if (a > 7 && a < 16) {
                        if (board[a + 8] == 0 && board[a + 16] == 0) {
                            top = addMove(a, a + 16, from, to, top);

                        } // if 2 forward squares clear
                    }// if 7 16

                    if (board[a + 8] == 0) {
                        top = addMove(a, a + 8, from, to, top);

                    } // if foward square is clear
                    if (a + 9 < 64)
                        if (board[a + 9] > 0) {
                            if (!compare(board[a + 9], color))
                                top = addMove(a, a + 9, from, to, top);

                        } // capture diagonal


                    if (board[a + 7] > 0) {
                        if (!compare(board[a + 7], color))
                            top = addMove(a, a + 7, from, to, top);

                    } //  capture diagonal


                    if (a + 9 < 64)
                        if (board[a + 9] == 0 && a > 31 && a < 40) {
                            if ((!compare(board[a + 1], color) && (board[a + 1] == 1 || board[a + 1] == 7)))
                                top = addMove(a, a + 9, from, to, top);

                        } // capture diagonal enpassant

                    if (board[a + 7] == 0 && a > 31 && a < 40) {
                        if ((!compare(board[a - 1], color) && (board[a - 1] == 1 || board[a - 1] == 7)))
                            top = addMove(a, a + 7, from, to, top);

                    } // capture diagonal enpassant

                }// end if foward moving piece
                else {
                    // up 2
                    if (a > 47 && a < 56) {
                        if (board[a - 8] == 0 && board[a - 16] == 0) {
                            top = addMove(a, a - 16, from, to, top);

                        } // if 2 forward squares clear
                    }// if 7 16
                    if (board[a - 8] == 0) {
                        top = addMove(a, a - 8, from, to, top);

                    } // if foward square is clear

                    if (a - 9 >= 0)
                        if (board[a - 9] > 0) {
                            if (!compare(board[a - 9], color))
                                top = addMove(a, a - 9, from, to, top);

                        } // capture diagonal

                    if (board[a - 7] > 0) {
                        if (!compare(board[a - 7], color))
                            top = addMove(a, a - 7, from, to, top);

                    } //  capture diagonal

                    if (a - 9 >= 0)
                        if (board[a - 9] == 0 && a > 23 && a < 32) {
                            if ((!compare(board[a - 1], color) && (board[a - 1] == 1 || board[a - 1] == 7)))
                                top = addMove(a, a - 9, from, to, top);

                        } // capture diagonal enpassant

                    if (board[a - 7] == 0 && a > 23 && a < 32) {
                        if ((!compare(board[a + 1], color) && (board[a + 1] == 1 || board[a + 1] == 7)))
                            top = addMove(a, a - 7, from, to, top);

                    } // capture diagonal enpassant
                }// end else
            }// end pawn

        }// end loop through board

        return top;

    }

    int generateBishopMoves(int[] from, int[] to, int[] board, int top, int color, int type) {
        for (int a = 0; a < 64; a++) {

            if (board[a] == type) // a white bishop
            {
                // up right + 9
                int b = 0;

                for (b = 1; b < 8; b++) {
                    if (a + b * 9 > 63)
                        break;
                    if ((a + (b - 1) * 9) % 8 == 7)
                        break;
                    if (board[a + b * 9] != 0)// capture or own piece
                    {

                        if (compare(board[a + b * 9], color))
                            break;
                        else
                            top = addMove(a, a + b * 9, from, to, top);

                        break;
                    } else
                        top = addMove(a, a + b * 9, from, to, top);

                }// end up right

                // up left + 7
                for (b = 1; b < 8; b++) {
                    if (a + b * 7 > 63)
                        break;
                    if ((a + (b - 1) * 7) % 8 == 0)
                        break;
                    if (board[a + b * 7] != 0)// capture or own piece
                    {

                        if (compare(board[a + b * 7], color))
                            break;
                        else
                            top = addMove(a, a + b * 7, from, to, top);

                        break;
                    } else
                        top = addMove(a, a + b * 7, from, to, top);

                } // end up left


                // down right -7


                for (b = 1; b < 8; b++) {
                    if (a + b * -7 < 0)
                        break;
                    if ((a + (b - 1) * -7) % 8 == 7)
                        break;
                    if (board[a + b * -7] != 0)// capture or own piece
                    {

                        if (compare(board[a + b * -7], color))
                            break;
                        else
                            top = addMove(a, a + b * -7, from, to, top);

                        break;
                    } else
                        top = addMove(a, a + b * -7, from, to, top);

                } // end down right

                // down left -9
                for (b = 1; b < 8; b++) {
                    if (a + b * -9 < 0)
                        break;
                    if ((a + (b - 1) * -9) % 8 == 0)
                        break;
                    if (board[a + b * -9] != 0)// capture or own piece
                    {

                        if (compare(board[a + b * -9], color))
                            break;
                        else
                            top = addMove(a, a + b * -9, from, to, top);

                        break;
                    } else
                        top = addMove(a, a + b * -9, from, to, top);

                }// end down left


            }// end bishop

        }// end loop through board

        return top;


    }// end method generator bishop moves


    int generateRookMoves(int[] from, int[] to, int[] board, int top, int color, int type) {
        for (int a = 0; a < 64; a++) {

            if (board[a] == type) // a rook or queen
            {
                // up + 8
                int b = 0;

                for (b = 1; b < 8; b++) {
                    if (a + b * 8 > 63)
                        break;

                    if (board[a + b * 8] != 0)// capture or own piece
                    {

                        if (compare(board[a + b * 8], color))
                            break;
                        else
                            top = addMove(a, a + b * 8, from, to, top);

                        break;
                    } else
                        top = addMove(a, a + b * 8, from, to, top);

                }// end up right

                // right + 1
                for (b = 1; b < 8; b++) {
                    if (a + b > 63)
                        break;
                    if ((a + (b - 1)) % 8 == 7)
                        break;
                    if (board[a + b] != 0)// capture or own piece
                    {

                        if (compare(board[a + b], color))
                            break;
                        else
                            top = addMove(a, a + b, from, to, top);

                        break;
                    } else
                        top = addMove(a, a + b, from, to, top);

                } // end right + 1


                // down -8


                for (b = 1; b < 8; b++) {
                    if (a + b * -8 < 0)
                        break;

                    if (board[a + b * -8] != 0)// capture or own piece
                    {

                        if (compare(board[a + b * -8], color))
                            break;
                        else
                            top = addMove(a, a + b * -8, from, to, top);

                        break;
                    } else
                        top = addMove(a, a + b * -8, from, to, top);

                } // end down

                // left -1
                for (b = 1; b < 8; b++) {
                    if (a - b < 0)
                        break;
                    if ((a - (b - 1)) % 8 == 0)
                        break;
                    if (board[a - b] != 0)// capture or own piece
                    {

                        if (compare(board[a - b], color))
                            break;
                        else
                            top = addMove(a, a - b, from, to, top);

                        break;
                    } else
                        top = addMove(a, a - b, from, to, top);

                }// end down left


            }// end rook

        }// end loop through board

        return top;


    }// end method generator rook moves

    int generateKnightMoves(int[] from, int[] to, int[] board, int top, int color, int type) {
        for (int a = 0; a < 64; a++) {

            if (board[a] == type) // a rook or queen
            {
                // up + 10


                if (!(a + 10 > 63))
                    if (!((a + 10) % 8 == 0 || (a + 10) % 8 == 1)) {

                        if (board[a + 10] != 0)// capture or own piece
                        {

                            if (compare(board[a + 10], color))
                                ;
                            else
                                top = addMove(a, a + 10, from, to, top);


                        } else
                            top = addMove(a, a + 10, from, to, top);
                    }

                // up + 17


                if (!(a + 17 > 63))
                    if ((a + 17) % 8 != 0) {

                        if (board[a + 17] != 0)// capture or own piece
                        {

                            if (compare(board[a + 17], color))
                                ;
                            else
                                top = addMove(a, a + 17, from, to, top);


                        } else
                            top = addMove(a, a + 17, from, to, top);
                    }

                // up + 6


                if (!(a + 6 > 63))
                    if (!((a + 6) % 8 == 7 || (a + 6) % 8 == 6)) {

                        if (board[a + 6] != 0)// capture or own piece
                        {

                            if (compare(board[a + 6], color))
                                ;
                            else
                                top = addMove(a, a + 6, from, to, top);


                        } else
                            top = addMove(a, a + 6, from, to, top);
                    }

                // up + 15


                if (!(a + 15 > 63))
                    if ((a + 15) % 8 != 7) {

                        if (board[a + 15] != 0)// capture or own piece
                        {

                            if (compare(board[a + 15], color))
                                ;
                            else
                                top = addMove(a, a + 15, from, to, top);


                        } else
                            top = addMove(a, a + 15, from, to, top);
                    }

                // down - 10


                if (a - 10 >= 0)
                    if (!((a - 10) % 8 == 6 || (a - 10) % 8 == 7)) {

                        if (board[a - 10] != 0)// capture or own piece
                        {

                            if (compare(board[a - 10], color))
                                ;
                            else
                                top = addMove(a, a - 10, from, to, top);


                        } else
                            top = addMove(a, a - 10, from, to, top);
                    }

                // down - 17


                if (a - 17 >= 0)
                    if ((a - 17) % 8 != 7) {

                        if (board[a - 17] != 0)// capture or own piece
                        {

                            if (compare(board[a - 17], color))
                                ;
                            else
                                top = addMove(a, a - 17, from, to, top);


                        } else
                            top = addMove(a, a - 17, from, to, top);
                    }


                // down - 6


                if (a - 6 >= 0)
                    if (!((a - 6) % 8 == 0 || (a - 6) % 8 == 1)) {

                        if (board[a - 6] != 0)// capture or own piece
                        {

                            if (compare(board[a - 6], color))
                                ;
                            else
                                top = addMove(a, a - 6, from, to, top);


                        } else
                            top = addMove(a, a - 6, from, to, top);
                    }

                // down - 15


                if (a - 15 >= 0)
                    if ((a - 15) % 8 != 0) {

                        if (board[a - 15] != 0)// capture or own piece
                        {

                            if (compare(board[a - 15], color))
                                ;
                            else
                                top = addMove(a, a - 15, from, to, top);


                        } else
                            top = addMove(a, a - 15, from, to, top);
                    }


            }// end knight

        }// end loop through board

        return top;


    }// end method generator knight moves

    int addMove(int f, int t, int[] from, int[] to, int top) {

        from[top] = f;
        to[top] = t;
        return ++top;
    }

    boolean compare(int piece, int color) // return true on same color
    {

        if (color == 1)// white on move
        {
            if (piece < 7)
                return true;

            return false;
        }

        if (piece > 6)
            return true;

        return false;

    }// end compare method


}// end class