public class AI{
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