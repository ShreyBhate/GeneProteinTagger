package com.molcon.nlp.BioAnnotator.utils;

import java.sql.ResultSet;

import com.molcon.nlp.BioAnnotator.tagger.models.Token;

public class MySqlUtil {
	
	private MySQLConnection mysql;
	
	public MySqlUtil() {
		Store store = new Store();
		
		mysql = new MySQLConnection(store.getProperty("db.host"), store.getProperty("db.database"),
				store.getProperty("db.user"));
		/*mysql = new MySQLConnection(store.getProperty("db.host"), store.getProperty("db.database"),
				store.getProperty("db.user"), store.getProperty("db.pwd"));*/
	}
	
/*	public Token getTokenForMcId(String mcid) {
		Token token = new Token();
		ResultSet rs = mysql.readData("SELECT * FROM protein_dict where mcid='" + mcid + "';");
		try {
			if (rs.next()) {
				token.setUniprotId(rs.getString(4));
				token.setOrganism(rs.getString(6));
				token.setSecUniprotId(rs.getString(9));
				token.setTaxaId(rs.getString(8));
				token.setEntrezId(rs.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return token;
	}*/
	
	public void close() {
		mysql.closeConnections();
	}
	
	public static void main(String[] args) {
		/*MySqlUtil util = new MySqlUtil();
		Token token = util.getTokenForMcId("PROT14723117");
		System.out.println(token.getTerm());
		System.out.println(token.getOrganism());
		System.out.println(token.getEntrezId());
		System.out.println(token.getTaxaId());*/
	}
}
