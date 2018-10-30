package com.molcon.nlp.BioAnnotator.tagger;

import java.util.ArrayList;
import java.util.List;

import org.erasmusmc.data_mining.peregrine.api.DisambiguationInfo;
import org.erasmusmc.data_mining.peregrine.api.IndexingDisambiguationResult;
import org.erasmusmc.data_mining.peregrine.api.IndexingResult;
import org.erasmusmc.data_mining.peregrine.disambiguator.api.DisambiguationDecisionMaker;

public class DummyDecisionMaker implements DisambiguationDecisionMaker {

	@Override
	public List<IndexingResult> decide(
			List<IndexingDisambiguationResult> indexingResults) {

        final List<IndexingResult> workingCopy = new ArrayList<>();

        for (int i = 0; i < indexingResults.size(); i++) {
        	IndexingDisambiguationResult result = indexingResults.get(i);
        	
        	for (DisambiguationInfo info : result.getDisambiguationInfos()) {
        		System.out.println(info);
        		workingCopy.add(IndexingDisambiguationResult.toIndexingResult(result, info));
			}
        	
        }

        return workingCopy;
	}

}
