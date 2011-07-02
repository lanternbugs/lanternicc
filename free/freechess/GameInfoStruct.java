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
 * A structure holding parsed information from a gameinfo line.
 * See the "help iv_gameinfo" helpfile on freechess.org for information about
 * the format.
 */

public class GameInfoStruct extends Struct{




  /**
   * Creates a new GameInfoStruct with the specified arguments.
   *
   * @param gameNumber The game number.
   * @param isGamePrivate Is the game private?
   * @param gameCategory A string specifying the category of the game. This will
   * contain the rating type if it's a chess game or the wild variant name if
   * it's a wild game.
   * @param isGameRated Is the game rated?
   * @param isWhiteRegistered Is the white player registered?
   * @param isBlackRegistered Is the black player registered?
   * @param whiteTime The initial amount of time on white's clock, in seconds.
   * @param whiteInc The amount of time white's clock is incremented by after
   * each move he makes, in seconds.
   * @param blackTime The initial amount of time on black's clock, in seconds.
   * @param blackInc The amount of time black's clock is incremented by after
   * each move he makes, in seconds.
   * @param partnerGameNumber The game number of the bughouse partner, or 0 if
   * it's not a bughouse game.
   * @param whiteRating White's rating.
   * @param whiteProvShow White's rating provshow character, 'E' is estimated,
   * 'P' if provisional and ' ' if neither.
   * @param blackRating Black's rating.
   * @param blackProvShow Black's rating provshow character, 'E' is estimated,
   * 'P' if provisional and ' ' if neither.
   * @param isWhiteTimesealed Is the white player using timeseal.
   * @param isBlackTimesealed Is the black player using timeseal.
   */

  public GameInfoStruct(int gameNumber, boolean isGamePrivate, String gameCategory,
      boolean isGameRated, boolean isWhiteRegistered, boolean isBlackRegistered,
      int whiteTime, int whiteInc, int blackTime, int blackInc, int partnerGameNumber,
      int whiteRating, char whiteProvShow, int blackRating, char blackProvShow, 
      boolean isWhiteTimesealed, boolean isBlackTimesealed){

    super(20);

    if (whiteTime < 0)
      throw new IllegalArgumentException("White's initial time ("+whiteTime+") may not be negative");

    if (blackTime < 0)
      throw new IllegalArgumentException("Black's initial time ("+blackTime+") may not be negative");

    if (whiteInc < 0)
      throw new IllegalArgumentException("White's increment ("+whiteInc+") may not be negative");

    if (blackInc < 0)
      throw new IllegalArgumentException("Black's increment ("+blackInc+") may not be negative");

    setIntegerProperty("GameNumber", gameNumber);
    setBooleanProperty("IsGamePrivate", isGamePrivate);
    setStringProperty("GameCategory", gameCategory);
    setBooleanProperty("IsGameRated", isGameRated);
    setBooleanProperty("IsWhiteRegistered", isWhiteRegistered);
    setBooleanProperty("IsBlackRegistered", isBlackRegistered);
    setIntegerProperty("WhiteTime", whiteTime);
    setIntegerProperty("WhiteInc", whiteInc);
    setIntegerProperty("BlackTime", blackTime);
    setIntegerProperty("BlackInc", blackInc);
    setIntegerProperty("PartnerGameNumber", partnerGameNumber);
    setIntegerProperty("WhiteRating", whiteRating);
    setCharProperty("WhiteProvShow", whiteProvShow);
    setIntegerProperty("BlackRating", blackRating);
    setCharProperty("BlackProvShow", blackProvShow);
    setBooleanProperty("IsWhiteTimesealed", isWhiteTimesealed);
    setBooleanProperty("IsBlackTimesealed", isBlackTimesealed);
  }




  /**
   * Parses a gameinfo line and returns a corresponding GameInfoStruct object.
   * Sample line:<br>
   * <g1> 1 p=0 t=blitz r=1 u=1,1 it=5,5 i=8,8 pt=0 rt=1586E,2100  ts=1,0<br>
   */

