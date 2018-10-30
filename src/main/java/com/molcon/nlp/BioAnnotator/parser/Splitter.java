package com.molcon.nlp.BioAnnotator.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;

public class Splitter {

	public static void main(String[] args) {
		try {
			File xmlFile = new File("/home/tanvi.h/Desktop/temp/RePORTER_PRJ_X_FY2016.xml");
			File outFolder = new File("/home/tanvi.h/Desktop/temp/RePORTER2016_ns");
			outFolder.mkdirs();
			BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
			
			String line = reader.readLine();
			StringBuilder sb;
			int counter = 1;
			
			while((line = reader.readLine()) != null) {
				if(line.contains("<row>")) {
					sb = new StringBuilder("<row xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
//					line = reader.readLine();
					boolean flag = true;
					
					while(flag) {
						line = reader.readLine();
						
						if(line == null) {
							flag = false;
							break;
						}
						else if(line.contains("</row>")) {
							flag = false;
							sb.append(line);
							FileUtils.writeStringToFile(new File(outFolder, counter++ + ".txt"), sb.toString());
							System.out.println(counter);
							sb = new StringBuilder();
						} else sb.append(line);
					}
					
					
				}
			}
			
			
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
