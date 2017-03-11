import java.awt.event.*;
import java.util.HashMap;

import rbsa.eoss.Orbit;

public class PointMouseAdapter extends MouseAdapter {
	
	public void mouseClicked(MouseEvent e) {
		System.out.println("checking pixelmap");
		int x = e.getX();
		int y = e.getY();
		String key = String.format("%d%d", x,y);
		//HashMap<String,GraphPoint> pmap = TuioDemoComponent.pixelMap;
		GraphPoint gp = TuioDemoComponent.pixelMap.get(key);
		if(gp != null){
			TuioDemoComponent.lastSelectedPoint = gp;
			TuioDemoComponent.Orbit [] configuration = gp.configuration;
			
//			for (TuioDemoComponent.Orbit o : configuration) {
//				System.out.print("Configuration ");
//				System.out.println("Orbit: " + o.fancyString() + "\n");
//			}
		}
	}

}
