package com.molcon.nlp.BioAnnotator.tagger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.molcon.nlp.BioAnnotator.tagger.models.Tag;
import com.molcon.nlp.BioAnnotator.tagger.models.Tags;
import com.molcon.nlp.BioAnnotator.utils.TermUtils;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import uk.ac.cam.ch.wwmm.chemicaltagger.ChemistryPOSTagger;
import uk.ac.cam.ch.wwmm.chemicaltagger.ChemistrySentenceParser;
import uk.ac.cam.ch.wwmm.chemicaltagger.POSContainer;
import uk.ac.cam.ch.wwmm.chemicaltagger.Utils;

public class ChemTagger {
	
	ChemistryPOSTagger chemistryPOSTagger;
	
	public ChemTagger() {
		chemistryPOSTagger = ChemistryPOSTagger.getDefaultInstance();
	}
	
	public Tags getTags(String text) {
		text = text.endsWith(".") ? text : text + "."; 
		Document doc = getChemTaggerXml(text);
		Tags tags = new Tags();
		
		ArrayList<String> chems = new ChemXmlParser(doc).getParsedTags(); 
		for (String textForm : chems) {
			if(TermUtils.isComplete(textForm)) {
				Tag tag = new Tag();
				tag.setTextForm(textForm);
				tag.setPreferedTerm("MC_OSCAR_CM_TAG");
				tags.add(tag);
			}
		}
		return tags;
	}
	
	private Document getChemTaggerXml(String text) {
		POSContainer posContainer = chemistryPOSTagger.runTaggers(text);
		ChemistrySentenceParser chemistrySentenceParser = new ChemistrySentenceParser(posContainer);
		chemistrySentenceParser.parseTags();
		return chemistrySentenceParser.makeXMLDocument();
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private List<String> parseMolecules(Document doc) {
		ArrayList<String> temp = new ArrayList<String>();
		Nodes nodes = doc.query("//MOLECULE");
		for (int i = 0; i < nodes.size(); i++) {
			Node ele = nodes.get(i);
			String chem = "";
			for (int j = 0; j < ele.getChildCount(); j++) {
				Node eleChild = ele.getChild(j);
				if (eleChild instanceof Element) {
					Element tempEle = (Element) eleChild;
					if(tempEle.getQualifiedName().matches("(OSCARCM|NN-CHEMENTITY|NN-STATE|JJ-CHEM|OSCAR-RN)")) {
						chem += getValue(eleChild);
					}
				}
			}

			temp.add(chem.replaceAll("\\s-", "-").replaceAll("-\\s", "-").replaceAll("\\s+", " ").trim());
			}
		return temp;
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private HashSet<String> parseUnnamedMolecules(Document doc) {
		HashSet<String> temp = new HashSet<String>();
		Nodes nodes = doc.query("//UNNAMEDMOLECULE");
		for (int i = 0; i < nodes.size(); i++) {
			Node ele = nodes.get(i);
			temp.add(getValue(ele));
			}
		return temp;
	}
	
	private String getValue(Node ele) {
		String chem = "";
		for (int j = 0; j < ele.getChildCount(); j++) {
			chem += ele.getChild(j).getValue() + " ";
		}
		return chem;
	}
	
	public void tagAndPrint(String text, String outputPath) {
	      POSContainer posContainer = chemistryPOSTagger.runTaggers(text);
	      ChemistrySentenceParser chemistrySentenceParser = new ChemistrySentenceParser(posContainer);
	      chemistrySentenceParser.parseTags();
	      Document doc = chemistrySentenceParser.makeXMLDocument();
	      Utils.writeXMLToFile(doc, outputPath);
	}
	
	public static void main(String[] args) {
	      String text = "YBa2Cu3O7−x thin film over 3 in. substrate using off‐axis excimer laser deposition";
	      
	      ChemTagger chemTagger = new ChemTagger();
	      Tags tags = chemTagger.getTags(text);
	      for (Tag tag : tags) {
			System.out.println(tag.getPreferedTerm() + "\t" + tag.getTextForm());
	      }
	      
//	      chemTagger.tagAndPrint(text, "/home/tanvi.h/Desktop/temp/t.xml");
	   }
}
