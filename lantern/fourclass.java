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
		// fail is 0 when we have a score, -1 when position didnt change alpha ( < alpha) and 1 when > beta. harray objects are used to store hashing info for a position.
import java.util.Random;



		class fourclass
		{
		public class harray
		{
		  public long hnumber;
		  public int fail;
		  public int value1;
		  public int depth;
		}
			public

			//int board[120];
			//int tops[8];
			//int killers[100];
			int [] board;
			int [] tops;
			int [] killers;
			int maxX;
			int maxY;
			int aborts;
			long theTime;
			int turn;
			long counter;
			int hashcounter;
			int gamenumber;
			int themove;
			int [] bonus;
			public int type;
			int [] bonus2;
			int maxscore;
			int return0;
			long hashtoggle;
			long startnumber;
			long oldstartnumber;
			long [][] hashboard;
			long hashmax;
			harray [] hasharray;
			public int level;
			public int levelreached;
			int waittime;
				void computestarthash()
				{
					startnumber=oldstartnumber;
					for(int a=0; a<120; a++)
						if(board[a]>0)
							startnumber=hashboard[a][board[a]]^startnumber;

				}
			public void reset()
			{
				for(int a=0; a<120; a++)
					board[a]=-1;
				for(int a=0; a<8; a++)
					tops[a]=0;
				maxX=7;
				maxY=6;
				turn=0;
				gamenumber=0;
				type=0;// 7 by 6 connect 4

			}

			public fourclass()
				{
					level=1;
				levelreached=0;
					waittime=1;
				    board = new int[120];
					tops= new int[9];// modified for my current method of passing maxX as a move for computer first MA 12-15-09
					killers= new int[100];
				    bonus = new int[7];
			        bonus2 = new int[9];
			        hashboard = new long[120][3];// [120][3]

					// hashing
					Random random = new Random();

					for(int a=0; a<120; a++)
						for(int b=1; b<3; b++)
						{
							hashboard[a][ b]=(long)(random.nextInt()*random.nextInt());
							if(hashboard[a][b]<0)
							hashboard[a][b]*=-1;
						}
					hashtoggle=(long)(random.nextInt()*random.nextInt());
					startnumber=(long)(random.nextInt()*random.nextInt());
					if(hashtoggle<0)
					hashtoggle*=-1;
					if(startnumber<0)
					startnumber*=-1;

					hashmax=1024*64;
					//hashmax=256;
					oldstartnumber=startnumber;
					//hasharray=malloc(sizeof(struct harray) *  hashmax);
					hasharray=new harray[(int)hashmax];
					for(int z=0; z< hashmax; z++)
						hasharray[z]=new harray();


					bonus[0]=0;  bonus[1]=0; bonus[2]=10; bonus[3]=30; bonus[4]=10; bonus[5]=0; bonus[6]=0;
					bonus2[0]=0;  bonus2[1]=0; bonus2[2]=10; bonus2[3]=20; bonus2[4]=20; bonus2[5]=10; bonus2[6]=0; bonus2[7]=0;



					reset();
			}

				int translate(int j)
				{
					int i=tops[j];
					return 10+i*10+1+j;
				}

				public int makemove(int x)
				{
					if(type!=0)
					{
						maxX=8;
						maxY=8;
					}
					else
					{
						maxX=7;
						maxY=6;
					}
					int maxlevel=30;
					waittime=1;
					if(level == 1)
						maxlevel =5;
					else if(level == 2)
						maxlevel = 8;
					else if(level == 3)
						maxlevel = 30;
					else
					{
					waittime=2;
					maxlevel=30;
					}
					hashcounter=0;
					for(int y=0; y<100; y++)
						killers[y]=-1;
					// makeing and deleting the hash array here in the move receiver / maker
					// actually works ok because the hash array only exists for a move
					// with multiple games at once sometimes and variable times between opponents moveing,
					// dont have to have the overhead of keeping it for a whole game for every game

				//	hasharray= new struct harray[hashmax];

								   for(int h=0; h<hashmax; h++)
								   hasharray[h].hnumber=0;

								   //int move=0;
								   counter=0;
								   turn++;
								   if(turn==1)
								   for(int i=1; i<=maxY; i++)
								   for(int j=0; j<maxX; j++)
								   board[10+i*10+1+j]=0;


								   computestarthash();

								   if(x!=-1)
							   {
								   tops[x]+=1;
								   board[translate(x)]=2;
								   startnumber=startnumber^hashtoggle;
								   startnumber=startnumber^hashboard[translate(x)][ 2];

							   }
				else if(turn == 1)
				turn=0;

				// call search, search returns move
				aborts=0;
				theTime=System.currentTimeMillis();
				int finalmove=0;
				counter=0;
				themove=-1;
				return0 = 0;
			int a=0;
					for( a=3; a<maxlevel; a++)
			{
				themove=search(-50000, 50000, a, 1, startnumber);
				if(aborts==0)
				finalmove=themove;
				else
				break;
			}
			levelreached = a-1;

			tops[finalmove]+=1;
			if(tops[finalmove]>7)
				levelreached= -finalmove;
			board[translate(finalmove)]=1;
			startnumber=startnumber^hashtoggle;
			startnumber=startnumber^hashboard[translate(finalmove)][ 1];

			//char [] temp = new temp[10];
			//char [] temp1 = new temp1[10];
			//temp[0]='\0';
			//itoa(finalmove+1, temp, 10);
			//temp1[0]='\0';
			//itoa(gamenumber, temp1, 10);
			char [] movestring = new char[500];
			//movestring[0]='\0';
			//strcat(movestring, "tell astrobot drop ");

//strcat(movestring, temp1);
			//strcat(movestring, " ");
			//strcat(movestring, temp);
			//Sendf("%s", movestring);
			//printf("\n move string is %s and depth is %d\n", movestring, a-1);
			//printf("\ndepth is %d maxscore is %d hashcounter is %d\n", a, maxscore, hashcounter);
			// delete hasharray;

			turn++;

			//printf("\n turn is %d type is %d\n", turn, type);
			return finalmove;
		}

		int search(int alpha, int beta, int depth1, int side, long currenthash)
		{
			int a, b;
			int gotmate;
			long passhash;
			int points;
			int [] myscores = new int[12];
			int token;
			if(side%2==1)
				token=1;
			else
				token=2;

			int [] moves = new int[12];
			for(int zz=0; zz<maxX; zz++)
				moves[zz]=zz;
			// draw code
			if((turn + side) == (maxX * maxY + 1))
			{
					if(return0 == 0)
				{
					return0=1;
				//	printf("\n returned 0 possibly do to end of moves limit\n");
				}
				return 0;
			}

			// sort
			if(side > 1)
			{
				for(int zz=0; zz<maxX; zz++)
					if(moves[zz]==killers[side])
					{
						//swap
						int temp9=moves[0];
						moves[0]=moves[zz];
						moves[zz]=temp9;
						zz=maxX+1;
					}
			}

			//root ordering
			if(side==1 && (themove>-1 && themove <maxX))
			{
				int temp10=moves[0];
				moves[0]=themove;
				moves[themove]=temp10;
				//for(int zx=0; zx<maxX; zx++)
				//	printf("%d ", moves[zx]);


			}


			counter++;

			if(aborts == 1)
				return 0;

			if(counter%100 == 0)
			{



				long thetime2 =System.currentTimeMillis();
				if(thetime2> (long) (theTime+1000 * waittime))
				{

					aborts = 1;
					return 0;

				}
			}

			if(depth1 == 0)
			{
				int resultscore=0;

				if(type==2)
				{
					for(int j=0; j<maxX; j++)
						for(int i=1; i<=tops[j]; i++)
						{
							resultscore+=evaluate(1, translate(j), j, 1);
							resultscore-=evaluate(2, translate(j), j, 2);
						}
				}// if type==2
				if(type > -1)
				{
					int opp1;
					int opp2;
					int comp1;
					int comp2;
					for(int j=0; j<maxX; j++)
					{
							if(type==0)
						{
							if(board[20+1+j]==1)
								resultscore+=bonus[j];
							if(board[20+1+j]==2)
								resultscore-=bonus[j];
						}
						else
						{
							if(board[20+1+j]==1)
								resultscore+=bonus2[j];
							if(board[20+1+j]==2)
								resultscore-=bonus2[j];
						}


						for(int i=2; i<=maxY; i++)// check what maxy goes to
						{
								opp1=opp2=comp1=comp2=0;

							if(type==0)
							{
								if(board[ 10+i*10+1+j]==1)
									resultscore+=bonus[j];
								if(board[ 10+i*10+1+j]==2)
									resultscore-=bonus[j];
							}
							else
							{
								if(board[ 10+i*10+1+j]==1)
									resultscore+=bonus2[j];
								if(board[ 10+i*10+1+j]==2)
									resultscore-=bonus2[j];
							}
							if(board[10+i*10+1+j]==0)
							{
								opp1+=findFour( 10+i*10+1+j, 2);
								comp1+=findFour( 10+i*10+1+j, 1);
							}
							if(board[10+(i-1)*10+1+j]==0)
							{
								opp2+=findFour( 10+(i-1)*10+1+j, 2);
								comp2+=findFour( 10+(i-1)*10+1+j, 1);
							}



							if(opp1==1)
								resultscore-=100;
							if(comp1==1)
								resultscore+=100;
							if(i==2 && opp2==1)
								resultscore-=100;
							if(i==2 && comp2==1)
								resultscore+=100;

							if((opp1 + opp2) == 2 && (comp1 + comp2) == 0)
							{
									resultscore-=1000;
								break;
							}
							else if((opp1 + opp2) == 0 && (comp1 + comp2) == 2)
							{
									resultscore=1000;
								break;
							}
							if((opp1 + opp2) == 2 && (comp1 + comp2) == 1)
							{
									resultscore-=300;
								break;
							}
							else if((opp1 + opp2) == 1 && (comp1 + comp2) == 2)
							{
									resultscore=300;
								break;
							}

						}// end i for
					}// end j for
				}// end else
				if(side%2==0)
					resultscore=-1 * resultscore;


				return resultscore;

			}// end if depth ==0



			// hashing
			if((depth1 > 0 && side > 1) && ( alpha > -40000 && beta < 40000))
			{
				if(currenthash==hasharray[(int) (currenthash%hashmax)].hnumber)
					if(hasharray[(int) (currenthash%hashmax)].depth>=depth1)
					{
						hashcounter++;
						if(hasharray[(int) (currenthash%hashmax)].fail==1 && hasharray[(int) (currenthash%hashmax)].value1>=beta)
							return beta;
						if(hasharray[(int) (currenthash%hashmax)].fail==-1 && hasharray[(int) (currenthash%hashmax)].value1<=alpha)
							return alpha;
						if(hasharray[(int) (currenthash%hashmax)].fail==0)
							if(hasharray[(int) (currenthash%hashmax)].value1< beta && hasharray[(int) (currenthash%hashmax)].value1> alpha)
								return hasharray[(int) (currenthash%hashmax)].value1;


					}

			}

			gotmate = 0;
			int	alphachanged=0;


			for(int a1=0; a1<maxX; a1++)
			{
				passhash=currenthash;
				a=moves[a1];
				myscores[a] = 0;
				if(tops[a] < maxY)
				{
					tops[a]+=1;

					board[translate(a)]=token;
					passhash=passhash^hashtoggle;
					passhash=passhash^hashboard[translate(a)][ token];




					if( findFour(translate(a), token)> 0)
					{
							points = 35000 - side;
						gotmate = 1;
					}
					else
						points = -search(-beta, -alpha, depth1 - 1, side+1, passhash);

					board[translate(a)]=0;
					tops[a] = tops[a] - 1;



					if(points > alpha)
					{
						alpha = points;
						alphachanged=1;

					}
					if(points >= beta && side > 1)
					{
							killers[side]=a;
						hasharray[(int) (currenthash%hashmax)].hnumber=currenthash;
						hasharray[(int) (currenthash%hashmax)].fail=1;
						hasharray[(int) (currenthash%hashmax)].depth=depth1;
						hasharray[(int) (currenthash%hashmax)].value1=beta;
						return beta;
					}
					if(side == 1)
						myscores[a] = points;
					if(side > 1 && gotmate == 1)
						break;
				}
				else
					if(side == 1) // an illegal drop
					myscores[a] = -100000;

			}



			if(alphachanged==0 && side > 1)
			{
				hasharray[(int) (currenthash%hashmax)].hnumber=currenthash;
				hasharray[(int) (currenthash%hashmax)].fail=-1;
				hasharray[(int) (currenthash%hashmax)].depth=depth1;
				hasharray[(int) (currenthash%hashmax)].value1=alpha;

			}
			else
				if(alphachanged==1 && side > 1)
			{
				hasharray[(int) (currenthash%hashmax)].hnumber=currenthash;
				hasharray[(int) (currenthash%hashmax)].fail=0;
				hasharray[(int) (currenthash%hashmax)].value1=alpha;
				hasharray[(int) (currenthash%hashmax)].depth=depth1;

			}
			if(side > 1)
				return alpha;
			b=0;
			if(side == 1)
			{

				b = moves[0];
				for(a = 0; a< maxX; a++)
				{
						int a1=moves[a];
					if(myscores[a1] > myscores[b])
						b = a1;
				}

			}
			if(side==1 && aborts==0)
				maxscore=myscores[b];
			return b;


		}

		int findFour(int x, int token)
		{





			//token is 1 or 2

			int win=0;
			int max=4;
			if(type==2)
				max=5;

			// diagonal down

			int count;

			count=1;
			int a;
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 9*a]==token)
					count++;
				else
					break;
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 9*a]==token)
					count++;
				else
					break;

			if(count >= max)
				win=1;

			count=1;

			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 11*a]==token)
					count++;
				else
					break;
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 11*a]==token)
					count++;
				else
					break;

			if(count >= max)
				win=1;

			count=1;

			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 10*a]==token)
					count++;
				else
					break;
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 10*a]==token)
					count++;
				else
					break;

			if(count >= max)
				win=1;

			count=1;

			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 1*a]==token)
					count++;
				else
					break;
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 1*a]==token)
					count++;
				else
					break;

			if(count >= max)
				win=1;

			return win;

		}




		int evaluate(int side, int x, int j, int call) // original
		{

			int token=board[x];
			if(token==2 && call==1)
				return 0;
			if(token==1 && call==2)
				return 0;
			if(board[x]==0)
				return 0;
			//if(board[x]==-1)
			//	printf("er");

			//token is 1 or 2

			int eval=0;
			/*if(type==0)
			eval+=bonus[j]; // piece square bonus were j is collum 0-6
			else
			eval+=bonus2[j];
			*/
			// diagonal down
			int max=4;
			if(type==2)
				max=5;

			int count;
			int sum=0;
			int left, right;
			left=right=0;
			int broke=0;

			count=1;
			int a;
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 9*a]==token)
					count++;
				else
				{
					if(board[x + 9*a]==0)
					{
						left=1;
						if(board[x + 9*(a+1)]==token)
							broke++;
					}
					break;
				}
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 9*a]==token)
					count++;
				else
				{
					if(board[x - 9*a]==0)
					{
						right=1;
						if(board[x - 9*(a+1)]==token)
							broke++;
					}
					break;
				}

			sum=left+right;
			eval=score(eval, count, sum, broke);
			left=right=0;
			count=1;
			sum=0;
			broke=0;

			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 11*a]==token)
					count++;
				else
				{
					if(board[x - 11*a]==0)
					{
						left=1;
						if(board[x - 11*(a+1)]==token)
							broke++;
					}
					break;
				}
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 11*a]==token)
					count++;
				else
				{
					if(board[x + 11*a]==0)
					{
						right=1;
						if(board[x + 11*(a+1)]==token)
							broke++;
					}
					break;
				}

			sum=left+right;
			eval=score(eval, count, sum, broke);
			left=right=0;
			count=1;
			sum=0;
			broke=0;


			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 10*a]==token)
					count++;
				else
				{
					if(board[x + 10*a]==0)
					{
						left=1;
						if(board[x + 10*(a+1)]==token)
							broke++;
					}
					break;
				}
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 10*a]==token)
					count++;
				else
				{
					if(board[x - 10*a]==0)
					{
						right=1;
						if(board[x - 10*(a+1)]==token)
							broke++;
					}
					break;
				}

			sum=left+right;
			eval=score(eval, count, sum, broke);
			left=right=0;
			count=1;
			sum=0;
			broke=0;

			for(a=1; a<max; a++) // up +11 down -9
				if(board[x - 1*a]==token)
					count++;
				else
				{
					if(board[x - 1*a]==0)
					{
						left=1;
						if(board[x - 1*(a+1)]==token)
							broke++;
					}
					break;
				}
			for(a=1; a<max; a++) // up +11 down -9
				if(board[x + 1*a]==token)
					count++;
				else
				{
					if(board[x + 1*a]==0)
					{
						right=1;
						if(board[x + 1*(a+1)]==token)
							broke++;
					}
					break;
				}

			sum=left+right;
			eval=score(eval, count, sum, broke);
			left=right=0;
			count=1;
			sum=0;
			broke=0;
			return eval;
		}


		int score(int eval, int count, int sum, int broke)
		{
			if(type!=2)
			{
				if(count == 2 && sum==1 && broke==0)
					eval+=15;
				if(count==2 && sum==2 && broke==0)
					eval+=50;
				if(count == 2 && broke>0)// only finds broke on one of the set of 3
					eval+=110;

				if(count > 2 && sum==1)
					eval+=75;
				if(count >2 && sum==2)
					eval+=100;
			}
			else
			{


				//if(count == 3 && sum==1 && broke==0)
				//eval+=40;
				if(count == 3 && sum==2 && broke==0)
					eval+=15;
				// broke
				/*if(count == 2 && broke>0)
				eval+=25;

				if(count == 3 && broke>0)
				eval+=225;




				if(count > 3 && sum==1)
				eval+=175;
				if(count > 3 && sum==2)
				eval+=250;
				*/
			}


			return eval;
		}

	} // end class