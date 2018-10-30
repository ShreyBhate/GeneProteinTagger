package com.molcon.nlp.BioAnnotator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.molcon.nlp.BioAnnotator.parser.DocumentA;
import com.molcon.nlp.BioAnnotator.parser.DocumentsA;
import com.molcon.nlp.BioAnnotator.parser.HtmlParserA;
import com.molcon.nlp.BioAnnotator.tagger.CcsChemicalTagger;
import com.molcon.nlp.BioAnnotator.tagger.models.Tag;
import com.molcon.nlp.BioAnnotator.tagger.models.Tags;

public class ChemTaggerController {
	public static void main(String[] args) throws Exception {
		System.setOut(new PrintStream("/home/tanvi.h/Desktop/chemprottrc_26.csv"));
		
		//TODO include crf model in the project
		CcsChemicalTagger chemicalTagger = new CcsChemicalTagger("/home/tanvi.h/git/MC-Miner/src/main/resources/data/crf/AL1_5Train.model");
		
		List<String> urls = new ArrayList<String>();
		urls.add("http://ubio.bioinfo.cnio.es/people/fleitner/chemprottrc_26.html");
		
		HtmlParserA parserA = new HtmlParserA();
		
		System.out.println("Url" + "\t"
				+ "Abstract ID" + "\t"
				+ "Chemical"
				);
		
		for (String url : urls) {
			DocumentsA allDocumentsA = parserA.getDocumentsFromUrl(url);
			System.err.println(url + "\t" + allDocumentsA.size());
			for (DocumentA documentA : allDocumentsA) {
				System.err.println(documentA.getId());
				Tags tags = chemicalTagger.getTags(documentA.getTitle());
				tags.addAll(chemicalTagger.getTags(documentA.getContent()));
				if(tags.size() > 0) {
					for (Tag tag : tags) {
						System.out.println(url + "\t"
								+ documentA.getId() + "\t"
								+ tag.getTextForm()
								);
					}
				} else {
						System.out.println(url + "\t"
								+ documentA.getId() + "\t" + "--"
								);
				}
				

			}
		}
	}
}
