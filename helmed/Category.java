package com.unir.helmed;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import com.unir.helmed.hive.Hive;

/**
 * @author Oscar Lopez
 * 
 */
public class Category implements Serializable{

	/**
	 * Serial version for the category
	 */
	private static final long serialVersionUID = -5464133605641135978L;
	private String category;				// Category
	private ArrayList<String> symptoms;		// Symptom List associated to this category
	
	
	/**
	 * Generates the object from the cat parameter
	 * @param cat
	 */
	public void setCategory(String cat) {
		Hive hive = new Hive();				// Creating a Hive Object to do hive queries
		try{
			// Establishing category and obtaining their symptoms from Hive  
			switch(cat){
			case "arr":
				category="Arrhythmia";
				symptoms = hive.getSymptoms("arrhythmia");
				break;
			case "bc":
				category = "Breast Cancer";
				symptoms = hive.getSymptoms("breastcancer");
				break;
			case "ckd":
				symptoms = hive.getSymptoms("ckd");
				break;
			case "derma":
				symptoms = hive.getSymptoms("dermatology");
				break;
			case "thy":
				symptoms = hive.getSymptoms("thyroid");
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to obtain the category
	 * @return Category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Method to obtain the list of symptoms associated to a disease Category
	 * @return List of symptoms
	 */
	public ArrayList<String> getSymptoms() {
		return symptoms;
	}
	
	
}
