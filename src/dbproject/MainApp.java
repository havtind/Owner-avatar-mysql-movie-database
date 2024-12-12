package dbproject;

import java.awt.EventQueue; 
import java.sql.Connection;
import java.sql.SQLException;


public class MainApp {
	
	private AppGUI myApplication;
	
	public MainApp(Connection conn) {	
		
		// let the core classes get the connection
		AddDAO mainAdder = new AddDAO(conn);
		GetDAO mainGetter = new GetDAO(conn);
		
		try {
			// feed the core classes to the GUI
			myApplication = new AppGUI(mainAdder, mainGetter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.runApp();
	}
	
	public void runApp() {
		// Runs the Java Swing application 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					myApplication.setVisible(true);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args) {

		// Configuring connection to database
		DBconn myDBconn = new DBconn();
		
		// Get connection
		Connection myConn = myDBconn.getConnection();
		
		// Give connection to app and run it 
		new MainApp(myConn);
	}
	
}
