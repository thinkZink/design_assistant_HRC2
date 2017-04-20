package DesignAssistant;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Filter {
	public HashMap<String,double[]> configurations;
	
	
	public Filter(){
		configurations = new HashMap<String,double[]>();
	}
	public Filter(HashMap<String,double[]> initConfig){
		configurations = initConfig;
	}
	public Filter(String dataFile){
		configurations = getInitialData(dataFile);
	}
	
	public boolean applyPresentFilter(long matchConfiguration, long configuration){
		return matchConfiguration == (matchConfiguration & configuration);
	}
	/*
	 * Returns a list of (science, cost) pairs that match the filter
	 */
	public ArrayList<double[]> getFilteredData(long matchConfiguration){
		ArrayList<double[]> resultSet = new ArrayList<double[]>();;
		for (String configuration : configurations.keySet()){
			if(applyPresentFilter(matchConfiguration,Long.parseLong(configuration,2))){
				resultSet.add(configurations.get(configuration));
			}
		}
		return resultSet;
	}
	//this is bad, this is redundant with preload getinitialdata
	private HashMap<String,double[]> getInitialData(String filename){
		HashMap<String,double[]> initialData = new HashMap<String,double[]>();
		String row = "";
		BufferedReader br = null;
	    try {
	           br = new BufferedReader(new FileReader(filename));
	           row = br.readLine(); //strip column headers
	            while ((row = br.readLine()) != null) {
	                String[] rawInstance = row.split(",");
	                double[] dataPair = {Double.parseDouble(rawInstance[1])*4000,Double.parseDouble(rawInstance[2])/12};
	                initialData.put(rawInstance[0],dataPair);
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
	
}
