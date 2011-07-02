/**
 * The freechess.org connection library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2002 Alexander Maryanovsky.
 * All rights reserved.
 *
 * The freechess.org connection library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The freechess.org connection library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the freechess.org connection library; if not, write to the Free
 * Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package free.freechess;

import java.util.StringTokenizer;
import free.util.Struct;


/**
 * A container for delta board information. More information is available by
 * issuing "help iv_compressmoves" on freechess.org
 */

public class DeltaBoardStruct extends Struct{



  /**
   * Creates a new DeltaBoardStruct with the specified arguments.
   *
   * @param gameNumber The game number.
   * @param pliesPlayedCount The amount of half-moves (plies) already played.
   * @param moveAlgebraic The move in algebraic format.
   * @param moveSmith The move in smith-warren format.
   * @param takenTime The amount of time taken to make the move, in
   * milliseconds.
   * @param remainingTime The amount of time remaining, in milliseconds.
   */

  public DeltaBoardStruct(int gameNumber, int pliesPlayedCount, String moveAlgebraic,
      String moveSmith, int takenTime, int remainingTime){

    if (pliesPlayedCount < 0)
      throw new IllegalArgumentException("The amount of plies played ("+pliesPlayedCount+") may not be negative");

    if (moveAlgebraic == null)
      throw new IllegalArgumentException("The move in algebraic format may not be null");

    if (moveSmith == null)
      throw new IllegalArgumentException("The move in Smith-Warren format may not be null");

    if (takenTime < 0)
      throw new IllegalArgumentException("The time taken to make the move ("+takenTime+") may not be negative");

    setIntegerProperty("GameNumber", gameNumber);
    setIntegerProperty("PliesPlayedCount", pliesPlayedCount);
    setStringProperty("MoveAlgebraic", moveAlgebraic);
    setStringProperty("MoveSmith", moveSmith);
    setIntegerProperty("TakenTime", takenTime);
    setIntegerProperty("RemainingTime", remainingTime);
  }




  /**
   * Parses the specified delta board line and returns a corresponding
   * DeltaBoardStruct board.
   */

  public static DeltaBoardStruct parseDeltaBoardLine(String line){
    StringTokenizer tokens = new StringTokenizer(line, " ");

    if (!tokens.nextToken().equals("<d1>")) // Skip the <d1> identifier
      throw new IllegalArgumentException("Missing \"<d1>\" identifier");

    int gameNumber = Integer.parseInt(tokens.nextToken()); // The game number.

    int pliesPlayedCount = Integer.parseInt(tokens.nextToken()); // The amount of plies played

    String moveAlgebraic = tokens.nextToken(); // The move in algebraic format
    String moveSmith = tokens.nextToken(); // The move in smith-warren format

    int takenTime = Integer.parseInt(tokens.nextToken()); // The amount of time taken to make the move
    int remainingTime = Integer.parseInt(tokens.nextToken()); // The amount of time remaining

    return new DeltaBoardStruct(gameNumber, pliesPlayedCount, moveAlgebraic, moveSmith, takenTime, remainingTime);
  }




  /**
   * Returns the game number.
   */

  public int getGameNumber(){
    return getIntegerProperty("GameNumber");
  }



  /**
   * Returns the amount of half-moves played in the game.
   */

  public int getPliesPlayedCount(){
    return getIntegerProperty("PliesPlayedCount");
  }



  /**
   * Returns the move in algebraic format.
   */

  public String getMoveAlgebraic(){
    return getStringProperty("MoveAlgebraic");
  }



  /**
   * Returns the move in Smith-Warren format.
   */

  public String getMoveSmith(){
    return getStringProperty("MoveSmith");
  }



  /**
   * Returns the amount of time taken to make the move, in milliseconds.
   */

  public int getTakenTime(){
    return getIntegerProperty("TakenTime");
  }



  /**
   * Returns the amount of time remaining, in milliseconds.
   */

  public int getRemainingTime(){
    return getIntegerProperty("RemainingTime");
  }


}


