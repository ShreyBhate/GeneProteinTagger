package com.molcon.nlp.BioAnnotator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;

public class Store extends Properties {
	private static final long serialVersionUID = 1L;

	public Store() {
		try {
			InputStream in = getClass().getResourceAsStream("store.properties");
			load(in);
			in.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}	
	}

	public Store(String filename) {
		try {
			InputStream in = new FileInputStream(filename);
			load(in);
			in.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}	
	}
	
	public HashSet<String> getRuleSet() {
		HashSet<String> rules = new HashSet<String>();
		for (String rule : getProperty("ALL_RULES").split(",", -1)) {
			rule = rule.trim();
			if(getProperty(rule) != null && getProperty(rule).equalsIgnoreCase("true"))
				rules.add(rule);
		}
		return rules;
	}
	
	public String getResourcePath(String property) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(getProperty(property)).getFile());
		return file.getAbsolutePath();
	}
	
	public static void main(String[] args) throws Exception {
		Store store = new Store();
		System.out.println(store.getProperty("lvgPath"));
	}
}
