package com.molcon.nlp.BioAnnotator.tagger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.erasmusmc.data_mining.ontology.api.Concept;
import org.erasmusmc.data_mining.ontology.api.Label;
import org.erasmusmc.data_mining.ontology.api.Language;
import org.erasmusmc.data_mining.ontology.api.Ontology;
import org.erasmusmc.data_mining.ontology.common.DatabaseId;
import org.erasmusmc.data_mining.ontology.common.LabelTypeComparator;
import org.erasmusmc.data_mining.ontology.impl.file.SingleFileOntologyImpl;
import org.erasmusmc.data_mining.peregrine.api.IndexingResult;
import org.erasmusmc.data_mining.peregrine.api.Peregrine;
import org.erasmusmc.data_mining.peregrine.disambiguator.api.DisambiguationDecisionMaker;
import org.erasmusmc.data_mining.peregrine.disambiguator.api.Disambiguator;
import org.erasmusmc.data_mining.peregrine.disambiguator.api.RuleDisambiguator;
import org.erasmusmc.data_mining.peregrine.disambiguator.impl.ThresholdDisambiguationDecisionMakerImpl;
import org.erasmusmc.data_mining.peregrine.disambiguator.impl.rule_based.LooseDisambiguator;
import org.erasmusmc.data_mining.peregrine.disambiguator.impl.rule_based.StrictDisambiguator;
import org.erasmusmc.data_mining.peregrine.disambiguator.impl.rule_based.TypeDisambiguatorImpl;
import org.erasmusmc.data_mining.peregrine.impl.hash.PeregrineImpl;
import org.erasmusmc.data_mining.peregrine.normalizer.api.NormalizerFactory;
import org.erasmusmc.data_mining.peregrine.normalizer.impl.LVGNormalizer;
import org.erasmusmc.data_mining.peregrine.normalizer.impl.NormalizerFactoryImpl;
import org.erasmusmc.data_mining.peregrine.tokenizer.api.TokenizerFactory;
import org.erasmusmc.data_mining.peregrine.tokenizer.impl.TokenizerFactoryImpl;
import org.erasmusmc.data_mining.peregrine.tokenizer.impl.UMLSGeneChemTokenizer;

import com.molcon.nlp.BioAnnotator.tagger.models.Tag;
import com.molcon.nlp.BioAnnotator.tagger.models.Tags;
import com.molcon.nlp.BioAnnotator.utils.TermUtils;

public class PeregrineTagger {
	private HashSet<String> negativeSynList, negativeTermList;
	private Peregrine peregrine;
	private Ontology ontology;
	
	protected PeregrineTagger() {

	}
	
	/**
	 * Initializes peregrine ontology 
	 * @param ontologyFilePath path to ontology file
	 * @param lvgPath path to lvg file
	 * @throws IOException if ontologyFilePath or lvgPath are invalid
	 */
	public PeregrineTagger(String ontologyFilePath, String lvgPath) throws IOException {
		
		File ontologyFile = new File(ontologyFilePath);
		File lvgFile = new File(lvgPath);
		
		if(!ontologyFile.exists()) throw new IOException("Invalid Ontology file path : " + ontologyFilePath);
		else if (!lvgFile.exists()) throw new IOException("Invalid lvg file path : " + lvgPath);
		else {
			ontology = new SingleFileOntologyImpl(ontologyFilePath);
			peregrine = createPeregrine(ontology, lvgPath + "lvg.properties");
		}
	}
	
	/**
	 * Initializes peregrine ontology 
	 * @param ontologyFile path to ontology file
	 * @param negSynFilePath path to file containing negative synonyms 
	 * @param negTermFilePath path to file containing negative terms 
	 * @param lvgPath path to lvg file
	 * @throws IOException if ontologyFilePath/negSynFilePath/negTermFilePath/lvgPath are invalid
	 */
	public PeregrineTagger(String ontologyFile, String negSynFilePath, String negTermFilePath, String lvgPath)
			throws IOException {
		this(ontologyFile, lvgPath);
		File negSynFile = new File(negSynFilePath);
		File negTermFile = new File(negTermFilePath);
		
		if(!negSynFile.exists()) throw new IOException("Invalid Negative Synonyms List file path : " + negSynFilePath);
		else if (!negTermFile.exists()) throw new IOException("Invalid Negative Terms List file path : " + negTermFilePath);
		negativeSynList = new HashSet<String>(FileUtils.readLines(negSynFile, "utf-8"));
		negativeTermList = new HashSet<String>(FileUtils.readLines(negTermFile, "utf-8"));
	}
	
	/**
	 * Creates the peregrine with the ontology provided; it uses loose and
	 * strict disambiguators for indexing the text
	 */
	private Peregrine createPeregrine(Ontology ontology, String lvgPropertiesPath) throws IOException {
		UMLSGeneChemTokenizer tokenizer = new UMLSGeneChemTokenizer();
		TokenizerFactory tokenizerFactory = TokenizerFactoryImpl.createDefaultTokenizerFactory(tokenizer);
		LVGNormalizer normalizer = new LVGNormalizer(lvgPropertiesPath);
		NormalizerFactory normalizerFactory = NormalizerFactoryImpl.createDefaultNormalizerFactory(normalizer);
		RuleDisambiguator[] disambiguators = { new LooseDisambiguator(), new StrictDisambiguator() };
		Disambiguator disambiguator = new TypeDisambiguatorImpl(disambiguators);
		DisambiguationDecisionMaker disambiguationDecisionMaker = new ThresholdDisambiguationDecisionMakerImpl();
		String ontologyLanguageToLoad = null;
		return new PeregrineImpl(ontology, tokenizerFactory, normalizerFactory,
				disambiguator, disambiguationDecisionMaker,
				ontologyLanguageToLoad);
	}
	
