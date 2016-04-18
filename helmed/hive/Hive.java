package com.unir.helmed.hive;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.sql.DriverManager;
import org.elasticsearch.hadoop.hive.*;

/**
 * Class created for doing all the HIVE queries
 * @author Oscar
 *
 */
public class Hive {
	
	  private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	  
	  /**
	   * Method for extracting all disease symptoms of a disease category
	   * @param category Disease category which symptoms will be extracted
	   * @return List of symptoms extracted
	   * @throws SQLException
	   */
	  public ArrayList<String> getSymptoms(String category) throws SQLException{
		
		  try {
			  Class.forName(driverName);
		  } catch (ClassNotFoundException e) {
		      e.printStackTrace();
		      System.exit(1);
		  }
		
		  // Opening a connection with Hive
		  Connection con = DriverManager.getConnection("jdbc:hive2://hadoop1.unir.net:10000/helmed", "hive", "hive");
		  Statement stmt = con.createStatement();
		
		  // Extracting columns from a specific table
		  String sql = "show columns from "+category;		// Construct the query
		  System.out.println("Running: " + sql);
		  ResultSet res = stmt.executeQuery(sql);			// Execute the query
		
		  // Processing the query response
		  ArrayList<String> symptoms = new ArrayList<String>();
		  while(res.next()){
			  if(!res.getString(1).equals("diagnosis"))
				  symptoms.add(res.getString(1));
		  }

		  con.close();			// Closing the connection with hive
		  return symptoms;		// Return all the symptoms extracted
	  }
	 
	  /**
	   * Method for extracting all symptom values
	   * @param symptom Symptom which values will be extracted
	   * @return List of values extracted
	   * @throws SQLException
	   */
	  public ArrayList<String> getValues(String symptom) throws SQLException{
			 
			ArrayList<String> result = new ArrayList<String>();
			
			try {
				  Class.forName(driverName);
			} catch (ClassNotFoundException e) {
			      e.printStackTrace();
			      System.exit(1);
			}
			
			// Opening a connection with Hive
			Connection con = DriverManager.getConnection("jdbc:hive2://hadoop1.unir.net:10000/helmed", "hive", "hive");
			Statement stmt = con.createStatement();
			
			// Adding elasticsearch library
		    String loadJar = "add jar hdfs://hadoop1:8020/user/hadoop/HelMed/elasticsearch-hadoop-2.2.0-rc1.jar";
		    stmt.execute(loadJar);
		    
			// Extracting values
			String sql = "select value from symptoms where symptom=='"+symptom.toLowerCase()+"'";  	// Construct the query 
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);													// Execute the query
			while (res.next()) {																	// Processing query results
				result.add(res.getString(1));
			}
			
			// Sorting query results
			HashSet<String> hs = new HashSet<String>();
			hs.addAll(result);
			result.clear();
			result.addAll(hs);
			
			con.close(); 		// Closing 
			return result;
		  }

	  /**
	   * Method for obtaining all the possible diseases that can be diagnosed from a symptom
	   * @param symptom Symptom
	   * @param value Symptom value
	   * @return Disease diagnosed
	   * @throws SQLException
	   */
	  public ArrayList<String[]> getDisease(String symptom, String value) throws SQLException{
			 
			ArrayList<String[]> result = new ArrayList<String[]>();
			
			try {
				  Class.forName(driverName);
			} catch (ClassNotFoundException e) {
			      e.printStackTrace();
			      System.exit(1);
			}
			//replace "hive" here with the name of the user the queries should run as
			Connection con = DriverManager.getConnection("jdbc:hive2://hadoop1.unir.net:10000/helmed", "hive", "hive");
			Statement stmt = con.createStatement();
			
			// Adding elasticsearch library
		    String loadJar = "add jar hdfs://hadoop1:8020/user/hadoop/HelMed/elasticsearch-hadoop-2.2.0-rc1.jar";
		    stmt.execute(loadJar);
		    
			
			String sql = "select diagnosis,confidencefactor from symptoms "
					+ "where symptom=='"+symptom.toLowerCase()+"' and value=='"+value+"'";
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				String disease[] = {res.getString(1),Float.toString(roundNumber(res.getString(2),3)*100)};
				result.add(disease);
			}
			con.close();		// Closing the connection with hive
			return result;		// Return the list of the possible values
		  }
		  
	  /**
	   * Method to obtain information about one disease 
	   * @param disease Disease which information will be extracted
	   * @return Information about the disease
	   * @throws SQLException
	   */
	  public String[] getInfo(String disease) throws SQLException{
			 
		  ArrayList<String[]> result = new ArrayList<String[]>();
			
	  
			try {
				  Class.forName(driverName);
			} catch (ClassNotFoundException e) {
			      e.printStackTrace();
			      System.exit(1);
			}
			
			// Opening a connection with Hive
			Connection con = DriverManager.getConnection("jdbc:hive2://hadoop1.unir.net:10000/helmed", "hive", "hive");
			Statement stmt = con.createStatement();
			
			// Adding elasticsearch library
		    String loadJar = "add jar hdfs://hadoop1:8020/user/hadoop/HelMed/elasticsearch-hadoop-2.2.0-rc1.jar";
		    stmt.execute(loadJar);
		    
			// Extracting info about the disease
			String sql = "select tag,info from tags where diagnosis == '"+disease+"'";	// Construct the query
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);										// Execute the query
			while (res.next()) {														// Processing query response
				String info[] = {res.getString(1),res.getString(2)};
				result.add(info);
			}
			
			con.close(); 				// Closing connection with hive
			return result.get(0);		// Return disease information
		  }

	  
	  /**
	   * Method to obtain drugs for the treatment a disease 
	   * @param tag Tag associated to a disease
	   * @return Drugs for disease treatment
	   * @throws SQLException
	   */
	  public ArrayList<String[]> getDrugs(String tag) throws SQLException{
			 
			ArrayList<String[]> result = new ArrayList<String[]>();
			
			try {
				  Class.forName(driverName);
			} catch (ClassNotFoundException e) {
			      e.printStackTrace();
			      System.exit(1);
			}
			
			// Opening a connection with Hive
			Connection con = DriverManager.getConnection("jdbc:hive2://hadoop1.unir.net:10000/helmed", "hive", "hive");
			Statement stmt = con.createStatement();
			
			// Adding elasticsearch library
		    String loadJar = "add jar hdfs://hadoop1:8020/user/hadoop/HelMed/elasticsearch-hadoop-2.2.0-rc1.jar";
		    stmt.execute(loadJar);
		    
			// Extracting the drug list
			String sql = "select name,indication from drugs where tag=='"+tag+"'";	// Construct the query
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);									// Execute the query
			while (res.next()) {													// Processing query response
				String[] drug = {res.getString(1),res.getString(2)};
				result.add(drug);
			}
			
			con.close();			// Closing connection with hive
			return result;			// Return drugs related to the disease
		  }
	
	  /**
	   * Method to round a number with specific decimals
	   * @param number Number to round
	   * @param decimals Number of decimals
	   * @return Number rounded
	   */
	  private float roundNumber(String number, int decimals){
			
		    BigDecimal big = new BigDecimal(number);
		    big = big.setScale(decimals, RoundingMode.HALF_UP);
			return Float.parseFloat(big.toString());
		}
	  
}
