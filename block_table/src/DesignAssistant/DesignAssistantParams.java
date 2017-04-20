package DesignAssistant;


public class DesignAssistantParams {
	/*
	 * DesignAssistantParams provides a set of static configuration variables
	 */
	public static double graphCostMax;
	public static double graphBenefitMax;
	public static String preliminaryDataPath;
	
	public static void initDesignAssistantParams(){
		graphCostMax = 9981; //max in RBSAEOSS prelim data
		graphBenefitMax = 0.281;
		preliminaryDataPath = "/Users/designassistant/Documents/workspace/design_assistant_HRC2/block_table/src/data/EOSS_data.csv";
	}
	public static void initDesignAssistantParams(String paramFile){
		
	}
}
