package com.molcon.nlp.BioAnnotator.parser;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RePorterXmlParser extends DefaultHandler {
	
	private HashSet<String> allPaths;
	private LinkedList<String> tNodes;
//	private boolean isRequired = true;
	private StringBuffer dataBuffer;
	
	public RePorterXmlParser(String xmlPath) {
		try {
			allPaths = new HashSet<String>();
			tNodes = new LinkedList<String>();
			dataBuffer = new StringBuffer();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			SAXParser parser = factory.newSAXParser();
			parser.parse(new File(xmlPath), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		System.out.println(qName);
		
		tNodes.add(qName);
		
		/*dataBuffer.append("/");
		dataBuffer.append(qName);*/
		
		/*if (qName.toLowerCase().equalsIgnoreCase("dbReference")) {
			if (attributes.getValue("type") != null && attributes.getValue("id") != null) {
				if(attributes.getValue("type").equalsIgnoreCase("GeneID")) {
					uniprotObject.addGenBankId(attributes.getValue("id"));
				} else if(attributes.getValue("type").equalsIgnoreCase("HGNC")) {
					uniprotObject.addHgncId(attributes.getValue("id"));
				} else if(attributes.getValue("type").equalsIgnoreCase("Ensembl")) {
					uniprotObject.addEnsemblId(attributes.getValue("id"));
				} else if(attributes.getValue("type").equalsIgnoreCase("NCBI Taxonomy")) {
					uniprotObject.setTaxaId(attributes.getValue("id"));
				}
			}
		} else if (qName.toLowerCase().equalsIgnoreCase("organism")) {
			inOrganismTag = true;
		} else if (qName.toLowerCase().equalsIgnoreCase("name") && inOrganismTag) {
			isRequired = true;
		}*/
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		/*if (qName.toLowerCase().equalsIgnoreCase("name") && inOrganismTag) {
			uniprotObject.setTaxa(getBufferData());
		} else if (qName.toLowerCase().equalsIgnoreCase("organism")) {
			inOrganismTag = false;
		}*/
		allPaths.add(formPath());
//		clearBuffer();
//		isRequired = false;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		/*String data = new String(ch, start, length);
		if (isRequired) { 
			dataBuffer.append(StringEscapeUtils.escapeHtml4(data));
		}*/
	}

	public void clearBuffer() {
		dataBuffer.delete(0, dataBuffer.length());
	}

	public String getBufferData() {
		return dataBuffer.toString().trim();
	}
	
	public String formPath() {
		StringBuilder path = new StringBuilder("/");
		for (String string : tNodes) {
			path.append(string + "/");
		}
		return path.toString();
	}
	
	public static void main(String[] args) throws Exception {
		RePorterXmlParser parser = new RePorterXmlParser("/home/tanvi.h/Desktop/temp/RePORTER_PRJ_X_FY2016.xml");
		FileUtils.writeLines(new File("/home/tanvi.h/Desktop/temp/RePORTER_PRJ_X_FY2016.txt"), parser.allPaths);
		/*UniprotObject uo = new RePorterXmlParser("C9SR45").getUniprotObject();
		System.out.println(uo.getUniprotId());
		System.out.println(uo.getEnsemblId());
		System.out.println(uo.getGenBankId());
		System.out.println(uo.getHgncId());
		System.out.println(uo.getTaxa());
		System.out.println(uo.getTaxaId());*/
	}
}
