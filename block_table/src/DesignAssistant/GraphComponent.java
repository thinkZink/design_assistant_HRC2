package DesignAssistant;

import java.awt.Color;
import java.awt.GraphicsDevice;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GraphComponent extends JComponent {
/*
 * GraphComponent provides a cost/benefit plot for a DesignAssistant TUI
 * 
 */
	
	private JFrame graphFrame;
	private GraphicsDevice displayDevice;
	private int pixelWidth,pixelHeight; //dimensions of the frame (i.e. screen resolution)
	private double xMax,yMax; //axes limits
	private double xScale,yScale; //multipliers to convert x/y values to pixels
	
	public GraphComponent(GraphicsDevice displayDevice,PointMouseAdapter mouseAdapt){
		this.displayDevice = displayDevice;
		pixelWidth = displayDevice.getDisplayMode().getWidth();
		pixelHeight = displayDevice.getDisplayMode().getHeight();
		xMax = DesignAssistantParams.graphBenefitMax;
		yMax = DesignAssistantParams.graphCostMax;
		xScale = pixelWidth/xMax;
		yScale = pixelHeight/yMax;
		//Initialize a jframe to plot the cost/benefit on
		//and attach it fullscreen to the display device
		graphFrame = new JFrame();
		graphFrame.setTitle("Cost vs Science Benefit Plot");
	    graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    graphFrame.setSize(pixelWidth,pixelHeight);
		graphFrame.setUndecorated(true);
	    displayDevice.setFullScreenWindow(graphFrame);
	    
	    //Initialize a graphcoordinates object to display the axes
	    GraphCoordinates initialGraph = new GraphCoordinates(pixelWidth, pixelHeight, xScale, yScale);
	    
	    initialGraph.addMouseListener(mouseAdapt);
	    
	    graphFrame.getContentPane().add(initialGraph);
	    
	    graphFrame.getContentPane().setBackground(Color.WHITE);
	    graphFrame.setVisible(true);
	    
	}
	
}
