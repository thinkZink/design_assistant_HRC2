import java.awt.event.*;

import rbsa.eoss.Orbit;

public class PointMouseAdapter extends MouseAdapter {
	
	public void mousePressed(MouseEvent e) {
		GraphPoint gp = (GraphPoint)e.getSource();
		TuioDemoComponent.Orbit [] configuration = gp.configuration;
		
		for (TuioDemoComponent.Orbit o : configuration) {
			System.out.println("Configuration");
			System.out.println("Orbit: " + o.fancyString() + "\n");
		}
	}

}
