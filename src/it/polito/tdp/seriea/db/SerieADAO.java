package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Insieme;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Team> listaSquadreMap(Map <String,Team> idMap) {
		String sql = "SELECT team FROM teams";

		List<Team> result = new ArrayList<>();

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t = new Team(res.getString("team")) ;
				idMap.put(t.getTeam(), t) ;
				result.add(t);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams(String stagione) {
		String sql = "SELECT DISTINCT m.HomeTeam AS squadra " + 
				"FROM seasons s, matches m " + 
				"WHERE s.description = ? AND s.season = m.Season " + 
				"GROUP BY m.HomeTeam " ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, stagione);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> caricaAnnate() {
		String sql = "SELECT description " + 
				"FROM seasons " ;
		
		List<String> result = new ArrayList<String>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String s = res.getString("description");
				result.add(s);
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Team> getSquadreForAnno(String stagione, Map<String, Team> idMap) {
		String sql = "SELECT DISTINCT m.HomeTeam AS squadra " + 
				"FROM seasons s, matches m " + 
				"WHERE s.description = ? AND s.season = m.Season " + 
				"GROUP BY m.HomeTeam " ;
		
		List<Team> result = new ArrayList<Team>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, stagione);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team squadra = idMap.get(res.getString("squadra"));
				result.add(squadra);
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<Insieme> getPuntiClassifica(String stagione, Map<String, Team> idMap) {
		String sql = "SELECT m.HomeTeam AS casa, m.AwayTeam AS ospite, m.FTR AS risultato " + 
				"FROM matches m, seasons s " + 
				"WHERE s.season = m.Season AND s.description = ? ";
		
		List<Insieme> list = new ArrayList<Insieme>();
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, stagione);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team t1 = idMap.get(res.getString("casa"));
				Team t2  = idMap.get(res.getString("ospite"));
				
				Insieme i = new Insieme(t1, t2, res.getString("risultato"));
				list.add(i);
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}


}