	private boolean isStopWord(String textTerm, String preferredTerm, boolean considerCase) {
		
		if(negativeSynList != null) {
			for (String negSyn : negativeSynList) {
				if ((considerCase && textTerm.equals(negSyn)) || (!considerCase && textTerm.equalsIgnoreCase(negSyn))) {
					return true;
				}
			}
		}
		if(negativeTermList != null) {
			for (String negTerm : negativeTermList) {
				if ((considerCase && preferredTerm.equals(negTerm)) || (!considerCase && preferredTerm.equalsIgnoreCase(negTerm))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Tags getTags(String text) {
		List<IndexingResult> indexingResults = peregrine.indexAndDisambiguate(text, Language.EN);
		Tags tags = new Tags();
		for (IndexingResult indexingResult : indexingResults) {
			Serializable conceptId = indexingResult.getTermId().getConceptId();
			int start = indexingResult.getStartPos(), stop = indexingResult.getEndPos() + 1;
			String textTerm = text.substring(start, stop);
			Concept concept = ontology.getConcept(conceptId);
			String preferredLabelText = LabelTypeComparator.getPreferredLabel(concept.getLabels()).getText();
			if(!isStopWord(textTerm, preferredLabelText, false) && TermUtils.isComplete(textTerm) && TermUtils.getCharCount(textTerm) > 1) {
				Tag tag = new Tag();
				tag.setEnd(stop);
				tag.setPreferedTerm(preferredLabelText);
				tag.setSentenceEnd(indexingResult.getSentenceEndPos());
				tag.setSentenceStart(indexingResult.getSentenceStartPos());
				tag.setStart(start);
				tag.setTextForm(textTerm);
				for (Label label : concept.getLabels()) {
					tag.addLabel(label.getText());
				}
				ArrayList<DatabaseId> dbidList = new ArrayList<DatabaseId>(concept.getDatabaseIds());
				for (DatabaseId dbid : dbidList) {
					String[] params = dbid.getCode().split("_");
					if(params[0].equalsIgnoreCase("MCID")|| params[0].equalsIgnoreCase("UNIPROT")) {
						tag.addId(params[0], params[1]);
					}
				}
				tags.add(tag);
			}
		}
		tags.sort((Tag s1, Tag s2)-> s2.getTextForm().length() - s1.getTextForm().length());
		Tags modTags = new Tags(tags);
		
		for(int i = tags.size() - 1; i >=0 ; i--) {
			Tag tagA = tags.get(i);
			for (int j = i - 1; j >= 0; j--) {
				Tag tagB = tags.get(j);
				if(!tagB.getTextForm().equals(tagA.getTextForm())) {
					if(tagA.getSentenceStart() == tagB.getSentenceStart() &&
							( (TermUtils.isInRange(tagA.getStart(), tagB.getStart(), tagB.getEnd()))
									|| (TermUtils.isInRange(tagA.getEnd(), tagB.getStart(), tagB.getEnd())))) {
						modTags.remove(tagA);
						continue;
					}
				}
			}
		}
		return modTags;
	}
	
	public static void main(String[] args) throws Exception {
		PeregrineTagger tagger = new PeregrineTagger("/home/tanvi.h/Desktop/temp/temp-gp.ontology",
				"/home/tanvi.h/git/GeneProteinTagger/src/main/resources/ontology/gene_protein/neg_syn_genes_proteins.list",
				"/home/tanvi.h/git/GeneProteinTagger/src/main/resources/ontology/gene_protein/neg_term_genes_proteins.list",
				"/home/tanvi.h/Peregrine/lvg2013/data/config/");
		String text = "We have cloned the gene for a novel Ets-related transcription factor, new Ets-related factor (NERF), from human spleen, fetal liver, and brain. Comparison of the deduced amino acid sequence of NERF with those of other members of the Ets family reveals that the level of homology to ELF-1, which is involved in the regulation of several T- and B-cell-specific genes, is highest. Homologies are clustered in the putative DNA binding domain in the middle of the protein, a basic domain just upstream of this domain, and several shorter stretches of homology towards the amino terminus. The presence of two predominant NERF transcripts in various fetal and adult human tissues is due to at least three alternative splice products, NERF-1a, NERF-1b, and NERF-2, which differ in their amino termini and their expression in different tissues. Only NERF-2 and ELF-1, and not NERF-1a and NERF-1b, function as transcriptional activators of the lyn and blk gene promoters, although all isoforms of NERF bind with affinities similar to those of ELF-1 to a variety of Ets binding sites in, among others, the blk, lck, lyn, mb-1, and immunoglobulin H genes and are expressed at similar levels. Since NERF and ELF-1 are coexpressed in B and T cells, both might be involved in the regulation of the same genes.";
		Tags tags = tagger.getTags(text);

		System.out.println();
		System.out.println();
		for (Tag tag : tags) {
			System.out.println(tag + "\t" + tag.getTextForm() + "\t" + tag.getIds());
		}
	}

}
