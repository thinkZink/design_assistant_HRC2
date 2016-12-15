import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

class MyGraph extends JComponent {
	int x;
	int y;
public MyGraph(double x, double y) {
	this.x = (int)x;
	this.y = (int)y;
}
  public void paint(Graphics g) {
	  g.drawOval(x, y, 5,5);
	  g.drawLine(25, 25, 25, 455);
	  g.drawLine(25, 455, 455, 455);
	  g.drawString("Science", 220, 470);
	  g.drawString("Cost", 10, 10);
	  
  }
}
