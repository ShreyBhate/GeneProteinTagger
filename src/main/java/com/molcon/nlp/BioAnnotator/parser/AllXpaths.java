package com.molcon.nlp.BioAnnotator.parser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AllXpaths {
	
	public static void main(String[] args) throws Exception {
		File stocks = new File("/home/tanvi.h/Desktop/temp/RePORTER2016/72363.txt");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
		Document doc = dBuilder.parse(stocks);
		
		AllXpaths allXpaths = new AllXpaths();
		allXpaths.run(doc);
	}
	
	public void run(Document doc) {
		try {
		    XPath xpath = XPathFactory.newInstance().newXPath();
		    // get all nodes in the document
		    NodeList nList = (NodeList) xpath.evaluate("//*", doc.getDocumentElement(), XPathConstants.NODESET);

		    for(int i=0;i<nList.getLength();i++) {
		        if(nList.item(i).getNodeType() == Node.ELEMENT_NODE)
		            System.out.println(getElementXPath((Element)nList.item(i), doc.getDocumentElement()));
		    }
		} catch (XPathExpressionException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
	


	 /**
	 * Finds the xPath relative to the given node, the relativeTo should always be a parent of elt
	 * @param elt 
	 * @param relativeTo should be a parent of elt, if it isnt the path from the document root will be returned
	 * @return
	 */
	public String getElementXPath(Element elt, Element relativeTo) {
	    String path = ""; 

	    do {
	        String xname = elt.getLocalName() + "[" + getElementIndex(elt) + "]";
	        path = "/" + xname + path;

	        if(elt.getParentNode() != null && elt.getParentNode().getNodeType() == Element.ELEMENT_NODE)
	            elt = (Element) elt.getParentNode();
	        else
	            elt = null;
	    } while(elt != null && !elt.equals(relativeTo));

	    return path;                            
	}
	
	private int getElementIndex(Element original) {
	    int count = 1;

	    for (Node node = original.getPreviousSibling(); node != null; node = node.getPreviousSibling()) {
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            Element element = (Element) node;
	            if (element.getLocalName().equals(original.getLocalName()) && 
	                    (element.getNamespaceURI() == original.getNamespaceURI() || (element.getNamespaceURI() != null && element.getNamespaceURI().equals(original.getNamespaceURI())))) {
	                count++;
	            }
	        }
	    }

	    return count;
	}

}
