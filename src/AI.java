/*
This class contains the AI logic of the game.
*/
public class AI{
	/*
	Selects a move for the current player using the indicated AI mode
	
	Parameters:
		board  : 8 by 8 array of integers representing the game board
		player : integer representing the player whose turn it is
			1 - Black
			2 - White
		mode   : String indicating the selected AI mode for the current player
	*/
	public static int[] decide(int[][] board, int player, String mode){
		int[] dummy = {-1, -1};
		switch(mode){
			case "Dumb":
				return dumb(board, player);
			case "Smart":
				return smart(board, player);
			case "Smart+":
				return smartPlus(board, player);
			default:
				return dummy;
		}
	}
	
	/*
	The "Dumb" AI level.
	In this mode, the AI makes a list of all the valid moves and then selects one at random.
	*/
	private static int[] dumb(int[][] board, int player){
		int[][] moveList = new int[64][2];
		int size = 0;
		
		int[] out = {0, 0};
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(Util.movePossible(board, i, j, player)){
					int[] el = {i, j};
					moveList[size] = el;
					size++;
				}
			}
		}
		
		out = moveList[(int)(Math.random() * size)];
		
		return out;
	}
	
	/*
	The "Smart" AI level.
	In this mode, the AI makes a list of all the valid moves, along with the amount of tiles each of those moves would flip.
	The moves are then sorted based on this "score", and the one with the biggest one is selected.
	If there is more than one move with the maximum score, one is selected at random.
	*/
	private static int[] smart(int[][] board, int player){
		int[][] moveList = new int[64][3];
		int size = 0;
		int[] out = {0, 0, 0};
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				int flips = Util.flipTiles(board, i, j, player, false);
				if(flips>0){
					int[] el = {i, j, flips};
					moveList[size] = el;
					size++;
				}
			}
		}
		
		moveList = sortByIndex(moveList, size, 2);
		
		int poolSize = 1;
		while(poolSize<size && moveList[0][2]==moveList[poolSize][2]) poolSize++;
		
		out = moveList[(int)(Math.random() * poolSize)];
		
		return out;
	}
	
	/*
	The "Smart+" AI level.
	In this mode, the AI makes a list of all the valid moves, along with the amount of tiles each of those moves would flip.
	Compared to the "Smart" AI level, bonus points are awarded to moves that are on the edges of the board.
	The moves are then sorted based on this "score", and the one with the biggest one is selected.
	If there is more than one move with the maximum score, one is selected at random.
	*/
	private static int[] smartPlus(int[][] board, int player){
		int[][] moveList = new int[64][3];
		int size = 0;
		int[] out = {0, 0, 0};
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				int flips = Util.flipTiles(board, i, j, player, false);
				if(flips>0){
					if(i==0 || i==7) flips++;
					if(j==0 || j==7) flips++;
					int[] el = {i, j, flips};
					moveList[size] = el;
					size++;
				}
			}
		}
		
		moveList = sortByIndex(moveList, size, 2);
		
		int poolSize = 1;
		while(poolSize<size && moveList[0][2]==moveList[poolSize][2]) poolSize++;
		
		out = moveList[(int)(Math.random() * poolSize)];
		
		return out;
	}
	
	//Gnome Sort implementation
	private static int[][] sortByIndex(int[][] array, int length, int index){
		int pos = 1;
		
		while(pos<length){
			if(pos==0 || array[pos][index] <= array[pos-1][index]){
				pos++;
			}else{
				int[] temp = array[pos];
				array[pos] = array[pos-1];
				array[pos-1] = temp;
				pos--;
			}
		}
		
		return array;
	}
}