  public static GameInfoStruct parseGameInfoLine(String line){
    StringTokenizer tokens = new StringTokenizer(line, " ,=");

    if (!tokens.nextToken().equals("<g1>")) // Skip the <g1> identifier
      throw new IllegalArgumentException("Missing \"<g1>\" identifier");

    int gameNumber = Integer.parseInt(tokens.nextToken()); // Game number

    assertToken(tokens, "p"); // p=
    boolean isGamePrivate = parseBoolean(tokens.nextToken()); // Is the game private?

    assertToken(tokens, "t"); // t=
    String gameType = tokens.nextToken(); // Game type

    assertToken(tokens, "r"); // r=
    boolean isGameRated = parseBoolean(tokens.nextToken()); // Is the game rated?

    assertToken(tokens, "u"); // u=
    boolean isWhiteRegistered = parseBoolean(tokens.nextToken()); // Is white registered?
    boolean isBlackRegistered = parseBoolean(tokens.nextToken()); // Is black registered?

    assertToken(tokens, "it"); // it=
    int whiteTime = Integer.parseInt(tokens.nextToken()); // White's initial time
    int whiteInc = Integer.parseInt(tokens.nextToken()); // White's increment    

    assertToken(tokens, "i");
    int blackTime = Integer.parseInt(tokens.nextToken()); // Black's initial time
    int blackInc = Integer.parseInt(tokens.nextToken()); // Black's increment

    assertToken(tokens, "pt");
    int partnerGameNumber = Integer.parseInt(tokens.nextToken()); // The partner's game number

    assertToken(tokens, "rt");
    String whiteRatingString = tokens.nextToken(); // White's rating + provshow character
    String blackRatingString = tokens.nextToken(); // Black's rating + provshow character
    char whiteProvShow = ' ';
    char blackProvShow = ' ';
    if (!Character.isDigit(whiteRatingString.charAt(whiteRatingString.length() - 1))){
      whiteProvShow = whiteRatingString.charAt(whiteRatingString.length() - 1);
      whiteRatingString = whiteRatingString.substring(0, whiteRatingString.length() - 1);
    }
    if (!Character.isDigit(blackRatingString.charAt(blackRatingString.length() - 1))){
      blackProvShow = blackRatingString.charAt(blackRatingString.length() - 1);
      blackRatingString = blackRatingString.substring(0, blackRatingString.length() - 1);
    }
    int whiteRating = Integer.parseInt(whiteRatingString);
    int blackRating = Integer.parseInt(blackRatingString);

    assertToken(tokens, "ts");
    boolean isWhiteTimesealed = parseBoolean(tokens.nextToken()); // Is white timesealed?
    boolean isBlackTimesealed = parseBoolean(tokens.nextToken()); // Is black timesealed?

    return new GameInfoStruct(gameNumber, isGamePrivate, gameType, isGameRated, isWhiteRegistered,
      isBlackRegistered, whiteTime, whiteInc, blackTime, blackInc, partnerGameNumber,
      whiteRating, whiteProvShow, blackRating, blackProvShow, isWhiteTimesealed, isBlackTimesealed);
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
   * Checks that the next token for the given StringTokenizer is the specified
   * String. Throws an IllegalArgumentException if it isn't.
   */

  private static void assertToken(StringTokenizer tokenizer, String token){
   String realToken = tokenizer.nextToken();
   if (!realToken.equals(token))
     throw new IllegalArgumentException("Bad token \""+realToken+"\", expected \""+token+"\" instead");
  }




  /**
   * Returns the game number.
   */

  public int getGameNumber(){
    return getIntegerProperty("GameNumber");
  }




  /**
   * Returns <code>true</code> if the game is private, <code>false</code>
   * otherwise.
   */

  public boolean isGamePrivate(){
    return getBooleanProperty("IsGamePrivate");
  }




  /**
   * Returns the game category. This will be the rating category in case of
   * chess games and the wild variant name in case of a wild game.
   */

  public String getGameCategory(){
    return getStringProperty("GameCategory");
  }




  /**
   * Returns <code>true</code> if the game is rated, <code>false</code>
   * otherwise.
   */

  public boolean isGameRated(){
    return getBooleanProperty("IsGameRated");
  }




  /**
   * Returns <code>true</code> if the white player is registered,
   * <code>false</code> otherwise.
   */

  public boolean isWhiteRegistered(){
    return getBooleanProperty("IsWhiteRegistered");
  }




  /**
   * Returns <code>true</code> if the black player is registered,
   * <code>false</code> otherwise.
   */

  public boolean isBlackRegistered(){
    return getBooleanProperty("IsBlackRegistered");
  }




  /**
   * Returns the initial amount of time on white's clock, in seconds.
   */

  public int getWhiteTime(){
    return getIntegerProperty("WhiteTime");
  }




  /**
   * Returns the initial amount of time on black's clock, in seconds.
   */

  public int getBlackTime(){
    return getIntegerProperty("BlackTime");
  }




  /**
   * Returns the amount of time white's clock increases by after each move he
   * makes, in seconds.
   */

  public int getWhiteInc(){
    return getIntegerProperty("WhiteInc");
  }




  /**
   * Returns the amount of time black's clock increases by after each move he
   * makes, in seconds.
   */

  public int getBlackInc(){
    return getIntegerProperty("BlackInc");
  }




  /**
   * Returns the number of the partner's game, or 0 if none.
   */

  public int getPartnerGameNumber(){
    return getIntegerProperty("PartnerGameNumber");
  }




  /**
   * Returns white's rating.
   */

  public int getWhiteRating(){
    return getIntegerProperty("WhiteRating");
  }




  /**
   * Returns white's rating provshow character. 'E' if the rating is estimated,
   * 'P' if provisional, and ' ' if neither.
   */

  public char getWhiteProvShow(){
    return getCharProperty("WhiteProvShow");
  }




  /**
   * Returns black's rating.
   */

  public int getBlackRating(){
    return getIntegerProperty("BlackRating");
  }




  /**
   * Returns black's rating provshow character. 'E' if the rating is estimated,
   * 'P' if provisional, and ' ' if neither.
   */

  public char getBlackProvShow(){
    return getCharProperty("BlackProvShow");
  }




  /**
   * Returns <code>true</code> if white uses timesealing, <code>false</code>
   * otherwise.
   */

  public boolean isWhiteTimesealed(){
    return getBooleanProperty("IsWhiteTimesealed");
  }




  /**
   * Returns <code>true</code> if black uses timesealing, <code>false</code>
   * otherwise.
   */

  public boolean isBlackTimesealed(){
    return getBooleanProperty("IsBlackTimesealed");
  }


}
