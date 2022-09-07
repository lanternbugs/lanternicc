# lanternicc

Lantern was started at the end of 2009 as my first large app that I made. When I started it, I did not yet understand design patterns like delegation or mvc. It does have retain cycles but the app was basicaly designed to run like a number of singletons and performance is not bad and it has enjoyed some commercial success outlasting chessclub.com's ICC for Mac and now replacing it when www.chessclub.com/downloads is accessed on a Mac. My Mars and More app represents a more refined method to coding. In the middle more advanced than lantern but not as mature as Mars and More is my iOS chess apps where delegation became a common pattern and retain cycles were to be avoided.  Lantern was brute force. I have decide at this time to not bother to refactor it.  To me it's still worth updating as the users like it. A beta now runs on FICS as Pearl Chess. search for the fics var near top of channels.java for some detailed instructions on how to build for that in comments there. 

Lantern is a GPL program.  See copyright.txt and pieces-boards_copyrights.txt in the lantern folder.
The free folder is files from jinchess.com client that are utility and under LGPL.
I think right now we just use the jin sound package.
The lantern folder is all the java code we use and wrote that make up the client and graphics and sounds etc. 

Compiling and running lantern is actualy very simple.  But to get a couple of packages we use that are not in this repo 
as they are not our code though they are open source,  unzip a current lantern jar, found at lanternchess.com
(you can change the file extension from jar to zip)
and get the layout and org folders. The package in the org folder is an sqlite driver from xerial.org. The layout folder contains an open source  layout known as grid layout, more on that further below.  Also get the opening book, lanternopeningbook19 which is referenced  by the program. Update there is now a second
apache.commons 1.1 package in the org folder.  This is used for base64 encoding/decoding.  
The source is on the Apache site if you google the package. 

So you should have from this repo two directories, lantern and free and from the jar the org and layout
directory and the book. The org folder contains the java sqlite driver used with the opening
book and its licence is in licence-for-sqlite-jdbc-master.txt and the source code for it is
in sqlite-jdbc-master.zip. I put both these files, the zip and txt, in the org folder. 
 There is also a readme.txt I placed in the org folder with more info.  Place these 4 folders and a book in a folder.  ( two folders you pull out of this repo)

Update - there is now a 5th folder called engines in the jar that currently includes Mediocre Chess
V0.5 and CuckooChess 1.12  Java engines contained in their own jars.  There is a mediocre_readme.txt and cuckoo_readme.txt
in the engines folder with info on their project pages and how to get their source. They are GPL. While they are
packaged in the jar, when it's time to use them, Lantern prompts the user to let it extract them to the Lantern folder and opens them like other engines, with a runtime, and does not link to them in code. 

To compiles lantern type javac lantern/*.java
to run lantern type java lantern/Lantern      Lantern.java contains main.

To jar lantern type jar -cvfm lantern###.jar lantern/manifest.txt lantern free layout org engines lanternopeningbook19.db

I compile lantern in java 6 for release.  I have also compiled in Java 8.  When i edit lantern I sometimes just compile 
my changed files, like javac lantern/Lantern.java etc. A string mentioned earlier that was not compiling in channels.java
on mac Java 8, has been fixed. Lantern also works now on Java 9 though i've not tried compiling all files on 9, just some.

The free folder files have to be compiled too or you can just get the class files out of the jar. The free
folder also contains timestammp which is only in the jar. Chessbot4.java's connect() method could also
be altered to just make a regular socket without timestamp. We havent changed the free files i beleive.
I havent compiled them in years, i get the class file from the free folder in the jar.
But its similar to compiling lantern ie. javac free/*.java etc 
but get all sub directories.  Package lantern only keeps code in the lantern directory not sub directories. 

Now some info on some important files.

Lantern.java contains main.  It launches the program and makes the menu.  It creates the inital program,
windows that are open when a user launches lantern or calls creator files.  It also creates an instance of chessbot4.java for the telnet and channels.java for the sharedVariables. 

chessbot4.java Is lantern's main second thread. It contains the telnet loop. It  uses some classes and is
a large file  that does a lot of things. Everything that prints in consoles is formated there or a class it uses, and it calls gameboard.java with new game datagrams and creates game boards.  It handles both level1 and level2 parsing. 

channels.java is sort of misnamed. it was meant to store information on variables that make up what channels 
a user has, but it has long since become the main place global variables are stored. 
All over the program an object called sharedVariables is referenced and this is an instance of channels.java

The game board is basically gameboard.java (its the main frame and loads 3 panels). 
The three panels are the 64 squares which is gameboardpanel.java    the console and tab area which is
gameboardconsolepanel.java and the right side of board where move list and stuff is in gameboardcontrolspanel.java
The data object as well as methods that reresents the game is in gamestate.java. The int array[64] that represents the chess board is in there. makemove is the function that makes moves in gamestate.java.  

Communication between things like console and board to telnet or chessbot4.java happen through two 
concurrentlylinkedqueues.  You'll see them all over one for the board and one for the consoles and rest of program. There is probably no reason to have two queues. That was an early decision.
Many files have a reference to the queue passed in and create objects of class myoutput and adds it to 
the queue. Telnet is constantly checking for new things to act on or send to ICC and this type of queue is 
thread safe.

Consoles are instances of subframe.java The consoles styleddocuments are modified via the chessbot4.java telnet thread or classes it creates when new data comes in.  Typed text is added to the document in subframe.java.  Lantern just swaps 
documents when a tab switch happens.  The same document can be displayed in multiple comsoles at once. 


I use grouplayout a lot for advanced layouts. Andrey has used for dialogs the package 'layout' which is a folder 
in the lantern jar as mentioned. I haven't learned how to use it. I think it's called grid layout.
