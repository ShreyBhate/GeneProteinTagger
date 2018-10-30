package com.molcon.nlp.BioAnnotator.tagger.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class Tag {
	int start, end, sentenceStart, sentenceEnd;
	String textForm, preferedTerm, section;
	HashSet<String> labels;
	public HashSet<String> getLabels() {
		return labels;
	}

	public void setLabels(HashSet<String> labels) {
		this.labels = labels;
	}
	
	public void addLabel(String label) {
		if(this.labels == null) this.labels = new HashSet<String>();
		this.labels.add(label);
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	HashMap<String, String> ids;
	
	public Tag() {
		ids = new LinkedHashMap<String, String>();
	}
	
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	public int getSentenceStart() {
		return sentenceStart;
	}
	public int getSentenceEnd() {
		return sentenceEnd;
	}
	public String getTextForm() {
		return textForm;
	}
	public String getPreferedTerm() {
		return preferedTerm;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public void setSentenceStart(int sentenceStart) {
		this.sentenceStart = sentenceStart;
	}
	public void setSentenceEnd(int sentenceEnd) {
		this.sentenceEnd = sentenceEnd;
	}
	public void setTextForm(String textForm) {
		this.textForm = textForm;
	}
	public void setPreferedTerm(String preferedTerm) {
		this.preferedTerm = preferedTerm;
	}
	public HashMap<String, String> getIds() {
		return ids;
	}
	public void setIds(HashMap<String, String> ids) {
		this.ids = ids;
	}
	public void addId(String db, String code) {
		this.ids.put(db, code);
	}

	@Override
	public String toString() {
		return getTextForm() + ":" + getIds() + ":" + getStart() + ":" + getEnd()  + ":" + getSentenceStart() + ":" + getSentenceEnd();
	}

}
