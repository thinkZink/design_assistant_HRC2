import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class HistoryWindow extends JComponent{
	private ArrayList<TuioDemoObject> objects;
	private int width;
	private int height;
	private TuioDemoComponent.Orbit[] selectedOrbits;
	JFrame frame;
	
	public HistoryWindow(ArrayList<TuioDemoObject> objects, int width, int height, JFrame frame, TuioDemoComponent.Orbit[] selectedOrbits) {
		this.objects = objects;
		this.width = width;
		this.height = height;
		this.frame = frame;
		this.selectedOrbits = selectedOrbits;
	}
	public HistoryWindow(int width, int height, JFrame frame) {
		this.objects = null;
		this.width = width;
		this.height = height;
		this.frame = frame;
		this.selectedOrbits = new TuioDemoComponent.Orbit[5];
	}
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawLine(0, 96, width, 96);
		g.drawLine(0, 192, width, 192);
		g.drawLine(0, 288, width, 288);
		g.drawLine(0, 384, width, 384);
		//System.out.println("Painting History Window");
		
		if(this.objects != null){
			
			//welp, we can simplify this a lot b/c markers are tobj's.
//			for (TuioDemoObject tobj : objects) {
//			
//				if (tobj!=null) { 
//					Insets insets = frame.getInsets();	
//					tobj.paintColor((Graphics2D)g, width,height,Color.BLUE);
//				}
//			}
			for (TuioDemoComponent.Orbit orbit : this.selectedOrbits){
				for (TuioDemoObject marker: orbit.markers){
					if(marker!=null){
						Insets insets = frame.getInsets();
						marker.paintColor((Graphics2D)g, width, height, Color.BLUE);
					}
				}
			}
			
			//need to initialize these to empty orbits in order to move outside condition
			g.setColor(Color.BLUE);
			g.drawString("Selected "+selectedOrbits[4].fancyString(), 20, 13);
			g.drawString("Selected "+selectedOrbits[3].fancyString(), 20, 110);
			g.drawString("Selected "+selectedOrbits[2].fancyString(), 20, 206);
			g.drawString("Selected "+selectedOrbits[1].fancyString(), 20, 302);
			g.drawString("Selected "+selectedOrbits[0].fancyString(), 20, 398);	
		}

	
}
}
