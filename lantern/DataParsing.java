package lantern;

import free.freechess.Style12Struct;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

class GameStartData {
    String whiteElo = "";
    String blackElo = "";
    String whiteTitle = "";
    String blackTitle = "";
    String rated = "";
    String ratingType = "";
    String wild = null;
    ArrayList<String> moves = new ArrayList<>();

}

class HashTellData {
    static String type = "C";
    /*
     "C" = console
     "B" == board
     "L" == Lookup
     */
    static int number = -1;
    static int userHashKey = -1;
}

public class DataParsing {
    int UNKNOWN_TYPE = 0;
    int NO_TYPE = 1;
    int CHANNEL_TELL = 2;
    int PERSONAL_TELL = 3;
    int SEEKING_LINE = 4;
    int SHOUT_TELL = 5;
    int SEEK_REMOVE = 6;
    int SEEK_REMOVE_ALL = 7;
    int SEEK_ADD = 8;
    int GAME_START = 2;
    int SEND_MOVE = 3;
    int GAME_ENDED = 4;
    int HISTORY_LIST = 9;
    int JOURNAL_LIST = 10;
    int NOTIFY_TYPE = 11;
    int CSHOUT_TYPE = 12;
    int SAY_TYPE = 13;
    int CHANNEL_LIST_TYPE = 14;
    int KIB_TYPE = 15;
    int HISTORY_TYPE = 16;
    int JOURNAL_TYPE = 17;
    int HASH_KEY_TYPE = 18;
    static boolean setChannelTabs = false;
    boolean processingLater = false;


    ArrayList<String> spaceSeperatedLine;
    int ficsType = NO_TYPE;
    static ConcurrentLinkedQueue<URL> soundURLQueue = new ConcurrentLinkedQueue<URL>();
    static boolean soundCurrentlyPlaying = false;
    int lineCount = 0;
    String ficsChatTell = "";
    String ficsChatTell2 = "";
    String lastGameListName = "";
    boolean skipShowingGameList = false;
    String lastGameStartString = "";
    static boolean backedUp = false; // resets on connect. for examine mode
    static boolean inFicsExamineMode = false;
    //static ArrayList<GameState> openGames = new ArrayList<GameState>();
    //static GameState mygame;

    ConcurrentLinkedQueue<myoutput> sendQueueConsole;
    ConcurrentLinkedQueue<newBoardData> gamequeue;
    String lastCreatingBlackELO = "";
    String lastCreatingWhiteELO = "";
    Map gameStartMap = new HashMap();
    channels mySettings;
    docWriter myDocWriter;
    int maxLinks = 75;
    int SUBFRAME_CONSOLES = 0;
    int GAME_CONSOLES = 1;
    int SUBFRAME_NOTIFY = 2;
    static boolean sentInChannel = false;
    chessbot4 mainTelnet;


    String myinput = "";
    char icc_data[] = new char[5000];
    int dataTop = -1;


    public DataParsing(channels sharedSettings, ConcurrentLinkedQueue<myoutput> sendQueueConsole1, ConcurrentLinkedQueue<newBoardData> gamequeue1, docWriter myDocWriter1, chessbot4 mainTelnet1) {
        spaceSeperatedLine = new ArrayList<String>();
        mySettings = sharedSettings;
        sendQueueConsole = sendQueueConsole1;
        gamequeue = gamequeue1;
        myDocWriter = myDocWriter1;
        mainTelnet = mainTelnet1;
        setFakeData();
        Random rand = new Random();
        HashTellData.userHashKey = rand.nextInt(1000000000);


    }


    void setFakeData() {

    }

    void getData(String data) {

        //  printInCOut("in here using a c style print");

        // if([data length]  < 3)
        //    return;
        // if([data hasPrefix:@"aics%"])
        //     return;
        int temp_top = 0;
        boolean fixedError = false;
        while (temp_top < data.length()) {
            dataTop = dataTop + 1; // should start for new data at -1 and this brings it to 0
            if (dataTop > 5 && icc_data[0] != '\031' && data.charAt(temp_top) == '\031' && icc_data[0] == 'R') {
                dataTop = 0;
                fixedError = true;
            }
            icc_data[dataTop] = data.charAt(temp_top);


            if (dataTop > 0 && icc_data[dataTop] == ')' && icc_data[dataTop - 1] == '\031') {

                if (icc_data[0] != '\031') {
                    if (fixData()) {
                        //[self parseDatagram:tv:soc];
                        String newdata = "";
                        for (int z = 0; z <= dataTop; z++) {
                            newdata += icc_data[z];
                        }
                        Datagram1 gram = new Datagram1(newdata);
                        parseDatagram(gram);
                    }
                } else {
                    String newdata = "";
                    for (int z = 0; z <= dataTop; z++) {
                        newdata += icc_data[z];
                    }
                    Datagram1 gram = new Datagram1(newdata);
                    parseDatagram(gram);
                }

                reset();
            } else if ((!mySettings.fics && icc_data[0] != '\031' && icc_data[dataTop] == '\n')

                    || (mySettings.fics && icc_data[dataTop] == '\r')) {
                if (mySettings.fics && dataTop > 1) {
                    String newdata = "";
                    for (int z = 0; z < dataTop; z++) {
                        newdata += icc_data[z];
                    }

                    processLine(newdata);
                } else {
                    if (dataTop > 1) {
                        if (!mySettings.fics) {
                            String newdata = "";
                            for (int z = 0; z < dataTop; z++) {
                                newdata += icc_data[z];
                            }

                            processLine(newdata);
                        }

                    }

                }

                reset();

            } else if (dataTop == 5 && !mySettings.fics) {
                if (icc_data[0] == 'a' && icc_data[1] == 'i' && icc_data[2] == 'c' && icc_data[3] == 's' && icc_data[4] == '%' && icc_data[5] == ' ') {
                    processLine("fics%");
                    reset();


                }
            } else if (dataTop == 5) {
                if (icc_data[0] == 'f' && icc_data[1] == 'i' && icc_data[2] == 'c' && icc_data[3] == 's' && icc_data[4] == '%' && icc_data[5] == ' ') {
                    processLine("fics%");
                    reset();


                }
            }


            temp_top++;
        }

        // [self reset];
    }

    void reset() {
        dataTop = -1;

    }

    void processLine(String data) {
        // myConsole.printToConsole(data);
        ficsProcessLine(data);
    }

    boolean fixData() {
        boolean go = false;
        int i = 0;
        for (int a = 0; a < dataTop; a++) {
            if (icc_data[a] == '\031' && go == false) {
                go = true;
            }
            if (go) {
                icc_data[i] = icc_data[a];
                i++;
            }
        }
        if (go) {
            dataTop = i - 1;
        }
        if (dataTop > 0 && icc_data[dataTop - 1] == '\031') {
            return true;
        }
        return false;


    }

    void ficsProcessLine(String data) {
        if (!lastGameStartString.equals("")) {
            setGameStartParamsAsNeeded(lastGameStartString);
        }
        if (isPrompt(data)) {
            if (ficsType != HASH_KEY_TYPE) {
                sendOutChat();
            }

            resetParsing();
            return;
        } else if (ficsChatTell.contains("(Logout screen by Alefith)") && ficsChatTell.length() > 400) {
            sendOutChat();
            resetParsing();
        }
        // Log.d("TAG", "ficsProcessLine: mike data is " + data);
        if (data.equals("[G]")) {
            return;
        }

        try {
            if (!data.startsWith("<s") && processGameLine(data)) {
                return;
            }
        } catch (Exception gameer) {
            if (data.startsWith("<12>")) {
                return;
            }
        }

        addToNotifyOnLoginAsNeeded(data);


        if ((ficsType == NO_TYPE || (ficsType == UNKNOWN_TYPE && mySettings.whoAmI.equals("")))
                && (lineCount == 0 || mySettings.whoAmI.equals(""))) {
            seperateLine(data, spaceSeperatedLine);
            ficsType = getType(data);


        } else if (data.startsWith("<s") && !mySettings.whoAmI.equals("")) {
            seperateLine(data, spaceSeperatedLine);
            ficsType = getType(data);
        }

        if (mySettings.whoAmI.equals("")) {
            checkIfLoggedIn();
        }
        if (data.trim().equals("") && lineCount == 0) {
            resetParsing();
            return;
        }

        lineCount++;
        if (mySettings.whoAmI.equals("") && !mySettings.fics && mySettings.amazonBuild) {
            data = "***ICC connecting...";
        }
        if (data.startsWith("E1 The feature you're attempting to use requires Full Membership.")) {

            data = "Woops. That's a member's command.  Guests can type in console 'seek' for a quick game, tell 1 my question, and if played, type 'examine -1' to make your last game appear on board to be examined.";
        } else if (data.startsWith("E2 The feature you're attempting to use requires Full Membership.")) {

            data = "Woops. That's a member's command.  Guests can type in console 'seek' for a quick game, tell 1 my question, and if played, type 'examine -1' to make your last game appear on board to be examined.";
        } else if ((ficsType == NO_TYPE || ficsType == UNKNOWN_TYPE) && data.contains("chessclub.com") && !mySettings.fics) {
            data = "***";
        }
        if (ficsType == HISTORY_TYPE && data.trim().startsWith("History")) {
            try {
                if (mainTelnet.myGameList != null)
                    if (mainTelnet.myGameList.isVisible() == true)
                        mainTelnet.myGameList.dispose();

                Thread.sleep(100);
                mainTelnet.gameList = new tableClass();
                //gameList.resetList();
                //    gameList.addToList(dg.getArg(1) + " " + dg.getArg(2), dg.getArg(2));
                mainTelnet.gameList.type1 = "history";
                mainTelnet.gameList.type2 = lastGameListName;
                mainTelnet.gameList.createHistoryListColumns();
                Thread.sleep(40);

                try {
                    gameListCreator gameT = new gameListCreator();
                    Thread gamet = new Thread(gameT);
                    gamet.start();

                } catch (Exception gam) {
                }
            } catch (Exception disposal) {
            }
        } else if (ficsType == HISTORY_TYPE && !data.trim().startsWith("History") && !data.trim().startsWith("Opponent")) {
            addHistoryItem(data);

        }


        if (ficsType == JOURNAL_TYPE && data.trim().startsWith("Journal")) {
            try {
                if (mainTelnet.myGameList != null)
                    if (mainTelnet.myGameList.isVisible() == true)
                        mainTelnet.myGameList.dispose();

                Thread.sleep(100);
                mainTelnet.gameList = new tableClass();
                //gameList.resetList();
                //    gameList.addToList(dg.getArg(1) + " " + dg.getArg(2), dg.getArg(2));
                mainTelnet.gameList.type1 = "liblist";
                mainTelnet.gameList.type2 = lastGameListName;
                mainTelnet.gameList.createLiblistColumns();
                Thread.sleep(40);

                try {
                    gameListCreator gameT = new gameListCreator();
                    Thread gamet = new Thread(gameT);
                    gamet.start();

                } catch (Exception gam) {
                }
            } catch (Exception disposal) {
            }
        } else if (ficsType == JOURNAL_TYPE && !data.trim().startsWith("Journal") && !data.trim().startsWith("White")) {
            addJournalItem(data);

        }
       /* if((ficsType == HISTORY_LIST) && lineCount == 1) {
            lastGameListName = getGameListName();

                if(lastGameListName.equals(mySettings.whoAmI)) {
                    String search = data;;


            }
            refreshGameListWithData(data, "none", true);
            if(((MainActivity)MainActivity.getAppActivity()).getTabHost().getCurrentTab() ==  MainActivity.BOARD_TAB) {
                if(lastGameListName.equals(mySettings.whoAmI)) {
                    skipShowingGameList = true;
                }

            }


        }

        if((ficsType == HISTORY_LIST) && lineCount == 2) {
            if(((MainActivity)MainActivity.getAppActivity()).getTabHost().getCurrentTab() ==  MainActivity.BOARD_TAB) {
                if(lastGameListName.equals(mySettings.whoAmI)) {
                    skipShowingGameList = true;
                    refreshGameListWithData(data, "none", false);
                }

            }

        }

        if((ficsType == HISTORY_LIST) && lineCount >2) {

                if(lastGameListName.equals(mySettings.whoAmI))
                {
                    if(((MainActivity)MainActivity.getAppActivity()).getTabHost().getCurrentTab() ==  MainActivity.BOARD_TAB) {
                        String search = data;
                        ;

                        refreshGameListWithData(search, getGameListCommand(data), false);
                        skipShowingGameList = true;
                    }
                }
        }
        */
        if (ficsType == SEEK_ADD || ficsType == SEEK_REMOVE || ficsType == SEEK_REMOVE_ALL) {
            processSeekData(ficsType, data);
            resetParsing();
            return;
        }
        /*
        if(ficsType == SAY_TYPE)
        {
            if(lineCount == 1 && spaceSeperatedLine.size() > 0) {
                updateTeller();
            }
            if((data.startsWith("(told ") ) && lineCount > 1) {
              ficsChatTell2 = ficsChatTell2 + data.trim();
            } else {
              ficsChatTell = ficsChatTell + data.trim();
            }
        }
        */

/*        if(ficsType == SEEK_ADD || ficsType == SEEK_REMOVE || ficsType == SEEK_REMOVE_ALL) {
        [self processSeekData: ficsType : data];
        [self resetParsing];
            return;
        }


        */

        if (ficsType == CHANNEL_LIST_TYPE && !data.startsWith("--") && sentInChannel) {
            sentInChannel = false;
            processChannels(data);
            data = "";
            sendConnectUserMessages();


        } else if (ficsType == CHANNEL_LIST_TYPE && sentInChannel) {
            data = "";
        }
        if (ficsType == CHANNEL_TELL || ficsType == NOTIFY_TYPE || ficsType == PERSONAL_TELL || ficsType == SHOUT_TELL || ficsType == KIB_TYPE || ficsType == SAY_TYPE /*|| ficsType == CSHOUT_TYPE*/) {

            if (ficsType == PERSONAL_TELL && lineCount == 1) {
                setLastTeller();
            }

            if ((data.startsWith("(told ") || data.startsWith("(shouted to ") || data.startsWith("(c-shouted to ") || data.startsWith("(kibitzed to") || data.startsWith("(whispered to")) && lineCount > 1) {
                ficsChatTell2 = ficsChatTell2 + data.trim();
            } else {

                String enter = "";
                if (ficsType == CHANNEL_TELL && !mySettings.fics && lineCount > 1) {
                    ArrayList<String> line = new ArrayList<String>();
                    seperateLine(data, line);
                    if (line.size() > 0) {
                        String tempo = line.get(0);
                        if (getChannelNumber(tempo) > -1) {
                            enter = "\n";

                        }
                    }
                }
                boolean timeStampChat = mySettings.timeStampChat;

                if ((ficsType == CHANNEL_TELL || ficsType == KIB_TYPE) && !mySettings.channelTimestamp) {
                    timeStampChat = false;
                }

                if (ficsType == SHOUT_TELL && !mySettings.shoutTimestamp) {
                    timeStampChat = false;
                }

                if ((ficsType == PERSONAL_TELL || ficsType == NOTIFY_TYPE) && !mySettings.tellTimestamp) {
                    timeStampChat = false;
                }

                if (timeStampChat && ficsChatTell.equals("")) {
                    ficsChatTell = getATimestamp();
                }


                if (enter.length() > 0 && timeStampChat) {
                    if (lineCount > 1 && (data.startsWith(" / ") || data.startsWith(" \\ "))) {
                        String tempo = data.trim();
                        if ((tempo.startsWith("/ ") || tempo.startsWith("\\ ")) && tempo.length() > 2) {
                            tempo = tempo.substring(2, tempo.length());
                        }
                        ficsChatTell = ficsChatTell + enter + getATimestamp() + tempo;
                    } else {
                        ficsChatTell = ficsChatTell + enter + getATimestamp() + data.trim();
                    }

                } else {
                    if (lineCount > 1 && (data.startsWith("/ ") || data.startsWith("\\ "))) {
                        String tempo = data.trim();
                        if ((tempo.startsWith("/ ") || tempo.startsWith("\\ ")) && tempo.length() > 2) {
                            tempo = tempo.substring(2, tempo.length());
                        }
                        ficsChatTell = ficsChatTell + enter + tempo;
                    } else {
                        ficsChatTell = ficsChatTell + enter + data.trim();

                    }

                }

            }

        }

        // if(ficsType == UNKNOWN_TYPE || ((ficsType == HISTORY_LIST || ficsType == JOURNAL_LIST) && !skipShowingGameList))
        if (ficsType != CHANNEL_TELL && ficsType != NOTIFY_TYPE && ficsType != PERSONAL_TELL && ficsType != SHOUT_TELL && ficsType != KIB_TYPE && ficsType != SAY_TYPE && ficsType != HISTORY_TYPE && ficsType != JOURNAL_TYPE) {
            if (data.length() == 1) {
                return; // not sure why we have these when playing
            }
            if (!data.trim().equals("")) {
                ficsChatTell = ficsChatTell + data;
            }

        }
        /*
        if(data.startsWith("crazyhouse set.") || data.startsWith("bell set to 0.")) {
        if(mySettings.guest) {
            mySettings.chatLog.addChat("Just type seek in the console and wait to be matched or go to Board tab's Option menu for the Seek a Game form.","bott_tell");
            if(data.startsWith("crazyhouse set.")) {
                mySettings.chatLog.addChat("Guests can create a free FICS account if they want at https://www.freechess.org/Register/index.html","bott_tell");
            } else {
                mySettings.chatLog.addChat("type tell 1 user's question - Contact ICC Online Help, questions may not be answered immediately.","bott_tell");
            }

        } else {
            mySettings.chatLog.addChat("To start playing go to Board tab's Option menu for the Seek a Game form.","bott_tell");
            if(data.startsWith("bell set to 0.")) {
                mySettings.chatLog.addChat("Members can purchase time in the chessclub's official 'Chess at ICC' app in App Store as an In-App purchase and use the time in this App or on any device.","bott_tell");
            }
        }
         
    }*/
    }

