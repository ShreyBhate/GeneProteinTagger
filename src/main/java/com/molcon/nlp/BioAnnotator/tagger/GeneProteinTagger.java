package com.molcon.nlp.BioAnnotator.tagger;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.molcon.nlp.BioAnnotator.parser.DocumentA;
import com.molcon.nlp.BioAnnotator.parser.UniprotXmlParser;
import com.molcon.nlp.BioAnnotator.tagger.models.Tag;
import com.molcon.nlp.BioAnnotator.tagger.models.Tags;
import com.molcon.nlp.BioAnnotator.tagger.models.Token;
import com.molcon.nlp.BioAnnotator.tagger.models.Tokens;
import com.molcon.nlp.BioAnnotator.tagger.models.UniprotObject;

public class GeneProteinTagger extends PeregrineTagger {
	
	private HashMap<String, UniprotObject> downloadedUniprots;
	
	public GeneProteinTagger(String ontologyFilePath, String lvgPath)
			throws IOException {
		super(ontologyFilePath, lvgPath);
		downloadedUniprots = new LinkedHashMap<String, UniprotObject>();
	}
	
	public GeneProteinTagger (String ontologyFile, String negSynFilePath, String negTermFilePath, String lvgPath)
			throws IOException {
		super(ontologyFile, negSynFilePath, negTermFilePath, lvgPath);
		downloadedUniprots = new LinkedHashMap<String, UniprotObject>();
	}
	
	public Tags getTags(String text) {
		return super.getTags(text);
	}
	
	public HashSet<Token> getTokens(DocumentA document) {
		Tokens tokens = new Tokens();
		Tags tags = new Tags();
		tags.addAll(getTags(document.getTitle()));
		tags.addAll(getTags(document.getContent()));
		
		for (Tag tag : tags) {
			Token token = new Token();
			
			String accession = tag.getIds().get("UNIPROT");
			UniprotObject uniprotObject;
			
			if(downloadedUniprots.containsKey(accession)) uniprotObject = downloadedUniprots.get(accession);
			else {
				uniprotObject = new UniprotXmlParser(accession).getUniprotObject();
				downloadedUniprots.put(accession, uniprotObject);
			}
			token.setGenBankId(String.join("; ", uniprotObject.getGenBankId()));
			token.setHgncId(String.join("; ", uniprotObject.getHgncId()));
			token.setEnsemblId(String.join("; ", uniprotObject.getEnsemblId()));
			token.setTerm(tag.getPreferedTerm());
			token.addTextForm(tag.getTextForm());
			token.setUniprotId(tag.getIds().get("UNIPROT"));
			token.setOrganism(uniprotObject.getTaxa());
			token.setTaxaId(uniprotObject.getTaxaId());
			tokens.add(token);
		}
		return new HashSet<Token>(tokens);
	}
}
