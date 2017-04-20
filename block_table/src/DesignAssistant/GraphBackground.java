package DesignAssistant;
import java.awt.Color;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JComponent;
import javax.swing.JFrame;

import rbsa.eoss.Architecture;
import rbsa.eoss.ArchitectureEvaluator;
import rbsa.eoss.ArchitectureGenerator;
import rbsa.eoss.Result;

public class GraphBackground extends JComponent {
	ArrayList<double[]> data;
	int xMin;
	int xMax;
	int yMax;
	int yMin;
	Color color;

	public GraphBackground(ArrayList<double[]> data, int xMin, int xMax, int yMin, int yMax) {
		this.data = data;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.color = Color.getHSBColor(0.0f, 0.0f, 0.80f);
	}
	public GraphBackground(ArrayList<double[]> data, int xMin, int xMax, int yMin, int yMax, Color color) {
		this.data = data;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.color = color;
	}
	public GraphBackground(int xMin,int xMax,int yMin, int yMax) {
		this.data = null;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
	}
	public void paint(Graphics g) {
		
		g.setColor(this.color);
		for(int i=0; i<data.size(); i++){
			g.fillRect(xMin+(int)data.get(i)[0], yMax-(int)data.get(i)[1], 5,5);	  
		}
	}
	public HashMap<String,double[]> generateRandomArchitectures(int numSamples, ArchitectureGenerator AG, ArchitectureEvaluator AE){
		HashMap<String,double[]> dataSet = new HashMap<String,double[]>();
		ArrayList<Architecture> randomConfigurations = AG.generateRandomPopulation(numSamples);
		for(Architecture config : randomConfigurations){
			Result result =AE.evaluateArchitecture(config,"Slow");
			double[] coordinates = {result.getScience(),result.getCost()};
			boolean[] bitString = config.getBitString();
			String strBitString = "";
			for(int i = bitString.length; i> 0; i--){
				strBitString += bitString[i-1] ? "1" : "0";
			}
			dataSet.put(strBitString,coordinates);			
		}
		this.data = new ArrayList<double[]>(dataSet.values());
		return dataSet;
	}
	
	public HashMap<String,double[]> generateRandomDataSet(int numSamples){
		HashMap<String,double[]> dataSet = new HashMap<String,double[]>();
		for(int i=0; i<numSamples; i++){
			String configuration = generateRandomConfiguration();
			
		}
		return dataSet;
	}
	private String generateRandomConfiguration(){
		Long randomLong = ThreadLocalRandom.current().nextLong(1,(2^60)+1);
		assert(randomLong.toString().substring(4).length() == 60);
		return randomLong.toString().substring(4); //return the last 60 chars
	}


}
