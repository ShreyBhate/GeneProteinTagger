package com.molcon.nlp.BioAnnotator;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.HashSet;

import com.molcon.nlp.BioAnnotator.parser.DocumentA;
import com.molcon.nlp.BioAnnotator.parser.DocumentsA;
import com.molcon.nlp.BioAnnotator.parser.HtmlParserA;
import com.molcon.nlp.BioAnnotator.tagger.GeneProteinTagger;
import com.molcon.nlp.BioAnnotator.tagger.models.Token;
import com.molcon.nlp.BioAnnotator.utils.Store;

public class Controller {
	
/*	public static void main(String[] args) throws Exception {
		System.setOut(new PrintStream("/home/tanvi.h/Desktop/gene-protein-tagger-bug-test.csv"));
		GeneProteinTagger geneProteinTagger = new GeneProteinTagger("/home/tanvi.h/Desktop/temp/temp-gp.ontology",
				"/home/tanvi.h/git/GeneProteinTagger/src/main/resources/ontology/gene_protein/neg_syn_genes_proteins.list",
				"/home/tanvi.h/git/GeneProteinTagger/src/main/resources/ontology/gene_protein/neg_term_genes_proteins.list",
				"/home/tanvi.h/Peregrine/lvg2013/data/config/");
		System.err.println("Ontology loaded ..");
		List<String> urls = new ArrayList<String>();
		urls.add("http://ubio.bioinfo.cnio.es/people/fleitner/chemprottrg_8.html");
//		urls.add("http://ubio.bioinfo.cnio.es/people/fleitner/abstracts_gr1.html");
//		urls.add("http://ubio.bioinfo.cnio.es/people/fleitner/abstracts_gr2.html");
//		urls.add("http://ubio.bioinfo.cnio.es/people/fleitner/abstracts_gr3.html");
//		urls.add("http://ubio.bioinfo.cnio.es/people/fleitner/abstracts_gr4.html");
//		
		HtmlParserA parserA = new HtmlParserA();
		
		System.out.println("Url" + "\t"
				+ "Abstract ID" + "\t"
				+ "Entity Name" + "\t"
				+ "Text Form Found" + "\t"
				+ "Uniprot Id" + "\t"
				+ "Genbank Id" + "\t"
				+ "HGNC Id" + "\t"
				+ "Ensembl Ids" + "\t"
				+ "Taxa[NCBI Id]" + "\t"
				+ "Secondary Uniprot Ids" + "\t"
				);
		
		for (String url : urls) {
			DocumentsA allDocumentsA = parserA.getDocumentsFromUrl(url);
			System.err.println(url + "\t" + allDocumentsA.size());
			for (DocumentA documentA : allDocumentsA) {
				System.err.println(documentA.getId());
				HashSet<Token> tokens = geneProteinTagger.getTokens(documentA);
				if(tokens.size() > 0) {
					for (Token token : tokens) {
						if(token.getTaxaId().equals("10090") || token.getTaxaId().equals("9606") || token.getTaxaId().equals("10116")) {
							System.out.println(url + "\t"
									+ documentA.getId() + "\t"
									+ token.getTerm() + "\t"
									+ String.join("; ", token.getTextForms()) + "\t"
									+ token.getUniprotId() + "\t"
									+ token.getGenBankId() + "\t"
									+ token.getHgncId() + "\t"
									+ token.getEnsemblId() + "\t"
									+ token.getOrganism() + "[" + token.getTaxaId() + "]" + "\t"
									+ token.getSecUniprotId()
									);
						}
					}
				} else {
						System.out.println(url + "\t"
								+ documentA.getId() + "\t" + "--"
								);
				}
				

			}
		}
	}*/
	
	public static void main(String[] args) throws Exception {
		System.setErr(new PrintStream("/home/tanvi.h/Desktop/temp/set1_genes.log"));
		File urlsFile = new File("/home/tanvi.h/CEM-GPRO/input_htmls/set1_genes.html");
		File output = new File(urlsFile.getParent(), urlsFile.getName().replaceAll("\\.html", ".csv"));
		FileWriter outFW = new FileWriter(output);
		
		outFW.write("Url" + "\t"
				+ "Abstract ID" + "\t"
				+ "Entity Name" + "\t"
				+ "Text Form Found" + "\t"
				+ "Uniprot Id" + "\t"
				+ "Genbank Id" + "\t"
				+ "HGNC Id" + "\t"
				+ "Ensembl Ids" + "\t"
				+ "Taxa[NCBI Id]" + "\t"
				+ "Secondary Uniprot Ids" 
				+ "\n");
		outFW.flush();
		
		Store store = new Store();
		
		GeneProteinTagger geneProteinTagger = new GeneProteinTagger(store.getResourcePath("geneProtein.ontology"),
				store.getResourcePath("geneProtein.negSyn"),
				store.getResourcePath("geneProtein.negTerm"),
				store.getProperty("lvg.path"));
		System.err.println("Ontology loaded ..");
		
		HtmlParserA parserA = new HtmlParserA();
		
		int counter = 0;
		
		for (String url : parserA.extractUrlsFromClientHtml(urlsFile)) {
			DocumentsA allDocumentsA = parserA.getDocumentsFromUrl(url);
			System.err.println(url + "\t" + allDocumentsA.size());
			
			for (DocumentA documentA : allDocumentsA) {
				try {
					System.err.println(documentA.getId());
					HashSet<Token> tokens = geneProteinTagger.getTokens(documentA);
					if(tokens.size() > 0) {
						for (Token token : tokens) {
							counter++;
							if(counter == 100) {
								System.err.println("Thread sleeping; Counter reset to 0 :)");
								Thread.sleep(10000);
								counter = 0;
							}
							
							if(token.getTaxaId().equals("10090") || token.getTaxaId().equals("9606") || token.getTaxaId().equals("10116")) {
								outFW.write(url + "\t"
										+ documentA.getId() + "\t"
										+ token.getTerm() + "\t"
										+ String.join("; ", token.getTextForms()) + "\t"
										+ token.getUniprotId() + "\t"
										+ token.getGenBankId() + "\t"
										+ token.getHgncId() + "\t"
										+ token.getEnsemblId() + "\t"
										+ token.getOrganism() + "[" + token.getTaxaId() + "]" + "\t"
										+ token.getSecUniprotId() + "\n"
										);
								outFW.flush();
							}
						}
					} else {
						outFW.write(url + "\t"
									+ documentA.getId() + "\t" + "--" + "\n"
									);
						outFW.flush();
					}
				} catch (Exception e) {
					System.err.println(documentA.getId() + " FAILED");
				}
			}
		}
		
		outFW.close();
	}
}
