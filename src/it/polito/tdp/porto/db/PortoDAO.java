package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author> getAllAuthorsDiArticoli(){
		
		List<Author> autori = new ArrayList<>();
		String sql = "SELECT a.id, a.lastname, a.firstname " + 
				"FROM author a, creator c, paper p " + 
				"WHERE a.id=c.authorid " + 
				"AND c.eprintid = p.eprintid " + 
				"AND p.`type` = 'article' " + 
				"GROUP BY id " + 
				"ORDER BY lastname, firstname" ;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Author autore = new Author(rs.getInt("a.id"), rs.getString("a.lastname"), rs.getString("a.firstname"));
				autori.add(autore);
			}

			return autori;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	
	
	public List<Paper> getAllArticoli(Map<Integer, Paper> articoliMap){
		
		List<Paper> articoli = new ArrayList<>();
		String sql = "SELECT *  " + 
				"FROM paper p " + 
				"WHERE p.`type` = 'article'";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);			

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Paper articolo = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"), rs.getString("publication"), "article", rs.getString("types"));
				articoli.add(articolo);
				articoliMap.put(rs.getInt("eprintid"), articolo);
		}

			return articoli;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}
	
	public List<Author> getCoautoriList(Author author){
		
		String sql = "SELECT distinct a.id, a.lastname, a.firstname " + 
				"from creator c1, creator c2, author a " + 
				"WHERE c1.eprintid = c2.eprintid " + 
				"AND c2.authorid = a.id " + 
				"AND c1.authorid = ? " + 
				"AND a.id <> c1.authorid " + 
				"ORDER BY lastname";
		
		List<Author> coautori = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setInt(1, author.getId());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore = new Author(rs.getInt("a.id"), rs.getString("a.lastname"), rs.getString("a.firstname"));
				coautori.add(autore);
				
			}
			conn.close();

			return coautori;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public Paper getArticoloComune(Author a1, Author a2) {
		
		String sql = "SELECT p.eprintid, title, issn, publication, p.`type`, types " + 
				"FROM creator c1, creator c2, paper p " + 
				"WHERE c1.eprintid = c2.eprintid " + 
				"AND c2.eprintid = p.eprintid " + 
				"AND c1.authorid = ? " + 
				"AND c2.authorid = ? " + 
				"LIMIT 1";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());

			ResultSet rs = st.executeQuery();

			Paper p = null;
			if(rs.next()) {
				p = new Paper(rs.getInt("p.eprintid"), 
						rs.getString("title"),
						rs.getString("issn"), 
						rs.getString("publication"), 
						rs.getString("type"), 
						rs.getString("types"));
			}
				
			
			conn.close();

			return p;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
			
			
		}
		
	}
}