    void processLink(StyledDocument doc, String thetell, Color col, int index, int attempt, int game, SimpleAttributeSet attrs, messageStyles myStyles) {
        myDocWriter.processLink(doc, thetell, col, index, attempt, game, attrs, myStyles);
    }

    void processLink2(StyledDocument doc, String thetell, Color col, int index, int attempt, int game, SimpleAttributeSet attrs, int[] allTabs, messageStyles myStyles) {
        myDocWriter.processLink2(doc, thetell, col, index, attempt, game, attrs, allTabs, myStyles);
    }

    void writeOutToShouts(String thetell) {
        channels sharedVariables = mySettings;
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        if (sharedVariables.shoutStyle == 1 || sharedVariables.shoutStyle == 3)
            StyleConstants.setItalic(attrs, true);
        if (sharedVariables.shoutStyle == 2 || sharedVariables.shoutStyle == 3)
            StyleConstants.setBold(attrs, true);

        StyledDocument doc;

        messageStyles myStyles = null;
        /* code to pass an array of consoles this goes to ( just needed if more than one and right now that can just be shouts*/
        int[] cindex = new int[sharedVariables.maxConsoleTabs];
        for (int z = 0; z < sharedVariables.maxConsoleTabs; z++) {
            if (z == sharedVariables.shoutRouter.shoutsConsole)
                cindex[z] = 1;
            else
                cindex[z] = 0;
        }
        if (sharedVariables.shoutsAlso == true)
            cindex[0] = 1;
        /*** end code for passing to process link where this is going */

        doc = sharedVariables.mydocs[sharedVariables.shoutRouter.shoutsConsole];

        /*
         
         processLink(doc, theNotifyTell, sharedVariables.ForColor, ztab, maxLinks, subframe_type, attrs, null);
         */


        processLink2(doc, thetell, sharedVariables.shoutcolor, sharedVariables.shoutRouter.shoutsConsole, maxLinks, SUBFRAME_CONSOLES, attrs, cindex, myStyles);
        if (sharedVariables.shoutRouter.shoutsConsole > 0 && sharedVariables.shoutsAlso == true) {
            doc = sharedVariables.mydocs[0];
            processLink2(doc, thetell, sharedVariables.shoutcolor, 0, maxLinks, SUBFRAME_CONSOLES, attrs, cindex, myStyles);
        }
    }

    void writeOutToChannel(String theTell, int channelNumber) {
        theTell += "\n";
        channels sharedVariables = mySettings;
        int[] cindex2 = new int[sharedVariables.maxConsoleTabs];
        cindex2[0] = 0; // default till we know more is its not going to main
        Color channelColor;
        boolean goTab = false;
        for (int b = 1; b < sharedVariables.maxConsoleTabs; b++) {
            if (sharedVariables.console[b][channelNumber] == 1) {
                cindex2[b] = 1;
                goTab = true;
            } else {
                cindex2[b] = 0;
            }

        }

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        if (sharedVariables.style[channelNumber] > 0) {
            if (sharedVariables.style[channelNumber] == 1 || sharedVariables.style[channelNumber] == 3)
                StyleConstants.setItalic(attrs, true);
            if (sharedVariables.style[channelNumber] == 2 || sharedVariables.style[channelNumber] == 3)
                StyleConstants.setBold(attrs, true);

        }

        if (sharedVariables.channelOn[channelNumber] == 1) {
            channelColor = sharedVariables.channelColor[channelNumber];
        } else {
            channelColor = sharedVariables.defaultChannelColor;
        }


        messageStyles myStyles = null;

        if (goTab == true && sharedVariables.mainAlso[channelNumber] == true)
            cindex2[0] = 1;// its going to main and tab. we set this so we can pass cindex2 to docwriter letting it know all tabs things go to for new info updates

        for (int b = 1; b < sharedVariables.maxConsoleTabs; b++) {
            if (cindex2[b] == 1 && sharedVariables.qtellController[b][channelNumber] != 2) {
                StyledDocument doc = sharedVariables.mydocs[b];

                processLink2(doc, theTell, channelColor, b, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, myStyles);


            }
        }

        if ((goTab == false || (sharedVariables.mainAlso[channelNumber] == true)) && sharedVariables.qtellController[0][channelNumber] != 2) {

            StyledDocument doc = sharedVariables.mydocs[0];
            processLink2(doc, theTell, channelColor, 0, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, myStyles);

        }
    }

    void addToNotifyOnLoginAsNeeded(String theTell) {
        try {
            if (theTell.startsWith("Present company includes: ")) {
                if (mainTelnet.notifyList.model.size() == 2) {
                    ArrayList<String> line = new ArrayList<String>();
                    seperateLine(theTell, line);
                    if (line.size() > 3) {
                        for (int a = 3; a < line.size(); a++) {
                            String temp = line.get(a);
                            if (temp.endsWith(".")) {
                                temp = temp.substring(0, temp.length() - 1);
                            }
                            mainTelnet.notifyList.notifyStateChanged(temp.trim(), "");
                        }
                    }
                }
            }
        } catch (Exception dui) {

        }


    }

    void writeOutToNotify(String tell, String name) {
        try {


            channels sharedVariables = mySettings;
            if (tell.contains("has arrived.")) {
                mainTelnet.notifyList.notifyStateChanged(name.trim(), ""); // arg2 state "P" playing etc
            } else {
                mainTelnet.notifyList.removeFromList(name);
            }

            boolean supressLogins = sharedVariables.getNotifyControllerState(name);


            String theNotifyTell = tell + "\n";
            StyledDocument doc;
            // we use main console now for notifications -- 0

            SimpleAttributeSet attrs = new SimpleAttributeSet();
            if (sharedVariables.nonResponseStyle == 1 || sharedVariables.nonResponseStyle == 3)
                StyleConstants.setItalic(attrs, true);
            if (sharedVariables.nonResponseStyle == 2 || sharedVariables.nonResponseStyle == 3)
                StyleConstants.setBold(attrs, true);


            if (supressLogins == false) {
                boolean wePrinted = false;

                notifyOnTabs tabsNotify = sharedVariables.getNotifyOnTabs(name);
                for (int ztab = 0; ztab < sharedVariables.maxConsoleTabs; ztab++) {

                    if (tabsNotify.notifyControllerTabs.get(ztab).equals("F"))
                        continue;
                    for (int znumber = 0; znumber < 400; znumber++) {

                        if (sharedVariables.console[ztab][znumber] == 1 || ztab == 0) {
                            int subframe_type = SUBFRAME_CONSOLES;
                            if (ztab > 0)
                                subframe_type = SUBFRAME_NOTIFY;
                            doc = sharedVariables.mydocs[ztab];
                            wePrinted = true;
                            if (sharedVariables.tabStuff[ztab].ForColor == null)
                                processLink(doc, theNotifyTell, sharedVariables.ForColor, ztab, maxLinks, subframe_type, attrs, null);
                            else
                                processLink(doc, theNotifyTell, sharedVariables.tabStuff[ztab].ForColor, ztab, maxLinks, subframe_type, attrs, null);
                            break;
                        }// end if print

                    } //end for
                }// end outer for

                try {
                    if (sharedVariables.makeSounds == true && sharedVariables.specificSounds[4] == true) {


                        if (wePrinted == true) {
                            Sound nsound = new Sound(sharedVariables.songs[4]);
                        }
                    }
                } catch (Exception notifysound) {
                }

            }// end of if suppress logins false
        } catch (Exception dui2) {
        }
    }

    void writeOutToTell(String thetell, String name) {
        channels sharedVariables = mySettings;
        thetell += "\n";
        String screenName = name.contains("(") ? name.substring(0, name.indexOf("(")) : name;
        sharedVariables.lasttell = screenName; // obsolete but why not leave the data
        sharedVariables.F9Manager.addName(screenName);
        StyledDocument doc = sharedVariables.mydocs[sharedVariables.looking[sharedVariables.tellconsole]];
        int direction = sharedVariables.looking[sharedVariables.tellconsole];
        if (sharedVariables.tellsToTab == true) {
            direction = sharedVariables.tellTab;
            doc = sharedVariables.mydocs[direction];
        }
/*** check if forced to tab ****/
        boolean him = false;
        boolean makeASound = true;
        for (int ab = 0; ab < sharedVariables.toldTabNames.size(); ab++) {
            if (sharedVariables.toldTabNames.get(ab).name.toLowerCase().equals(name.toLowerCase())) {
                direction = sharedVariables.toldTabNames.get(ab).tab;
                doc = sharedVariables.mydocs[direction];
                him = true;
                makeASound = sharedVariables.toldTabNames.get(ab).sound;
                break;
            }
        }

        messageStyles myStyles = null;
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        if (sharedVariables.tellStyle == 1 || sharedVariables.tellStyle == 3)
            StyleConstants.setItalic(attrs, true);
        if (sharedVariables.tellStyle == 2 || sharedVariables.tellStyle == 3)
            StyleConstants.setBold(attrs, true);


        if (sharedVariables.tabStuff[direction].tellcolor == null)
            processLink(doc, thetell, sharedVariables.tellcolor, direction, maxLinks, SUBFRAME_CONSOLES, attrs, myStyles);
        else
            processLink(doc, thetell, sharedVariables.tabStuff[direction].tellcolor, direction, maxLinks, SUBFRAME_CONSOLES, attrs, myStyles);

        try {

            for (int z = 0; z < sharedVariables.openBoardCount; z++) {
                if (mainTelnet.myboards[z] != null)
                    if (sharedVariables.boardConsoleType != 0)// dont send tell to board if console disabled, would have later caused it to print twice in main
                        if (sharedVariables.mygame[z] != null && (sharedVariables.mygame[z].realname1.equals(name) || sharedVariables.mygame[z].realname2.equals(name))) {


                            doc = sharedVariables.mygamedocs[z];
                            processLink(doc, thetell, sharedVariables.tellcolor, z, maxLinks, GAME_CONSOLES, attrs, myStyles);
                        }
            }
        } catch (Exception dumb) {
        }

        try {
            if (sharedVariables.tellsToTab == true && sharedVariables.switchOnTell == true && him == false) {
                FocusOwner whohasit = new FocusOwner(sharedVariables, mainTelnet.consoleSubframes, mainTelnet.myboards);
                int xxx = mainTelnet.getCurrentConsole();
                mainTelnet.consoleSubframes[sharedVariables.tellconsole].makeHappen(sharedVariables.tellTab);

                if (xxx != sharedVariables.tellconsole || !sharedVariables.operatingSystem.equals("mac"))
                    mainTelnet.giveFocus(whohasit);
                if (sharedVariables.addNameOnSwitch == true)
                    mainTelnet.consoleSubframes[sharedVariables.tellconsole].addNameToCombo(name);
            }
        } catch (Exception donthave) {
        }
        Sound ptell;
        if (sharedVariables.makeSounds == true && makeASound == true && sharedVariables.makeTellSounds)
            ptell = new Sound(sharedVariables.songs[0]);
        if (sharedVariables.rotateAways == true) {
            try {

                Random generator = new Random(System.currentTimeMillis());
                int randomIndex = generator.nextInt(sharedVariables.lanternAways.size());
                String myaway = sharedVariables.lanternAways.get(randomIndex);
//sendMessage("Away " + myaway + "\n");// implment fics send
            } catch (Exception d) {
            }
        }
    } // end write out tell


    void resetParsing() {
        ficsType = NO_TYPE;
        lineCount = 0;
        ficsChatTell = "";
        ficsChatTell2 = "";
        lastGameListName = "";
        skipShowingGameList = false;
        spaceSeperatedLine.clear();
    }

    boolean isPrompt(String data) {
        if (data.equals("fics%")) {
            return true;
        }
        return false;
    }

