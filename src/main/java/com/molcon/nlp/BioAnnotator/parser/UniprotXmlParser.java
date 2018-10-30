package com.molcon.nlp.BioAnnotator.parser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.molcon.nlp.BioAnnotator.tagger.models.UniprotObject;

public class UniprotXmlParser extends DefaultHandler {
	
	private boolean inOrganismTag = false, isRequired = false;
	private StringBuffer dataBuffer;
	
	private UniprotObject uniprotObject;

	public UniprotObject getUniprotObject() {
		return uniprotObject;
	}

	public void setUniprotObject(UniprotObject uniprotObject) {
		this.uniprotObject = uniprotObject;
	}
	
	public UniprotXmlParser(String uniprotAccession) {
		try {
			dataBuffer = new StringBuffer();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			SAXParser parser = factory.newSAXParser();
			uniprotObject = new UniprotObject(uniprotAccession);
			String uri = "http://www.uniprot.org/uniprot/" + uniprotAccession + ".xml";
			parser.parse(uri, this);
		} catch (Exception e) {
			System.err.println(uniprotAccession);
			e.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (qName.toLowerCase().equalsIgnoreCase("dbReference")) {
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
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.toLowerCase().equalsIgnoreCase("name") && inOrganismTag) {
			uniprotObject.setTaxa(getBufferData());
		} else if (qName.toLowerCase().equalsIgnoreCase("organism")) {
			inOrganismTag = false;
		}
		clearBuffer();
		isRequired = false;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data = new String(ch, start, length);
		if (isRequired) { 
			dataBuffer.append(StringEscapeUtils.escapeHtml4(data));
		}
	}

	public void clearBuffer() {
		dataBuffer.delete(0, dataBuffer.length());
	}

	public String getBufferData() {
		return dataBuffer.toString().trim();
	}
	
	public static void main(String[] args) {
		UniprotObject uo = new UniprotXmlParser("C9SR45").getUniprotObject();
		System.out.println(uo.getUniprotId());
		System.out.println(uo.getEnsemblId());
		System.out.println(uo.getGenBankId());
		System.out.println(uo.getHgncId());
		System.out.println(uo.getTaxa());
		System.out.println(uo.getTaxaId());
	}
}
