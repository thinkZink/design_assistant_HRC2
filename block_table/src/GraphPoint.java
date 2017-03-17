import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

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
	int diameter = 11;
	int xMin;
	int xMax;
	int yMax;
	int yMin;
	TuioDemoComponent.Orbit [] configuration;
	ArrayList<TuioDemoObject> objectList;
	

	public GraphPoint(double x, double y, int xMin, int xMax, int yMin, int yMax,TuioDemoComponent.Orbit[] orbits,int index, ArrayList<TuioDemoObject> objectList) {
	//xPrev and yPrev are static. will this work?
	
		this.dim1 = x;
		this.dim2 = y;
		this.xPlot = (int)x+xMin-diameter/2;
		this.yPlot = yMax-(int)y-diameter/2;
		this.dim2 = y;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.configuration = orbits;
		this.index = index;
		this.objectList = objectList;
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
		//circle the last selected point in blue
		try{
			if(this.index == TuioDemoComponent.currentSelectedPoint.index){
				g.setColor(Color.BLUE);
				g.fillOval(xPlot-2, yPlot-2, size+4,size+4);
				g.setColor(Color.getHSBColor(0.0f, 1.0f, 1.0f));
				size = (int)1.5*diameter;
			}
		}
		catch(NullPointerException e) {
			//do nothing
		}
		//circle the current point in black
		int recencyIndex = TuioDemoComponent.pointCounter-index;
		//if this is one of the last two points:
		
		switch (recencyIndex){
		case 0:	g.setColor(Color.black);
				g.fillOval(xPlot-2, yPlot-2, size+4,size+4);
				g.setColor(Color.getHSBColor(0.0f, 1.0f, 1.0f));
				size = (int)1.5*diameter;
				break;
		case 1: 
				g.setColor(Color.getHSBColor(0.0f, 0.5f, 1.0f));
				size = (int)1.5*diameter;
				break;
		default: 
			
			//g.setColor(Color.getHSBColor(1.0f,(float)index/TuioDemoComponent.pointCounter, (float)index/TuioDemoComponent.pointCounter));
			g.setColor(Color.getHSBColor(0.75f,(float)index/TuioDemoComponent.pointCounter, 1.0f));
		}

		g.fillOval(xPlot, yPlot, size,size);	  
	}


	public int[] getBoundaries(){
		
		int[] bounds = {xPlot,yPlot,diameter};  
		return bounds;
	}
	
	public void paintBlocks(Graphics2D g) {

	}
}