    int getType(String lineData) {

        if (!mySettings.whoAmI.equals("")) {
            if (spaceSeperatedLine.size() > 0) {
                String line1 = spaceSeperatedLine.get(0);
                if (line1.equals("<s>")) {
                    return SEEK_ADD;
                }
            }

            if (spaceSeperatedLine.size() > 0) {
                String line1 = spaceSeperatedLine.get(0);
                if (line1.equals("<sr>")) {
                    return SEEK_REMOVE;
                }
            }

            if (spaceSeperatedLine.size() > 0) {
                String line1 = spaceSeperatedLine.get(0);
                if (line1.equals("<sc>")) {
                    return SEEK_REMOVE_ALL;
                }
            }
        }


        if (spaceSeperatedLine.size() > 2) {
            String tempo = spaceSeperatedLine.get(0);
            String tempo1 = spaceSeperatedLine.get(1);
            String tempo2 = spaceSeperatedLine.get(2);
            if (tempo.equals("--") && tempo1.equals("channel") && tempo2.equals("list:")) {
                return CHANNEL_LIST_TYPE;
            }
        }
        if (spaceSeperatedLine.size() > 0) {
            String tempo = spaceSeperatedLine.get(0);
            if (tempo.equals("Notification:")) {
                return NOTIFY_TYPE;
            }
        }
        if (spaceSeperatedLine.size() == 3) {
            String tempo = spaceSeperatedLine.get(0);
            String tempo2 = spaceSeperatedLine.get(1);
            if (tempo.equals("History") && tempo2.equals("for")) {
                setLastGameListName(spaceSeperatedLine.get(2));
                return HISTORY_TYPE;
            }
        }

        if (spaceSeperatedLine.size() == 3) {
            String tempo = spaceSeperatedLine.get(0);
            String tempo2 = spaceSeperatedLine.get(1);
            if (tempo.equals("Journal") && tempo2.equals("for")) {
                setLastGameListName(spaceSeperatedLine.get(2));
                return JOURNAL_TYPE;
            }
        }
        if (isKibWhisperInfo(lineData)) {
            return KIB_TYPE;
        }
        if (spaceSeperatedLine.size() > 1) {
            String tempo = spaceSeperatedLine.get(1);
            if (tempo.equals("c-shouts:")) {
                return CSHOUT_TYPE;
            }
        }


        if (spaceSeperatedLine.size() > 1) {
            String tempo = spaceSeperatedLine.get(1);
            if (tempo.equals("says:")) {
                return SAY_TYPE;
            }
        }


        if (spaceSeperatedLine.size() > 2) {
            String tempo = spaceSeperatedLine.get(0);
            String tempo2 = spaceSeperatedLine.get(1);
            if (tempo.equals("History") && tempo2.equals("for") && mySettings.fics) {
                return HISTORY_LIST;
            }
            if (tempo.equals("Recent") && tempo2.equals("games") && !mySettings.fics) {
                return HISTORY_LIST;
            }
        }
        /*

        if([spaceSeperatedLine count] > 2) {
        NSString *tempo = [spaceSeperatedLine objectAtIndex:0];
        NSString *tempo2 = [spaceSeperatedLine objectAtIndex:1];
        if([tempo isEqualToString:@"Journal"] && [tempo2 isEqualToString:@"for"]) {
            return JOURNAL_LIST;
        }
    }
        */
        // adammr(99): this atomic channel
        if (spaceSeperatedLine.size() > 1) {
            String tempo = spaceSeperatedLine.get(0);
            if (getChannelNumber(tempo) > -1 && tempo.endsWith("):")) {
                return CHANNEL_TELL;
            }
        }

        if (spaceSeperatedLine.size() > 2) {
            String line1 = spaceSeperatedLine.get(1);
            String line2 = spaceSeperatedLine.get(2);
            if (line1.equals("tells") && line2.equals("you:")) {
                if (isHashKeyTell(lineData, spaceSeperatedLine)) {
                    return HASH_KEY_TYPE;
                } else {
                    return PERSONAL_TELL;
                }
            }
        }
        if (spaceSeperatedLine.size() > 1) {
            String line1 = spaceSeperatedLine.get(1);
            if (line1.equals("shouts:")) {
                return SHOUT_TELL;
            }
        }

        if (spaceSeperatedLine.size() > 0) {
            String line1 = spaceSeperatedLine.get(0);
            if (line1.equals("-->")) {
                return SHOUT_TELL;
            }
        }


        if (spaceSeperatedLine.size() > 2) {
            String line1 = spaceSeperatedLine.get(1);
            String line2 = spaceSeperatedLine.get(2);
            if (line1.startsWith("(") && line1.endsWith(")") && line2.equals("seeking")) {
                return SEEKING_LINE;
            }
        }

        if (spaceSeperatedLine.size() > 3) {
            String line1 = spaceSeperatedLine.get(1);
            String line2 = spaceSeperatedLine.get(2);
            String line3 = spaceSeperatedLine.get(3);
            if (line1.startsWith("(") && line2.endsWith(")") && line3.equals("seeking")) {
                return SEEKING_LINE;
            }
        }


        return UNKNOWN_TYPE;
    }

