package DesignAssistant;
import java.awt.Component;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import rbsa.eoss.Orbit;

public class PointMouseAdapter extends MouseAdapter {
	TableComponent demoComponent;
	public PointMouseAdapter(TableComponent demoComponent){
		this.demoComponent = demoComponent;
	}
	
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouse click");
		if(e.getButton()==MouseEvent.BUTTON1){
			System.out.println("checking pixelmap");
			int x = e.getX();
			int y = e.getY();
			String key = String.format("%d%d", x,y);
			//HashMap<String,GraphPoint> pmap = TuioDemoComponent.pixelMap;
			GraphPoint gp = TableComponent.pixelMap.get(key);
			if(gp != null){
				TableComponent.currentSelectedPoint = gp;
	
	//			TuioDemoComponent.Orbit [] configuration = gp.configuration;
	//			for (TuioDemoComponent.Orbit o : configuration) {
	//				System.out.print("Configuration ");
	//				System.out.println("Orbit: " + o.fancyString() + "\n");
	//			}
			}
		}
		else if(e.getButton()==MouseEvent.BUTTON3){
			System.out.println("right-click");
			//at this point we should probably be passing the tuiodemocomponent in to the mouse adapter
			//JFrame frame = (JFrame)SwingUtilities.windowForComponent((Component) e.getSource());
			//frame.repaint();
			
			this.demoComponent.filteringMode ^= true;
			this.demoComponent.performUpdate(true);
		}
	}

}
