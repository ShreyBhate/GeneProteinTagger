package com.molcon.nlp.BioAnnotator.tagger;

import com.molcon.nlp.BioAnnotator.utils.MySQLConnection;

public class OntologyCreator {
	public static void main(String[] args) {
		MySQLConnection sql = new MySQLConnection("", "", "");
		
		try {
			sql.connectDatabase("protein_dict");
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			sql.closeConnections();
		}
		
	}
}