    boolean isHashKeyTell(String line, ArrayList<String> spaceSeperatedLine) {
        /*
         static boolean console = true;
         static int number = -1;
         static int userHashKey = -1;
         */
        String name = spaceSeperatedLine.get(0);
        String screenName = name.contains("(") ? name.substring(0, name.indexOf("(")) : name;
        screenName = screenName.toLowerCase();
        // 4:20:54 MasterGameBot(TD) tells you: 9999 open c 1
        if (!screenName.equals(mySettings.whoAmI.toLowerCase())) {
            return false;
        }
        String key = spaceSeperatedLine.get(3);
        if (!key.equals("" + HashTellData.userHashKey)) {
            return false;
        }
        if (spaceSeperatedLine.size() != 7) {
            // close flow
            if (spaceSeperatedLine.size() == 5) {
                String temp = spaceSeperatedLine.get(4);
                if (temp.equals("close")) {
                    HashTellData.number = -1;
                    HashTellData.type = "C";
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        String temp = spaceSeperatedLine.get(4);
        String temp2 = spaceSeperatedLine.get(5);
        String temp3 = spaceSeperatedLine.get(6);
        if (temp.equals("open") && (temp2.equals("c") || temp2.equals("g") || temp2.equals("f"))) {
            try {
                int num = Integer.parseInt(temp3);
                if (num > 0) {
                    if (temp2.equals("c")) {
                        if (num > 11) {
                            return false;
                        }
                        HashTellData.type = "C";
                        HashTellData.number = num;
                        return true;

                    } else if (temp2.equals("f")) {
                        HashTellData.type = "F";
                        HashTellData.number = 1;
                        return true;
                    }
                } else {
                    return false;
                }
            } catch (Exception dui) {
                return false;
            }

        }

        return false;
    }

    int getChannelNumber(String data) {
        String tempo = "";
        boolean opening = false;
        int lastStart = 0;
        for (int b = 0; b < data.length(); b++) {
            if (data.charAt(b) == '(') {
                lastStart = b;
            }
        }
        for (int a = lastStart; a < data.length(); a++) {
            String spot = "" + data.charAt(a);
            if (spot.equals("(")) {
                tempo = "";
                opening = true;
            } else if (spot.equals(")")) {
                //check if number and return;
                int number = -1;
                try {
                    return Integer.parseInt(tempo);
                } catch (Exception duie) {
                }
                return number;
            } else if (opening) {
                tempo = tempo + spot;
            }
        }
        return -1;
    }

    boolean isToldNameMessage(String tell) {
        ArrayList<String> line = new ArrayList<String>();
        seperateLine(tell, line);
        if (line.size() != 2) {
            return false;
        }
        String two = line.get(1);
        if (two.length() < 3) {
            return false;
        }
        String sub = two.substring(0, 1);
        try {
            Integer.parseInt(sub);
        } catch (Exception due) {
            return true;
        }
        return false;
    }

    String getToldName(String tell) {
        ArrayList<String> line = new ArrayList<String>();
        seperateLine(tell, line);
        if (line.size() != 2) {
            return "";
        }
        String two = line.get(1);
        if (two.length() > 1) {
            return two.substring(0, two.length() - 1);
        }
        return "";
    }

    void launchLookupUser(String mess) {
        if (mess == null || mess.equals("")) {
            return;
        }

        mainTelnet.launchFingerPopup(mess);
    }

    void writeOutToMain(String ficsChatTell) {
        try {
            int consoleNumber = 0;
            if (HashTellData.type.equals("C") && HashTellData.number > 0) {
                consoleNumber = HashTellData.number;
            }
            if (HashTellData.type.equals("F") && !ficsChatTell.startsWith("Your communication has been queued for ")) {
                launchLookupUser(ficsChatTell);
                return;
            } else if (HashTellData.type.equals("F")) {
                consoleNumber = 1;
            }
            if (consoleNumber > 0 && ficsChatTell.startsWith("Your communication has been queued for ")) {
                return;
            }
            if (ficsChatTell.startsWith("(told ") && isToldNameMessage(ficsChatTell)) {
                String screenName = getToldName(ficsChatTell);
                mySettings.F9Manager.addName(screenName);
                if (mySettings.tellsToTab == true) {
                    consoleNumber = mySettings.tellTab;
                }
            }

            StyledDocument doc = mySettings.mydocs[consoleNumber];// 0 for main console
            channels sharedVariables = mySettings;
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            if (sharedVariables.tabStuff[consoleNumber].ForColor == null)
                StyleConstants.setForeground(attrs, mySettings.ForColor);
            else
                StyleConstants.setForeground(attrs, sharedVariables.tabStuff[consoleNumber].ForColor);

            StyleConstants.setForeground(attrs, mySettings.ForColor);
            int[] cindex2 = new int[mySettings.maxConsoleTabs];
            cindex2[0] = consoleNumber; // default till we know more is its not going to main
            if (sharedVariables.tabStuff[consoleNumber].ForColor == null)
                processLink2(doc, ficsChatTell, mySettings.ForColor, consoleNumber, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, null);
            else
                processLink2(doc, ficsChatTell, sharedVariables.tabStuff[consoleNumber].ForColor, consoleNumber, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, null);
        } catch (Exception e) {
        }
    }

    void setLastTeller() {

    }

    class gameListCreator implements Runnable {
        public void run() {
            mainTelnet.myGameList = new gameFrame(mainTelnet.sharedVariables, mainTelnet.queue, mainTelnet.gameList);
            mainTelnet.sharedVariables.myGameList = mainTelnet.myGameList;
            mainTelnet.myGameList.setSize(600, 425);
            mainTelnet.myGameList.setVisible(true);
            //sharedVariables.desktop.add(myGameList);

            try {
                mainTelnet.myGameList.setSelected(true);
                if (!(mainTelnet.gameList.type1.equals("") && mainTelnet.gameList.type2.equals("")))
                    if (mainTelnet.gameList.type1.equals("liblist")) {
                        mainTelnet.myGameList.setTitle("journal" + " " + mainTelnet.gameList.type2);
                    } else {
                        mainTelnet.myGameList.setTitle(mainTelnet.gameList.type1 + " " + mainTelnet.gameList.type2);
                    }

            } catch (Exception couldnt) {
            }

        }// end run
    }// end class gamelistcreator

    void setLastGameListName(String data) {
        if (data != null && data.length() > 2) {
            lastGameListName = data.substring(0, data.length() - 1);
        } else {
            lastGameListName = "";
        }
    }

    void addHistoryItem(String data) {
        ArrayList<String> line = new ArrayList<String>();
        seperateLine(data, line);
        try {
            if (line.size() == 18) {

                gameItem myItem = new gameItem();
            
            /*
             void addHistoryRow(String index, String whiteName, String blackName, String whiteRating, String blackRating, String date, String time, String whitetime, String whiteinc,
                String rated, String ratedType, String wild, String eco, String status, String color, String mode, tableClass myTable)
             */
                // 29: + 1753 B 1713 ZwazO         [ sr 15   0] A25 Mat Wed Apr  5, 00:50 EDT 2023
                String index = line.get(0).substring(0, line.get(0).length() - 1);
                String result = line.get(1);
                String color = line.get(3);
                String whiteName = color.equals("B") ? line.get(5) : lastGameListName;
                String blackName = color.equals("W") ? line.get(5) : lastGameListName;
                String whiteRating = color.equals("W") ? line.get(2) : line.get(4);
                String blackRating = color.equals("B") ? line.get(2) : line.get(4);
                String date = line.get(13) + " " + line.get(14) + " " + line.get(17);
                String time = line.get(15) + " " + line.get(16);
                String wTime = line.get(8);
                String wInc = line.get(9);
                try {
                    wInc = wInc.substring(0, wInc.length() - 1);
                } catch (Exception badInc) {
                }
                String rated = line.get(7).contains("r") ? "1" : "0";
                String ratingType = line.get(7);
                String eco = line.get(10);
                String end = line.get(11);
                try {
                    ratingType = ratingType.substring(0, 1);
                } catch (Exception badtype) {
                }

                myItem.addHistoryRow(index, whiteName, blackName, whiteRating, blackRating, date, time, wTime, wInc,
                        rated, ratingType, line.get(11), eco, result, line.get(14), end, mainTelnet.gameList);


            }
        } catch (Exception duiiw) {
        }
    }

    void addJournalItem(String data) {
        ArrayList<String> line = new ArrayList<String>();
        seperateLine(data, line);
        try {
            /*
             this one has a lenght of 12
             %20: johnas        1573    Liszt         1727    [ br  5   2] E60  NM 1/2-1/2
             
             this one has a lenght of 13
             %21: *KeresPaulGM( 1203    *WinterWillia 1203    [ uu  0   0] B29 --- *
             */
            if (line.size() == 13 || line.size() == 12) {

                gameItem myItem = new gameItem();
            
            /*
             void addSearchLiblistRow(String index, String whiteName, String blackName, String whiteRating, String blackRating, String date, String time, String whitetime, String whiteinc,
                 String rated, String ratedType, String wild, String eco, String status, String color, String mode, String libnote,  tableClass myTable)
             */
                // 29: + 1753 B 1713 ZwazO         [ sr 15   0] A25 Mat Wed Apr  5, 00:50 EDT 2023
                String index = line.get(0).substring(0, line.get(0).length() - 1);
                String result = line.get(11);
                String color = line.get(3);
                String whiteName = line.get(1);
                String blackName = line.get(3);
                String whiteRating = line.get(2);
                String blackRating = line.get(4);
                String date = "date";
                String time = "time";
                String wTime = line.get(8);
                String wInc = line.get(9);
                try {
                    wInc = wInc.substring(0, wInc.length() - 1);
                } catch (Exception badInc) {
                }
                String rated = line.get(7).contains("r") ? "1" : "0";
                String ratingType = line.get(7);
                String eco = line.get(10);
                ;
                String end = line.get(11);
                String timeControl = line.get(5) + " " + line.get(6) + " " + line.get(7) + " " + line.get(8);
                try {
                    ratingType = ratingType.substring(0, 1);
                } catch (Exception badtype) {
                }

                myItem.addSearchLiblistRow(index, whiteName, blackName, whiteRating, blackRating, date, timeControl, wTime, wInc,
                        rated, ratingType, line.get(11), end, result, line.get(9), eco, "", mainTelnet.gameList);


            }
        } catch (Exception duiiw) {
        }
    }

    void sendOutChat() {/*
        if(ficsType == SAY_TYPE)  {
           mySettings.gameChatLog.addChat(ficsChatTell, "tell");
            if(!ficsChatTell2.equals("")) {
               mySettings.gameChatLog.addChat(ficsChatTell2, "server_text");
            }
            if(mySettings.saysInMain) {
                mySettings.chatLog.addChat(ficsChatTell,"tell");
                if(!ficsChatTell2.equals("")) {
                  mySettings.chatLog.addChat(ficsChatTell2, "server_text");
                }
                consoleManager.updateChat();
            }

            gameConsoleManager.updateChat();
            if(mySettings.otherSounds) {
               MainActivity.playSound("tell");
            }
        }
      */
        try {
            if (ficsType == SEEKING_LINE) {
                return;
            }
            if (ficsType == CHANNEL_TELL) {
                writeOutToChannel(ficsChatTell, getChannelNumber(spaceSeperatedLine.get(0)));

                if (!ficsChatTell2.equals("")) {
                    writeOutToChannel(ficsChatTell2, getChannelNumber(spaceSeperatedLine.get(0)));
                }
            } else if (ficsType == KIB_TYPE) {
                writeOutKibWhisperToGameConsoles(ficsChatTell, ficsChatTell2);
            } else if (ficsType == SAY_TYPE) {
                writeOutSays(ficsChatTell, ficsChatTell2);
            } else if (ficsType == NOTIFY_TYPE) {

                writeOutToNotify(ficsChatTell, spaceSeperatedLine.get(1));
                if (!ficsChatTell2.equals("")) {

                }

            } else if (ficsType == PERSONAL_TELL) {

                writeOutToTell(ficsChatTell, spaceSeperatedLine.get(0));
                if (!ficsChatTell2.equals("")) {
                    writeOutToMain(ficsChatTell2 + "\n");
                }


            } else if (ficsType == SHOUT_TELL) {

                if (!ficsChatTell2.equals("")) {
                    writeOutToShouts(ficsChatTell + "\n");
                    writeOutToShouts(ficsChatTell2 + "\n");
                } else {
                    writeOutToShouts(ficsChatTell + "\n");

                }
            } else {
                if (!ficsChatTell.trim().equals("")) {
                    writeOutToMain(ficsChatTell);
                }


            }

         /*else if(ficsType == CSHOUT_TYPE) {
              mySettings.chatLog.addChat(ficsChatTell, "s-shout");
              if(!ficsChatTell2.equals("")) {
                  mySettings.chatLog.addChat(ficsChatTell2, "server_text");
              }
          consoleManager.updateChat();
          } else if(ficsType == NOTIFY_TYPE) {
              mySettings.chatLog.addChat(ficsChatTell, "notify");
              mySettings.gameChatLog.addChat(ficsChatTell, "notify");
              if(!ficsChatTell2.equals("")) {
                  mySettings.chatLog.addChat(ficsChatTell2, "server_text");
                  mySettings.gameChatLog.addChat(ficsChatTell2, "notify");
              }
              consoleManager.updateChat();
              gameConsoleManager.updateChat();
          }
        */
        } catch (Exception dui) {
            System.out.println("exception printing fics type " + ficsType + " with content: " + ficsChatTell);
        }


    }


    void seperateLine(String data, ArrayList<String> spaceSeperatedLine1) {
        String tempo = "";
        spaceSeperatedLine1.clear();
        // [@"abc xyz http://www.abc.com aaa bbb ccc" substringWithRange:NSMakeRange(8, 18)]
        for (int a = 0; a < data.length(); a++) {
            String spot = "" + data.charAt(a);
            if (spot.equals(" ")) {
                if (tempo.length() > 0) {
                    spaceSeperatedLine1.add(tempo.trim());
                    tempo = "";
                } else {
                    tempo = "";
                }
            } else {
                tempo = tempo + spot;
            }
        }
        if (tempo.length() > 0) {
            spaceSeperatedLine1.add(tempo.trim());
        }

    }

    void checkIfLoggedIn() {
        // **** Starting FICS session as name ****
        if (spaceSeperatedLine.size() > 4) {
            String line1 = spaceSeperatedLine.get(0);
            String line2 = spaceSeperatedLine.get(1);
            String line3 = spaceSeperatedLine.get(2);
            String line4 = spaceSeperatedLine.get(3);
            String line5 = spaceSeperatedLine.get(4);

            String line6 = "";
            if (spaceSeperatedLine.size() > 5)
                line6 = spaceSeperatedLine.get(5);
            String line7 = "";
            if (spaceSeperatedLine.size() > 6)
                line7 = spaceSeperatedLine.get(6);
            if (
                    line2.equals("Starting") &&
                            line3.equals("FICS") &&
                            line4.equals("session") &&
                            line5.startsWith("as")) {
                if (line6.equals("")) {

                } else {
                    mySettings.whoAmI = line6;
                    mySettings.myname = line6;
                    mySettings.guest = false;
                    for (int a = 1; a < line6.length(); a++) {
                        if (line6.charAt(a) == '(') {
                            mySettings.whoAmI = line6.substring(0, a);
                            mySettings.myname = mySettings.whoAmI;
                            for (int b = a + 1; b < line6.length(); b++) {
                                if (line6.charAt(b) == 'U') {
                                    mySettings.guest = true;
                                }
                            }
                            break;
                        }
                    }
                }

                // [self sendToFICS:@"set prompt fics%" :soc];
                String OS = "Linux";
                if (mySettings.operatingSystem.equals("mac"))
                    OS = "Mac";
                else if (mySettings.operatingSystem.equals("win"))
                    OS = "Windows";
                sendToFICS("$set interface Pearl Chess on FICS " + mySettings.version + " for " + OS);

                sendToFICS("$set prompt");
                sendToFICS("$set style 12");
                sendToFICS("$set width 240");
                if (mySettings.showSeeks) {
                    sendToFICS("$set seek 1"); // now off since not hooked up
                } else {
                    sendToFICS("$set seek 0");
                }

                sendToFICS("$iset seekinfo 1");

                sendToFICS("$iset crazyhouse 1");
                sendToFICS("=channel");
                sentInChannel = true;


                if (mySettings.sendILoggedOn == true) {
                    sendToFICS("iloggedonipad");
                }
                lastGameStartString = "";
                //[self sendToFICS: @"iset seekremove 1" : soc];

            }

        }
    }

    void sendConnectUserMessages() {
        writeOutToMain("To resize the board and console click in bottom right corner and drag were it shows 2 lines \"//\" (by bottom right corner of typing input field)\n");
        if (mySettings.guest) {
            writeOutToMain("FICS offers a free lifetime account https://www.freechess.org/Register/index.html\n");
        }
    }

    void processChannels(String channels) {
        ArrayList<String> line = new ArrayList<String>();
        seperateLine(channels, line);
        for (int a = 0; a < line.size(); a++) {
            try {
                int b = Integer.parseInt(line.get(a));
                addChannel(line.get(a), mySettings.whoAmI);
            } catch (Exception dui) {

            }
        }

        if (mySettings.hasSettings == false && !setChannelTabs) {
            //mainTelnet.writeToConsole("Welcome to Pearl");   //sharedVariables.newUserMessage);
            setUpNewUserTabs();
            setChannelTabs = true;
        }

        populateEventData();

    }

    void addChannel(String channel, String name) {
        for (int a = 0; a < mySettings.channelNamesList.size(); a++)
            if (mySettings.channelNamesList.get(a).channel.equals(channel)) {
                return;
            }

        // new channel
        nameListClass tempNameList = new nameListClass();
        tempNameList.channel = channel;
        tempNameList.addToList(channel);
        tempNameList.addToList(name);
        mySettings.channelNamesList.add(tempNameList);
    }

    void sendToFICS(String input) {
        myoutput data = new myoutput();
        data.data = input;
        if (!data.data.endsWith("\n")) {
            data.data += "\n";
        }

        sendQueueConsole.add(data);
    }

    String getATimestamp() {
        if (!mySettings.timeStampChat) {
            return "";
        }
        String theTime = "";
        try {

            Calendar Now = Calendar.getInstance();
            String hour = "" + Now.get(Now.HOUR);// was HOUR_OF_DAY for 24 hour time
            if (hour.equals("0"))
                hour = "12";

            String minute = "" + Now.get(Now.MINUTE);
            if (minute.length() == 1)
                minute = "0" + minute;

            String second = "" + Now.get(Now.SECOND);
            if (second.length() == 1)
                second = "0" + second;


            theTime = hour + ":" + minute + ":" + second + " ";

        } catch (Exception dumtime) {
        }

        return theTime;
    }


    public class Datagram1 {

        Datagram1(String s) {
            try {
                if (s.length() == 0)// this is fics dummy datagram
                    return;

                type = -1;

                int len = s.length();
                if (len >= 100000) {
                    // Datagram to long!!
                    len = 100000 - 1;
                }


                String p = s.substring(2, len - 1);

                // newbox
                //newbox.append("trying to parse 2, p.length =" + p.length() + " p is: " + p + "\n");
				/*StyledDocument doc=consoles[0].getStyledDocument();
										try {
											doc.insertString(doc.getEndPosition().getOffset(), "trying to parse 2, p.length =" + p.length() + " p is: " + p + "\n", null);


										consoles[0].setStyledDocument(doc);
										}
										catch(Exception e)
										{
										}

				*/
                argc = 0;
                int a = 1; // allways on
                while (a == 1) {
                    if (p.charAt(0) == '{') {
                        int end = p.indexOf("}");
                        String p2;
                        if (end != 1)
                            p2 = p.substring(1, end);
                        else
                            p2 = "";
                        arg[argc++] = p2;
                        try {
                            p = p.substring(end + 1, p.length());
                            if (p.length() < 3)
                                return;
                        } catch (Exception dd) {
                            return;
                        }

                    } else if (p.charAt(0) == '\031' && p.charAt(1) == '{') {
                        int counter = 0;
                        while (p.charAt(0) == '\031' && p.charAt(1) == '{') {
                            counter++;

                            int end = p.indexOf("\031}");
                            String p2;
                            if (end != 2)
                                p2 = p.substring(2, end);
                            else
                                p2 = "";
                            arg[argc++] = p2;
                            try {
                                p = p.substring(end + 2, p.length());
                                if (p.length() < 3)
                                    return;
                            } catch (Exception dd) {
                                return;
                            }

                        }// end while
                    }// end if
                    else if (p.charAt(0) != ' ' && p.charAt(0) != ')') {
                        int end = p.indexOf(" ");
                        //writedg("p remains start :" + p + ": and lenght is " + p.length());
                        if (end == -1) {
                            end = p.indexOf("\031");
                            if (end == -1)
                                return;
                        }
                        //writedg("final else " + argc);
                        String p2 = p.substring(0, end);
                        arg[argc++] = p2;
                        p = p.substring(end, p.length());
                        //    writedg("final else2 " + argc + " and arg is :" + arg[argc-1] + ":");
                        //    writedg("p remains end :" + p + ": and lenght is " + p.length());

                    }

                    //if(p.charAt(0) == '\031' && p.charAt(1) == ')')
                    // return;

                    while (a == 1) {
                        if (p.length() <= 1) // " )'\n''031'" in no particular order
                            return;
                        else {
                            if (p.charAt(0) == '{')
                                break;
                            else if (p.charAt(0) == '\031' && p.charAt(1) == '{')
                                break;
                            else
                                p = p.substring(1, p.length());
                        }
                        if (p.length() == 1) // " )'\n''031'" in no particular order
                            return;
                        if (p.charAt(0) != ' ')          // Look for a non-space.
                            break;
                    }
                }
            }// end try
            catch (Exception dui) {//writeToSubConsole(" datagram exception \n", sharedVariables.openConsoleCount-1);
            }
        }

        public String getArg(int i) {
            if (i >= argc || i < 0)
                return "";

            return arg[i];
        }

        public String[] arg = new String[250];
        public int argc;
        public int type;

    }// end class

    void parseDatagram(Datagram1 gram) {

    }


    boolean processGameLine(String newdata) {

        boolean startClock = false;

        int GAME_TYPE = NO_TYPE;
        // mygame = mySquares.mygame;
        boolean returnValue = false;

        if (newdata.startsWith("<12>") || newdata.startsWith("<b1>")) {
            returnValue = true;
        }
        if (newdata.startsWith("<12>")) {
            Style12Struct styleLine = getStyle12StructString(newdata);
            boolean gameExists = checkIfGameExists(styleLine.getGameNumber());
            if (styleLine != null) {
                if (!gameExists) {
                    String wild = "";
                    if (!lastGameStartString.equals("")) {
                        ArrayList<String> line = new ArrayList<String>();
                        seperateLine(lastGameStartString, line);
                        if (line.size() > 6) {
                            wild = line.get(6);
                        }
                    }
                    gameStarted(styleLine, wild);
                }

                if (gameExists) {
                    if (backedUp) {
                        backedUp = false;
                    } else {
                        if (checkIfIllegalPlayedMoveWasMade(styleLine)) {
                            sendIllegalMove(styleLine);
                        } else {
                            if (getMyRelation(newdata) == 2 && checkGameStatus(styleLine.getGameNumber()) == mySettings.STATE_OBSERVING && !processingLater) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        processingLater = true;
                                        try {
                                            processGameLine(newdata);
                                        } catch (Exception e1) {
                                        }
                                        processingLater = false;
                                    }
                                });
                                return returnValue;
                            }
                            moveSent(styleLine);
                        }

                    }
                }
                updateBoard(styleLine, newdata);
                updateFicsClocks(styleLine);

            } else {
                System.out.println("our game is null");
            }
        }


        if (checkIfGameOver(newdata)) {
            return false;
        }

        if (newdata.startsWith("Game ") || newdata.startsWith("Creating: ")) {
            setGameStartParamsAsNeeded(newdata);
        }
        parseCreatingAsNeededForRatings(newdata);
        

        
        /*

        if(isPrimaryMessage(newdata)) {
            return true;
        }
        */


        if (newdata.startsWith("{Game ") || newdata.startsWith("Game ") || newdata.startsWith("You're at the ")) {
            // Game 156:

            if (isExamineInfo(newdata, mySettings)) {
                gameMessage(newdata);
                return true;
            }
        }

        if (newdata.startsWith("Illegal move")) {
            return illegalMessage(newdata, mySettings);
        }


        if (newdata.startsWith("<b1>")) {
            // <b1> game 818 white [] black [PP]
            ArrayList<String> spaceArray = new ArrayList<>();
            seperateLine(newdata, spaceArray);
            String gameNumber = "9999999";
            if (spaceArray.size() > 6) {
                gameNumber = spaceArray.get(2);
            }
            setCrazyBoard(newdata, gameNumber);
            return returnValue;
        }
        
