/* 
Utility class with static methods useful throught the whole codebase.
*/
public class Util{
	
	/*
	Method meant for the tile flipping mechanic from reversi.
	Can also be used to determine the viability of a move.
	
	Parameters:
		board  : 8 by 8 array of integers representing the game board
		x & y  : the coordinates of the desired move
		player : integer representing which player is making the move
			1 - Black
			2 - White
		forReal: boolean value -- the board array is only modified if set to true
	
	The method returns how many tiles have (or would have been) flipped.
	*/
	public static int flipTiles(int[][] board, int x, int y, int player, boolean forReal){
		//Sanity checks
		if(!inBounds(x, y)) return -1;
		if(!(board[y][x]==0)) return 0;
		
		//Data init
		int flips = 0;
		int opposite = (player==1)? 2 : 1;
		
		//Actual loop
		//dx & dy represent directions, as we have to search in all 8 possible directions
		for(int dx=-1; dx<=1; dx++){
			for(int dy=-1; dy<=1; dy++){
				if(!(dx==0 && dy==0)){
					int lx = x+dx;
					int ly = y+dy;
					int count = 0;
					//First we search
					while(inBounds(lx, ly) && board[ly][lx]==opposite){
						lx+=dx;
						ly+=dy;
						count++;
					}
					//And then we go back flipping tiles along the way (if applicable)
					if(inBounds(lx, ly) && board[ly][lx]==player && count>0){
						while(!(lx==x && ly==y)){
							lx-=dx;
							ly-=dy;
							flips++;
							if(forReal) board[ly][lx]=player;
						}
						flips--;//We have to take into account that the tile placed when making a move is not actually flipped
					}
				}
			}
		}
		return flips;
	}
	
	/*
	Returns true if a move at x & y is possible
	Simple usage of flipTiles with forReal set to false.
	
	Parameters:
		board  : 8 by 8 array of integers representing the game board
		x & y  : the coordinates of the desired move
		player : integer representing which player is making the move
			1 - Black
			2 - White
	*/
	public static boolean movePossible(int[][] board, int x, int y, int player){
		return flipTiles(board, x, y, player, false)>0;
	}
	
	
	
	/*
	Returns true if a move is possible
	
	Parameters:
		board  : 8 by 8 array of integers representing the game board
		player : integer representing the player whose turn it is
			1 - Black
			2 - White
	*/
	public static boolean movesPossible(int[][] board, int player){
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(movePossible(board, i, j, player)) return true;
			}
		}
		return false;
	}
	
	/*
	Returns a copy of the board with the tiles where a move is possible marked with the value "3".
	
	Parameters:
		board  : 8 by 8 array of integers representing the game board
		player : integer representing the player whose turn it is
			1 - Black
			2 - White
	*/
	public static int[][] prepareForRender(int[][] board, int player){
		int[][] pBoard = new int[8][8];
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				pBoard[j][i] = board[j][i];
				if(movePossible(board, i, j, player)) pBoard[j][i] = 3;
			}
		}
		return pBoard;
	}
	
	/*
	Counts the current score of the specified player
	
	Parameters:
		board  : 8 by 8 array of integers representing the game board
		player : integer representing the player for which we are counting the score
			1 - Black
			2 - White
	*/
	public static int countScore(int[][] board, int player){
		int score = 0;
		for(int i=0; i<8; i++) for(int j=0; j<8; j++) if(board[j][i]==player) score++;
		return score;
	}
	
	//Checks whether the coordinates x & y are within the allowed coordinate space
	public static boolean inBounds(int x, int y){
		return (x>=0 && x<=7) && (y>=0 && y<=7);
	}
	
	//Normalizes coordinates from render space to game space
	public static int adjustCoordinate(int n, double scale){
		n = (int)((n/scale)/29.0);
		if(n<0) n=0;
		if(n>7) n=7;
		return n;
	}
}