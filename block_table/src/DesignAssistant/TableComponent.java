package DesignAssistant;
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
import java.util.Arrays;

import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

//import StudentGroups.AssignGroups;
import TUIO.*;
import rbsa.eoss.Architecture;
import rbsa.eoss.ArchitectureEvaluator;
import rbsa.eoss.ArchitectureGenerator;
import rbsa.eoss.Result;
import rbsa.eoss.ResultManager;
import rbsa.eoss.local.Params;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class TableComponent extends JComponent implements TuioListener {

	private Hashtable<Long,TuioDemoObject> objectList = new Hashtable<Long,TuioDemoObject>();
	private Hashtable<Long,TuioCursor> cursorList = new Hashtable<Long,TuioCursor>();
	private Hashtable<Long,TuioDemoBlob> blobList = new Hashtable<Long,TuioDemoBlob>();
	
	public static boolean filteringMode = true;
	
	public static final int finger_size = 15;
	public static final int object_size = 110;
	public static final int table_size = 760;
	
	public static int pointCounter = 0;
	
	public JFrame window;
	public JFrame pc_window;
	public double cost = 0;
	public double science = 0;

	public static int width, height;
	private float scale = 1.0f;
	public boolean verbose = false;
	private ArchitectureGenerator AG;
	private ArchitectureEvaluator AE;
	private Filter filterer;
	private HashMap<String, double[]> randomData;
	private String preDataFile = null;
	private Orbit [] lastOrbits = null;
	private HistoryWindow hw;
	private int histWidth,histHeight;
	private final int xMin = 85;
	private final int xMax = 1285+100; //4000x (0,0.281) 100px margin of error (these bounds are estimated on the pre-data)
	private final int yMin = 25;
	private final int yMax = 825+50; //(1/12)x (0,9981), with a 50px margin of error (some of the points were above the plot)
	private final double xScale = 4000;
	private final double yScale = 1/12.0;
	private final int configWindowWidth = 0;
	private final int configWindowHeight = 0;
	private final int xPadding = 55;
	private final int yPadding = 75;
	private final int yWindowMax = configWindowHeight*2-yPadding;
	private final int xWindowMax = 1920-configWindowWidth;
	public static final int numOrbits = 5;
	public static final int numInstruments = 12;
	//private final int xMax = 1920-configWindowWidth-xPadding;
	private static final double changeEpsilon = 1e-3;
	private MouseAdapter mouseAdapt;
	public static GraphPoint lastSelectedPoint = null;
	public static GraphPoint currentSelectedPoint = null;
	public static HashMap<String,GraphPoint> pixelMap = new HashMap<String,GraphPoint>();
	private GraphBackground lastFilter = null;
	
	public TableComponent(JFrame frame) {

		super();
		preDataFile = null;
		//start by getting the screens
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		
		window = new JFrame();
		window.setTitle("Cost vs Science Benefit Plot");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(configWindowWidth+5, yMin+5, xWindowMax-10, yMax+75);
	    GraphCoordinates initialGraph = new GraphCoordinates(xMin, xMax, yMin, yMax, xScale, yScale);
	    mouseAdapt = new PointMouseAdapter(this);
	    initialGraph.addMouseListener(mouseAdapt);
	    
	    window.getContentPane().add(initialGraph);
	    
	    window.getContentPane().setBackground(Color.WHITE);
	    window.setVisible(true);
	    
	   
	    pc_window = frame;
	    
	    if( gs.length > 1 )
	    {
	    	gs[0].setFullScreenWindow( pc_window );
	        histWidth = (int)gs[0].getDefaultConfiguration().getBounds().getWidth();
		    histHeight = (int)gs[0].getDefaultConfiguration().getBounds().getHeight();
	    }
	    else{
	    	histWidth = this.configWindowWidth;
		    histHeight = this.configWindowHeight;
		    pc_window.setBounds(0, yMin+configWindowHeight, configWindowWidth, configWindowHeight);
	    }

	    pc_window.setTitle("Configuration History");
	    pc_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	   
	    
	    pc_window.getContentPane().setBackground(Color.WHITE);
	    
	    //pc_window.setVisible(true);
	    
	    hw = new HistoryWindow(histWidth, histHeight, pc_window,this.numOrbits);
	    pc_window.getContentPane().removeAll();
	    pc_window.getContentPane().add(hw);
	    //pc_window.setVisible(true);
	    
		
		
		
		
//		super();
//		
//		window = new JFrame();
//	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    window.setBounds(xMin+5, yMin+5, xMax+55, yMax+75);
//	    GraphCoordinates initialGraph = new GraphCoordinates(xMin,xMax,yMin,yMax,xScale,yScale);
//	    mouseAdapt = new PointMouseAdapter(this);
//	    initialGraph.addMouseListener(mouseAdapt);
//	    
//	    window.getContentPane().add(initialGraph);
//	    window.getContentPane().setBackground(Color.WHITE);
//	    window.setVisible(true);
//	    /*
//	    pc_window = new JFrame();
//	    pc_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    
//	   
//	    pc_window.setBounds(xMin+5, yMin+5, 640, 480);
//	    pc_window.getContentPane().setBackground(Color.WHITE);
//	    
//	    pc_window.setVisible(true);
//	    */
	    
	    
		
	}
	public TableComponent(String preDataPath,JFrame tableFrame) {
		super();
		preDataFile = preDataPath;
		//start by getting the screens
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		
		window = new JFrame();
		window.setTitle("Cost vs Science Benefit Plot");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(configWindowWidth+5, yMin+5, xWindowMax-10, yMax+75);
	    GraphCoordinates initialGraph = new GraphCoordinates(xMin, xMax, yMin, yMax, xScale, yScale);
	    mouseAdapt = new PointMouseAdapter(this);
	    initialGraph.addMouseListener(mouseAdapt);
	    
	    window.getContentPane().add(initialGraph);
	    
	    window.getContentPane().setBackground(Color.WHITE);
	    window.setVisible(true);
	    plotInitialData(preDataPath);
	   
	    pc_window = tableFrame;
	    
	    if( gs.length > 1 )
	    {
	    	gs[0].setFullScreenWindow( pc_window );
	        histWidth = (int)gs[0].getDefaultConfiguration().getBounds().getWidth();
		    histHeight = (int)gs[0].getDefaultConfiguration().getBounds().getHeight();
	    }
	    else{
	    	histWidth = this.configWindowWidth;
		    histHeight = this.configWindowHeight;
		    pc_window.setBounds(0, yMin+configWindowHeight, configWindowWidth, configWindowHeight);
	    }

	    pc_window.setTitle("Configuration History");
	    pc_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	   
	    
	    pc_window.getContentPane().setBackground(Color.WHITE);
	    
	    //pc_window.setVisible(true);
	    
	    hw = new HistoryWindow(histWidth, histHeight, pc_window,this.numOrbits);
	    pc_window.getContentPane().removeAll();
	    pc_window.getContentPane().add(hw);
	    //pc_window.setVisible(true);
	    
		
	}
	
	public void setSize(int w, int h) {
		super.setSize(w,h);
		width = w;
		height = h;
		scale  = height/(float)TableComponent.table_size;	
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
		performUpdate(false);
	}
	public void performUpdate(boolean forceEval){
		//Graphics2D g2 = (Graphics2D)g;
		//Component info_comp = new Component();
		
		//Graphics2D g2_info = new Graphics2D();
		//JFrame info_frame = new JFrame();
		//Graphics g_info = info_frame.getGraphics();
		
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	
		//g2.setColor(Color.white);
		//g2.fillRect(0,0,width,height);
		
		//g2.setColor(Color.black);
		//g2.drawLine(0, 96, width, 96);
		//g2.drawLine(0, 192, width, 192);
		//g2.drawLine(0, 288, width, 288);
		//g2.drawLine(0, 384, width, 384);
	
		int w = (int)Math.round(width-scale*finger_size/2.0f);
		int h = (int)Math.round(height-scale*finger_size/2.0f);
		
		Enumeration<TuioCursor> cursors = cursorList.elements();
//		while (cursors.hasMoreElements()) {
//			TuioCursor tcur = cursors.nextElement();
//			if (tcur==null) continue;
//			ArrayList<TuioPoint> path = tcur.getPath();
//			TuioPoint current_point = path.get(0);
//			//if (current_point!=null) {
//				// draw the cursor path
//				//g2.setPaint(Color.blue);
//				//for (int i=0;i<path.size();i++) {
//					//TuioPoint next_point = path.get(i);
//					//g2.drawLine(current_point.getScreenX(w), current_point.getScreenY(h), next_point.getScreenX(w), next_point.getScreenY(h));
//					//current_point = next_point;
//				//}
//			}
//			
//			// draw the finger tip
//			g2.setPaint(Color.lightGray);
//			int s = (int)(scale*finger_size);
//			g2.fillOval(current_point.getScreenX(w-s/2),current_point.getScreenY(h-s/2),s,s);
//			g2.setPaint(Color.black);
//			g2.drawString(tcur.getCursorID()+"",current_point.getScreenX(w),current_point.getScreenY(h));
//		}

		// draw the objects and print orbits
		Enumeration<TuioDemoObject> objects = objectList.elements();
		ArrayList<TuioDemoObject> objectList = new ArrayList<TuioDemoObject>();
		ArrayList<TuioDemoObject> [] markers = new ArrayList[this.numOrbits];
		for (int i=0; i<markers.length; i++) {
			markers[i] = new ArrayList<TuioDemoObject>(); 
		}
		
		
		Orbit [] orbits = new Orbit[this.numOrbits];
		TuioDemoObject tobj = null;
		while (objects.hasMoreElements()) {
			tobj = objects.nextElement();
			if (tobj!=null) { 
				objectList.add(tobj);
		//		tobj.paint(g2, width,height);
				
				/*Sorting objects into orbits*/
				//TuioPoint obj_point = tobj.getPosition();
				//float obj_x = obj_point.getX();
				float obj_y = tobj.getYtPos();
				//System.out.println("("+obj_x+","+obj_y+")");
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
			}
		}
					

		for (int i=0; i<orbits.length; i++) {
			orbits[i] = new Orbit("Orbit " + (i+1), markers[i]);
		}
		hw.setCurrentOrbits(orbits);
		if (tobj!=null||true) { //remove this if statement; always paint the point
			//write the orbits and their contents
//			g2.setColor(Color.RED);
//			g2.drawString("Current "+orbits[4].fancyString(), 20, 13);
//			g2.drawString("Current "+orbits[3].fancyString(), 20, 110);
//			g2.drawString("Current "+orbits[2].fancyString(), 20, 206);
//			g2.drawString("Current "+orbits[1].fancyString(), 20, 302);
//			g2.drawString("Current "+orbits[0].fancyString(), 20, 398);
		if (currentSelectedPoint != null && !currentSelectedPoint.equals(lastSelectedPoint)) {
       		 lastSelectedPoint = currentSelectedPoint;
       		//currentSelectedPoint.paint(window.getContentPane().getGraphics());
       		window.revalidate();
       		window.repaint();
  	    	//HistoryWindow hw = new HistoryWindow(currentSelectedPoint.objectList, configWindowWidth, configWindowHeight, pc_window,lastSelectedPoint.configuration);
  	    	//pc_window.getContentPane().removeAll();
  	    	//pc_window.getContentPane().add(hw);
  	    	//pc_window.setVisible(true);
       		hw.setSelectedOrbits(lastSelectedPoint.configuration,currentSelectedPoint.objectList);
  	    	
  	    	
  	    }
       	 else{
//       		HistoryWindow hw = new HistoryWindow(configWindowWidth, configWindowHeight, pc_window);
//  	    	pc_window.getContentPane().removeAll();
//  	    	pc_window.getContentPane().add(hw);
//  	    	pc_window.setVisible(true);
       	 }
		pc_window.revalidate();
	    pc_window.repaint();
			//write the last selected point's orbits and their contents
//			if(lastSelectedPoint != null){
//				Orbit[] selectedOrbits = lastSelectedPoint.configuration;
//				g2.setColor(Color.BLUE);
//				g2.drawString("Selected "+selectedOrbits[4].fancyString(), 400, 13);
//				g2.drawString("Selected "+selectedOrbits[3].fancyString(), 400, 110);
//				g2.drawString("Selected "+selectedOrbits[2].fancyString(), 400, 206);
//				g2.drawString("Selected "+selectedOrbits[1].fancyString(), 400, 302);
//				g2.drawString("Selected "+selectedOrbits[0].fancyString(), 400, 398);
//				
				
//				for (TuioDemoObject tobj2: lastSelectedPoint.objectList) {
//					
//					if (tobj2!=null) {
//						tobj2.paintColor(g2, width,height, Color.BLUE);
//					}
//				}
				
//			}
			if((objectList.size()>0 && !Arrays.deepEquals(lastOrbits, orbits))||forceEval == true){
				//System.out.println(lastOrbits);
				//System.out.println(orbits);
				if (filteringMode){
					plotFilter(orbits);
				}
				else{
					evaluateArchitecture(orbits, objectList);
				}

			}
			lastOrbits = orbits;
		}
		
		
	}
	
	public void init () {
	      
        // Set a path to the project folder
        String path = "/Users/Nikhil/Desktop/git_repo/RBSAEOSS-Eval";
       path = "/Users/designassistant/Documents/workspace/design_assistant_HRC2/RBSAEOSS-Eval";
        //path = "/Users/mvl24/Documents/workspace/design_assistant_HRC2/RBSAEOSS-Eval2";
        AE = ArchitectureEvaluator.getInstance();
        AG = ArchitectureGenerator.getInstance();
        Params params = null;
        String search_clps = "";
        params = new Params( path, "FUZZY-ATTRIBUTES", "test","normal",search_clps);//FUZZY or CRISP
        AE.init(1);
        if(preDataFile != null){
        	filterer = new Filter(preDataFile);
        }
        else{
        	randomData = plotInitialData();
        	filterer = new Filter(randomData);
        }
        //filterer = preDataFile != null ? new Filter(preDataFile) : new Filter(randomData);
	}
	
	private ArrayList<double[]> getInitialData(String filename){
		ArrayList<double[]> initialData = new ArrayList<double[]>();
		String row = "";
		BufferedReader br = null;
	    try {
	           br = new BufferedReader(new FileReader(filename));
	           row = br.readLine(); //strip column headers
	            while ((row = br.readLine()) != null) {
	                String[] rawInstance = row.split(",");
	                double[] dataPair = {Double.parseDouble(rawInstance[1])*4000,Double.parseDouble(rawInstance[2])/12};
	                initialData.add(dataPair);
	            }

	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    return initialData;
	}
	private HashMap<String, double[]> plotInitialData(){
		GraphBackground preDataGraph = new GraphBackground(this.xMin, this.xMax,this.yMin,this.yMax);
		HashMap<String, double[]> data = preDataGraph.generateRandomArchitectures(1, AG, AE);
		window.getContentPane().add(preDataGraph);
		window.setVisible(true);
		return data;
	}
	private void plotInitialData(String filename){
		ArrayList<double[]> data = getInitialData(filename);
		GraphBackground preDataGraph = new GraphBackground(data,this.xMin, this.xMax,this.yMin,this.yMax);
		window.getContentPane().add(preDataGraph);
		window.setVisible(true);
		/*for(int i=0; i<data.size(); i++){
			MyGraph preDataGraph = new MyGraph(data.get(i)[0]*1000+25,data.get(i)[1]/10.0);
			preDataGraph.clearLatest();
	        window.getContentPane().add(preDataGraph);
	        window.setVisible(true);
		}
		*/
		
	}
	/*
	private void evaluateTeams(Orbit[] orbits) {
		ArrayList<Integer>[] input_teams = new ArrayList[5];
		
		for (int i = 0; i < 5; i++) {
			input_teams[i] = orbits[i].getIDs();
		}
		
		StudentGroups.Point p = AssignGroups.getPoint(input_teams);
		science = p.happiness;
		cost = p.productivity;
	}*/
	private void plotFilter(Orbit [] orbits){
		long filterMatch = 0L;
		ArrayList<double[]> filteredData;
		for(int i = 0; i<numOrbits; i++){
			filterMatch |= (orbits[i].toBinaryOneHot())<<((numOrbits-(i+1))*numInstruments); //simplify this!
		}
		filteredData = filterer.getFilteredData(filterMatch);
		GraphBackground filteredPlot = new GraphBackground(filteredData,this.xMin, this.xMax,this.yMin,this.yMax,Color.blue);
		if(lastFilter != null){
			window.getContentPane().remove(lastFilter);
		}
		System.out.println("Filtering");
		lastFilter = filteredPlot;
		window.getContentPane().add(filteredPlot,0);
		window.setVisible(true);
	}
	private double[] evaluateArchitecture(Orbit [] orbits, ArrayList<TuioDemoObject> objectList) {
		//System.out.println(orbits[0]);
		ArrayList<String> input_arch = new ArrayList<>();
			for (Orbit o : orbits) {
				input_arch.add(o.toString());
			}
			//System.out.println(input_arch);
	        // Generate a new architecture
	        Architecture architecture = AG.defineNewArch(input_arch);
	        
	        // Evaluate the architecture
        Result result = AE.evaluateArchitecture(architecture,"Slow");
	        
	        // Save the score and the cost
	        double newCost = result.getCost();
	        double newScience = result.getScience();
	        //I think it's ok to call this every time now, and we actually should be, since
	        //evaluate architecture is now only called when needed, and overlapping points should show the most recent.
	        //the problem here is getting bloat where the number of graphpoints is going to grow for overlapping points.
	        //the tradeoff is getting the most recent configuration associated with a stack of points if the cost /benefit doesn't change
	        //if(true || Math.abs(newCost-cost) + Math.abs(newScience-science) > changeEpsilon){
	        	System.out.println("Changing color");
	        	//int numComponents = window.getContentPane().getComponentCount();
	        	//if(numComponents>2){ //2 is the number of non-point components (background + coordinates) come up with a better way
	        	//	GraphPoint lastGraph = (GraphPoint)window.getContentPane().getComponent(0);
	        	//	lastGraph.clearLatest();
	        	//}
	        	GraphPoint newPoint = new GraphPoint(newScience*4000,newCost/12.0, this.xMin, this.xMax, this.yMin,this.yMax,orbits,++pointCounter, objectList);
	        	

	        	
	        	Container cp = window.getContentPane();
		        window.getContentPane().add(newPoint,0);
		        int [] bounds = newPoint.getBoundaries();
		        for(int i=bounds[0]; i<=(bounds[0]+bounds[2]); i++){
		        	for(int j=bounds[1]; j<=(bounds[1]+bounds[2]); j++){
		        		HashMap<String,GraphPoint> debugmap = pixelMap;
		        		pixelMap.put(String.format("%d%d",i,j), newPoint);
		        	}
		        }
		        
		        
		        //window.getContentPane().getComponent(numComponents-1).repaint();
		        //window.getContentPane().getComponent(numComponents).repaint();
		        //window.setVisible(true);
		        window.revalidate();
		        window.repaint();
	        //}
	        cost = newCost;
	        science = newScience;
		    //System.out.println("Graph");
	        //System.out.println("Xprev "+lastGraph.x+" Yprev "+lastGraph.y);
	        
	        
	        
	        System.out.println("Performance Score: " + science + ", Cost: " + cost);
	        return new double [] {cost, science};
	        
	}

	public static class Orbit {

		ArrayList<TuioDemoObject> markers;
		String name;

		
		public Orbit (String name, ArrayList<TuioDemoObject> markers){
			this.name = name;
			this.markers = copyMarkers(markers);
		}
		//careful here, lots of warnings about using Object.clone(), consider implementing a deep copy for TuioDemoComponent
		private ArrayList<TuioDemoObject> copyMarkers(ArrayList<TuioDemoObject> toCopy){
			ArrayList<TuioDemoObject> toReturn = new ArrayList<TuioDemoObject>();
			for(int i = 0; i < toCopy.size(); i++){
				toReturn.add((TuioDemoObject)toCopy.get(i).clone());
			}
			return toReturn;
		}
		
		public ArrayList<Integer> getIDs() {
			ArrayList<Integer> IDs = new ArrayList<Integer>();
			for (int i = 0; i < markers.size(); i++) {
				IDs.add(markers.get(i).getSymbolID());
			}
			return IDs;
		}
		
		public String toString () {
			String rs = "";

			for (TuioDemoObject e : markers)
				rs = rs + e.toTuioLetter();	
			
			
			return rs.trim();
		}
		
		public long toBinaryOneHot() {
			long oneHotRepr = 0;
			for(TuioDemoObject e : markers){
				oneHotRepr |= 1<<(numInstruments-(e.mod_id+1)); 
			}
			return oneHotRepr;
		}
		
		public String fancyString() {
			String rs = name.toUpperCase() + " = [";
			
			for (TuioDemoObject e : markers)
				rs = rs + (e.toTuioLetter() + " ");	
			
			rs = rs.trim() + "]";
			return rs;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((markers == null) ? 0 : markers.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Orbit other = (Orbit) obj;
			if (markers == null) {
				if (other.markers != null)
					return false;
			} else if (markers.size() != other.markers.size()){
				return false;
			}
			else{
				for(int i=0; i< markers.size(); i++){
					if (markers.get(i).getSymbolID() != other.markers.get(i).getSymbolID()){
						return false; //note that you will get some false positives here insofar as the order doesn't matter
					}
				}
			}
				
//			if (name == null) {
//				if (other.name != null)
//					return false;
//			} else if (!name.equals(other.name))
//				return false;
			return true;
		}
	}

}

