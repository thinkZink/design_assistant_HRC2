import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class BackgroundGraph extends JComponent {
	ArrayList<double[]> data;

	public BackgroundGraph(ArrayList<double[]> data) {
		this.data = data;
	}
	public void paint(Graphics g) {
		g.setColor(Color.black);
		for(int i=0; i<data.size(); i++){
			g.fillOval((int)data.get(i)[0], 455-(int)data.get(i)[1], 5,5);	  
		}
	}


}
