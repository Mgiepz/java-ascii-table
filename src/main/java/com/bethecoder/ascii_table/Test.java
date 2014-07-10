/**
 * Copyright (C) 2011 K Venkata Sudhakar <kvenkatasudhakar@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bethecoder.ascii_table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.bethecoder.ascii_table.impl.CollectionASCIITableAware;
import com.bethecoder.ascii_table.impl.JDBCASCIITableAware;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

/**
 * ASCII Table test cases.
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		basicTests();
		//h2JDBCTests();
		//oracleJDBCTests();
		collectionTests();
	}
	
	private static void basicTests() {
		
		String [] header = { "User Name", 
	    		"Salary", "Designation",
	    		"Address", "Lucky#"
	    		};
		
	    String[][] data = {
	    		{ "Ram", "2000", "Manager", "#99, Silk board", "1111"  },
	    		{ "Sri", "12000", "Developer", "BTM Layout", "22222" },
	    		{ "Prasad", "42000", "Lead", "#66, Viaya Bank Layout", "333333" },
	    		{ "Anu", "132000", "QA", "#22, Vizag", "4444444" },
	    		{ "Sai", "62000", "Developer", "#3-3, Kakinada"  },
	    		{ "Venkat", "2000", "Manager"   },
	    		{ "Raj", "62000"},
	    		{ "BTC"},
	    };
	    
	    //ASCIITable.getInstance().printTable(header, ASCIITable.ALIGN_RIGHT, data, ASCIITable.ALIGN_LEFT);
	    //ASCIITable.getInstance().printTable(header, data, ASCIITable.ALIGN_LEFT);
	    
	    ASCIITableHeader[] headerObjs = {
	    		new ASCIITableHeader("User Name", ASCIITable.ALIGN_LEFT),
	    		new ASCIITableHeader("Salary"),
	    		new ASCIITableHeader("Designation", ASCIITable.ALIGN_CENTER),
	    		new ASCIITableHeader("Address", ASCIITable.ALIGN_LEFT),
	    		new ASCIITableHeader("Lucky#", ASCIITable.ALIGN_RIGHT),
	    };
	    
	    ASCIITable.getInstance().printTable(headerObjs, data);
	    ASCIITable.getInstance().printTable(header, data);
	    System.out.println(ASCIITable.getInstance().getTable(headerObjs, data));
	}

	private static void h2JDBCTests() throws ClassNotFoundException, SQLException {
		//Need to have h2-1.3.160.jar in classpath.
		Class.forName("org.h2.Driver");
	    Connection conn = DriverManager.getConnection(
	        "jdbc:h2:tcp://localhost/~/test", "sa", "");
	    
	    //Print BUG_STAT table
		IASCIITableAware asciiTableAware = new JDBCASCIITableAware(
				conn, "select STATUS, COUNT from BUG_STAT");
		ASCIITable.getInstance().printTable(asciiTableAware);
		
		//Print USER table
		asciiTableAware = new JDBCASCIITableAware(
				conn, "select * from USER");
		ASCIITable.getInstance().printTable(asciiTableAware);

	}
	
	private static void oracleJDBCTests() throws ClassNotFoundException, SQLException {
		//Need to have ojdbc6.jar in classpath.
		Class.forName("oracle.jdbc.driver.OracleDriver");
	    Connection conn = DriverManager.getConnection(
	        "jdbc:oracle:thin:@localhost:1521:ORADBVENKAT", "digital_transformation", "digital_transformation");
	    
	    //Print BUG_STAT table
		IASCIITableAware asciiTableAware = new JDBCASCIITableAware(
				conn, "select * from CONTACTINFO");
		ASCIITable.getInstance().printTable(asciiTableAware);
	}
	
	private static void collectionTests() {
	
		Employee stud = new Employee("Sriram", 2, "Chess", false, 987654321.21d);
		Employee stud2 = new Employee("Sudhakar", 29, "Painting", true, 123456789.12d);
	    List<Employee> students = Arrays.asList(stud, stud2);
	 
	    IASCIITableAware asciiTableAware = 
	    	new CollectionASCIITableAware<Employee>(students, 
	    			"name", "age", "married", "hobby", "salary");  //properties to read
	    ASCIITable.getInstance().printTable(asciiTableAware);
	    
	    
	    asciiTableAware = 
	    	new CollectionASCIITableAware<Employee>(students, 
	    			Arrays.asList("name", "age", "married", "hobby", "salary"), //properties to read
	    			Arrays.asList("STUDENT_NAME", "HIS_AGE")); //custom headers
	    ASCIITable.getInstance().printTable(asciiTableAware);
	}

	public static class Employee {
	
		private String name;
		private int age;
		private String hobby;
		private boolean married;
		private double salary; 
	
		public Employee(String name, int age, String hobby, boolean married, double salary) {
			super();
			this.name = name;
			this.age = age;
			this.hobby = hobby;
			this.married = married;
			this.salary = salary;
		}
	
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
	
		public int getAge() {
			return age;
		}
	
		public void setAge(int age) {
			this.age = age;
		}
	
		public String getHobby() {
			return hobby;
		}
	
		public void setHobby(String hobby) {
			this.hobby = hobby;
		}
	
		public boolean isMarried() {
			return married;
		}

		public void setMarried(boolean married) {
			this.married = married;
		}

		public double getSalary() {
			return salary;
		}

		public void setSalary(double salary) {
			this.salary = salary;
		}
	}
	
}
