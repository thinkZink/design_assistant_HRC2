import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;


class GraphPoint extends JComponent {
	//these are the two dimension values 
	double dim1;
	double dim2;
	
	//these are the plotted versions of the dimensions
	int xPlot;
	int yPlot;
	int index = 0;
	int diameter = 5;
	int xMin;
	int xMax;
	int yMax;
	int yMin;
	TuioDemoComponent.Orbit [] configuration;
	

	public GraphPoint(double x, double y, int xMin, int xMax, int yMin, int yMax,TuioDemoComponent.Orbit[] orbits,int index) {
	//xPrev and yPrev are static. will this work?
	
		this.dim1 = x;
		this.dim2 = y;
		this.xPlot = (int)x+xMin-diameter/2;
		this.yPlot = yMax-yMin-(int)y-diameter/2;
		this.dim2 = y;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.configuration = orbits;
		this.index = index;
//		setLocation(this.xplot-this.diameter/2,this.yplot-this.diameter/2);
//		Dimension d = new Dimension(this.diameter,this.diameter);
//		setMaximumSize(d);
//		setMinimumSize(d);
//		setPreferredSize(d);
//		setSize(d);
		
		
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
			g.setColor(Color.getHSBColor(1.0f, 1.0f, 1.0f));
			size = diameter;
		}
		else{
			g.setColor(Color.getHSBColor(0.5f,(float)index/TuioDemoComponent.pointCounter, 1-(float)index/TuioDemoComponent.pointCounter));
		}
		g.fillRect(xPlot, yPlot, size,size);	  
	}


	public int[] getBoundaries(){
		
		int[] bounds = {xPlot,yPlot,diameter};  
		return bounds;
	}
}