        /*




        if(newdata.startsWith("<12>"))

    {
        String number = getGameNumber(newdata);
        GameState openGame = getAnOpenGameState(false, number, mySettings);
        if (openGame == null || !openGame.gameNumber.equals(number) || openGame.relationToGame.equals("-3")) {
            GAME_TYPE = GAME_START;
        }
        if (GAME_TYPE != NO_TYPE) {
           GameState tempo = processGameType(GAME_TYPE, number);
            if (GAME_TYPE == GAME_START && tempo != null) {
                openGame = tempo;
                openGame.gameNumber = number;
                startClock = true;
            }
        }


        openGame.setStyle12Board(newdata, checkIfPlaying(openGame));
        openGame.runningClockUpdate(openGame.sideToMove, getMilliSeconds(openGame), "1");

        if(GAME_TYPE == GAME_START) {
            for(int aa = 0; aa < 64; aa++) {
                if(openGame.flipType.equals("1")) {
                    openGame.initialScrollBoard[aa] = openGame.board[63 - aa];
                } else {
                    openGame.initialScrollBoard[aa] = openGame.board[aa];
                }

            }
            if(openGame.relationToGame.equals("1") || openGame.relationToGame.equals("-1")) {
                if(mySettings.gameSounds) {
                    MainActivity.playSound("gamestart");
                }
                dismissSeekGraph();
            }
            if(!lastGameStartString.equals("")) {
                 setGameStartParamsAsNeeded(lastGameStartString);
                lastGameStartString = "";
            } else if(!mySettings.fics) {
                setICCGameStartParamsAsNeeded(openGame);
            }

            //remove seek graph
            if(openGame.relationToGame.equals("-1") || openGame.relationToGame.equals("1"))
            {
                MainActivity.mySquares.mygame = openGame;
                mygame = openGame;
                MainActivity main  = (MainActivity)MainActivity.getAppActivity();
                int tab = main.getTabHost().getCurrentTab();
                if(tab == MainActivity.BOARD_TAB) {
                    ICSBoard board = (ICSBoard) main.getLocalActivityManager().getCurrentActivity();
                    setStockfishButtonVisiblity(board, false);
                }
            }




        } else {
            GAME_TYPE = SEND_MOVE;
             processGameType(SEND_MOVE, number);
        }

        if(openGame.relationToGame.equals("1") && !openGame.premoveMade.equals(""))
        {
            OutputDataClass data = new OutputDataClass();
             data.sendData = openGame.premoveMade;
            openGame.premoveMade = "";
            openGame.premoveFrom = -1;
            openGame.premoveTo = -1;
            sendQueueConsole.add(data);
        }

        if(startClock && mygame == openGame && !mygame.relationToGame.equals("2") && !mygame.relationToGame.equals("-3")) {
                 mygame.clockRunning = "1";
                 openGame.runningClockUpdate(openGame.sideToMove, getMilliSeconds(openGame), "1");
                 MainActivity.mySquares.startStopClock("1");
    }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity.mySquares.invalidate();
                } catch (Exception dui) {
                    //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                }
            }
        });

    }
        
*/
        if (!gamequeue.isEmpty()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mainTelnet.client.Run();
                }
            });
        }// end if not empty queue
        return returnValue;

    }


    void writeOutSays(String theTell, String theTell2) {
        try {
            channels sharedVariables = mySettings;
            String name = theTell.contains("[") ? theTell.substring(0, theTell.indexOf("[")) : theTell.trim().substring(0, theTell.length());
            if (theTell.contains("(")) {
                name = theTell.substring(0, theTell.indexOf("("));
            }
            int spaceIndex = name.indexOf(" ");
            if (spaceIndex > -1 && spaceIndex + 1 < name.length()) {
                name = name.substring(spaceIndex + 1, name.length());
            }
            sharedVariables.lasttell = name; // obsolete but why not leave the data
            sharedVariables.F9Manager.addName(name);
            boolean him = false;

            try {
                messageStyles myStyles = null;
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyledDocument doc;

                {
                    doc = sharedVariables.mydocs[sharedVariables.looking[sharedVariables.tellconsole]];
                    int direction = sharedVariables.looking[sharedVariables.tellconsole];
                    if (sharedVariables.tellsToTab == true) {
                        direction = sharedVariables.tellTab;
                        doc = sharedVariables.mydocs[direction];
                    }
                    /*** check if forced to tab ****/
                    // boolean him = false; moved up for scope
                    boolean makeASound = true;
                    for (int ab = 0; ab < sharedVariables.toldTabNames.size(); ab++) {
                        if (sharedVariables.toldTabNames.get(ab).name.toLowerCase().equals(name.toLowerCase())) {
                            direction = sharedVariables.toldTabNames.get(ab).tab;
                            doc = sharedVariables.mydocs[direction];
                            him = true;
                            makeASound = sharedVariables.toldTabNames.get(ab).sound;
                            break;
                        }
                    }
                    int consoleType = SUBFRAME_CONSOLES;
                    int number = direction;
                    int[] cindex2 = new int[mySettings.maxConsoleTabs];
                    cindex2[0] = direction; // default till we know more is its not going to main

                    if (!theTell2.equals("")) {
                        processLink2(doc, theTell + "\n" + theTell2 + "\n", sharedVariables.tellcolor, direction, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, null);
                    } else {
                        processLink2(doc, theTell + "\n", sharedVariables.tellcolor, direction, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, null);
                    }

                    try {
                        if (sharedVariables.tellsToTab == true && sharedVariables.switchOnTell == true && him == false) {
                            FocusOwner whohasit = new FocusOwner(sharedVariables, mainTelnet.consoleSubframes, mainTelnet.myboards);
                            int xxx = mainTelnet.getCurrentConsole();
                            mainTelnet.consoleSubframes[sharedVariables.tellconsole].makeHappen(sharedVariables.tellTab);

                            if (xxx != sharedVariables.tellconsole || !sharedVariables.operatingSystem.equals("mac"))
                                mainTelnet.giveFocus(whohasit);
                            if (sharedVariables.addNameOnSwitch == true) {
                                mainTelnet.consoleSubframes[sharedVariables.tellconsole].addNameToCombo(name);
                            }
                        }
                    } catch (Exception donthave) {
                    }
                    if (sharedVariables.makeSounds == true && makeASound && sharedVariables.makeTellSounds) {
                        Sound ptell = new Sound(sharedVariables.songs[0]);
                    }

                }

                for (int z = 0; z < sharedVariables.openBoardCount && him == false; z++) {
                    if (mainTelnet.myboards[z] != null) {

                        if (sharedVariables.mygame[z] != null
                                && (sharedVariables.mygame[z].state == sharedVariables.STATE_PLAYING || (sharedVariables.mygame[z].state == sharedVariables.STATE_OVER
                                && (sharedVariables.mygame[z].realname1.toLowerCase().equals(name.toLowerCase()) || sharedVariables.mygame[z].realname2.toLowerCase().equals(name.toLowerCase()))))) {


                            doc = sharedVariables.mygamedocs[z];
                            int consoleType = GAME_CONSOLES;
                            int number = z;


                            if (sharedVariables.boardConsoleType != 0) {

                                if (!theTell2.equals("")) {
                                    processLink(doc, theTell + "\n" + theTell2 + "\n", sharedVariables.tellcolor, number, maxLinks, consoleType, attrs, myStyles);
                                } else {
                                    processLink(doc, theTell + "\n", sharedVariables.tellcolor, number, maxLinks, consoleType, attrs, myStyles);
                                }
                            }

                        }
                    }
                }
            } catch (Exception dumb) {
            }
        } catch (Exception dumb1) {
        }
    }


    void writeOutKibWhisperToGameConsoles(String theTell, String theTell2) {
        try {


            int gameNumber = getGameNumberFromKibWhisper(theTell);
            channels sharedVariables = mySettings;
            try {
                messageStyles myStyles = null;
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyledDocument doc;

                for (int z = 0; z < sharedVariables.openBoardCount; z++) {
                    if (mainTelnet.myboards[z] != null)

                        if (sharedVariables.mygame[z] != null && sharedVariables.mygame[z].myGameNumber == gameNumber) {


                            doc = sharedVariables.mygamedocs[z];
                            int consoleType = GAME_CONSOLES;
                            int number = z;
                            if (sharedVariables.boardConsoleType == 0) {
                                doc = mySettings.mydocs[0];
                                consoleType = SUBFRAME_CONSOLES;
                                number = 0;
                                int[] cindex2 = new int[mySettings.maxConsoleTabs];
                                cindex2[0] = 0; // default till we know more is its not going to main

                                if (!theTell2.equals("")) {
                                    processLink2(doc, theTell + "\n" + theTell2 + "\n", sharedVariables.kibcolor, 0, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, null);
                                } else {
                                    processLink2(doc, theTell + "\n", sharedVariables.kibcolor, 0, maxLinks, SUBFRAME_CONSOLES, attrs, cindex2, null);
                                }
                            } else {

                                if (!theTell2.equals("")) {
                                    processLink(doc, theTell + "\n" + theTell2 + "\n", sharedVariables.kibcolor, number, maxLinks, consoleType, attrs, myStyles);
                                } else {
                                    processLink(doc, theTell + "\n", sharedVariables.kibcolor, number, maxLinks, consoleType, attrs, myStyles);
                                }
                            }

                        }
                }
            } catch (Exception dumb) {
            }
        } catch (Exception dumb1) {
        }
    }

 /*
    GameState processGameType(int GAME_TYPE, String number)
    {


        if(GAME_TYPE == GAME_START)
        {
            GameState openGame = null;
            if(mygame != null)
            {
                boolean isPlaying = false;
                if(checkIfPlaying(mygame)) {
                isPlaying = true;
            }
                if(checkIfObserving())
                {
                    mygame.observingGameNumber = number;

                }
                openGame = getAnOpenGameState(true , number , MainActivity.mySettings);
         

                openGame.gameResult = "";
                openGame.premoveMade = "";
                openGame.premoveFrom = -1;
                openGame.premoveTo = -1;
           
              try {
                  if(!isPlaying) {
                      mygame = openGame;
                      MainActivity.mySquares.mygame = openGame;

                  }
                  MainActivity main = (MainActivity) MainActivity.getAppActivity();
                  int tab = main.getTabHost().getCurrentTab();
                  if(tab == MainActivity.BOARD_TAB) {
                      ICSBoard board = (ICSBoard) main.getLocalActivityManager().getCurrentActivity();
                      turnOffStockfishIfOn(board);
                  }
                  new Handler(Looper.getMainLooper()).post(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              MainActivity.mySquares.invalidate();
                          } catch (Exception dui) {
                              //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                          }
                      }
                  });
              } catch(Exception dui) {

              }


            }
            return openGame;
        }

        if(GAME_TYPE == SEND_MOVE)// send_move
        {

            GameState openGame = getAnOpenGameState(false , number , MainActivity.mySettings);
            if(openGame != null) {
                if(mySettings.fics) {
                    openGame.setLastFromTo(getMoveFromVerbose(openGame.verboseMove));
                }
            playMoveSoundAsNeeded(openGame);
            }

        }



        if(GAME_TYPE == GAME_ENDED) {
            GameState openGame = getAnOpenGameState(false , number , MainActivity.mySettings);
            if(openGame != null) {
                if(mySettings.fics && !openGame.relationToGame.equals("0")) {
                    sendToFICS("$iset seekinfo 1");
                }
            openGame.myGameEnded();
                // set result and my relation to game
                openGame.gameResult = "*";
                openGame.relationToGame = "-3";
                if(openGame.observingGameNumber.equals(mygame.observingGameNumber)) {
                    openGame.observingGameNumber = "";
                }
            //[boardvc.mysquares setBackground];
                if(openGame  == mygame) {
                    openGame.clockRunning = "0";
                    MainActivity.mySquares.startStopClock("0");
                }


                MainActivity main = (MainActivity) MainActivity.getAppActivity();
                int tab = main.getTabHost().getCurrentTab();
                if(tab == MainActivity.BOARD_TAB) {
                    ICSBoard board = (ICSBoard) main.getLocalActivityManager().getCurrentActivity();
                    if(!board.amPlayingAnyGame()) {
                       setStockfishButtonVisiblity(board, true);
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MainActivity.mySquares.invalidate();
                        } catch (Exception dui) {
                            //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                        }
                    }
                });
            }

        }


        return null;
    }

    void turnOffStockfishIfOn(final ICSBoard board) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    board.setConsoleEngineMode(false);
                } catch (Exception dui) {
                    //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                }
            }
        });
    }

    void setStockfishButtonVisiblity(final ICSBoard board, final boolean visible) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    board.setStockfishButtonVisible(visible);
                    if(!visible) {
                        board.setConsoleEngineMode(visible);
                    }
                } catch (Exception dui) {
                    //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                }
            }
        });
    }

    void playMoveSoundAsNeeded(GameState openGame)
    {
    */
        /* my relation to this game: -3 isolated position, as in "ref 3" or sposition
        -2 observing examined game
        2 the examiner of this game
            -1 I am playing, it's the opponent's move
        1 I am playing and it's my move
        0 observing played game
         */
       /* if(mySettings.gameSounds && openGame == MainActivity.mySquares.mygame) {
            if(openGame.relationToGame.equals("1") || openGame.relationToGame.equals("-1") || openGame.relationToGame.equals("2")) {
                MainActivity.playSound("move");
            } else if(mySettings.observeGameSounds) {
                if(openGame.relationToGame.equals("-2") || openGame.relationToGame.equals("0")) {
                    MainActivity.playSound("move");
                }
            }
        }
        */
    //  }

    boolean checkIfObserving() {
        return false;
    }


    String getGameNumber(String delta12) {
        String gameNumber = "";

        try {


            int c = 4;
            for (int a = 0; a < 8; a++) {
                for (int b = 0; b < 8; b++) {
                    c += 1;

                }
                c += 1;
            }
            boolean go = true;
            int d = 0;

            while (go) {
                c += 1;
                String newString = delta12.substring(c, c + 1);
                if (newString.equals(" ") || d == 0) {
                    d++;
                    if (d == 1) {
                        getNameFromString(delta12, c++);

                    }

                    if (d == 7) {
                        return getNameFromString(delta12, c++);
                    }

                    //  just before white name is game nubmer nad 6 past is white clock in seconds.
                }
            }
        } catch (Exception exception) {
            // error happened! do something about the error state
        } finally {
            // do something to keep the program still running properly
        }


        return gameNumber;
    }

    /*

        GameState getAnOpenGameState(boolean new1, String gameNumber,  SharedSettings mySettings)
        {

            GameState theGame = null;

            if(new1) {
            removeGameThatEnded();
                theGame  = new GameState();
                theGame.mySettings = mySettings;
                openGames.add( theGame);



            } else {
            try {
                for(int a = 0; a < openGames.size(); a++) {
                    if (openGames.get(a).gameNumber.equals(gameNumber)) {
                        theGame = openGames.get(a);
                        break;

                    }
                }

                } catch (Exception exception) {
                    GameState myGame = new GameState();
                    myGame.mySettings = mySettings;
                    return myGame;
                }
            finally {
                    // do something to keep the program still running properly
                }
            }
            return theGame;

        }

        void removeGameThatEnded()
        {
            try {
                GameState theGame = null;
                ArrayList<GameState> mygames = openGames;
                for(int a =0; a < mygames.size(); a++) {
                    GameState theGame2 = mygames.get(a);
                    if (theGame2.relationToGame.equals("-3")) {
                        mygames.remove(a);
                        return;
                    }
                }
            } catch (Exception dui) {
              }
        }

        boolean checkIfPlaying(GameState mygame)
        {
            boolean amPlaying = false;
            if(mygame != null)
            {

                if(mygame.relationToGame.equals("1") ||
               mygame.relationToGame.equals("-1"))
                {
                    if(mygame.clockRunning.equals("1" ))
                    amPlaying = true;

                }
            }
            return amPlaying;
        }
    */
    String getNameFromString(String temp, int i) {

        boolean go = true;
        int d = i;
        while (go) {
            d += 1;

            String newString = temp.substring(d, d + 1);
            if (newString.equals(" ") || newString.equals("\n")) {
                temp = temp.substring(i, d);
                return temp.trim();

            }
        }
        return "";
    }

    String getMoveFromVerbose(String verboseMove, String color) {
        // R/d3-h3 or e2-e4
        String aMove = "e2e4";
        try {
            if (verboseMove.equals("o-o") || verboseMove.equals("o-o-o")) {

                if (verboseMove.equals("o-o") && color.equals("W")) // was blacks move
                {
                    return "e8g8c";
                } else if (verboseMove.equals("o-o")) {
                    return "e1g1c";
                }
                if (verboseMove.equals("o-o-o") && color.equals("W")) // was blacks move
                {
                    return "e8c8C";
                } else if (verboseMove.equals("o-o-o")) {
                    return "e1c1C";
                }
            }

            for (int a = 0; a < verboseMove.length() - 1; a++) {
                if (verboseMove.charAt(a) == '/') {
                    verboseMove = verboseMove.substring(a + 1, verboseMove.length());
                    break;
                }
            }
            if (verboseMove.length() > 4) {
                int index = -1;
                for (int a = 0; a < verboseMove.length() - 1; a++) {
                    if (verboseMove.charAt(a) == '-' && a > 0) {
                        String from = verboseMove.substring(0, a);
                        String to = verboseMove.substring(a + 1, verboseMove.length());
                        String tempo = from + to;
                        return tempo;
                    }
                }
            }
        } catch (Exception dui) {

        }


        return aMove;
    }
