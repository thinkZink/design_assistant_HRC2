package DesignAssistant;
/* Main class for Design Assistant.
 
 Adapted TUIO Java GUI
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

public class DesignAssistant  {

	private final int window_width  = 480;
	private final int window_height = 480;

	private boolean fullscreen = true;
	
	private TableComponent tableComponent;
	private JFrame tableFrame;
	private GraphicsDevice tableDevice;
	private Cursor invisibleCursor;
	
	public DesignAssistant() {
		String preDataPath = "/Users/Nikhil/Desktop/git_repo/design_assistant_HRC2/block_table/src/data/EOSS_data.csv";
		preDataPath = "/Users/designassistant/Documents/workspace/design_assistant_HRC2/block_table/src/data/EOSS_data.csv";
		//preDataPath = "/Users/mvl24/Documents/workspace/design_assistant_HRC2/block_table/src/data/EOSS_data.csv";
		tableFrame = new JFrame();
		tableComponent = new TableComponent(preDataPath,tableFrame);
		//demo = new TuioDemoComponent(frame);
		tableComponent.init();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		tableDevice = gs[0];//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invisible cursor");
		tableFrame.add(tableComponent);
		tableFrame.setVisible(true);
		tableFrame.repaint();
		//setupWindow();
		//showWindow();
	}
	
	public TuioListener getTuioListener() {
		return tableComponent;
	}
	
	public void setupWindow() {
	
		
		tableFrame.add(tableComponent);

		//frame.setTitle("Current Configuration");
		tableFrame.setResizable(false);
		tableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
	    tableFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		tableFrame.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent evt) {
				System.exit(0);
			} });
		
		tableFrame.addKeyListener( new KeyAdapter() { public void keyPressed(KeyEvent evt) {
			if (evt.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
			else if (evt.getKeyCode()==KeyEvent.VK_F1) {
				destroyWindow();
				setupWindow();
				fullscreen = !fullscreen;
				showWindow();
			}
			else if (evt.getKeyCode()==KeyEvent.VK_V) tableComponent.verbose=!tableComponent.verbose;
		} });
	}
	
	public void destroyWindow() {
	
		tableFrame.setVisible(false);
		if (fullscreen) {
			tableDevice.setFullScreenWindow(null);		
		}
		tableFrame = null;
	}
	
	public void showWindow() {
	
		if (fullscreen) {
			int width  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			tableComponent.setSize(width,height);

			tableFrame.setSize(width,height);
			tableFrame.setUndecorated(true);
			tableDevice.setFullScreenWindow(tableFrame);
			tableFrame.setCursor(invisibleCursor);
		} else {
//			int width  = window_width;
//			int height = window_height;
//			demo.setSize(width,height);
//			
//			frame.pack();
//			Insets insets = frame.getInsets();			
//			frame.setSize(width,height +insets.top);
//			frame.setCursor(Cursor.getDefaultCursor());
		}
		
		tableFrame.setVisible(true);
		tableFrame.repaint();
		System.out.println("cost: " + tableComponent.cost + "   " + "y: " + tableComponent.science);
	}
	
	public static void main(String argv[]) {
		
		DesignAssistant design_assistant = new DesignAssistant();
		TuioClient client = null;
		
		
		
		
		switch (argv.length) {
			case 1:
				try { 
					client = new TuioClient(Integer.parseInt(argv[0])); 
				} catch (Exception e) {
					System.out.println("usage: java TuioDesignAssistant [port]");
					System.exit(0);
				}
				break;
			case 0:
				client = new TuioClient();
				break;
			default: 
				System.out.println("usage: java TuioDesignAssistant [port]");
				System.exit(0);
				break;
		}
		
		if (client!=null) {
			client.addTuioListener(design_assistant.getTuioListener());
			client.connect();
		} else {
			System.out.println("usage: java TuioDesignAssistant [port]");
			System.exit(0);
		}
	}
	
	
	public class GraphComponent extends JComponent {

		private static final long serialVersionUID = 1L;

		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int[] x = new int[]{65, 122, 77, 20};
	        int[] y = new int[]{226, 258, 341, 310};
	        g.setColor(Color.RED);
	        g.drawPolygon(x, y, x.length);
	    }

	    //so our panel is the correct size when pack() is called on JFrame
	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(400, 400);
	    }
	}
	
}
