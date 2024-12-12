package dbproject;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AddDAO {
	
	static Connection myConn = null;
	
	public AddDAO(Connection conn) {
		try {			
			myConn = conn;
			myConn.setAutoCommit(false);
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	
	protected void addUser(String username) throws SQLException { 
		PreparedStatement myStmt = myConn.prepareStatement("INSERT INTO bruker (brukernavn) VALUES (?)");
		myStmt.setString(1, username);
		myStmt.executeUpdate();
		myConn.commit();
		
		System.out.println("Bruker er lagt til");
		myStmt.close();
	}
	
	protected void addPerson(String name, int birthyear, String country) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("INSERT INTO person (navn,fødselsår, fødselsland) VALUES (?,?,?)");
		myStmt.setString(1, name);
		myStmt.setInt(2, birthyear);
		myStmt.setString(3, country);
		myStmt.executeUpdate();
		myConn.commit();
		myStmt.close();
		
		System.out.println("Person er lagt til");
	}
	
	protected void addCompany(String name, String address, String country, String URL) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("INSERT INTO selskap (navn, adresse, land, url) VALUES (?,?,?,?)");
		myStmt.setString(1, name);
		myStmt.setString(2, address);
		myStmt.setString(3, country);
		myStmt.setString(4, URL);
		myStmt.executeUpdate();
		myConn.commit();
		myStmt.close();
		
		System.out.println("Selskap er lagt til");
	}

	protected void addGenre(String genre) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("INSERT INTO sjanger (sjangernavn) VALUES (?)");
		myStmt.setString(1, genre);
		myStmt.executeUpdate();
		myConn.commit();
		myStmt.close();
		
		System.out.println("Sjanger er lagt til");
	}
	
	protected void addSeason(String name, int serieID) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("INSERT INTO sesong (sesongnavn, serieID) VALUES (?,?)");
		myStmt.setString(1, name);
		myStmt.setInt(2, serieID);
		myStmt.executeUpdate();
		myConn.commit();
		myStmt.close();
		
		System.out.println("Sesong er lagt til");
	}
	
	protected void addSeries(String name) throws SQLException {
		PreparedStatement myStmt = myConn.prepareStatement("INSERT INTO serie (serienavn) VALUES (?)");
		myStmt.setString(1, name);
		myStmt.executeUpdate();
		myConn.commit();
		myStmt.close();
				
		System.out.println("Serie er lagt til");
	}
	
	protected void addReview(int brukerID, int screenplayID, String tekst, int rating) throws SQLException {
		PreparedStatement myStmt1 = myConn.prepareStatement("INSERT INTO anmeldelse (screenplayID, brukerID, tekst, rating) VALUES (?,?,?,?)");
		myStmt1.setInt(1, screenplayID);
		myStmt1.setInt(2, brukerID);
		myStmt1.setString(3, tekst);
		myStmt1.setInt(4,rating);
		myStmt1.executeUpdate();
		myConn.commit();
		myStmt1.close();
		
		System.out.println("Anmeldelse er lagt til");
	}
	
	protected static void addActor(int screenplayID, int personID, String role) throws SQLException {	
		PreparedStatement myStmtActor = myConn.prepareStatement("INSERT INTO skuespillerifilm(screenplayID, personID, rolle) VALUES (?,?,?)");
		myStmtActor.setInt(1, screenplayID);
		myStmtActor.setInt(2, personID);
		myStmtActor.setString(3, role);
		myStmtActor.executeUpdate();
		myStmtActor.close();
	}
	
	protected static void addWriter(int screenplayID, int personID) throws SQLException {
		PreparedStatement myStmtWriter = myConn.prepareStatement("INSERT INTO forfatterifilm VALUES (?,?)");
		myStmtWriter.setInt(1, screenplayID);
		myStmtWriter.setInt(2, personID);
		myStmtWriter.executeUpdate();
		myStmtWriter.close();
	}
	
	protected static void addDirector(int screenplayID, int personID) throws SQLException {
		PreparedStatement myStmtDirector = myConn.prepareStatement("INSERT INTO regissørifilm VALUES (?,?)");
		myStmtDirector.setInt(1, screenplayID);
		myStmtDirector.setInt(2, personID);
		myStmtDirector.executeUpdate();
		myStmtDirector.close();
	}

	protected static void addGenreToScreenplay(int genreID, int screenplayID) throws SQLException {	
		PreparedStatement myStmtActor = myConn.prepareStatement("INSERT INTO harSjanger VALUES (?,?)");
		myStmtActor.setInt(1, genreID);
		myStmtActor.setInt(2, screenplayID);
		myStmtActor.executeUpdate();
		myStmtActor.close();
	}
	
	protected void addScreenplay(
			String tittel, int mediumID, int companyID,
			int år, int dato, int lengde, int videoutg, String storyline,
			List<Integer> actorsID, List<Integer> writersID, 
			List<Integer> directorsID, String komponist, String fremfører, int sesongID, List<Integer> genresID, List<String> roles) throws SQLException {
		
			try {
			PreparedStatement myStmt = myConn.prepareStatement(
					"INSERT INTO Screenplay (tittel, mediumID, selskapID,"
					+ "produksjonsår, dato, lengde, videoutgivelse, Storyline, komponist, fremførelse, sesongID) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			myStmt.setString(1, tittel);
			myStmt.setInt(2, mediumID);
			myStmt.setInt(3, companyID);
			myStmt.setInt(4, år);
			myStmt.setInt(5, dato);
			myStmt.setInt(6, lengde);
			myStmt.setInt(7, videoutg);
			myStmt.setString(8, storyline);
			myStmt.setString(9, komponist);
			myStmt.setString(10, fremfører);
			myStmt.setInt(11, sesongID);
			myStmt.executeUpdate();
			
			// hent screenplay ID
			ResultSet rs = myStmt.getGeneratedKeys();
			int screenplayID = 0;
			if(rs.next())
			   screenplayID = rs.getInt(1);
			
			// legg til alle personer og sjangre for screenplay
			for (int i = 0; i < actorsID.size(); i++) {
				addActor(screenplayID, actorsID.get(i), roles.get(i));
			}
			for (int i = 0; i < writersID.size(); i++) {
				addWriter(screenplayID, writersID.get(i));
			}
			for (int i = 0; i < directorsID.size(); i++) {
				addDirector(screenplayID, directorsID.get(i));
			}
			for (int i = 0; i < genresID.size(); i++) {
				addGenreToScreenplay(genresID.get(i), screenplayID);
			}
	
			// utfør transaksjonene
			myConn.commit();
			System.out.println("Screenplay er lagt til");
			
			} catch (SQLException sqle) {
				sqle.printStackTrace();
				System.out.println("ERROR: Screenplay ble ikke lagt til!");
				myConn.rollback();
			}
	}
	
	protected void closeConnection() throws SQLException {
		myConn.close();
		System.out.println("Connection closed from AddDAO!");
	}

}