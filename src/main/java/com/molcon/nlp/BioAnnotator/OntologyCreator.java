package com.molcon.nlp.BioAnnotator;

import java.io.FileWriter;
import java.sql.ResultSet;

import com.molcon.nlp.BioAnnotator.utils.MySQLConnection;
import com.molcon.nlp.BioAnnotator.utils.Store;


public class OntologyCreator {
	
	public static void main(String[] args) throws Exception {
		createGeneProteinOntology("/home/tanvi.h/Desktop/temp/temp-gp.ontology", "0.0", "Test");
	}
	
	private static void generateAndAddVariants(StringBuilder sb, String term) {
		if(term != null && term.trim().length() > 2) {
			sb.append("TM " + term + "\t@match=ci\n");
			if(term.matches(".+-.+"))  {
				sb.append("TM " + term.replaceAll("-", "") + "\t@match=ci\n");
				sb.append("TM " + term.replaceAll("-", " ") + "\t@match=ci\n");
			} else	if(term.matches("([^\\d]*)(\\d.*)")) {
				sb.append("TM " + term.replaceAll("([^\\d]*)(\\d+)", "$1-$2") + "\t@match=ci\n");
				sb.append("TM " + term.replaceAll("([^\\d]*)(\\d+)", "$1 $2") + "\t@match=ci\n");
			}
		}
	}
	
	private static void createGeneProteinOntology(String path, String ver, String comment) throws Exception {
		Store store = new Store();
		
		MySQLConnection mysql = new MySQLConnection(store.getProperty("db.host"), store.getProperty("db.user"), store.getProperty("db.pwd"));
		mysql.connectDatabase(store.getProperty("db.database"));
		FileWriter fr = new FileWriter(path);
		fr.write("# ErasmusMC ontology file\nVR " + ver + "\nON " + comment + "\n--\n");
		fr.flush();
		
		int conceptID = 1;
//		ResultSet rs = mysql.readData("SELECT * FROM Proteins ", 500);
		ResultSet rs = mysql.readData("SELECT * FROM `protein_dict` where orgName like 'Homo sapiens' ", 500);
		System.out.println(rs.getFetchSize());
		try {
			while (rs.next()) {
				System.out.println(conceptID);
				StringBuilder sb = new StringBuilder();
				sb.append("ID " + conceptID++ + "\n");
				sb.append("NA " + rs.getString("fullName")+ "\n");
				sb.append("TM " + rs.getString("fullName")+ "\t@match=ci\n");
				
				String term = rs.getString("swissName");
				if(term != null) {
					for(String syn : term.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}
				/*term = rs.getString("AllergenName");
				
				if(term != null) {
					for(String syn : term.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}*/
				
				String synonyms = rs.getString("synonyms");
				if(synonyms != null) {
					for(String syn : synonyms.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}
				/*synonyms = rs.getString("GeneNames");
				if(synonyms != null) {
					for(String syn : synonyms.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}*/
				sb.append("DB UMLS_MCID_" + rs.getString("mcid")+ "\n");
				sb.append("DB UMLS_UNIPROT_" + rs.getString("accession")+ "\n");
				sb.append("--\n");
				
				fr.write(sb.toString());
				fr.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rs.close();
		fr.close();
		mysql.closeConnections();
	}
	
	
/*	private static void createGeneProteinOntology(String path, String ver, String comment) throws Exception {
		Store store = new Store();
		
		MySQLConnection mysql = new MySQLConnection(store.getProperty("db.host"), store.getProperty("db.user"), store.getProperty("db.pwd"));
		mysql.connectDatabase(store.getProperty("db.database"));
		FileWriter fr = new FileWriter(path);
		fr.write("# ErasmusMC ontology file\nVR " + ver + "\nON " + comment + "\n--\n");
		fr.flush();
		
		int conceptID = 1;
//		ResultSet rs = mysql.readData("SELECT * FROM Proteins ", 500);
		ResultSet rs = mysql.readData("SELECT * FROM `protein_dict` where orgName like 'Homo sapiens' ", 500);
		System.out.println(rs.getFetchSize());
		try {
			while (rs.next()) {
				System.out.println(conceptID);
				StringBuilder sb = new StringBuilder();
				sb.append("ID " + conceptID++ + "\n");
				sb.append("NA " + rs.getString("ProteinName")+ "\n");
				sb.append("TM " + rs.getString("ProteinName")+ "\t@match=ci\n");
				
				String term = rs.getString("RecommendedName");
				if(term != null) {
					for(String syn : term.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}
				term = rs.getString("AllergenName");
				
				if(term != null) {
					for(String syn : term.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}
				
				String synonyms = rs.getString("AlternativeNames");
				if(synonyms != null) {
					for(String syn : synonyms.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}
				synonyms = rs.getString("GeneNames");
				if(synonyms != null) {
					for(String syn : synonyms.split("\\|\\|", -1)) {
						generateAndAddVariants(sb, syn);
					}
				}
				sb.append("DB UMLS_MCID_" + rs.getInt("ProteinId")+ "\n");
				sb.append("DB UMLS_UNIPROT_" + rs.getString("AccessionPrimary")+ "\n");
				sb.append("--\n");
				
				fr.write(sb.toString());
				fr.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rs.close();
		fr.close();
		mysql.closeConnections();
	}
	*/
}
