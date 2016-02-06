package three.mans.morris;

public class Rules {
	private final int P1 = 1; // Player 1 peg
	private final int P2 = 2; // Player 2 peg
	
	private GUI g = new GUI();
	
	private int pRow = -1; // Row to move from
	private int pCol = -1; // Column to move from
	
	private int row = -1; // Current row
	private int col = -1; // Current column
	
	private boolean turn = true; //true if P1's turn, false if P2's turn
	
	private int count = 0; // Number of turns passed
	
	public class GameRules implements Runnable{
		@Override
		public void run() {
			while(true){
				while ((row == -1 || col == -1) || (count >= 6 && (pRow == -1 || pCol == -1))) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if(count < 6){
					if(turn){
						if(g.place(row, col, P1)){
							g.delRed();
							count++;
							turn = !turn;
						}
					}else{
						if(g.place(row, col, P2)){
							g.delBlack();
							count++;
							turn = !turn;
						}
					}
				}else if(!(pRow == -1 || pCol == -1)){
					if(turn){
						if(g.move(new int[] {pRow,pCol}, new int[] {row,col}, P1)) turn = !turn;
					}else{
						if(g.move(new int[] {pRow,pCol}, new int[] {row,col}, P2)) turn = !turn;
					}
				}
				
				row = -1;
				col = -1;
				pRow = -1;
				pCol = -1;
				
				if(g.wins(P1) || g.wins(P2)) while(true);
			}
		}
	}
	
	public class ActionSetter implements Runnable{
		public void run(){
			while(true){
				if(count < 6){
					row = g.getPoint()[0];
					col = g.getPoint()[1];
				}else {
					while(pRow == -1 || pCol == -1){
						pRow = g.getPPoint()[0];
						pCol = g.getPPoint()[1];
					}
					while(row == -1 || col == -1){
						row = g.getPoint()[0];
						col = g.getPoint()[1];
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		Rules r = new Rules();
		Rules.GameRules g = r.new GameRules();
		Rules.ActionSetter s = r.new ActionSetter();
		
		Thread myThread1 = new Thread(g);
		myThread1.start();
		
		Thread myThread2 = new Thread(s);
		myThread2.start();

		try {
			myThread1.join();
			myThread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}