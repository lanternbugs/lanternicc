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

import free.util.Struct;
import java.util.StringTokenizer;


/**
 * A structure holding parsed information from a style12 line.
 * See <A HREF="http://www.freechess.org/WWWhelp/style12.html">http://www.freechess.org/WWWhelp/style12.html</A>
 * or the "help style12" helpfile on freechess.org for information about the format.
 */

public class Style12Struct extends Struct{



  /**
   * The constant for the user's game.
   */

  public static final int MY_GAME = 1;



  /**
   * The constant for an observed game.
   */

  public static final int OBSERVED_GAME = 2;



  /**
   * The constant for a game which is just an isolated peek into someone's game.
   */

  public static final int ISOLATED_BOARD = 3;




  /**
   * Creates a new Style12Struct with the specified arguments. You would usually
   * want to use the <code>parseStyle12Line</code> method and not this
   * constructor to obtain Style12Struct objects.
   *
   * @param boardLexigraphic The current board, in lexigraphic format.
   * @param currentPlayer The player to move, either "B" or "W".
   * @param doublePawnPushFile The file of the double pawn push on the last
   * move, or -1 if the last move wasn't a double pawn push.
   * @param canWhiteCastleKingside Can white castle kingside?
   * @param canWhiteCastleQueenside Can white castle queenside?
   * @param canBlackCastleKingside Can black castle kingside?
   * @param canBlackCastleQueenside Can black castle queenside?
   * @param pliesSinceIrreversible The number of half moves made since the last
   * irreversible move (0 if the last move is irreversible).
   * @param gameNumber The game number.
   * @param whiteName The white player's nickname.
   * @param blackName The black player's nickname.
   * @param gameType The code for the type of the game. Possible values are
   * <code>MY_GAME</code>, <code>OBSERVED_GAME</code> and
   * <code>ISOLATED_BOARD</code>.
   * @param isPlayedGame <code>true</code> if the game is played,
   * @param isMyTurn Is it my turn or the opponent's? Only relevant if
   * <code>gameType</code> is <code>MY_GAME</code> and it's a played game.
   * <code>false</code> if it's an examined game.
   * @param initTime Initial time (in seconds) of the match.
   * @param increment Increment (in seconds) of the match.
   * @param whiteMaterialStrength White's material strength.
   * @param blackMaterialStrength Black's material strength.
   * @param whiteTime White's remaining time, in milliseconds.
   * @param blackTime Black's remaining time, in milliseconds.
   * @param nextMoveNumber The number of the move about to be made (standard
   * chess numbering -- White's and Black's first moves are both 1, etc.)
   * @param moveVerbose A verbose representation of the last move,
   * <code>null</code> if none.
   * @param movePretty A SAN (Standard Algebraic Notation) representation of the
   * last move, <code>null</code> if none.
   * @param moveTime The amount of time taken to make the last move, in
   * milliseconds.
   * @param isBoardFlipped <code>true</code> if the board should be flipped
   * (black at bottom), <code>false</code> otherwise.
   * @param isClockRunning <code>true</code> if the clock of the player to move
   * is running.
   * @param lag The lag incurred when making the move, in milliseconds.
   */

