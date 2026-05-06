package com.orangeHRM.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {
	private static final String DB_URL="jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME="root";
	private static final String DB_PASSWORD="";
	
	public static Connection getDBConnection() {
		try {
			System.out.println("Starting DB connection");
			Connection con=DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			System.out.println("DB connection succesfull");
			return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Starting DB connection error");
			e.printStackTrace();
			return null;
		}
	}		
		public static Map<String,String> getEmployeeDetails(String employee_id){
			String query="SELECT emp_firstname,emp_lastname,emp_middle_name FROM `hs_hr_employee` WHERE employee_id="+employee_id;
			Map<String,String> employeeDetails=new HashMap<>();
			
			try(Connection con=getDBConnection();
					Statement stmt=con.createStatement();
					ResultSet rs=stmt.executeQuery(query)){
						System.out.println("Executing query: "+query);
						if(rs.next()) {
							String firstName=rs.getString("emp_firstname");
							String middleName=rs.getString("emp_middle_name");
							String lastName=rs.getString("emp_lastname");
							
							//Store in a map
							employeeDetails.put("firstName", firstName);
							employeeDetails.put("middleName", middleName!=null?middleName:"");
							employeeDetails.put("lastName", lastName);
							
							System.out.println("Query executed successfully");
						}
						else
							System.out.println("Employee not found");
						
					}
			
		
		catch(Exception e) {
			System.out.println("Error while executing query");
		}
		return employeeDetails;
}
}	
	
