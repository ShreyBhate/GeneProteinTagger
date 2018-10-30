package com.molcon.nlp.BioAnnotator.tagger;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.TreeSet;

import com.molcon.nlp.BioAnnotator.tagger.models.Tag;
import com.molcon.nlp.BioAnnotator.tagger.models.Tags;

public class CcsChemicalTagger {
	private ChemTagger chemTagger;
	private CRFTagger crfTagger;
	
	public CcsChemicalTagger(String crfModelPath) {
		chemTagger = new ChemTagger();
		crfTagger = new CRFTagger(crfModelPath);
	}
	
	public Tags getTags(String text) {
		
		Tags oscarTags = new Tags();
		Tags crfTags = new Tags();
		
		try {
			oscarTags = chemTagger.getTags(text);
		} catch (Exception e) {
		}

		try {
			crfTags = crfTagger.getTags(text);
		} catch (Exception e) {
		}
		return getMergedTags(oscarTags, crfTags);
	}

	public Tags getTags(String text, PrintStream ps) {
		Tags oscarTags = chemTagger.getTags(text);
		ps.println("OSCAR");
		for (Tag tag : oscarTags) {
			ps.println(tag.getTextForm());
		}
		ps.println();
		Tags crfTags = crfTagger.getTags(text);
		ps.println("CRF");
		for (Tag tag : crfTags) {
			ps.println(tag.getTextForm());
		}
		ps.println();
		return getMergedTags(oscarTags, crfTags);
	}
	
	private Tags getMergedTags(Tags oscarTags, Tags crfTags) {
		Tags tags = new Tags();
		
		TreeSet<String> oscarTextForms = (TreeSet<String>) oscarTags.getAllTextForms(); // abc, def, xyz, mno, q
		TreeSet<String> crfTextForms = (TreeSet<String>) crfTags.getAllTextForms(); // abcde, def, wxyz, hi
		
		HashSet<String> finalSet = new HashSet<String>(oscarTextForms); // abc, def, xyz, mno, q
		finalSet.retainAll(crfTextForms); // def
		
		HashSet<String> traversed = new HashSet<String>();
		
		
		oscarTextForms.removeAll(finalSet); // abc, xyz, mno, q
		crfTextForms.removeAll(finalSet); // abcde, wxyz, hi
		
		for (String oscar : oscarTextForms) {
			for (String string : crfTextForms) {
				if(!oscar.equals(string) && oscar.contains(string)) {
					traversed.add(oscar); // abc, xyz
					finalSet.add(oscar); // def, abcde, wxyz
					break;
				} else if (!oscar.equals(string) && string.contains(oscar)) {
					traversed.add(oscar); // abc
					finalSet.add(string); // def, abcde
					break;
				}
			}
			crfTextForms.removeAll(finalSet); // wxyz, hi // hi
		}
		
		oscarTextForms.removeAll(traversed); // mno, q
		
		for (String string : oscarTextForms) {
			if(crfTagger.getTags(string).getAllTextForms().contains(string)) finalSet.add(string);
		}
		
		for (String string : finalSet) {
			Tag tag = new Tag();
			tag.setTextForm(string);
			tag.setPreferedTerm("MC_OSCAR_CM_TAG");
			tags.add(tag);
		}
		
		return tags;
	}
	
}
