import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class HistoryWindow extends JComponent{
	private ArrayList<TuioDemoObject> objects;
	private int width;
	private int clusterAreaWidth;
	private int orbitWidth;
	private int height;
	private TuioDemoComponent.Orbit[] selectedOrbits;
	private TuioDemoComponent.Orbit[] currentOrbits;
	private ArrayList<TuioDemoComponent.Cluster> currentClusters;
	private long emptyOrbitMask=0;
	private int numOrbits;
	JFrame frame;
	private Color[] orbitColors = {Color.blue, Color.red};
	private boolean initial;
	public HistoryWindow(ArrayList<TuioDemoObject> objects, int width, int height, JFrame frame, TuioDemoComponent.Orbit[] selectedOrbits,TuioDemoComponent.Orbit[] currentOrbits, ArrayList<TuioDemoComponent.Cluster> currentClusters ) {
		this.objects = objects;
		this.width = width;
		this.orbitWidth = (int) (width*0.7);
		this.clusterAreaWidth = width-this.orbitWidth;
		this.height = height;
		this.frame = frame;
		this.selectedOrbits = selectedOrbits;
		this.currentOrbits = currentOrbits;
		this.numOrbits = this.selectedOrbits.length;
		this.initial = true;
		this.currentClusters = currentClusters;
	}
	public HistoryWindow(int width, int height, JFrame frame, int numOrbits) {
		this.objects = null;
		this.width = width;
		this.orbitWidth = (int) (width*0.7);
		this.clusterAreaWidth = width-this.orbitWidth;
		this.height = height;
		this.frame = frame;
		this.selectedOrbits = new TuioDemoComponent.Orbit[numOrbits];
		this.currentOrbits = new TuioDemoComponent.Orbit[numOrbits];
		this.currentClusters = new ArrayList<TuioDemoComponent.Cluster>();
		this.numOrbits = numOrbits;
		this.initial = false;
	}
	public void setCurrentOrbits(TuioDemoComponent.Orbit[] orbits){
		this.currentOrbits = orbits;
	}
	public void setCurrentClusters(ArrayList<TuioDemoComponent.Cluster> clusters){
		this.currentClusters = clusters;
	}
	public void setEmptyMask(long mask){
		emptyOrbitMask = mask;
	}
	public void setSelectedOrbits(TuioDemoComponent.Orbit[] orbits, ArrayList<TuioDemoObject> objects){
		this.objects = objects;
		this.selectedOrbits = orbits;
	}
	public void paint(Graphics g) {
		g.setColor(Color.black);
		//get the largest possible height that is divisible by the numOrbits
		//we will divide this into our orbits
		int usableHeight = this.height-(this.height%this.numOrbits);
		int orbitHeight = usableHeight/this.numOrbits;
//		for(int i=orbitHeight; i<this.height; i+= orbitHeight){
//			g.drawLine(0, i, this.width, i);
//		}
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 4F);
		
		g.setFont(newFont);
		
		//paint the clustering area
		if(TuioDemoComponent.isExplorationMode()){
			g.setColor(Color.blue);
			g.fillRect(this.orbitWidth, 0, this.clusterAreaWidth, usableHeight);
		}
		else{
			g.setColor(Color.black);
			g.fillRect(this.orbitWidth, 0, this.clusterAreaWidth, usableHeight);
			g.setColor(Color.white);
			g.drawString("Global Filters",(int)(this.orbitWidth+this.clusterAreaWidth*0.1),usableHeight/2 );
		}
		
		//paint the orbits
		for(int i=0; i<=this.height-orbitHeight; i+= orbitHeight){
			int orbitIndex = i/orbitHeight;
			long empty = emptyOrbitMask & 0b111111111111L<<(12*orbitIndex);
			int colorIndex = orbitIndex%2;
			if(empty == 0){
				g.setColor(this.orbitColors[colorIndex]);
			}
			else{
				g.setColor(Color.black);
			}
			g.fillRect(0, i, this.orbitWidth, orbitHeight);
			g.setColor(Color.white);
			g.drawString(this.currentOrbits[orbitIndex].fancyString(), orbitWidth/25, i+7*orbitHeight/10);
			//g.drawString("ORBIT "+(orbitIndex+1), width/10, i+9*orbitHeight/10 );
		}
		/*
		g.drawLine(0, 96, width, 96);
		g.drawLine(0, 192, width, 192);
		g.drawLine(0, 288, width, 288);
		g.drawLine(0, 384, width, 384);
		*/
		//System.out.println("Painting History Window");
		
//		for (TuioDemoComponent.Orbit orbit : this.currentOrbits){
//			if(orbit != null){
//				for (TuioDemoObject marker: orbit.markers){
//					if(marker!=null){
//						Insets insets = frame.getInsets();
//						marker.paintColor((Graphics2D)g, width, height, Color.black);
//					}
//				}
//			}
//		}
		if(!TuioDemoComponent.isExplorationMode()){
			Graphics2D g2 = (Graphics2D) g;
			for (TuioDemoComponent.Cluster cluster : this.currentClusters){
	//			for (TuioDemoObject marker: cluster.markers){
	//				if(marker!=null){
	//					Insets insets = frame.getInsets();
	//					marker.paintColor((Graphics2D)g, width, height, Color.white);
	//				}
	//			}
				
				Stack<Point2D> points = (Stack<Point2D>) cluster.getClusterHull(width,height);
				GeneralPath polyline = 
					        new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.size());
				Point2D start = points.pop();
				polyline.moveTo(start.x(), start.y());
				while(!points.isEmpty()){
					Point2D point = points.pop();
					polyline.lineTo(point.x(), point.y());
				}
				g2.setColor(Color.white);
				g2.draw(polyline);
				
			}
		}
		
		
		for(int i=0; i<orbitWidth; i+=orbitWidth/10){
			
				g.drawLine(i, 0, i, height);
			
		}
		for(int j=0; j<height; j+=height/10){
			g.drawLine(0, j, orbitWidth, j);
		}
		
		if(this.objects != null ){
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
						marker.paintColor((Graphics2D)g, orbitWidth, height, Color.darkGray);
					}
				}
			}
			
			//need to initialize these to empty orbits in order to move outside condition
//			g.setColor(Color.BLUE);
//			g.drawString("Selected "+selectedOrbits[4].fancyString(), 20, 13);
//			g.drawString("Selected "+selectedOrbits[3].fancyString(), 20, 110);
//			g.drawString("Selected "+selectedOrbits[2].fancyString(), 20, 206);
//			g.drawString("Selected "+selectedOrbits[1].fancyString(), 20, 302);
//			g.drawString("Selected "+selectedOrbits[0].fancyString(), 20, 398);	
		}

	
}
	
}
