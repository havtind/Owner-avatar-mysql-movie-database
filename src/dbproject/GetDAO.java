package dbproject;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class GetDAO {
	
	static Connection myConn = null;
	
	private ArrayList<String> listOfSeries;
	private ArrayList<String> listOfPersons;
	private ArrayList<String> listOfCompanies;
	private ArrayList<String> listOfGenres;
	private ArrayList<String> listOfSeasonNames;
	private ArrayList<Integer> listOfSeasonIDs;
	private ArrayList<Integer> listOfReviewObjectIDs;
	
	public GetDAO(Connection conn) {
		try {			
			myConn = conn;
			myConn.setAutoCommit(false);
			listOfSeries = new ArrayList<String>();
			listOfReviewObjectIDs = new ArrayList<Integer>();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	
	// Usecase 1:
	protected ArrayList<String> getRolesForActor(String personnavn) throws SQLException { 
			PreparedStatement myStmt = myConn.prepareStatement(""
					+ "SELECT rolle "
					+ "FROM skuespillerifilm "
					+ "JOIN person ON skuespillerifilm.personID = person.personID "
					+ "WHERE navn = ?");
			myStmt.setString(1, personnavn);
			ResultSet myRs = myStmt.executeQuery();
			myConn.commit();
			
			ArrayList<String> listOfRoles = new ArrayList<String>();
	        while (myRs.next()) {
				listOfRoles.add(myRs.getString("Rolle"));
			}
			myStmt.close();
			return listOfRoles;
		}
		
	// Usecase 2:
	protected ArrayList<String> getFilmsForActor(String personnavn) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement(""
				+ "SELECT tittel "
				+ "FROM skuespillerifilm "
				+ "JOIN person ON skuespillerifilm.personID = person.personID "
				+ "JOIN screenplay ON skuespillerifilm.screenplayID = screenplay.screenplayID "
				+ "WHERE navn = ?");
		myStmt.setString(1, personnavn);
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		ArrayList<String> listOfActorFilms = new ArrayList<String>();
        while (myRs.next()) {
        	listOfActorFilms.add(myRs.getString("tittel"));
		}
		myStmt.close();
		return listOfActorFilms;
	}
	
	// Usecase 3:
	protected ArrayList<ArrayList<String>> getCompanyGenreRecords() throws SQLException {  
		PreparedStatement myStmtDirector = myConn.prepareStatement(""
				+ "WITH t1 AS (SELECT sjangernavn, navn, count(*) AS cnt"
				+ " FROM sjanger JOIN harSjanger ON sjanger.sjangerID = harSjanger.sjangerID"
				+ " JOIN screenplay ON harSjanger.screenplayID = screenplay.screenplayID"
				+ " JOIN selskap on screenplay.selskapID = selskap.selskapID"
				+ " GROUP BY sjangernavn, navn"
				+ " ), t2 AS ("
				+ " SELECT sjangernavn, MAX(cnt) AS cnt"
				+ " FROM t1"
				+ " GROUP BY sjangernavn"
				+ " )"
				+ " SELECT *"
				+ " FROM t1"
				+ " NATURAL JOIN t2");
		
		ResultSet myRs = myStmtDirector.executeQuery();
		myConn.commit();
		
		ArrayList<ArrayList<String>> resultArray = new ArrayList<ArrayList<String>>();
        while (myRs.next()) {
        	ArrayList<String> resultRow = new ArrayList<String>(); 
        	resultRow.add(myRs.getString(1));
        	resultRow.add(myRs.getString(3));
        	resultRow.add(myRs.getString(2));
        	resultArray.add(resultRow);
		}
        myStmtDirector.close();
        return resultArray;
	}
	
	
	protected List<String> getPersons() throws SQLException { 
		PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM person");
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		listOfPersons = new ArrayList<String>();
        while (myRs.next()) {
			listOfPersons.add(myRs.getString("Navn"));
		}
		myStmt.close();
		return listOfPersons;
	}
	
	protected List<String> getCompanies() throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM selskap");
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		listOfCompanies = new ArrayList<String>();
        while (myRs.next()) {
			listOfCompanies.add(myRs.getString("Navn"));
		}
		myStmt.close();
		return listOfCompanies;
	}
	
	protected List<String> getGenres() throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM sjanger");
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		listOfGenres = new ArrayList<String>();
        while (myRs.next()) {
			listOfGenres.add(myRs.getString("sjangernavn"));
		}
		myStmt.close();
		return listOfGenres;
	}
	
	protected List<String> getSeasons(int serieID) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM sesong WHERE serieID = ?");
		myStmt.setInt(1, serieID);
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		listOfSeasonNames = new ArrayList<String>();
		listOfSeasonIDs = new ArrayList<Integer>();
        while (myRs.next()) {
		 	listOfSeasonIDs.add(myRs.getInt("sesongID"));
			listOfSeasonNames.add(myRs.getString("sesongnavn"));
		}
		myStmt.close();
		return listOfSeasonNames;
	}
	
	protected List<Integer> getSeasonIDs() {
		return listOfSeasonIDs;
	}
	
	protected List<String> getSeries() throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM serie");
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		listOfSeries = new ArrayList<String>();
        while (myRs.next()) {
			listOfSeries.add(myRs.getString("serienavn"));
		}
		myStmt.close();
		return listOfSeries;
	}
	
	protected ArrayList<String> getFilmNames() throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("SELECT tittel, screenplayID FROM screenplay WHERE sesongID = 0");
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		ArrayList<String> listOfFilmNames = new ArrayList<String>();
		listOfReviewObjectIDs.clear();
        while (myRs.next()) {
			listOfFilmNames.add(myRs.getString("tittel"));
			listOfReviewObjectIDs.add(myRs.getInt("screenplayID"));
		}
		myStmt.close();
		return listOfFilmNames;
	} 
	
	protected ArrayList<String> getEpisodeNames(String seasonName, String serieName) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement(""
				+ "SELECT tittel, screenplayID "
				+ "FROM screenplay "
				+ "JOIN sesong ON screenplay.sesongID = sesong.sesongID "
				+ "JOIN serie ON serie.serieID = sesong.serieID "
				+ "WHERE sesongnavn = ? AND serienavn = ?");
		myStmt.setString(1, seasonName);
		myStmt.setString(2, serieName);
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		ArrayList<String> listOfEpisodeNames = new ArrayList<String>();
		listOfReviewObjectIDs.clear();
        while (myRs.next()) {
			listOfEpisodeNames.add(myRs.getString("tittel"));
			listOfReviewObjectIDs.add(myRs.getInt("screenplayID"));
		}
		myStmt.close();
		return listOfEpisodeNames;
	}
	
	protected List<String> getUserNames() throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM bruker");
		ResultSet myRs = myStmt.executeQuery();
		myConn.commit();
		
		ArrayList<String> listOfUserNames = new ArrayList<String>();
        while (myRs.next()) {
        	listOfUserNames.add(myRs.getString("brukernavn"));
		}
		myStmt.close();
		return listOfUserNames;
	}
	
	protected ArrayList<Integer> getReviewObjectIDs() {
		return listOfReviewObjectIDs;
	}

	
	protected void closeConnection() throws SQLException {
		myConn.close();
		System.out.println("Connection closed from GetDAO!");
	}
	
	protected void trunctateDB() throws SQLException {
		// Tilleggsfunksjon: gjør at innholdet i DB kan slettes. Hører egentlig ikke under GetDAO.
		
		Statement myStmtWriter = myConn.createStatement();
		myStmtWriter.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
		myStmtWriter.executeUpdate("TRUNCATE bruker");
		myStmtWriter.executeUpdate("TRUNCATE screenplay");
		myStmtWriter.executeUpdate("TRUNCATE person");
		myStmtWriter.executeUpdate("TRUNCATE skuespillerifilm");
		myStmtWriter.executeUpdate("TRUNCATE regissørifilm");
		myStmtWriter.executeUpdate("TRUNCATE forfatterifilm");
		myStmtWriter.executeUpdate("TRUNCATE sjanger");
		myStmtWriter.executeUpdate("TRUNCATE harSjanger");
		myStmtWriter.executeUpdate("TRUNCATE sesong");
		myStmtWriter.executeUpdate("TRUNCATE serie");
		myStmtWriter.executeUpdate("TRUNCATE selskap");
		myStmtWriter.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
		myConn.commit();
		myStmtWriter.close();
		System.out.println("database er nullstilt");
	}
	
}
