import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;


class GraphPoint extends JComponent {
	int x;
	int y;
	int index = 0;
	int diameter = 8;
	int xMin;
	int xMax;
	int yMax;
	int yMin;
	TuioDemoComponent.Orbit [] configuration;
	

	public GraphPoint(double x, double y, int xMin, int xMax, int yMin, int yMax,TuioDemoComponent.Orbit[] orbits,int index) {
	//xPrev and yPrev are static. will this work?
	
	
		this.x = (int)x;
		this.y = (int)y;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.index = index;
		
	}
	public void paint(Graphics g) {
		System.out.println("Painting");
		g.setColor(Color.blue);
		int size = diameter;
		//compare the current point index to a static variable
		//in the demoComponent that tracks how many points there are
		//if this is the most recent point, make it red.
		System.out.println("index:" + index + "pc:"+ TuioDemoComponent.pointCounter);
		if(index==TuioDemoComponent.pointCounter){
			g.setColor(Color.getHSBColor(0.56f, 1.0f, 0.8f));
			size = 2*diameter;
		}
		g.fillOval(xMin+x, yMax-yMin-y, size,size);	  
	}


	public void clearLatest(){
		//do nothing for now
	}
}


