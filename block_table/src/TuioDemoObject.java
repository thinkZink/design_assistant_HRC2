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

import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import TUIO.*;

public class TuioDemoObject extends TuioObject implements Cloneable{

	private Shape square;
	//transformation coordinates mapping xpos,ypos (camera) to pixel space
	/*
	 * Derived as follows:
	 * Given:(0.07,0.8) = (20.20), (0.07,0.4) = (20,1060), (0.66,0.41) = (1900,1060) and not-used (0.64,0.82) = 1900,20), 
	 * solve: x' = ax + by + c, y' = dx + ey + f for a,b,c,d,e,f
	 * That is: |x1 y1 1| |a| |x1'|
	 * 			|x2 y2 1|*|b|=|x2'| and the same for d,e,f and y1',y2',y3'
	 * 			|x3 y3 1| |c|=|x3'|  
	 */
	
	private final double A = 1.398950768; 
	private final double B = -7.718288101e-2;
	private final double C = 2.060058086e-1;
	private final double D = -1.524867568e-1;
	private final double E = 2.777299649e-2;
	private final double F = -4.160476243e-2;
	
	private final double G = -4.333079092e-2; 
	private final double H = -5.437062104e-1;
	private final double I = -1.939513704e-1;
	private final double J = -1.868984906e-2;
	private final double K = 1.85563729;
	private final double L = 1.444873739e-1;
	
	public int mod_id;
	public TuioDemoObject(TuioObject tobj) {
		super(tobj);
		mod_id = symbol_id % 26;
		//xpos = (int)(a*xpos+b*ypos+c);
		//ypos = (int)(d*xpos+e*ypos+f);
		int size = TuioDemoComponent.object_size;
		square = new RoundRectangle2D.Float(-size/2,-size/2,size,size,size/5,size/5);
		
		AffineTransform transform = new AffineTransform();
		transform.translate(xpos,ypos);
		transform.rotate(angle,xpos,ypos);
		square = transform.createTransformedShape(square);
	}
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	public void paint(Graphics2D g, int width, int height) {
	
		float Xpos = xpos*width;
		float Ypos = height-ypos*height;
		float scale = height/(float)TuioDemoComponent.table_size;
		AffineTransform trans = new AffineTransform();
		trans.translate(-xpos,-ypos);
		trans.translate(Xpos,Ypos);
		trans.scale(scale,scale);
		Shape s = trans.createTransformedShape(square);
	
		g.setPaint(Color.red);
		g.fill(s);
		g.setPaint(Color.black);
//		Font currentFont = g.getFont();
//		Font newFont = currentFont.deriveFont(currentFont.getSize() * 5F);
//		
//		g.setFont(newFont);
		g.drawString(toTuioLetter()+"",Xpos-10,Ypos);
	}
	
	public void paintColor(Graphics2D g, int width, int height, Paint p) {
		
		float xtpos = getXtPos();
		float ytpos = getYtPos();
		float Xpos = xtpos*width;
		float Ypos = height-(ytpos*height);
		float scale = height/(float)TuioDemoComponent.table_size;
		//System.out.println("p:("+xpos+","+ypos+"),tp:("+xtpos+","+ytpos+"),P("+Xpos+","+Ypos+")");
		AffineTransform trans = new AffineTransform();
		trans.translate(-xtpos,-ytpos);
		trans.translate(Xpos,Ypos);
		trans.scale(scale,scale);
		Shape s = trans.createTransformedShape(square);
		g.setPaint(p);
		g.fill(s);
		g.setPaint(Color.white);
		g.drawString(toTuioLetter()+"",Xpos-10,Ypos);
	}
	
	
	
	PathIterator getPathItr(int width,int height){
		float Xpos = xpos*width;
		float Ypos = height-ypos*height;
		float scale = height/(float)TuioDemoComponent.table_size;
		AffineTransform trans = new AffineTransform();
		trans.translate(-xpos,-ypos);
		trans.translate(Xpos,Ypos);
		trans.scale(scale,scale);
		Shape s = trans.createTransformedShape(square);
		return s.getPathIterator(null, 1);
	}
	public float getXtPos(){
		//return xpos*(1f/.65f)-(.08f*1f/.65f);
		//return (float)(A*xpos + B*ypos + C*xpos*ypos + D*xpos*xpos + E*ypos*ypos + F);
		//return 1.40228f*xpos+0.0439f*ypos-.084426f;
		return xpos;
	}
	public float getYtPos(){
		//return ypos*1f/.48f-.39f*1f/.48f;
		//return (float)(G*xpos + H*ypos + I*xpos*ypos + J*xpos*xpos + K*ypos*ypos + L);
		//return -0.1681f*xpos + 1.676f*ypos-.48326f;
		return ypos;
	}
	public void update(TuioObject tobj) {
		
		float dx = tobj.getX() - xpos;
		float dy = tobj.getY() - ypos;
		float da = tobj.getAngle() - angle;

		if ((dx!=0) || (dy!=0)) {
			AffineTransform trans = AffineTransform.getTranslateInstance(dx,dy);
			square = trans.createTransformedShape(square);
		}
		
		if (da!=0) {
			AffineTransform trans = AffineTransform.getRotateInstance(da,tobj.getX(),tobj.getY());
			square = trans.createTransformedShape(square);
		}

		super.update(tobj);
	}

}
