package com.molcon.nlp.BioAnnotator.tagger;

import java.util.List;

import org.erasmusmc.data_mining.peregrine.api.DisambiguationInfo;
import org.erasmusmc.data_mining.peregrine.api.IndexingDisambiguationResult;
import org.erasmusmc.data_mining.peregrine.disambiguator.api.RuleDisambiguator;

public class UselessDisambiguator implements RuleDisambiguator {
	private String kw;
	@Override
	public void init(List<IndexingDisambiguationResult> indexingResults) {
		System.out.println("In init" + "\t" + indexingResults.size());
		for (IndexingDisambiguationResult indexingDisambiguationResult : indexingResults) {
			System.out.println(indexingDisambiguationResult.getText());
			kw = indexingDisambiguationResult.getText();
//			indexingDisambiguationResult.
		}
		
	}

	@Override
	public void disambiguate(IndexingDisambiguationResult indexingResult) {
		for (DisambiguationInfo info : indexingResult.getDisambiguationInfos()) {
			System.out.println(info.getTermId());
			System.out.println(info.getWeight());
			
			info.withKeyword(kw, 100);
			info.withWeight(DisambiguationInfo.POSITIVE_WEIGHT, this);
			System.out.println(info.getWeight());
		}
	}

}
