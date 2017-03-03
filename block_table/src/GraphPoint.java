import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

class GraphPoint extends JComponent {
	int x;
	int y;
	boolean latest = true;
	int diameter = 8;
	int xMin;
	int xMax;
	int yMax;
	int yMin;
	

	public GraphPoint(double x, double y, int xMin, int xMax, int yMin, int yMax) {
	//xPrev and yPrev are static. will this work?
	
	
		this.x = (int)x;
		this.y = (int)y;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
	}
	public void paint(Graphics g) {
		System.out.println("Painting");
		g.setColor(Color.blue);
		int size = diameter;
		if(latest){
			g.setColor(Color.red);
			size = 2*diameter;
		}
		g.fillOval(xMin+x, yMax-yMin-y, size,size);	  
	}


	public void clearLatest(){
		latest = false;
	}
}