  public Style12Struct(String boardLexigraphic, String currentPlayer,
      int doublePawnPushFile, boolean canWhiteCastleKingside, 
      boolean canWhiteCastleQueenside, boolean canBlackCastleKingside,
      boolean canBlackCastleQueenside, int pliesSinceIrreversible,
      int gameNumber, String whiteName, String blackName, int gameType,
      boolean isPlayedGame, boolean isMyTurn, int initTime, int increment,
      int whiteMaterialStrength, int blackMaterialStrength, int whiteTime,
      int blackTime, int nextMoveNumber, String moveVerbose, String moveSAN,
      int moveTime, boolean isBoardFlipped, boolean isClockRunning, int lag){

    if ((doublePawnPushFile < -1) || (doublePawnPushFile > 7))
      throw new IllegalArgumentException("Bad value for double pawn push file: "+doublePawnPushFile);

    if (boardLexigraphic == null)
      throw new IllegalArgumentException("Board may not be null");

    if (boardLexigraphic.length() != 64)
      throw new IllegalArgumentException("Board string length ("+boardLexigraphic.length()+") must be 64 characters");

    if ((currentPlayer == null) || ((!currentPlayer.equals("W")) && (!currentPlayer.equals("B"))))
      throw new IllegalArgumentException("Current player string ("+currentPlayer+") must be either \"W\" or \"B\"");

    if (pliesSinceIrreversible < 0)
      throw new IllegalArgumentException("Plies since irreversible move ("+pliesSinceIrreversible+") cannot be negative");

    if (whiteName == null)
      throw new IllegalArgumentException("White name may not be null");

    if (blackName == null)
      throw new IllegalArgumentException("Black name may not be null");

    if (initTime < 0) // Can be 0 for examined games.
      throw new IllegalArgumentException("Initial time ("+initTime+") must be positive");

    if (increment < 0)
      throw new IllegalArgumentException("Increment ("+increment+") may not be negative");

    if (whiteMaterialStrength < 0)
      throw new IllegalArgumentException("White's material strength ("+whiteMaterialStrength+") may not be negative");

    if (blackMaterialStrength < 0)
      throw new IllegalArgumentException("Black's material strength ("+blackMaterialStrength+") may not be negative");

    if (nextMoveNumber <= 0)
      throw new IllegalArgumentException("The next move number ("+nextMoveNumber+") must be positive");

    if (moveTime < 0)
      throw new IllegalArgumentException("The amount of time taken by the last move ("+moveTime+") may not be negative");

    switch(gameType){
      case MY_GAME:
      case OBSERVED_GAME:
      case ISOLATED_BOARD:
        break;
      default:
        throw new IllegalArgumentException("Unknown game type: "+gameType);
    }

    if (lag < 0)
      throw new IllegalArgumentException("Lag may not be negative (really, it's against the laws of physics)");

    setStringProperty("BoardLexigraphic", boardLexigraphic);
    setStringProperty("CurrentPlayer", currentPlayer);
    setIntegerProperty("DoublePawnPushFile", doublePawnPushFile);
    setBooleanProperty("CanWhiteCastleKingside", canWhiteCastleKingside);
    setBooleanProperty("CanWhiteCastleQueenside", canWhiteCastleQueenside);
    setBooleanProperty("CanBlackCastleKingside", canBlackCastleKingside);
    setBooleanProperty("CanBlackCastleQueenside", canBlackCastleQueenside);
    setIntegerProperty("PliesSinceIrreversible", pliesSinceIrreversible);
    setIntegerProperty("GameNumber", gameNumber);
    setStringProperty("WhiteName", whiteName);
    setStringProperty("BlackName", blackName);
    setIntegerProperty("GameType", gameType);
    setBooleanProperty("IsPlayedGame", isPlayedGame);
    setBooleanProperty("IsMyTurn", isMyTurn);
    setIntegerProperty("InitTime", initTime);
    setIntegerProperty("Increment", increment);
    setIntegerProperty("WhiteMaterialStrength", whiteMaterialStrength);
    setIntegerProperty("BlackMaterialStrength", blackMaterialStrength);
    setIntegerProperty("WhiteTime", whiteTime);
    setIntegerProperty("BlackTime", blackTime);
    setIntegerProperty("NextMoveNumber", nextMoveNumber);
    setStringProperty("MoveVerbose", moveVerbose);
    setStringProperty("MoveSAN", moveSAN);
    setIntegerProperty("MoveTime", moveTime);
    setBooleanProperty("IsBoardFlipped", isBoardFlipped);
    setBooleanProperty("IsClockRunning", isClockRunning);
    setIntegerProperty("Lag", lag);
  }




  /**
   * Parses a style12 line and returns a corresponding Style12Struct object.
   */

