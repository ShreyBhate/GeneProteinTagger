package com.molcon.nlp.BioAnnotator.tagger;

import com.molcon.nlp.BioAnnotator.tagger.models.Tag;
import com.molcon.nlp.BioAnnotator.tagger.models.Tags;

import de.fhg.scai.bio.crf.user.LindaDisorder;
import de.fhg.scai.bio.crf.user.tools.Entity;

public class CRFTagger {
	public static void main(String[] args) throws Exception {
//		CRFTagger tagger = new CRFTagger("/home/tanvi.h/CRF/AL1_3Train.model");
		CRFTagger tagger = new CRFTagger("/home/tanvi.h/CRF/CRFModel/All/AL1_5Train.model");
		Tags tags = 
				tagger.getTags("A Fundamental Equation of State for 1,1,1,3,3-Pentafluoropropane");
		for (Tag tag : tags) {
			System.out.println(tag.getTextForm());
		}
	}
	
	private static int i = 1;
	private LindaDisorder crfTagger;
	
	public CRFTagger(String crfModelPath) {
		crfTagger = new LindaDisorder(crfModelPath);
	}
	
	public Tags getTags(String text) {
		Tags tags = new Tags();
		try {
			@SuppressWarnings("static-access")
			Entity[] chemEntities = crfTagger.tagline(String.valueOf(i++), text);
			for (Entity entity : chemEntities) {
				Tag tag = new Tag();
				tag.setPreferedTerm(entity.getClassName());
				tag.setTextForm(entity.getUntokenized());
				tags.add(tag);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return tags;	
	}
}
