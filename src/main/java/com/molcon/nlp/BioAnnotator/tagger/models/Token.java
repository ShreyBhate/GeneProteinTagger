package com.molcon.nlp.BioAnnotator.tagger.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;


public class Token implements Comparable<Token> {
	private String term, uniprotId, secUniprotId, organism, taxaId, entrezId;
	private int frequency;
	private HashSet<String> textForms;
	private String genBankId, hgncId, ensemblId;
	
	public String getGenBankId() {
		return genBankId;
	}
	public void setGenBankId(String genBankId) {
		this.genBankId = genBankId;
	}
	public String getHgncId() {
		return hgncId;
	}
	public void setHgncId(String hgncId) {
		this.hgncId = hgncId;
	}
	public String getEnsemblId() {
		return ensemblId;
	}
	public void setEnsemblId(String ensemblId) {
		this.ensemblId = ensemblId;
	}
	public HashSet<String> getTextForms() {
		return textForms;
	}
	public void setTextForms(HashSet<String> textForms) {
		this.textForms = textForms;
	}
	public void addTextForm(String textForm) {
		if(textForms == null) textForms = new HashSet<String>();
		textForms.add(textForm);
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getUniprotId() {
		return uniprotId;
	}
	public void setUniprotId(String uniprotId) {
		this.uniprotId = uniprotId;
	}
	public String getSecUniprotId() {
		return secUniprotId;
	}
	public void setSecUniprotId(String secUniprotId) {
		this.secUniprotId = secUniprotId;
	}
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
	}
	public String getTaxaId() {
		return taxaId;
	}
	public void setTaxaId(String taxaId) {
		this.taxaId = taxaId;
	}
	public String getEntrezId() {
		return entrezId;
	}
	public void setEntrezId(String entrezId) {
		this.entrezId = entrezId;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public void addFrequency(int n) {
		setFrequency(getFrequency() + n);
	}
	@Override
	public int compareTo(Token o) {
		return uniprotId.compareTo(o.getUniprotId());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(!(o instanceof Token)) return false;
		return uniprotId.equalsIgnoreCase(((Token) o).getUniprotId());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
        result += 31 * result + getUniprotId().hashCode();
        for (String string : textForms) {
        	result += 31 * result + string.hashCode();
		}
        return result;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("/home/tanvi.h/Desktop/genes_proteins.ontology"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("/home/tanvi.h/Desktop/genes_proteins_temp.ontology"));
		
		String line = "";
		
		while((line = br.readLine()) != null) {
			/*if(line.startsWith("TM ") && !line.contains("@match=")) {
				bw.write(line + "\t@match=ci,no\n");
			} else {
				bw.write(line + "\n");
			}
			
			if(line.startsWith("TM")) {
				String term = line.split("\t")[0].replace("TM ", "");
				if(term.contains("-")) {
					System.out.println(term);
				}
			}*/
			
			if(line.startsWith("TM ")) {
				String term = line.split("\t")[0].replace("TM ", "");
				if(term.contains("-")) {
					if(!line.contains("@match=")) {
						bw.write("TM " + term + "\t@match=ci,no\n");
						bw.write("TM " + term.replaceAll("-", "") + "\t@match=ci,no\n");
					} else {
						bw.write("TM " + term + "\n");
						bw.write("TM " + term.replaceAll("-", "") + "\n");
					}
				} else {
					if(!line.contains("@match=")) {
						bw.write(line + "\t@match=ci,no\n");
					} else {
						bw.write(line + "\n");
					}
				}
			} else {
				bw.write(line + "\n");
			}
		}
		
		br.close();
		bw.close();
	}

}
