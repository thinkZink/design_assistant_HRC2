import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

class GraphCoordinates extends JComponent {
	int xMin;
	int xMax;
	int yMin;
	int yMax;
	double xScale;
	double yScale;
	int numTicks = 10;
	int xTickWidth;
	int yTickWidth;

	
	public GraphCoordinates(int xMin, int xMax, int yMin, int yMax, double xScale, double yScale) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMax = yMax;
		this.yMin = yMin;
		this.xScale = xScale;
		this.yScale = yScale;
		this.xTickWidth = (int)((this.xMax-this.xMin)/this.numTicks); 
		this.yTickWidth = (int)((this.yMax-this.yMin)/this.numTicks); 
	}
	public void paint(Graphics g) {

		System.out.println("Painting Coordinates");
		g.setColor(Color.black);
		g.drawLine(xMin, yMin, xMin, yMax); //y axis
		g.drawLine(xMin, yMax, xMax, yMax); //x axis
		g.drawString("Science", xMax/2, yMax+40);
		g.drawString("Cost", xMin/2, yMin/2);	  
		//tick marks
		for(int xTick = xMin; xTick < xMax; xTick+=xTickWidth){
			g.drawLine(xTick, yMax+5, xTick, yMax-5);
			g.drawString(String.format("%.3f",(1/xScale)*(xTick-xMin)), xTick-20, yMax+20);
		}
		for(int yTick = yMin; yTick < yMax; yTick+=yTickWidth){
			g.drawLine(xMin+5, yTick, xMin-5, yTick);
			g.drawString(String.format("%d",(int)(1/yScale)*(yMax-yTick)),xMin-45,yTick+5);
		}

		
		
	}

}


