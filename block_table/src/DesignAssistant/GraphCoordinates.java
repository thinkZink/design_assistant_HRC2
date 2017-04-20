package DesignAssistant;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

class GraphCoordinates extends JComponent {
	int xMin;
	int pxMax;
	int yMin;
	int pyMax;
	double xScale;
	double yScale;
	int numTicks = 10;
	int xTickWidth;
	int yTickWidth;

	
	public GraphCoordinates(int pxMax, int pyMax, double xScale, double yScale) {
		this.pxMax = pxMax;
		this.pyMax = pyMax;
		this.xScale = xScale;
		this.yScale = yScale;
		this.xTickWidth = (int)((this.pxMax-this.xMin)/this.numTicks); 
		this.yTickWidth = (int)((this.pyMax-this.yMin)/this.numTicks); 
	}
	public void paint(Graphics g) {
		String mode = TableComponent.filteringMode ? "Filtering" : "Exploration";
		
		System.out.println("Painting Coordinates");
		g.setColor(Color.black);
		g.drawLine(xMin, yMin, xMin, pyMax); //y axis
		g.drawLine(xMin, pyMax, pxMax, pyMax); //x axis
		g.drawString("Science", pxMax/2, pyMax+40);
		g.drawString("Cost", xMin/2, yMin/2);
		g.drawString("Mode: "+ mode, pxMax/2, yMin+40);
		//tick marks
		for(int xTick = xMin; xTick < pxMax; xTick+=xTickWidth){
			g.drawLine(xTick, pyMax+5, xTick, pyMax-5);
			g.drawString(String.format("%.3f",(1/xScale)*(xTick-xMin)), xTick-20, pyMax+20);
		}
		for(int yTick = yMin; yTick < pyMax; yTick+=yTickWidth){
			g.drawLine(xMin+5, yTick, xMin-5, yTick);
			g.drawString(String.format("%d",(int)(1/yScale)*(pyMax-yTick)),xMin-45,yTick+5);
		}

		
		
	}

}


