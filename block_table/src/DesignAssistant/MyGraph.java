package DesignAssistant;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

class MyGraph extends JComponent {
	int x;
	int y;
	boolean latest = true;

	public MyGraph(double x, double y) {
	//xPrev and yPrev are static. will this work?
	
	
		this.x = (int)x;
		this.y = (int)y;
	}
	public void paint(Graphics g) {
		System.out.println("Painting");
		g.setColor(Color.black);
		g.drawLine(25, 25, 25, 455);
		g.drawLine(25, 455, 455, 455);
		g.drawString("Science", 220, 470);
		g.drawString("Cost", 10, 10);
		if(latest){
			g.setColor(Color.red);
		}
		g.fillOval(x, 455-y, 5,5);	  
	}


	public void clearLatest(){
		latest = false;
	}
}


