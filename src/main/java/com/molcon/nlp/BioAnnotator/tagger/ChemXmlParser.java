package com.molcon.nlp.BioAnnotator.tagger;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nu.xom.converters.DOMConverter;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChemXmlParser {
	private Document w3cDoc;
	private String allowedPrefixes = "(crystal.*|alloy.*|.* nano.*|compound.*|.* complex.*|composition|ligand.*|.*film.*|molecule.*|derivative.*|.* ester.*|acid.*|substrate.*)";
	
	public ChemXmlParser(nu.xom.Document nuDoc) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			w3cDoc = DOMConverter.convert(nuDoc, impl);
		} catch (Exception e) {
			e.printStackTrace();
			w3cDoc = null;
		}
	}
	
	public ArrayList<String> getParsedTags() {
		ArrayList<String> chemText = new ArrayList<String>();;
		
		if(w3cDoc != null) {
			NodeList molecules = w3cDoc.getElementsByTagName("MOLECULE");
			for(int i = 0; i < molecules.getLength(); i++) {
				Node molecule = molecules.item(i);
				NodeList molChildren = molecule.getChildNodes();
				String chem = "";
				for(int j = 0; j < molChildren.getLength(); j++) {
					Node molChild = molChildren.item(j);
					if(molChild.getNodeName().matches("(OSCARCM|NN-CHEMENTITY|NN-STATE|JJ-CHEM|OSCAR-RN||JJ)")) {
						chem += getValue(molChild);
					}
				}
//				if(!chem.trim().matches("[A-Z0-9]+")) {
				if(chem.trim().matches(".*[a-z]+.*")) {
					Node sibling = molecule.getNextSibling();
					String suffix = "";
					while(sibling != null) {
						if(sibling.getNodeName().startsWith("NN")) {
							suffix += " " +  sibling.getTextContent();
							sibling = sibling.getNextSibling();
						} else break;
					}
					if(suffix.trim().isEmpty() || suffix.trim().toLowerCase().matches(allowedPrefixes)) {
						chem += suffix;
						chemText.add(chem
								.replaceAll("\\s-", "-").replaceAll("-\\s", "-")
								.replaceAll("\\s/", "/").replaceAll("/\\s", "/")
								.replaceAll("\\(\\s", "(").replaceAll("\\s\\)", ")")
								.replaceAll("\\s+", " ")
								.trim());
					}
					
				}
				
			}
		}
		return chemText;
	}
	
	private String getValue(Node ele) {
		String chem = "";
		NodeList children = ele.getChildNodes();
		for (int j = 0; j < children.getLength(); j++) {
			chem += children.item(j).getTextContent() + " ";
		}
		return chem;
	}
}
