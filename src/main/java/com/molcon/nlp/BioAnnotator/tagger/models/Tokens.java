package com.molcon.nlp.BioAnnotator.tagger.models;

import java.util.ArrayList;

public class Tokens extends ArrayList<Token> {
	private static final long serialVersionUID = 1L;
	
	public Token getTokenByName(String term) {
		for (Token token : this) {
			if(token.getTerm().equalsIgnoreCase(term)) {
				boolean flag = remove(token);
				if(flag) return token;
			}
		}
		return null;
	}

	public Token getTokenByUniprotId(String id) {
		for (Token token : this) {
			if(token.getUniprotId().equals(id)) {
				boolean flag = remove(token);
				if(flag) return token;
			}
		}
		return null;
	}
}
