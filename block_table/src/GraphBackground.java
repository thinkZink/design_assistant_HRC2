import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GraphBackground extends JComponent {
	ArrayList<double[]> data;
	int xMin;
	int xMax;
	int yMax;
	int yMin;

	public GraphBackground(ArrayList<double[]> data, int xMin, int xMax, int yMin, int yMax) {
		this.data = data;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}
	public void paint(Graphics g) {
		
		g.setColor(Color.gray);
		for(int i=0; i<data.size(); i++){
			g.fillRect(xMin+(int)data.get(i)[0], yMax-yMin-(int)data.get(i)[1], 5,5);	  
		}
	}


}
