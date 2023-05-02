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

import java.util.ArrayList;


class highScoreManagement {
    ArrayList<scores> list;
    int top;
    int max;


    highScoreManagement() {
        list = new ArrayList();
        max = 10;
        top = 0;
    }

    void addScore(int score, String date, String name) {
        int spot = 0;

        //  handle  first score
        if (top == 0) {
            scores myscore = new scores();
            myscore.score = score;
            myscore.date = date;
            myscore.name = name;
            list.add(myscore);
            top++;
            return;


        }

        for (int a = 0; a < top; a++) {
            if (list.get(a).score > score) {
                reorder(score, date, name, a);
                return;
            }
        }

        if (top < max) {
            scores myscore = new scores();
            myscore.score = score;
            myscore.date = date;
            myscore.name = name;
            list.add(myscore);
            top++;

        }
    }

    void reorder(int score, String date, String name, int spot) {

        if (top < max) {
            scores ascore = new scores();
            list.add(ascore);
        }
        for (int a = max - 1; a > spot; a--) {
            if (a - 1 < top) {
                list.get(a).score = list.get(a - 1).score;
                list.get(a).date = list.get(a - 1).date;
                list.get(a).name = list.get(a - 1).name;

            }
        }
        list.get(spot).score = score;
        list.get(spot).date = date;
        list.get(spot).name = name;


        if (top < max)
            top++;

    }

    class scores {
        int score;
        String date;
        String name;


    }

}