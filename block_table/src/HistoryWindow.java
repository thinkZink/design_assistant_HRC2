import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class HistoryWindow extends JComponent{
	private Enumeration<TuioDemoObject> objects;
	private int width;
	private int height;
	JFrame frame;
	
	public HistoryWindow(Enumeration<TuioDemoObject> objects, int width, int height, JFrame frame) {
		this.objects = objects;
		this.width = width;
		this.height = height;
		this.frame = frame;
	}
	
	public void paint(Graphics g) {
		System.out.println("Painting History Window");
		g.setColor(Color.GRAY);
		
		TuioDemoObject tobj = null;
		while (objects.hasMoreElements()) {
			
			tobj = objects.nextElement();
			if (tobj!=null) { 
				Insets insets = frame.getInsets();	
				tobj.paint((Graphics2D)g, width,height+insets.top);
			}
	}
		
		g.drawLine(0, 96, width, 96);
		g.drawLine(0, 192, width, 192);
		g.drawLine(0, 288, width, 288);
		g.drawLine(0, 384, width, 384);
	
	
}
}
