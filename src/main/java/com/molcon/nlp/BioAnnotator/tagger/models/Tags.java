package com.molcon.nlp.BioAnnotator.tagger.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class Tags extends ArrayList<Tag> {
	private static final long serialVersionUID = 1L;
	
	public Tags() {
		super();
	}
	
    public Tags(Collection<Tag> c) {
        super();
        addAll(c);
    }
	
	public void setSection(String section) {
		for (Tag tag : this) tag.setSection(section); 
	}
	
	public Set<String> getAllTextForms() {
		ArrayList<String> allTextForms = new ArrayList<String>();
		for (Tag tag : this) allTextForms.add(tag.getTextForm());
		allTextForms.sort((String s1, String s2)-> s2.length() - s1.length());
		Set<String> set=  new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		set.addAll(allTextForms);
		return set;
	}

}
