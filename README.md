# lanternicc

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
V0.5, a Java engine contained in its own jar.  There is a mediocre_readme.txt in the engines folder with
info on its project page and how to get the source. It's GPL. While it's packaged in the jar, when it's
time to use it, Lantern prompts the user to let it extract it to the Lantern folder and opens
it like other engines and does not link to it in code. 

To compiles lantern type javac lantern/*.java
to run lantern type java lantern/Lantern      Lantern.java contains main.

To jar lantern type jar -cvfm lantern###.jar lantern/manifest.txt lantern free layout org engines lanternopeningbook19.db

I compile lantern in java 6 for release.  I think right now in java 8 the channels.java file is erroring. I'll probably fix
it but its simple to work around probably as it appears there is just some long string used by flags that has some illegal
characters.  You can just fix the string maybe  delete the parts that error and shouldnt hurt lantern or take 
channels.java class files out of the jar. When i edit lantern on java 8 i avoid javac lantern/*.java and just compile 
my changes like javac lantern/Lantern.java etc. You can take all class files out of the jar if an issue and just compile changed files to avoid the channels situaton for now. The string in question is just used to find country names for flags. 

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
