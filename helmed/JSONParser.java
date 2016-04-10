package com.unir.helmed;

import java.util.ArrayList;

import org.json.*;


public class JSONParser {

	/**
	 * Method to convert an ArrayList of diseases to JSON objects
	 * @param diseases Diseases to convert
	 * @return	JSONArray with the parameters of the diseases
	 * @throws JSONException
	 */
	public JSONArray parseToJSON(ArrayList<String[]> diseases) throws JSONException{
		JSONArray ja = new JSONArray();
		
		for(String[] disease: diseases){
			JSONObject jo = new JSONObject();	// Create a JSONObject
		
			try {
				// Putting the disease parameters into new JSONObject
				jo.put("Disease", disease[0]);
				jo.put("ConfidenceFactor", Float.parseFloat(disease[1]));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ja.put(jo);		// Adding the JSON Object with the diseases parameters to the JSON Array.
		}
		return ja;
	}	
		
	
}