/*
    String getMilliSeconds(GameState openGame)
    {
        int seconds = 0;
        if(openGame.sideToMove.equals("W")) {
        try { seconds = Integer.parseInt(openGame.whiteClockSeconds); } catch(Exception dui) {};
    } else {
            try { seconds = Integer.parseInt(openGame.blackClockSeconds); } catch(Exception dui) {};
    }
        seconds *=1000;
        return "" + seconds;

    }
 
 */

    boolean checkIfGameOver(String newdata) {
        // also "You are no longer examining game 18.
        if (newdata.startsWith("You are no longer examining game ")) {
            String number = "";
            for (int a = 0; a < mySettings.mygame.length; a++) {
                if (mySettings.mygame[a] != null) {
                    gamestate openGame = mySettings.mygame[a];
                    if (openGame.state == mySettings.STATE_EXAMINING) {
                        number = "" + openGame.myGameNumber;
                        break;
                    }
                }
            }
            if (!number.equals("")) {
                gameEnded(number);
                return false;
            }

        } else if (newdata.startsWith("Removing game ")) {

            int i = 0;
            int start = 0;
            int stop = 0;
            for (int a = 0; a < newdata.length(); a++) {
                if (newdata.charAt(a) == ' ') {
                    i++;
                    if (i == 2) {
                        start = a;
                    }
                    if (i == 3) {
                        stop = a;
                        break;
                    }
                }

            }
            if (stop > (start + 1) && start > 0) {
                gameEnded(newdata.substring(start + 1, stop));// end is? mike stop - start -1
            }
            return false;
        } else {
            // check for played games ending
            if (!newdata.startsWith("{Game")) {
                return false;
            }

            ArrayList<String> spaceArray = new ArrayList<>();
            seperateLine(newdata, spaceArray);
            if (spaceArray.size() > 3) {
                String line1 = spaceArray.get(0);
                String line2 = spaceArray.get(1);
                // {Game 126 (adammr vs. AnderssenA) adammr resigns} 0-1
                if (line1.equals("{Game")) {
                    String number = line2;
                    gamestate openGame = null;
                    for (int a = 0; a < mySettings.mygame.length; a++) {
                        if (mySettings.mygame[a] != null && mySettings.mygame[a].state == mySettings.STATE_PLAYING && number.equals("" + mySettings.mygame[a].myGameNumber)) {
                            openGame = mySettings.mygame[a];
                            break;
                        }
                    }
                    if (openGame != null) {
                        // 1 -1 playing
                        String lineEnd = spaceArray.get(spaceArray.size() - 1);
                        String prev = spaceArray.get(spaceArray.size() - 2);
                        if (prev.endsWith("}")) {
                            int type = NO_TYPE;
                            if (lineEnd.equals("1-0")) {
                                type = GAME_ENDED;
                            }
                            if (lineEnd.equals("0-1")) {
                                type = GAME_ENDED;
                            }
                            if (lineEnd.equals("1/2-1/2")) {
                                type = GAME_ENDED;
                            }
                            if (lineEnd.equals("*")) {
                                type = GAME_ENDED;
                            }
                            if (type == GAME_ENDED) {
                                gameEnded(number);
                                String pgnResult = lineEnd;
                                openGame.ficsResult = pgnResult;
                                return false;
                            }
                        }

                    }
                }


            }

        }
        return false;
    }

    /*
        void setICCGameStartParamsAsNeeded(GameState openGame)
        {

            try {
                GameStartData data = (GameStartData)gameStartMap.get(openGame.gameNumber);
                if(data != null) {
                    String wElo = data.whiteElo;
                    String bElo = data.blackElo;
                    String wTitle = data.whiteTitle;
                    String bTitle = data.blackTitle;
                    String rType = data.ratingType;
                    String r = data.rated;
                    String wild = data.wild;

                    openGame.setGameStartedParmsFics(rType,  r,  wElo,  bElo,  wTitle,  bTitle, wild);
                    for(int a = 0; a < data.moves.size(); a++) {
                        openGame.setLastFromTo(data.moves.get(a));
                    }
                }
            } catch (Exception dui) {

            }

        }
    */
    void setGameStartParamsAsNeeded(String data) {
        boolean found = false;
        ArrayList<String> spaceArray = new ArrayList<>();
        seperateLine(data, spaceArray);
        if (spaceArray.size() == 10) {
            String tempo1 = spaceArray.get(3);
            String tempo2 = spaceArray.get(5);
            if (tempo1.startsWith("(") && tempo1.endsWith(")")) {
                if (tempo2.startsWith("(") && tempo2.endsWith(")")) {
                    found = true;
                }
            }
        }
        // Game 156: pecula (2279) johnlivelong (2073) rated blitz 3 0
        //-(void) setGameStartedParmsFics:  (NSString *) rType  : (NSString *) r : (NSString *) wElo : (NSString *) bElo : (NSString *) wTitle : (NSString *) bTitle ;
        if (found) {
            String number = "999999";
            String tempNumber = spaceArray.get(1);
            if (tempNumber.length() > 1) {
                number = tempNumber.substring(0, tempNumber.length() - 1);
            }
            gamestate openGame = null;
            for (int a = 0; a < mySettings.mygame.length; a++) {
                if (mySettings.mygame[a] != null && number.equals("" + mySettings.mygame[a].myGameNumber)) {
                    openGame = mySettings.mygame[a];
                    break;
                }
            }
            if (openGame != null && number.equals("" + openGame.myGameNumber)) {
                String rType = spaceArray.get(7);
                if (rType.equals("crazyhouse")) {
                    rType = "Crazyhouse";
                }
                String r = "";
                if (spaceArray.get(6).equals("rated")) {
                    r = "1";
                } else {
                    r = "0";
                }
                String wElo = "";
                String bElo = "";
                String tempo1 = spaceArray.get(3);
                String tempo2 = spaceArray.get(5);
                wElo = getGameStartRating(tempo1);
                bElo = getGameStartRating(tempo2);
                String wTitle = "";
                String bTitle = "";
                setGameStartedParmsFics("" + openGame.myGameNumber, rType, r, wElo, bElo, wTitle, bTitle);
                gameMessage(lastGameStartString);
                lastGameStartString = "";


            } else {
                lastGameStartString = data;
            }
        } else {
            // check for played game
            // Creating: name1 (+++) name2 (---) unrated crazyhouse 10 0
            String tempo1 = "-1";
            String tempo2 = "-1";
            if (spaceArray.size() == 9 && spaceArray.get(0).equals("Creating:")) {
                tempo1 = spaceArray.get(1);
                tempo2 = spaceArray.get(3);
                if (tempo1.equals(mySettings.whoAmI) || tempo2.equals(mySettings.whoAmI)) {
                    found = true;
                }
            }
            if (found) {
                found = false;
                gamestate openGame = null;
                for (int a = 0; a < mySettings.mygame.length; a++) {
                    if (mySettings.mygame[a] != null && mySettings.mygame[a].realname1.equals(tempo1) && mySettings.mygame[a].realname2.equals(tempo2)) {
                        openGame = mySettings.mygame[a];
                        found = true;
                        break;
                    }
                }

                if (found) {
                    String rType = spaceArray.get(6);
                    if (rType.equals("crazyhouse")) {
                        rType = "Crazyhouse";
                    }
                    String r = "";
                    if (spaceArray.get(5).equals("rated")) {
                        r = "1";
                    } else {
                        r = "0";
                    }

                    String wElo = "";
                    String bElo = "";
                    if (!(lastCreatingWhiteELO.equals("") && lastCreatingBlackELO.equals(""))) {
                        wElo = lastCreatingWhiteELO;
                        bElo = lastCreatingBlackELO;
                        lastCreatingWhiteELO = "";
                        lastCreatingBlackELO = "";
                    }

                    String wTitle = "";
                    String bTitle = "";
                    setGameStartedParmsFics("" + openGame.myGameNumber, rType, r, wElo, bElo, wTitle, bTitle);
                    gameMessage(lastGameStartString);
                    lastGameStartString = "";


                } else {
                    lastGameStartString = data;
                }
            }

        }
    }

    String getGameStartRating(String tempo) {
        String rating = "";
        // (1923)
        String temp = "";
        for (int a = 1; a < tempo.length(); a++) {
            if (tempo.charAt(a) == ')') {
                return temp;
            } else {
                temp += tempo.charAt(a);
            }
        }
        return rating;
    }

    void parseCreatingAsNeededForRatings(String newdata) {
        // Creating: GuestNGQR (++++) MasterGameBot (----) unrated blitz 10 0
        if (!newdata.startsWith("Creating:")) {
            return;
        }
        ArrayList<String> spaceArray = new ArrayList<>();
        seperateLine(newdata, spaceArray);
        if (spaceArray.size() > 5) {
            String tempo2 = spaceArray.get(2);
            String tempo4 = spaceArray.get(4);
            if (tempo2.length() < 3) {
                return;
            }
            if (tempo4.length() < 3) {
                return;
            }
            if (!(tempo2.startsWith("(") && tempo2.endsWith(")"))) {
                return;
            }
            if (!(tempo4.startsWith("(") && tempo4.endsWith(")"))) {
                return;
            }

            lastCreatingWhiteELO = tempo2.substring(1, tempo2.length());
            lastCreatingWhiteELO = lastCreatingWhiteELO.substring(0, lastCreatingWhiteELO.length() - 1);
            lastCreatingBlackELO = tempo4.substring(1, tempo4.length());
            lastCreatingBlackELO = lastCreatingBlackELO.substring(0, lastCreatingBlackELO.length() - 1);

        }
    }

    int getGameNumberFromKibWhisper(String data) {
        int start = data.indexOf("[");
        int end = data.indexOf("]");
        if (start > -1 && end > -1 && end > start) {
            String strNumber = data.substring(start + 1, end);
            try {
                return Integer.parseInt(strNumber);
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    boolean isKibWhisperInfo(String data) {
        //MasterGameBot(----)[15] kibitzes: hi
        //MasterGameBot(----)[15] whispers: hi
        //MasterGameBot(----)[33] whispers: hi
        //adammr(1250)[5] kibitzes: hi

        if (data == null || !data.contains("[") || !data.contains("]")) {
            return false;
        }

        ArrayList<String> spaceArray = new ArrayList<>();
        seperateLine(data, spaceArray);
        if (spaceArray.size() > 1) {
            String item0 = spaceArray.get(0);
            String item1 = spaceArray.get(1);
            if (item1.equals("kibitzes:")) {
                return true;
            }
            if (item1.equals("whispers:")) {
                return true;
            }
            if (item0.equals("(kibitzed") && item1.equals("to")) {
                return true;
            }
            if (item0.equals("(whispered") && item1.equals("to")) {
                return true;
            }
        }
        if (spaceArray.size() > 3) {
            // trainaingbot with 3 titles
            String item00 = spaceArray.get(0);
            String item0 = spaceArray.get(2);
            String item1 = spaceArray.get(3);
            if (item1.equals("kibitzes:") && item0.endsWith(")") && item00.contains("(") && !item00.contains(")")) {
                return true;
            }
        }
        if (spaceArray.size() > 2) {
            // 2  titles
            String item00 = spaceArray.get(0);
            String item0 = spaceArray.get(1);
            String item1 = spaceArray.get(2);
            if (item1.equals("kibitzes:") && item0.endsWith(")") && item00.contains("(") && !item00.contains(")")) {
                return true;
            }
        }


        return false;
    }

    boolean isExamineInfo(String data, channels mySettings) {
        if (data == null) {
            return false;
        }
        if (data.startsWith("You're at the end of the game.") || data.startsWith("You're at the beginning of the game.")) {
            return true;
        }
        ArrayList<String> spaceArray = new ArrayList<>();
        seperateLine(data, spaceArray);
        if (spaceArray.size() == 5) {
            String item0 = spaceArray.get(0);
            if (!item0.equals("Game")) {
                return false;
            }
            String possNumber = spaceArray.get(1);
            if (possNumber.length() > 1 && possNumber.endsWith(":")) {
                String number = possNumber.substring(0, possNumber.length() - 1);
                try {
                    int num = Integer.parseInt(number);
                    for (int a = 0; a < mySettings.mygame.length; a++) {
                        if (mySettings.mygame[a] != null) {
                            if (mySettings.mygame[a].state == mySettings.STATE_EXAMINING && mySettings.mygame[a].myGameNumber == num) {
                                return true;
                            }
                        }
                    }

                } catch (Exception duinotnumber) {
                }
            }

            String item2 = spaceArray.get(2);
            String item3 = spaceArray.get(3);
            String item4 = spaceArray.get(4);
            if (item2.equals(mySettings.whoAmI)) {
                if (item3.equals("moves:")) {
                    return true;
                }
                if (item3.equals("commits") && item4.equals("the")) {
                    return true;
                }
            }
        }
        if (spaceArray.size() == 6) {
            String item0 = spaceArray.get(0);
            if (!item0.equals("Game")) {
                return false;
            }
            String item2 = spaceArray.get(2);
            String item3 = spaceArray.get(3);
            String item4 = spaceArray.get(4);
            if (item2.equals(mySettings.whoAmI)) {
                if (item3.equals("commits") && item4.equals("the")) {
                    return true;
                }
            }
        }
        if (spaceArray.size() == 7) {
            String item0 = spaceArray.get(0);
            if (!item0.equals("Game")) {
                return false;
            }
            String item2 = spaceArray.get(2);
            String item3 = spaceArray.get(3);
            String item4 = spaceArray.get(4);
            if (item3.equals("backs") && item4.equals("up")) {
                for (int a = 0; a < mySettings.mygame.length; a++) {
                    if (mySettings.mygame[a] != null) {
                        if (mySettings.mygame[a].state == mySettings.STATE_EXAMINING) {
                            parseForwardBackward(spaceArray);
                            return true;
                        }
                    }
                }
            }
            if (item3.equals("goes") && item4.equals("forward")) {
                for (int a = 0; a < mySettings.mygame.length; a++) {
                    if (mySettings.mygame[a] != null) {
                        if (mySettings.mygame[a].state == mySettings.STATE_EXAMINING) {
                            parseForwardBackward(spaceArray);
                            return true;
                        }
                    }
                }
            }
        }
        if (spaceArray.size() == 6) // same thing but for icc
        {
            String item0 = spaceArray.get(0);
            if (!item0.equals("Game")) {
                return false;
            }
            String item2 = spaceArray.get(2);
            String item3 = spaceArray.get(3);
            String item4 = spaceArray.get(4);
            if (item2.equals(mySettings.whoAmI)) {
                if (item3.equals("backs") && item4.equals("up")) {
                    return true;
                }
                if (item3.equals("goes") && item4.equals("forward")) {
                    return true;
                }
            }
        }
        return false;
    }

    void parseForwardBackward(ArrayList<String> spaceArray) {
        /*
         23 DG_BACKWARD
             Form: (gamenumber backup-count)
             A player backed up in an examined game.
             Also generated by the "revert" command.

         */

        String item3 = spaceArray.get(3);
        String item4 = spaceArray.get(4);
        String item5 = spaceArray.get(5);
        try {
            if (item3.equals("backs") && item4.equals("up")) {
                newBoardData temp = new newBoardData();
                temp.dg = 23;
                temp.arg1 = spaceArray.get(1).replace(":", "").trim();// game number
                temp.arg2 = item5.trim(); // backup count
                gamequeue.add(temp);
                backedUp = true;
            }
        } catch (Exception badparse) {
        }
    }

    void setCrazyBoard(String data, String number) {
        // if(sharedVariables.mygame[Looking].crazypieces[piece]
        newBoardData temp = new newBoardData();
        temp.dg = 233;
        temp.arg1 = number;
        temp.arg2 = data;
        gamequeue.add(temp);
    }

    /*
        String getGameListName()
        {
            String name = "";
            if(mySettings.fics) {
                if(spaceSeperatedLine.size() > 2) {
                    String tempo = spaceSeperatedLine.get(2);
                    if (tempo.endsWith(":") && tempo.length() > 2) {
                        return tempo.substring(0, tempo.length() - 1);
                    }
                }
            } else {
                    if(spaceSeperatedLine.size() > 3) {
                        String tempo = spaceSeperatedLine.get(3);
                        if (tempo.endsWith(":") && tempo.length() > 2) {
                            return tempo.substring(0, tempo.length() - 1);
                        }
                    }
            }

            return name;
        }

        String getGameListCommand(String data)
        {
            String tempo = "$examine " + lastGameListName;
            if(!mySettings.fics) {
                tempo = "multi examine " + lastGameListName;
            }
            tempo = tempo + " ";
            // seperateLine:(NSString*) data : (NSMutableArray*) spaceSeperatedLine1
            ArrayList<String> myArray = new ArrayList<>();
            seperateLine(data,myArray);
            if(myArray.size() > 1) {
            String item1 = myArray.get(0);
            if(item1.endsWith(":") && item1.length() > 1) {
                String command = tempo + item1.substring(0, item1.length() -1);
                return command;
            } else {
                return "";
            }
        }

            return tempo;
        }

        void refreshGameListWithData(final String data, final String command, final boolean clearData)
        {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(ICSBoard.gamesAdapter != null) {
                            if(clearData) {
                                mySettings.currentGameListing.clear();;
                                mySettings.currentGameCommands.clear();
                            }
                            mySettings.currentGameListing.add(data);
                            mySettings.currentGameCommands.add(command);
                            ICSBoard.gamesAdapter.notifyDataSetChanged();
                        }

                    } catch (Exception dui) {
                        //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                    }
                }
            });

        }


        void updateTeller()
        {

            if(spaceSeperatedLine.size() > 0) {
                String tempo = spaceSeperatedLine.get(0);
                if(tempo.contains("(")) {
                    int i = tempo.indexOf("(");
                    if(i > 0) {
                        tempo = tempo.substring(0, i);
                    }
                } else if(tempo.contains("[")) {
                    int i = tempo.indexOf("[");
                    if(i > 0) {
                        tempo = tempo.substring(0, i);
                    }
                }
                tempo = "/tell " + tempo + "! ";
                mySettings.tellManager.addName(tempo);

            }
            MainActivity main = (MainActivity) MainActivity.getAppActivity();
            int tab = main.getTabHost().getCurrentTab();
            if(tab == MainActivity.CONSOLE_TAB) {
                ICSConsole console = (ICSConsole) main.getLocalActivityManager().getCurrentActivity();
                console.setTellerVisibility();
            }
        }

        boolean isPrimaryMessage(String data)
        {
            if(data.startsWith("Changing your primary observed game to ")) {
                return true;
            }

            if(data.startsWith("Your primary game is now ")) {
                return true;
            }
            if(data.startsWith("Game")) {
                ArrayList<String> spaceArray = new ArrayList<>();
                seperateLine(data, spaceArray);
                if(spaceArray.size() > 6) {
                    if(spaceArray.get(2).trim().equals("is"))
                        if(spaceArray.get(3).trim().equals("already"))
                            if(spaceArray.get(4).trim().equals("your"))
                                if(spaceArray.get(5).trim().equals("primary"))
                                    if(spaceArray.get(6).trim().equals("game."))
                                        return true;
                }
            }


            return false;
        }
    */
    void processSeekData(int ficsType, String data) {
        try {
            if (!data.startsWith("<s")) {
                return;
            }


            if (ficsType == SEEK_ADD) {
                // fill for seek graph


                // <s> 8 w=visar ti=02 rt=2194  t=4 i=0 r=r tp=suicide c=? rr=0-9999 a=t f=f
                // <s> 12 w=saeph ti=00 rt=1407  t=1 i=0 r=r tp=lightning c=? rr=0-9999 a=t f=f
                if (spaceSeperatedLine.size() < 2) {
                    return;
                }
                String sIndex = spaceSeperatedLine.get(1);
                String sName = "";
                if (spaceSeperatedLine.size() > 2) {
                    sName = getSeekArgument(spaceSeperatedLine.get(2));
                }
                String sTitles = "";

                String tempTitles = "";
                if (spaceSeperatedLine.size() > 3) {
                    tempTitles = getSeekArgument(spaceSeperatedLine.get(3));
                }
                if (tempTitles.equals("02")) {
                    sTitles = "C";
                }
                String sProvisional = "2";
                String sRating = "";
                if (spaceSeperatedLine.size() > 4) {
                    sRating = getSeekArgument(spaceSeperatedLine.get(4));
                }
                if (sRating.charAt(sRating.length() - 1) == 'E' || sRating.charAt(sRating.length() - 1) == 'P') {
                    if (sRating.charAt(sRating.length() - 1) == 'E') {
                        sProvisional = "3";
                    } else if (sRating.charAt(sRating.length() - 1) == 'P') {
                        sProvisional = "1";
                    }
                    if (sRating.length() > 1) {
                        sRating = sRating.substring(0, sRating.length() - 1);
                    }
                }
                //provisional-status is 0 (no games), 1 (provisional), or 2 (established),
                // we use 3 for estimated

                String sWild = "0";
                String sRatingType = "";
                if (spaceSeperatedLine.size() > 8) {
                    sRatingType = getSeekArgument(spaceSeperatedLine.get(8));
                }
                if (sRatingType.equals("standard")) {
                    sRatingType = "Standard";
                } else if (sRatingType.equals("lightning")) {
                    sRatingType = "Lightning";
                } else if (sRatingType.equals("crazyhouse")) {
                    sRatingType = "Crazyhouse";
                    sWild = "23";
                } else if (sRatingType.equals("crazyhouse")) {
                    sRatingType = "Crazyhouse";
                    sWild = "23";
                } else if (sRatingType.equals("blitz")) {
                    sRatingType = "Blitz";
                } else if (sRatingType.equals("losers")) {
                    sRatingType = "Losers";
                    sWild = "17";
                } else if (sRatingType.equals("fischerrandom")) {
                    sRatingType = "Chess960";
                    sWild = "22";
                } else if (sRatingType.equals("suicide")) {
                    sRatingType = "Suicide";
                    sWild = "26";
                } else {
                    sRatingType = "Unknown";
                    // NSLog(@"made rating type unkown for type %@", [self getSeekArgument:[spaceSeperatedLine objectAtIndex:8]] );
                }
                // 7 8 9 time inc rating
                String sTime = "";
                if (spaceSeperatedLine.size() > 5) {
                    sTime = getSeekArgument(spaceSeperatedLine.get(5));
                }
                String sInc = "";
                if (spaceSeperatedLine.size() > 6) {
                    sInc = getSeekArgument(spaceSeperatedLine.get(6));
                }
                String ratedness = "";
                if (spaceSeperatedLine.size() > 7) {
                    ratedness = getSeekArgument(spaceSeperatedLine.get(7));
                }


                String sRated = ratedness;

                String sRange = "";
                if (spaceSeperatedLine.size() > 10) {
                    sRange = getSeekArgument(spaceSeperatedLine.get(10));
                }

                String sColor = "";


                String sFormula = "";
                if (spaceSeperatedLine.size() > 12) {
                    sFormula = getSeekArgument(spaceSeperatedLine.get(12));
                }
                String sManual = "";


                if (mySettings.graphData != null) {
                    mySettings.graphData.addSeek(sIndex, sName, sTitles, sRating, sProvisional, sWild, sRatingType, sTime, sInc, sRated, sRange, sColor, sFormula, sManual, mainTelnet.notifyList);
                }

                //refresh
                refreshSeekView();

            }
            if (ficsType == SEEK_REMOVE)// 51 seek removed
            {


                if (mySettings.graphData != null) {
                    for (int a = 1; a < spaceSeperatedLine.size(); a++) {
                        mySettings.graphData.removeSeek(spaceSeperatedLine.get(a));
                    }
                    // [self.graphData removeSeek:[NSString stringWithFormat:@"%s",arg[0]]];

                }
                //refresh
                refreshSeekView();

            }

            if (ficsType == SEEK_REMOVE_ALL) {
                mySettings.graphData.resetToStartCondition();
            }

        } catch (Exception exception) {
            ;

        }

    }

    String getSeekArgument(String arg) {
        String tempo = "";
        for (int a = 0; a < arg.length(); a++) {
            if (arg.charAt(a) == '=' && a < arg.length() - 1) {
                return arg.substring(a + 1);
            }
        }
        return tempo;
    }

    void refreshSeekView() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    if (mainTelnet.seekGraph.isVisible())
                        mainTelnet.seekGraph.mypanel.repaint();
                } catch (Exception dui) {
                    //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                }
            }
        });

    }

    void dismissSeekGraph() {
        /*MainActivity main = (MainActivity) MainActivity.getAppActivity();
        int tab = main.getTabHost().getCurrentTab();
        if(tab == MainActivity.BOARD_TAB) {
            final ICSBoard board = (ICSBoard) main.getLocalActivityManager().getCurrentActivity();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(ICSBoard.seekGraphWindow != null) {
                            board.dismissSeekGraph();
                        }
                    } catch (Exception dui) {
                        //Log.d("TAG", "setStockfishButtonVisiblity: mike exceptoin on visiblity " + dui.getMessage());
                    }
                }
            });

        }
         */
    }

    boolean illegalMessage(String message, channels sharedVariables) {
        try {
            for (int a = 0; a < mySettings.mygame.length; a++) {
                if (mySettings.mygame[a] != null) {
                    if (mySettings.mygame[a].state == mySettings.STATE_PLAYING || mySettings.mygame[a].state == mySettings.STATE_EXAMINING) {
                        newBoardData temp = new newBoardData();
                        temp.dg = 77;
                        temp.arg1 = "" + mySettings.mygame[a].myGameNumber;
                        temp.arg2 = message;
                        gamequeue.add(temp);
                        return true;
                    }
                }
            }
        } catch (Exception e) {

        }
        return false;

    }

    void gameMessage(String message) {
        try {
            ArrayList<String> spaceArray = new ArrayList<>();
            seperateLine(message, spaceArray);
            newBoardData temp = new newBoardData();
            temp.dg = 77;
            if (spaceArray.size() < 2 || spaceArray.get(1) == null) {
                return;
            }
            temp.arg1 = spaceArray.get(1).replace(":", "");
            temp.arg2 = message;

            //
            if (spaceArray.get(0).startsWith("Creating")) {
                gamestate openGame = null;
                for (int a = 0; a < mySettings.mygame.length; a++) {
                    if (mySettings.mygame[a] != null && mySettings.mygame[a].realname1.toLowerCase().equals(spaceArray.get(1).toLowerCase())) {
                        temp.arg1 = "" + mySettings.mygame[a].myGameNumber;
                        break;
                    }
                }
            }
            if (message.startsWith("You're at the end of the game.") || message.startsWith("You're at the beginning of the game.")) {
                for (int a = 0; a < mySettings.mygame.length; a++) {
                    if (mySettings.mygame[a] != null && mySettings.mygame[a].state == mySettings.STATE_EXAMINING) {
                        temp.arg1 = "" + mySettings.mygame[a].myGameNumber;
                        break;
                    }
                }
            }
            gamequeue.add(temp);
        } catch (Exception dui) {
            System.out.println(dui.getMessage());
        }
        ;

    }

    int checkGameStatus(int num) {
        for (int a = 0; a < mySettings.mygame.length; a++) {
            if (mySettings.mygame[a] != null && mySettings.mygame[a].myGameNumber == num) {
                return mySettings.mygame[a].state;
            }
        }
        return -1;
    }

    int getMyRelation(String line) {
        try {
            ArrayList<String> spaceArray = new ArrayList<String>();
            seperateLine(line, spaceArray);
            return Integer.parseInt(spaceArray.get(19));
        } catch (Exception dui) {
        }
        ;

        return -1;
    }

    void gameStarted(Style12Struct myGameStruct, String wild) {
        newBoardData temp = new newBoardData();
        temp.type = myGameStruct.getGameType();
        temp.arg1 = "" + myGameStruct.getGameNumber();
        temp.arg2 = myGameStruct.getWhiteName();
        temp.arg3 = myGameStruct.getBlackName();
        temp.arg4 = "0";
        if (wild.equals("atomic")) {
            temp.arg4 = "27";
        } else if (wild.equals("losers")) {
            temp.arg4 = "17";
        } else if (wild.equals("crazyhouse")) {
            temp.arg4 = "23";
        } else if (wild.equals("suicide")) {
            temp.arg4 = "26";
        } else if (wild.equals("wild/fr")) {
            temp.arg4 = "22";
        }
        temp.arg5 = !temp.arg4.equals("0") ? wild : "blitz";
        temp.arg6 = "0";
        temp.arg7 = "" + (myGameStruct.getInitialTime() / 60);
        temp.arg8 = "" +
                myGameStruct.getIncrement();
        temp.arg11 = "0";
        temp.arg13 = "";
        temp.arg14 = "";
        temp.arg16 = "";
        temp.arg17 = "0";
        if (myGameStruct.isPlayedGame()) {
            temp.arg11 = "1";
        } else {
            temp.arg11 = "0";
        }
        temp.dg = 18;
        //if(dg.getArg(0).equals("40"))
        //temp.arg18="isolated";
        //else
        temp.arg18 = "!";
        gamequeue.add(temp);
        initialPositionSent(myGameStruct);

        // void gameStarted(String icsGameNumber, String WN, String BN,
        //String wildNumber, String rating_type, String rated,
        //String white_initial, String white_inc, String type,
        //String white_rating, String black_rating,
        //String white_titles, String black_titles, int played)

        // myboards[gamenum].gameStarted(temp.arg1, temp.arg2, temp.arg3, temp.arg4, temp.arg5, //temp.arg6, temp.arg7, temp.arg8, temp.arg11, temp.arg13, temp.arg14, temp.arg16, //temp.arg17, temp.type); // pass game number

        //@param gameType The code for the type of the game. Possible values are
        //* <code>MY_GAME</code>, <code>OBSERVED_GAME</code> and
        //* <code>ISOLATED_BOARD</code>.
        //@param isPlayedGame <code>true</code> if the game is played,
    }

    void setGameStartedParmsFics(String myGameNumber, String rType, String r, String wElo, String bElo, String wTitle, String bTitle) {
        newBoardData temp = new newBoardData();
        temp.dg = 250;
        temp.arg1 = myGameNumber;
        temp.arg2 = rType;
        temp.arg3 = r;
        temp.arg4 = wElo;
        temp.arg5 = bElo;
        temp.arg6 = wTitle;
        temp.arg7 = bTitle;
        gamequeue.add(temp);
    }

    void gameEnded(String gameNumber) {
        newBoardData temp = new newBoardData();
        temp.type = 0;
        temp.arg1 = gameNumber;
        temp.dg = 13;
        gamequeue.add(temp);
        for (int a = 0; a < mySettings.mygame.length; a++) {
            if (mySettings.mygame[a] != null && gameNumber.equals("" + mySettings.mygame[a].myGameNumber)) {
                if (mySettings.mygame[a].state == mySettings.STATE_PLAYING || mySettings.mygame[a].state == mySettings.STATE_EXAMINING) {
                    sendToFICS("$iset seekinfo 1");
                }
            }
        }


    }


    boolean checkIfIllegalPlayedMoveWasMade(Style12Struct myGameStruct) {
        try {
            for (int a = 0; a < mySettings.mygame.length; a++) {
                if (mySettings.mygame[a] != null && mySettings.mygame[a].myGameNumber == myGameStruct.getGameNumber()) {
                    if (mySettings.mygame[a].style12Boards.size() > 0 && (mySettings.mygame[a].state == mySettings.STATE_PLAYING || mySettings.mygame[a].state == mySettings.STATE_EXAMINING)) {
                        String last = mySettings.mygame[a].style12Boards.get(mySettings.mygame[a].style12Boards.size() - 1);
                        Style12Struct styleLine = getStyle12StructString(last);
                        if (styleLine.getBoardLexigraphic().equals(myGameStruct.getBoardLexigraphic())) {
                            if (styleLine.getCurrentPlayer().equals(myGameStruct.getCurrentPlayer()))
                                return true;
                        }
                    }
                }
            }
        } catch (Exception badformat) {

        }

        return false;
    }

    void sendIllegalMove(Style12Struct myGameStruct) {
        newBoardData temp = new newBoardData();
        temp.type = 0;
        temp.arg1 = "" + myGameStruct.getGameNumber();
        temp.arg2 = "";
        temp.arg3 = "";
        temp.dg = 42;
        gamequeue.add(temp);
    }

    void updateBoard(Style12Struct myGameStruct, String newdata) {
        newBoardData temp = new newBoardData();
        temp.type = 0;
        temp.arg1 = "" + myGameStruct.getGameNumber();
        temp.arg2 = myGameStruct.getBoardLexigraphic();
        temp.arg3 = newdata;
        temp.arg4 = myGameStruct.getCurrentPlayer();
        temp.arg5 = myGameStruct.canWhiteCastleKingside() ? "K" : "";
        temp.arg6 = myGameStruct.canWhiteCastleQueenside() ? "Q" : "";
        temp.arg7 = myGameStruct.canBlackCastleKingside() ? "k" : "";
        temp.arg8 = myGameStruct.canBlackCastleQueenside() ? "q" : "";
        temp.arg9 = myGameStruct.isBoardFlipped() ? "1" : "0";
        temp.arg10 = "" + myGameStruct.getDoublePawnPushFile();
        temp.arg11 = myGameStruct.getWhiteName(); // names can change in examine mode
        temp.arg12 = myGameStruct.getBlackName();

        temp.dg = 15202;
        gamequeue.add(temp);
    }

    void updateFicsClocks(Style12Struct myGameStruct) {
        // Color whose turn it is to move ("B" or "W")
        //String color = myGameStruct.getCurrentPlayer();
        newBoardData temp = new newBoardData();
        temp.type = 0;
        temp.arg1 = "" + myGameStruct.getGameNumber();
        temp.arg2 = "W";
        temp.arg3 = "" + myGameStruct.getWhiteTime() * 1000;
        temp.dg = 56;
        gamequeue.add(temp);
        /*if(color.equals("W")) {
            color = "B";
        } else {
            color = "W";
        }*/

        newBoardData temp1 = new newBoardData();
        temp1.type = 0;
        temp1.arg1 = "" + myGameStruct.getGameNumber();
        temp1.arg2 = "B";
        temp1.arg3 = "" + myGameStruct.getBlackTime() * 1000;
        temp1.dg = 56;
        gamequeue.add(temp1);
    }

    void moveSent(Style12Struct myGameStruct) {
        // void moveSent(String icsGameNumber, String amove,
        // String algabraicMove, boolean makeSound)
        newBoardData temp = new newBoardData();
        temp.type = 0;
        temp.arg1 = "" + myGameStruct.getGameNumber();
        temp.arg2 = getMoveFromVerbose(myGameStruct.getMoveVerbose(), myGameStruct.getCurrentPlayer());
        temp.arg3 = myGameStruct.getMoveSAN();
        temp.arg4 = "true"; // true for sound
        temp.dg = 24;
        gamequeue.add(temp);
    }

    void initialPositionSent(Style12Struct myGameStruct) {
        newBoardData temp = new newBoardData();
        temp.type = 0;
        temp.arg1 = "" + myGameStruct.getGameNumber();
        temp.arg2 = myGameStruct.getBoardLexigraphic();
        temp.dg = 25;
        gamequeue.add(temp);
    }

    boolean checkIfGameExists(int gameNumber) {
      /*  int gameNum = -1;
        try {
            gameNum = Integer.parseInt(gameNumber);
        } catch(Exception dui) {
            return true;
        }
       */
        for (int a = 0; a < mySettings.mygame.length; a++) {
            if (mySettings.mygame[a] != null && mySettings.mygame[a].myGameNumber == gameNumber) {
                return true;
            }
        }
        return false;
    }

    Style12Struct getStyle12StructString(String input) {
        try {
            Style12Struct style12line;
            //writeToConsole("looking for style 12 struct and myinput2 is now:" + myinput2 + ":::end myinput2\n");


            style12line = Style12Struct.parseStyle12Line(" " + input.trim() + " ");
            if (mySettings.fics) {
                return style12line;
            }


            // now we set the game info.
            // the method in gameboard will only act once. setting ficsSet to 1 and returning on future calls to set
            String thenumber = "" + style12line.getGameNumber();

            newBoardData temp = new newBoardData();
            temp.dg = 250;
            temp.arg1 = thenumber;
            try {
                temp.arg2 = style12line.getWhiteName();
            } catch (Exception e) {
                temp.arg2 = "somebody";
            }

            try {
                temp.arg3 = style12line.getBlackName();
            } catch (Exception e) {
                temp.arg3 = "somebody";
            }
            temp.arg4 = "" + style12line.getGameType(); // MY_GAME=1 OBSERVED_GAME=2 ISOLATED_BOARD
            String played = "False";
            if (style12line.isPlayedGame())
                played = "True";
            temp.arg5 = played;

            // we do a check here for if the game is examined, if so we try to set it up because we dont get game info on it
            if (played.equals("False") && temp.arg4.equals("1")) {
                //setupExaminingFics(thenumber);
            }

            temp.arg6 = "" + style12line.getPlayedPlyCount();

            String myturn = "True";
            try {
                if (!style12line.isMyTurn())
                    myturn = "False";

            } catch (Exception e) {
            }
            temp.arg7 = myturn;

            gamequeue.add(temp);

            temp = new newBoardData();
            temp.dg = 25;
            temp.arg1 = thenumber;
            temp.arg2 = style12line.getBoardLexigraphic();
            gamequeue.add(temp);

            // now we set the clocks
            temp = new newBoardData();
            temp.dg = 56;
            temp.arg1 = thenumber;
            temp.arg2 = "" + style12line.getWhiteTime();
            temp.arg3 = "" + style12line.getBlackTime();
            gamequeue.add(temp);
        } catch (Exception e) {
            //if(!(e.toString().contains("Missing \"<12>\" identifier")))
            //writeToConsole(" not a style 12 struct and error is: " + e.toString());
            System.out.println(e.getMessage());
        }
        return null;

    }

    void setUpNewUserTabs() {
        try {
            channels sharedVariables = mySettings;

            if (sharedVariables.channelNamesList.size() < sharedVariables.maxConsoleTabs - 1) {
                // condition that there are enough channels to be one per tab
                for (int li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    sharedVariables.console[li + 1][Integer.parseInt(sharedVariables.channelNamesList.get(li).channel)] = 1;
                    mainTelnet.setConsoleSendPrefixes(sharedVariables.channelNamesList.get(li).channel, li + 1);
                }
            }// end if
            else {


                int tabNumber = 0;
                boolean go = false;
                int li = 0;
                // *************** Stage 1
                for (li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    if (sharedVariables.channelNamesList.get(li).channel.equals("1")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][1] = 1;


                    }
                    if (sharedVariables.channelNamesList.get(li).channel.equals("2")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][2] = 1;

                    }

                    if (sharedVariables.channelNamesList.get(li).channel.equals("6")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][6] = 1;

                    }

                }
                if (sharedVariables.console[tabNumber][1] == 1 &&
                        sharedVariables.console[tabNumber][2] != 1 &&
                        sharedVariables.console[tabNumber][6] != 1)
                    mainTelnet.setConsoleSendPrefixes("1", tabNumber);
                // *************** Stage 2
                go = false;
                for (li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    if (sharedVariables.channelNamesList.get(li).channel.equals("39")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][39] = 1;
                    }


                }// end for

                if (sharedVariables.console[tabNumber][39] == 1)
                    mainTelnet.setConsoleSendPrefixes("39", tabNumber);
                // *********************** Stage 3
                go = false;
                for (li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    if (sharedVariables.channelNamesList.get(li).channel.equals("50")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][50] = 1;
                    }


                }// end for
                if (sharedVariables.console[tabNumber][50] == 1)
                    mainTelnet.setConsoleSendPrefixes("50", tabNumber);
                // ********************** stage 4
                // pass we need a tell tab

                // ********************* Stage 5
                go = false;
                for (li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    if (sharedVariables.channelNamesList.get(li).channel.equals("34")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][34] = 1;
                    }


                }// end for
                if (sharedVariables.console[tabNumber][34] == 1)
                    mainTelnet.setConsoleSendPrefixes("34", tabNumber);
                // ******************* Stage 6
                go = false;
                for (li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    if (sharedVariables.channelNamesList.get(li).channel.equals("85")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][85] = 1;
                    }


                }// end for
                if (sharedVariables.console[tabNumber][85] == 1)
                    mainTelnet.setConsoleSendPrefixes("85", tabNumber);
                // ************** Stage 7
                go = false;
                for (li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    if (sharedVariables.channelNamesList.get(li).channel.equals("40")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][40] = 1;
                    }


                }// end for
                if (sharedVariables.console[tabNumber][40] == 1)
                    mainTelnet.setConsoleSendPrefixes("40", tabNumber);
                // ******************** Stage 8

                if (sharedVariables.console[tabNumber][71] == 1)
                    mainTelnet.setConsoleSendPrefixes("71", tabNumber);

                //******************** Stage 9
                // *********************** tomato
                go = false;
                for (li = 0; li < sharedVariables.channelNamesList.size(); li++) {
                    if (sharedVariables.channelNamesList.get(li).channel.equals("49")) {
                        if (go == false) {
                            go = true;
                            tabNumber++;
                        }// go = false
                        sharedVariables.console[tabNumber][49] = 1;
                    }


                }// end for


            } // end else


            setConsoleTabTitles asetter = new setConsoleTabTitles();

            for (int z = 1; z < sharedVariables.openConsoleCount - 1; z++)
                if (sharedVariables.console[z][221] == 1)
                    asetter.createConsoleTabTitle(sharedVariables, z, mainTelnet.consoleSubframes, "Tomato");
                else
                    asetter.createConsoleTabTitle(sharedVariables, z, mainTelnet.consoleSubframes, "");// last argument tab name
        } catch (Exception dui) {
        }
    }// end  method set up new user tabs

    void populateEventData() {
        String eventData[] = {
                "tell puzzlebot getmate",
                "tell puzzlebot getstudy2",
                "tell puzzlebot getmate1",

                "tell endgamebot play kpk",
                "tell endgamebot play kbnk",
                "tell endgamebot play kbbk",

                "tell endgamebot play kqk",
                "tell endgamebot play krpkr",
                "tell endgamebot play krk",

                "tell endgamebot play kqkr",
                "tell endgamebot play kppkp",
                "tell endgamebot play kpkp",

                "tell endgamebot play kqpkq",
                "tell endgamebot play knnkp",
                "tell endgamebot play kppkpp",
                "tell endgamebot play kqqkqr"
        };
        boolean addedPuzzleInfo = false;
        boolean addedEndgameInfo = false;
        for (int a = 0; a < eventData.length; a++) {
            if (!addedPuzzleInfo && eventData[a].contains("puzzle")) {
                mainTelnet.eventsList.addToEvents(eventData[a], "" + a, eventData[a], "", mySettings.addHashWrapperToLookupUser("$finger puzzlebot\n"));
                addedPuzzleInfo = true;
            } else if (!addedEndgameInfo && eventData[a].contains("endgame")) {
                mainTelnet.eventsList.addToEvents(eventData[a], "" + a, eventData[a], "", mySettings.addHashWrapperToLookupUser("$finger endgamebot\n"));
                addedEndgameInfo = true;
            } else {
                mainTelnet.eventsList.addToEvents(eventData[a], "" + a, eventData[a], "", "");
            }

        }

    }

    void startSound() {
        // off now using lantern sound in sound.java flow
       /* try {
            FicsSoundPlayer sound = new FicsSoundPlayer();
            sound.sharedVariables = mySettings;
        Thread soundThread = new Thread(sound);
        soundThread.start();
        
        }
        catch(Exception gam){}
        */
    }

    class FicsSoundPlayer implements Runnable {

        channels sharedVariables = null;

        private AudioInputStream audio;

        public void run() {


            while (true) {
                try {
                    if (!soundCurrentlyPlaying) {
                        URL url = soundURLQueue.poll();
                        if (url != null) {
                            InputStream audioSrc = null;
                            InputStream bufferedIn = null;


                            try {
                                soundCurrentlyPlaying = true;
                                audioSrc = url.openStream();
                                bufferedIn = new BufferedInputStream(audioSrc);
                                audio = AudioSystem.getAudioInputStream(bufferedIn);
                                final Clip clip = AudioSystem.getClip();
                                clip.addLineListener(new LineListener() {
                                    @Override
                                    public void update(LineEvent event) {
                                        if (event.getType() == LineEvent.Type.STOP)
                                            clip.close();
                                    }
                                });
                                clip.open(audio);
                                clip.start();
                            } catch (Exception nosoundnow) {
                                soundURLQueue.add(url);
                                if (sharedVariables != null && sharedVariables.openBoardCount > 0) {
                                    Thread.sleep(10);
                                } else {
                                    Thread.sleep(100);
                                }
                                soundCurrentlyPlaying = false;
                            } finally {
                                if (bufferedIn != null) {
                                    bufferedIn.close();
                                }
                                if (audioSrc != null) {
                                    audioSrc.close();
                                }
                                soundCurrentlyPlaying = false;
                            } // end finlly
                        } // if url not null


                    } // if sound not playing
                    if (sharedVariables != null && sharedVariables.openBoardCount > 0) {
                        Thread.sleep(10);
                    } else {
                        Thread.sleep(100);
                    }
                } // try
                catch (Exception couldnt) {
                }


            }// end while
        }// end run


    }// end class FicsSoundPlayer


}
