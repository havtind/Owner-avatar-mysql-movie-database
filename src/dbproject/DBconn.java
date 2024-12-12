package dbproject;

import java.util.Properties;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBconn {
	
	private Connection myConn;
	private String username;
	private String password;
	private String DBurl;
	
	public DBconn () {
		readPropterties();
		connect();
	
		
	}
	
	public void readPropterties() {
		// read database credentials from file
		try {
		Properties props = new Properties();
		props.load(new FileInputStream("credentials.properties"));
		username = props.getProperty("user");
		password = props.getProperty("password");
		DBurl = props.getProperty("DBurl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void connect() {
		// connect to database
		try {
			myConn = DriverManager.getConnection(DBurl, username, password);
			System.out.println("Connection created from DBconn!");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return myConn;
	}
	
	public void closeConnection() throws SQLException {
		// this is done by the GUI when the app is running
		myConn.close();
		System.out.println("Connection closed from DBconn!");
	}
	
}