  public static Style12Struct parseStyle12Line(String line){
    StringTokenizer tokens = new StringTokenizer(line, " ");

    if (!tokens.nextToken().equals("<12>")) // Skip the "<12>" identifier
      throw new IllegalArgumentException("Missing \"<12>\" identifier");

    StringBuffer posBuf = new StringBuffer(64);
    for (int i = 0; i < 8; i++)
      posBuf.append(tokens.nextToken()); // The board
    String positionLexigraphic = posBuf.toString();

    String currentPlayer = tokens.nextToken(); // The color string, either "B" or "W"

    int doublePawnPushFile = Integer.parseInt(tokens.nextToken()); // The double pawn push file

    boolean canWhiteCastleKingside = parseBoolean(tokens.nextToken()); // Can white castle kingside
    boolean canWhiteCastleQueenside = parseBoolean(tokens.nextToken()); // Can white castle queenside
    boolean canBlackCastleKingside = parseBoolean(tokens.nextToken()); // Can black castle kingside
    boolean canBlackCastleQueenside = parseBoolean(tokens.nextToken()); // Can black castle queenside

    int pliesSinceIrreversible = Integer.parseInt(tokens.nextToken()); // Number of plies since an irreversible move

    int gameNumber = Integer.parseInt(tokens.nextToken()); // The game number

    String whiteName = tokens.nextToken(); // White's name
    String blackName = tokens.nextToken(); // Black's name

    int myRelation = Integer.parseInt(tokens.nextToken()); // My relation to the game
    int gameType;
    boolean isPlayedGame;
    switch (myRelation){
      case -3:
        gameType = ISOLATED_BOARD;
        isPlayedGame = false;
        break;
      case -2:
        gameType = OBSERVED_GAME;
        isPlayedGame = false;
        break;
      case -1:
      case 1:
        gameType = MY_GAME;
        isPlayedGame = true;
        break;
      case 0:
        gameType = OBSERVED_GAME;
        isPlayedGame = true;
        break;
      case 2:
        gameType = MY_GAME;
        isPlayedGame = false;
        break;
      default:
        throw new IllegalArgumentException("Bad myRelation value: "+myRelation);
    }

    boolean isMyTurn = myRelation > 0;

    int initTime = 60*Integer.parseInt(tokens.nextToken()); // Initial time
    int increment = Integer.parseInt(tokens.nextToken()); // Increment

    int whiteMaterialStrength = Integer.parseInt(tokens.nextToken()); // White's material strength
    int blackMaterialStrength = Integer.parseInt(tokens.nextToken()); // Black's material strength

    int whiteTime = Integer.parseInt(tokens.nextToken()); // White's remaining time
    int blackTime = Integer.parseInt(tokens.nextToken()); // Black's remaining time

    int nextMoveNumber = Integer.parseInt(tokens.nextToken()); // The number of the next move

    String moveVerbose = tokens.nextToken(); // The move in verbose notation
    if (moveVerbose.equals("none"))
      moveVerbose = null;

    String moveTimeString = tokens.nextToken(); // The amount of time taken for the last move
    moveTimeString = moveTimeString.substring(1, moveTimeString.length() - 1);
    StringTokenizer timeTokens = new StringTokenizer(moveTimeString, ":.");
    int minutes = Integer.parseInt(timeTokens.nextToken());
    int seconds = Integer.parseInt(timeTokens.nextToken());
    int milliseconds = Integer.parseInt(timeTokens.nextToken());
    int moveTime = 60*1000*minutes + 1000*seconds + milliseconds;

    String moveSAN = tokens.nextToken(); // The move in SAN notation
    if (moveSAN.equals("none"))
      moveSAN = null;

    boolean isBoardFlipped = parseBoolean(tokens.nextToken()); // Is the board flipped?

    boolean isClockRunning = parseBoolean(tokens.nextToken()); // Is the clock of the player to move running?

    int lag = Integer.parseInt(tokens.nextToken()); // The lag, in milliseconds.

    return new Style12Struct(positionLexigraphic, currentPlayer, doublePawnPushFile, canWhiteCastleKingside,
      canWhiteCastleQueenside, canBlackCastleKingside, canBlackCastleQueenside, pliesSinceIrreversible,
      gameNumber, whiteName, blackName, gameType, isPlayedGame, isMyTurn, initTime, increment,
      whiteMaterialStrength, blackMaterialStrength, whiteTime, blackTime, nextMoveNumber, moveVerbose,
      moveSAN, moveTime, isBoardFlipped, isClockRunning, lag);
  }




