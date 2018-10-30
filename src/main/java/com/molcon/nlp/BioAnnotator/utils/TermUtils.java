package com.molcon.nlp.BioAnnotator.utils;

import org.apache.commons.lang3.StringUtils;

public class TermUtils {
	
	public static boolean isComplete(String term) {
		return ((StringUtils.countMatches(term, "(") == StringUtils
					.countMatches(term, ")"))
					&& (StringUtils.countMatches(term, "[") == StringUtils
							.countMatches(term, "]")) && (StringUtils.countMatches(
					term, "{") == StringUtils.countMatches(term, "}")));
	}
	
	public static int getCharCount(String term) {
		int charCount = 0;
		for (int i = 0; i < term.length(); i++)
			charCount = (Character.isLetter(term.charAt(i))) ? charCount+1 : charCount; return charCount;
	}
	
	public static int getLevenshteinDistance(String s1, String s2) {
		return StringUtils.getLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase());
	}
	
	public static boolean isAcronym(String term) {
		if(term.matches("[a-z]?[A-Z]*[a-z]?")) return true;
		else return false;
	}

	public static boolean isInRange(int n, int i, int j) {
		return (n >= i && n < j); 
	}
	
	public static void main(String[] args) {
		System.out.println(getLevenshteinDistance("surface water", "surface of water"));
	}
}
