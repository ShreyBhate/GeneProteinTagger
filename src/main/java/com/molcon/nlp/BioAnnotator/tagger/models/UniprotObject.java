package com.molcon.nlp.BioAnnotator.tagger.models;

import java.util.HashSet;

public class UniprotObject {
	
	private String uniprotId;
	
	private HashSet<String> genBankId, hgncId, ensemblId;
	private String taxa, taxaId;
	
	public UniprotObject(String accession) {
		setUniprotId(accession);
		genBankId = new HashSet<String>();
		hgncId = new HashSet<String>();
		ensemblId = new HashSet<String>();
	}
	
	public HashSet<String> getGenBankId() {
		return genBankId;
	}

	public void setGenBankId(HashSet<String> genBankId) {
		this.genBankId = genBankId;
	}

	public void addGenBankId(String genBankId) {
		this.genBankId.add(genBankId);
	}

	public HashSet<String> getHgncId() {
		return hgncId;
	}

	public void setHgncId(HashSet<String> hgncId) {
		this.hgncId = hgncId;
	}

	public void addHgncId(String hgncId) {
		this.hgncId.add(hgncId);
	}

	public HashSet<String> getEnsemblId() {
		return ensemblId;
	}

	public void setEnsemblId(HashSet<String> ensemblId) {
		this.ensemblId = ensemblId;
	}

	public void addEnsemblId(String ensemblId) {
		this.ensemblId.add(ensemblId);
	}

	public void setUniprotId(String uniprotId) {
		this.uniprotId = uniprotId;
	}

	public String getUniprotId() {
		return uniprotId;
	}

	public String getTaxa() {
		return taxa;
	}

	public void setTaxa(String taxa) {
		this.taxa = taxa;
	}

	public String getTaxaId() {
		return taxaId;
	}

	public void setTaxaId(String taxaId) {
		this.taxaId = taxaId;
	}
	
}