  /**
   * If the given string is "1", return <code>true</code>. If it's "0", returns
   * <code>false</code>. Otherwise throws an IllegalArgumentException.
   */

  private static boolean parseBoolean(String val){
    if (val.equals("1"))
      return true;
    else if (val.equals("0"))
      return false;
    else
      throw new IllegalArgumentException("Bad boolean value: "+val);
  }
  




  /**
   * Returns the current board in lexigraphic format.
   */

  public String getBoardLexigraphic(){
    return getStringProperty("BoardLexigraphic");
  }




  /**
   * Returns the current board in FEN format.
   */

  public String getBoardFEN(){
    StringBuffer buf = new StringBuffer();
    String boardLexigraphic = getBoardLexigraphic();
    int emptySquareCounter = 0;
    for (int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        char c = boardLexigraphic.charAt(j+i*8);
        if (c == '-')
          emptySquareCounter++;
        else{
          if (emptySquareCounter != 0){
            buf.append(emptySquareCounter);
            emptySquareCounter = 0;
          }
          buf.append(c);
        }
      }
      if (emptySquareCounter != 0){
        buf.append(emptySquareCounter);
        emptySquareCounter = 0;
      }
      if (i != 7)
        buf.append('/');
    }

    buf.append(" ");

    buf.append(getCurrentPlayer().toLowerCase());

    buf.append(" ");

    StringBuffer castlingAvailability = new StringBuffer();
    if (canWhiteCastleKingside())
      castlingAvailability.append('K');
    if (canWhiteCastleQueenside())
      castlingAvailability.append('Q');
    if (canBlackCastleKingside())
      castlingAvailability.append('k');
    if (canBlackCastleQueenside())
      castlingAvailability.append('q');

    buf.append(castlingAvailability.length() == 0 ? "-" : castlingAvailability.toString());

    buf.append(" ");

    String enPassantSquare;
    if (getDoublePawnPushFile() == -1)
      enPassantSquare = "-";
    else
      enPassantSquare = "" + ('a' + getDoublePawnPushFile()) + (getCurrentPlayer().equals("W") ? "6" : "3");

    buf.append(enPassantSquare);

    buf.append(" ");

    buf.append(getPliesSinceIrreversible());

    buf.append(" ");

    buf.append(getNextMoveNumber());

