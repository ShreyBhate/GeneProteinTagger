package com.molcon.nlp.BioAnnotator.parser;

import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

public class UniprotSplitter {
	public static void main(String[] args) throws Exception {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader xsr = xif.createXMLStreamReader(new FileReader("/home/tanvi.h/Desktop/temp/RePORTER_PRJ_X_FY2016.xml"));
//        xsr.nextTag(); // Advance to entry element
        xsr.next();

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        
        File file = null;
/*        while(xsr.hasNext()) {
        	int next = xsr.next();
        	
        	if(next == XMLStreamConstants.START_ELEMENT) {
        		StAXSource stAXSource = new StAXSource(xsr);
        		System.out.println(xsr.getName().getLocalPart());
        		if(xsr.getName().getLocalPart().equals("entry")) {
        			String accession = stAXSource.getXMLStreamReader().getVersion();
        			file = new File("/home/tanvi.h/BioDBs/Uniprot-split/" + accession + ".xml");
        			System.out.println(accession + "\t" + file.getAbsolutePath());
        			t.transform(stAXSource, new StreamResult(file));
        			continue;
        		} else if(xsr.getName().getLocalPart().equals("accession")) {
        			String accession = stAXSource.getXMLStreamReader().getElementText();
        			System.out.println(accession);
        			file.renameTo(new File("/home/tanvi.h/BioDBs/Uniprot-split/" + accession + ".xml"));
        			File file = new File("/home/tanvi.h/BioDBs/Uniprot-split/" + accession + ".xml");
        			System.out.println(accession + "\t" + file.getAbsolutePath());
        			t.transform(new StAXSource(entryNode), new StreamResult(file));
        		}
        		
        	}
        }*/
        
        while(xsr.hasNext()) {
        	int next = xsr.next();
        	
        	if(next == XMLStreamConstants.START_ELEMENT) {
        		StAXSource stAXSource = new StAXSource(xsr);
        		if(xsr.getName().getLocalPart().equals("entry")) {
        			String accession = "temp";
        			file = new File("/home/tanvi.h/Desktop/temp/RePORTER2016/" + accession + ".xml");
        			System.out.println(accession + "\t" + file.getAbsolutePath());
        			t.transform(stAXSource, new StreamResult(file));
        			xsr.nextTag();
//        			xsr.nextTag();
        			if(xsr.getName().getLocalPart().equals("APPLICATIO_ID")) {
            			accession = stAXSource.getXMLStreamReader().getElementText();
            			System.out.println(accession);
            			file.renameTo(new File("/home/tanvi.h/Desktop/temp/RePORTER2016/" + accession + ".xml"));
            		}
        		} 
        		
        	}
        }
	}
}
