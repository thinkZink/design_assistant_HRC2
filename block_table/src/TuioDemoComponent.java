/*
 TUIO Java GUI Demo
 Copyright (c) 2005-2014 Martin Kaltenbrunner <martin@tuio.org>
 
 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files
 (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify, merge,
 publish, distribute, sublicense, and/or sell copies of the Software,
 and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:
 
 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import TUIO.*;
import rbsa.eoss.Architecture;
import rbsa.eoss.ArchitectureEvaluator;
import rbsa.eoss.ArchitectureGenerator;
import rbsa.eoss.Result;
import rbsa.eoss.ResultManager;
import rbsa.eoss.local.Params;

public class TuioDemoComponent extends JComponent implements TuioListener {

	private Hashtable<Long,TuioDemoObject> objectList = new Hashtable<Long,TuioDemoObject>();
	private Hashtable<Long,TuioCursor> cursorList = new Hashtable<Long,TuioCursor>();
	private Hashtable<Long,TuioDemoBlob> blobList = new Hashtable<Long,TuioDemoBlob>();
	
	public static final int finger_size = 15;
	public static final int object_size = 60;
	public static final int table_size = 760;
	
	public JFrame window;
	public double cost = 0;
	public double science = 0;
	
	public static int width, height;
	private float scale = 1.0f;
	public boolean verbose = false;
	
	private ArchitectureGenerator AG;
	private ArchitectureEvaluator AE;
	
	public TuioDemoComponent() {
		super();
		
		window = new JFrame();
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(30, 30, 500, 500);
	    window.getContentPane().add(new MyGraph(50, 50));
	    window.getContentPane().setBackground(Color.CYAN);
	    window.setVisible(true);
	   
		
	}
	
	public void setSize(int w, int h) {
		super.setSize(w,h);
		width = w;
		height = h;
		scale  = height/(float)TuioDemoComponent.table_size;	
	}
	
	public void addTuioObject(TuioObject tobj) {
		TuioDemoObject demo = new TuioDemoObject(tobj);
		objectList.put(tobj.getSessionID(),demo);

		if (verbose) 
			System.out.println("add obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle());	
	}

	public void updateTuioObject(TuioObject tobj) {

		TuioDemoObject demo = (TuioDemoObject)objectList.get(tobj.getSessionID());
		demo.update(tobj);
		
		if (verbose) 
			System.out.println("set obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle()+" "+tobj.getMotionSpeed()+" "+tobj.getRotationSpeed()+" "+tobj.getMotionAccel()+" "+tobj.getRotationAccel()); 	
	}
	
	public void removeTuioObject(TuioObject tobj) {
		objectList.remove(tobj.getSessionID());
		
		if (verbose) 
			System.out.println("del obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+")");	
	}

	public void addTuioCursor(TuioCursor tcur) {
	
		if (!cursorList.containsKey(tcur.getSessionID())) {
			cursorList.put(tcur.getSessionID(), tcur);
			repaint();
		}
		
		if (verbose) 
			System.out.println("add cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY());	
	}

	public void updateTuioCursor(TuioCursor tcur) {

		repaint();
		
		if (verbose) 
			System.out.println("set cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY()+" "+tcur.getMotionSpeed()+" "+tcur.getMotionAccel()); 
	}
	
	public void removeTuioCursor(TuioCursor tcur) {
	
		cursorList.remove(tcur.getSessionID());	
		repaint();
		
		if (verbose) 
			System.out.println("del cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+")"); 
	}

	public void addTuioBlob(TuioBlob tblb) {
		TuioDemoBlob demo = new TuioDemoBlob(tblb);
		blobList.put(tblb.getSessionID(),demo);
		
		if (verbose) 
			System.out.println("add blb "+tblb.getBlobID()+" ("+tblb.getSessionID()+") "+tblb.getX()+" "+tblb.getY()+" "+tblb.getAngle());	
	}
	
	public void updateTuioBlob(TuioBlob tblb) {
		
		TuioDemoBlob demo = (TuioDemoBlob)blobList.get(tblb.getSessionID());
		demo.update(tblb);
		
		if (verbose) 
			System.out.println("set blb "+tblb.getBlobID()+" ("+tblb.getSessionID()+") "+tblb.getX()+" "+tblb.getY()+" "+tblb.getAngle()+" "+tblb.getMotionSpeed()+" "+tblb.getRotationSpeed()+" "+tblb.getMotionAccel()+" "+tblb.getRotationAccel()); 	
	}
	
	public void removeTuioBlob(TuioBlob tblb) {
		blobList.remove(tblb.getSessionID());
		
		if (verbose) 
			System.out.println("del blb "+tblb.getBlobID()+" ("+tblb.getSessionID()+")");	
	}
	
	
	public void refresh(TuioTime frameTime) {
		repaint();
	}
	
	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {
	
		Graphics2D g2 = (Graphics2D)g;
		//Component info_comp = new Component();
		
		//Graphics2D g2_info = new Graphics2D();
		//JFrame info_frame = new JFrame();
		//Graphics g_info = info_frame.getGraphics();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	
		g2.setColor(Color.white);
		g2.fillRect(0,0,width,height);
		
		g2.setColor(Color.black);
		g2.drawLine(0, 96, width, 96);
		g2.drawLine(0, 192, width, 192);
		g2.drawLine(0, 288, width, 288);
		g2.drawLine(0, 384, width, 384);
	
		int w = (int)Math.round(width-scale*finger_size/2.0f);
		int h = (int)Math.round(height-scale*finger_size/2.0f);
		
		Enumeration<TuioCursor> cursors = cursorList.elements();
		while (cursors.hasMoreElements()) {
			TuioCursor tcur = cursors.nextElement();
			if (tcur==null) continue;
			ArrayList<TuioPoint> path = tcur.getPath();
			TuioPoint current_point = path.get(0);
			if (current_point!=null) {
				// draw the cursor path
				g2.setPaint(Color.blue);
				for (int i=0;i<path.size();i++) {
					TuioPoint next_point = path.get(i);
					g2.drawLine(current_point.getScreenX(w), current_point.getScreenY(h), next_point.getScreenX(w), next_point.getScreenY(h));
					current_point = next_point;
				}
			}
			
			// draw the finger tip
			g2.setPaint(Color.lightGray);
			int s = (int)(scale*finger_size);
			g2.fillOval(current_point.getScreenX(w-s/2),current_point.getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			g2.drawString(tcur.getCursorID()+"",current_point.getScreenX(w),current_point.getScreenY(h));
		}

		// draw the objects and print orbits
		Enumeration<TuioDemoObject> objects = objectList.elements();
		
		ArrayList<TuioDemoObject> [] markers = new ArrayList[5];
		for (int i=0; i<markers.length; i++) {
			markers[i] = new ArrayList<TuioDemoObject>(); 
		}
		

		
		while (objects.hasMoreElements()) {
			TuioDemoObject tobj = objects.nextElement();
			if (tobj!=null) { 
				
				tobj.paint(g2, width,height);
				
				/*Sorting objects into orbits*/
				TuioPoint obj_point = tobj.getPosition();
				float obj_x = obj_point.getX();
				float obj_y = obj_point.getY();
				
				if (obj_y < 0.2) //96
					markers[4].add(tobj);
				
				else if (obj_y < 0.400) //192
					markers[3].add(tobj);
				
				else if (obj_y < 0.601) //288
					markers[2].add(tobj);
				
				else if (obj_y < 0.79977) //384
					markers[1].add(tobj);
				
				else 
					markers[0].add(tobj);

				Orbit [] orbits = new Orbit[5];				

				for (int i=0; i<orbits.length; i++) {
					orbits[i] = new Orbit("Orbit " + (i+1), markers[i]);
				}
				

				g2.setColor(Color.BLACK);
				g2.drawString(orbits[4].fancyString(), 20, 13);
				g2.drawString(orbits[3].fancyString(), 20, 110);
				g2.drawString(orbits[2].fancyString(), 20, 206);
				g2.drawString(orbits[1].fancyString(), 20, 302);
				g2.drawString(orbits[0].fancyString(), 20, 398);
				
				evaluateArchitecture(orbits);

			}
		}
		
		
		
	}
	
	public void init () {
	      
        // Set a path to the project folder
        String path = "/Users/Nikhil/Desktop/git_repo/RBSAEOSS-Eval";
        
        AE = ArchitectureEvaluator.getInstance();
        AG = ArchitectureGenerator.getInstance();
        Params params = null;
        String search_clps = "";
        params = new Params( path, "FUZZY-ATTRIBUTES", "test","normal",search_clps);//FUZZY or CRISP
        AE.init(1);
	}
	
	private double[] evaluateArchitecture(Orbit [] orbits) {

		ArrayList<String> input_arch = new ArrayList<>();
			for (Orbit o : orbits) {
				input_arch.add(o.toString());
			}
			
	        // Generate a new architecture
	        Architecture architecture = AG.defineNewArch(input_arch);
	        
	        // Evaluate the architecture
	        Result result = AE.evaluateArchitecture(architecture,"Slow");
	        
	        // Save the score and the cost
	        cost = result.getCost();
	        science = result.getScience();
	        
	        window.getContentPane().add(new MyGraph(science*1000+25, cost));
	        
	        window.repaint();
	        window.setVisible(true);
	        System.out.println("Performance Score: " + science + ", Cost: " + cost);
	        return new double [] {cost, science};
	        
	}

	public static class Orbit {

		ArrayList<TuioDemoObject> markers;
		String name;
		
		public Orbit (String name, ArrayList<TuioDemoObject> markers){
			this.name = name;
			this.markers = markers;
		}

		public String toString () {
			String rs = "";

			for (TuioDemoObject e : markers)
				rs = rs + e.toTuioLetter();	
			
			return rs.trim();
		}
		
		public String fancyString() {
			String rs = name + " = [";
			
			for (TuioDemoObject e : markers)
				rs = rs + (e.toTuioLetter() + " ");	
			
			rs = rs.trim() + "]";
			return rs;
		}
	}

}

