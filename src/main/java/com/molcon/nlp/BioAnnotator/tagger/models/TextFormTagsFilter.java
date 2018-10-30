package com.molcon.nlp.BioAnnotator.tagger.models;

import com.google.common.base.Predicate;

public class TextFormTagsFilter implements Predicate<Tag> {
	
	private String cid;
	
	public TextFormTagsFilter(String cid) {
		this.cid = cid;
	}
	
	@Override
	public boolean apply(Tag t) {
		return t.getIds().containsKey("TID") && t.getIds().get("TID").equals(cid);
	}
}
