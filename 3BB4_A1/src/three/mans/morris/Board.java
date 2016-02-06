package three.mans.morris;

public class Board {
	int[][] board = {{0,0,0}, //Board is empty
			 		 {0,0,0},
			 		 {0,0,0}};
	
	public boolean place(int row, int col, int p){
		if(board[row][col] == 0){
			board[row][col] = p; // Place peg of player on the board
			return true;
		}
		return false;
	}
	
	public boolean wins(int p){
		boolean flag;
		
		//Check horizontal win:
		for(int i = 0; i < 3; i++){
			flag = true;
			for(int j = 0; j < 3; j++)
				if(board[i][j] != p)
					flag = false;
			if(flag) return true;
		}
		
		//Check vertical win:
		for(int i = 0; i < 3; i++){
			flag = true;
			for(int j = 0; j < 3; j++)
				if(board[j][i] != p)
					flag = false;
				if(flag) return true;
		}
		return false;
	}
	
	public boolean move(int[] f, int[] t, int p){
		if(board[t[0]][t[1]] == 0 && board[f[0]][f[1]] == p){
			board[f[0]][f[1]] = 0; // Move piece from initial spot
			board[t[0]][t[1]] = p; // Place it in new spot
			return true;
		}
		return false;
	}
	
	public int[][] getBoard(){return board;}
	
	public String toString(){
		String ret = "";
		
		for(int[] i : board){
			for(int j : i)
				ret += j;
			ret += "\n";
		}
		return ret;
	}
}