    return buf.toString();
  }



  
  /**
   * Returns a string representing the player whose turn it currently is, either
   * "W" or "B".
   */

  public String getCurrentPlayer(){
    return getStringProperty("CurrentPlayer");
  }




  /**
   * Returns the file of the double pawn push on the last move, or -1 if the
   * last move wasn't a double pawn push.
   */

  public int getDoublePawnPushFile(){
    return getIntegerProperty("DoublePawnPushFile");
  }




  /**
   * Returns <code>true</code> if white can still castle kingside. Returns
   * <code>false</code> otherwise.
   */

  public boolean canWhiteCastleKingside(){
    return getBooleanProperty("CanWhiteCastleKingside");
  }




  /**
   * Returns <code>true</code> if white can still castle queenside. Returns
   * <code>false</code> otherwise.
   */

  public boolean canWhiteCastleQueenside(){
    return getBooleanProperty("CanWhiteCastleQueenside");
  }

  


  /**
   * Returns <code>true</code> if black can still castle kingside. Returns
   * <code>false</code> otherwise.
   */

  public boolean canBlackCastleKingside(){
    return getBooleanProperty("CanBlackCastleKingside");
  }




  /**
   * Returns <code>true</code> if black can still castle queenside. Returns
   * <code>false</code> otherwise.
   */

  public boolean canBlackCastleQueenside(){
    return getBooleanProperty("CanBlackCastleQueenside");
  }




  /**
   * Returns the amount of half moves made since the last irreversible move, or 0 if
   * the last move was irreversible.
   */

  public int getPliesSinceIrreversible(){
    return getIntegerProperty("PliesSinceIrreversible");
  }




  /**
   * Returns the game number.
   */

  public int getGameNumber(){
    return getIntegerProperty("GameNumber");
  }




  /**
   * Returns the white player's nickname.
   */

  public String getWhiteName(){
    return getStringProperty("WhiteName");
  }




  /**
   * Returns the black player's nickname.
   */

  public String getBlackName(){
    return getStringProperty("BlackName");
  }




  /**
   * Returns the game type. Possible values are <code>MY_GAME</code>, 
   * <code>OBSERVED_GAME</code> and <code>ISOLATED_BOARD</code>.
   */

  public int getGameType(){
    return getIntegerProperty("GameType");
  }




  /**
   * Returns <code>true</code> if the game is a played game, <code>false</code>
   * if examined.
   */

  public boolean isPlayedGame(){
    return getBooleanProperty("IsPlayedGame");
  }



  /**
   * Returns <code>true</code> if it's the user's turn, <code>false</code> if
   * it's the opponent's turn. This is only relevant if the game type is
   * <code>MY_GAME</code> and it's a played game.
   *
   * @throws IllegalStateException if the game type is not <code>MY_GAME</code>
   * or it's not a played game.
   */

  public boolean isMyTurn(){
    if ((getGameType() != MY_GAME) || !isPlayedGame())
      throw new IllegalStateException("Either the game type is not MY_GAME or it's not a played game.");

    return getBooleanProperty("IsMyTurn");
  }



  /**
   * Returns the initial time in the match, in seconds.
   */

  public int getInitialTime(){
    return getIntegerProperty("InitTime");
  }



  /**
   * Returns the increment in the time, in seconds.
   */

  public int getIncrement(){
    return getIntegerProperty("Increment");
  }



  /**
   * Returns white's material strength.
   */

  public int getWhiteMaterialStrength(){
    return getIntegerProperty("WhiteMaterialStrength");
  }



  /**
   * Returns black's material strength.
   */

  public int getBlackMaterialStrength(){
    return getIntegerProperty("BlackMaterialStrength");
  }



  /**
   * Returns white's remaining time, in seconds.
   */

  public int getWhiteTime(){
    return getIntegerProperty("WhiteTime");
  }



  /**
   * Returns black's remaining time, in seconds.
   */

  public int getBlackTime(){
    return getIntegerProperty("BlackTime");
  }



  /**
   * Returns the number of the next move. (standard chess numbering -- White's
   * and Black's first moves are both 1, etc.)
   */

  public int getNextMoveNumber(){
    return getIntegerProperty("NextMoveNumber");
  }




  /**
   * Returns the amount of plies since the beginning of the game. The method
   * assumes that the first player to move was white.
   */

  public int getPlayedPlyCount(){
    return (getNextMoveNumber()-1)*2 + (getCurrentPlayer().equals("B") ? 1 : 0);
  }



  /**
   * Returns a verbose representation of the move, or <code>null</code> if none.
   * The format is either "o-o", "o-o-o" or something like "Q/c7-c5" or
   * for promotions "P/g7-h8=Q".
   */

  public String getMoveVerbose(){
    return getStringProperty("MoveVerbose");
  }



  /**
   * Returns a SAN (Standard Algebraic Notation) representation of the move, or
   * <code>null</code> if none.
   */

  public String getMoveSAN(){
    return getStringProperty("MoveSAN");
  }



  /**
   * Returns the amount of time it took to make the last move, in seconds.
   */

  public int getMoveTime(){
    return getIntegerProperty("MoveTime");
  }



  /**
   * Returns <code>true</code> if the board is flipped (black at bottom), false
   * otherwise.
   */

  public boolean isBoardFlipped(){
    return getBooleanProperty("IsBoardFlipped");
  }



  /**
   * Returns <code>true</code> if the clock of the player to move is running.
   */

  public boolean isClockRunning(){
    return getBooleanProperty("IsClockRunning");
  }



  /**
   * Returns the amount of lag incurred when making the move that caused this
   * style12 message to be sent.
   */

  public int getLag(){
    return getIntegerProperty("Lag");
  }



}
