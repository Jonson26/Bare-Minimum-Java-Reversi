public class Util{
	
	public static int flipTiles(int[][] board, int x, int y, int player, boolean forReal){
		if(!inBounds(x, y)) return -1;
		if(!(board[y][x]==0)) return 0;
		int flips = 0;
		int opposite = (player==1)? 2 : 1;
		for(int dx=-1; dx<=1; dx++){
			for(int dy=-1; dy<=1; dy++){
				if(!(dx==0 && dy==0)){
					int lx = x+dx;
					int ly = y+dy;
					int count = 0;
					while(inBounds(lx, ly) && board[ly][lx]==opposite){
						lx+=dx;
						ly+=dy;
						count++;
					}
					if(inBounds(lx, ly) && board[ly][lx]==player && count>0){
						while(!(lx==x && ly==y)){
							lx-=dx;
							ly-=dy;
							flips++;
							if(forReal) board[ly][lx]=player;
						}
						flips--;
					}
				}
			}
		}
		return flips;
	}
	
	public static boolean movePossible(int[][] board, int x, int y, int player){
		return flipTiles(board, x, y, player, false)>0;
	}
	
	public static boolean movesPossible(int[][] board, int player){
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(movePossible(board, i, j, player)) return true;
			}
		}
		return false;
	}
	
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
	
	public static int countScore(int[][] board, int player){
		int score = 0;
		for(int i=0; i<8; i++) for(int j=0; j<8; j++) if(board[j][i]==player) score++;
		return score;
	}
	
	public static boolean inBounds(int x, int y){
		return (x>=0 && x<=7) && (y>=0 && y<=7);
	}
	
	public static int adjustCoordinate(int n, double scale){
		n = (int)((n/scale)/29.0);
		if(n<0) n=0;
		if(n>7) n=7;
		return n;
	}
}