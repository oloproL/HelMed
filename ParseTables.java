import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class created for extract all symptoms and their values from their tables
 * and put them into onesymptomx table
 * @author Oscar Lopez Rodrigo
 *
 */
public class ParseTables {

	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	
	public static void main(String[] args) throws SQLException{
		
		try {
			  Class.forName(driverName);
		} catch (ClassNotFoundException e) {
		      e.printStackTrace();
		      System.exit(1);
		}
		
		// Opening a connection with Hive
		Connection con = DriverManager.getConnection("jdbc:hive2://hadoop1.unir.net:10000/helmed", "hive", "hive");
		Statement stmt = con.createStatement();
		
		// Tables with symptoms
		String[] tables = {"arrhythmia", "breastcancer", "ckd", "dermatology", "thyroid"};
		
		String sql = null;
		for(int i=0; i<tables.length;i++){
			String table = tables[i];
			
			// Extracting columns from one table
			sql = "show columns from "+table;			// Construct the query
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);		// Execute the query
			
			// Processing the query response
			ArrayList<String> symptoms = new ArrayList<String>();
			while(res.next()){
				if(!res.getString(1).equals("diagnosis"))
					symptoms.add(res.getString(1));
			}
			
			// Inserting the results into onesymptomx table
			for (String symptom: symptoms) {
				
				sql = "INSERT INTO TABLE onesymptomx "
						+ "SELECT \""+symptom+"\","+symptom+",count("+symptom+"),diagnosis from "+table
						+" group by "+symptom+",diagnosis";
				System.out.println("Running: " + sql);
				stmt.execute(sql);
			}
		}
		con.close(); // Closing the connection with hive
	}
}
