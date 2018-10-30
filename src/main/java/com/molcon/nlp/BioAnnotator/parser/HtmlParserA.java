package com.molcon.nlp.BioAnnotator.parser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserA {
	public DocumentsA getDocumentsFromUrl(String url) {
		DocumentsA documents = new DocumentsA();
		
		try {
			Document doc = Jsoup.connect(url).get();
			Elements divs = doc.body().getElementsByTag("div");
			for (Element div : divs) {
				String id = div.attr("id");
				DocumentA docA = new DocumentA();
				docA.setId(id);
				docA.setTitle(div.getElementsByTag("h1").first().text().replace(id, "").trim());
				docA.setContent(div.getElementsByTag("p").first().text());
				documents.add(docA);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documents;
	}
	
	public HashSet<String> extractUrlsFromClientHtml(File iFile) {
		HashSet<String> absUrls = new HashSet<String>();
		
		try {
			Document doc = Jsoup.parse(iFile, "UTF-8");
			Elements tds = doc.body().getElementsByTag("td");
			for (Element td : tds) {
				absUrls.add(td.getElementsByTag("a").first().attr("href"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return absUrls;
	}
	
	public static void main(String[] args) throws Exception {
/*		System.setErr(new PrintStream("/home/tanvi.h/Desktop/temp/htmlparsertest1.txt"));
		System.setOut(new PrintStream("/home/tanvi.h/Desktop/temp/htmlparsertest.csv"));
		HtmlParserA parserA = new HtmlParserA();
		for (DocumentA docA : parserA.getDocumentsFromUrl("http://ubio.bioinfo.cnio.es/people/fleitner/gpro_set2_1.html")) {
			System.out.println(docA.getId() + "\t" + docA.getTitle() + "\t" + docA.getContent());
		}*/
		System.setOut(new PrintStream("/home/tanvi.h/Desktop/temp/htmlparsertest.csv"));
		HtmlParserA parserA = new HtmlParserA();
		for (String url : parserA.extractUrlsFromClientHtml(new File("/home/tanvi.h/CEM-GPRO/input_htmls/set1_genes.html"))) {
			System.out.println(url);
			for (DocumentA docA : parserA.getDocumentsFromUrl(url)) {
				System.out.println(docA.getId() + "\t" + docA.getTitle() + "\t" + docA.getContent());
			}
		}
		System.out.println();
	}
}
