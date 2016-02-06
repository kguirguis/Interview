package three.mans.morris;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;

public class GUI  extends Frame implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int red = 0;
	private int black = 0;
	
	private boolean turn = true;
	
	//width of board
	private int w = 200;
	
	//upper corner of board
	private int x1 = 100;
	private int y1 = 100;
	
	private Ellipse2D.Double[] p = new Ellipse2D.Double[6];
	//diameter of pieces
	private int d = 20;
	
	private int x2 = x1 - d/2;
	private int y2 = y1 - d/2;
	
	private int[][] intersection = {{x2,y2}, {x2+w/2,y2}, {x2+w,y2},
			{x2,y2+w/2}, {x2+w/2, y2+w/2}, {x2+w,y2+w/2},
			{x2,y2+w}, {x2+w/2,y2+w}, {x2+w,y2+w}};
	
	private boolean mouseClicked = false;
	
	private int[] point = {-1,-1}; //point clicked on (to place peg in)
	private int[] pPoint = {-1,-1};
	
	private Board b = new Board();
	
	public GUI(){
		super("Three Man's Morris");
		setSize(400,400);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
				System.exit(0);
			}
		});
		
		addMouseListener(this);
		
		setResizable(false);
		
		setVisible(true);
	}
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		//update screen
		for(int j = 0; j < b.getBoard().length; j++)
			for(int i = 0; i < b.getBoard()[j].length; i++)
				if(b.getBoard()[j][i] != 0){
					g2d.setColor(b.getBoard()[j][i]==1?Color.red:Color.black);
					g2d.fillOval(intersection[j*3 + i][0], intersection[j*3 + i][1], d, d);
				}
		
		g2d.setColor(Color.black);
		
		//middle horizontal line
		g2d.drawLine(x1,y1+w/2,x1+w,y1+w/2);
		
		//middle vertical line
		g2d.drawLine(x1+w/2,y1,x1+w/2,y1+w);
		
		//outside square
		g2d.drawRect(x1,y1,w,w);
		
		//black pieces
		g2d.setColor(Color.black);
		for(int i = black; i < 3; i++){
			p[i] = new Ellipse2D.Double(30*(i+1), 30, d, d);
			g2d.fill(p[i]);
		}
		
		//red pieces
		g2d.setColor(Color.red);
		for(int i = 3; i < 6-red; i++){
			p[i] = new Ellipse2D.Double(350 - 30*(i-3), 30, d, d);
			g2d.fill(p[i]);
		}
		
		g2d.setColor(Color.blue);
		if(turn)
			for(int i = 3; i < 6; i++){
				p[i] = new Ellipse2D.Double(350 - 30*(i-3), 30, d, d);
				g2d.draw(p[i]);
			}
		else
			for(int i = 0; i < 3; i++){
				p[i] = new Ellipse2D.Double(30*(i+1), 30, d, d);
				g2d.draw(p[i]);
			}
		
		//blue circles around empty intersections
		if(mouseClicked){
			g2d.setColor(Color.blue);
			
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++)
					if(b.getBoard()[i][j] == 0)
						g2d.drawOval(intersection[i*3 + j][0], intersection[i*3 +j][1], d, d);
		}
	}
	
	public synchronized boolean place(int row, int col, int p){
		if(b.place(row, col, p)){
			repaint();
			
			turn = !turn;
			
			mouseClicked = !mouseClicked;
			
			point[0] = -1;
			point[1] = -1;
			
			return true;
		}
		return false;
	}
	
	public synchronized boolean move(int[] f, int[] t, int p){
		if(b.move(f, t, p)){
			repaint();
			
			turn = !turn;
			
			mouseClicked = !mouseClicked;
			
			point[0] = -1;
			point[1] = -1;
			
			pPoint[0] = -1;
			pPoint[1] = -1;
			
			return true;
		}
		return false;
	}
	
	public boolean wins(int p){
		if(b.wins(p)){
			if(p == 1) red = 0;
			else black = 0;
			repaint();
		}
		return b.wins(p);
	}
	
	public synchronized int[] getPoint(){return point;}
	
	public synchronized int[] getPPoint(){return pPoint;}
	
	public void delRed(){
		red++;
		for(int i = 5; i > 2; i--)
			if(p[i] != null){
				p[i] = null;
				break;
			}
	}
	
	public void delBlack(){
		black++;
		for(int i = 2; i >= 0; i--)
			if(p[i] != null){
				p[i] = null;
				break;
			}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		double z;
		for(int i = 0; i < intersection.length; i++){
			z = (e.getY()-(intersection[i][1]+d/2))*(e.getY()-(intersection[i][1]+d/2)) + 
			(e.getX()-(intersection[i][0]+d/2))*(e.getX()-(intersection[i][0])+d/2);
			 if(z <= d*d && b.getBoard()[i/3][i-3*(i/3)] == 0 && mouseClicked){
					point[0] = i/3;
					point[1] = i-3*point[0];
					break;
			}else if(z <= d*d && (turn?1:2) == b.getBoard()[i/3][i-3*(i/3)]){
				pPoint[0] = i/3;
				pPoint[1] = i-3*pPoint[0];
				mouseClicked = !mouseClicked;
				break;
			}
		}
		
		if(turn){
			for(int i = 3; i<6-red; i++)
				if(p[i].contains(e.getPoint()))
					mouseClicked = !mouseClicked;
		}else
			for(int i = black; i<3; i++)
				if(p[i].contains(e.getPoint()))
					mouseClicked = !mouseClicked;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}