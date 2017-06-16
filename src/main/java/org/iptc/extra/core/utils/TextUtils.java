package org.iptc.extra.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TextUtils {
	
	public static String clean(String txt) {
		
		txt = txt.replaceAll("<!--.*?-->", " ").replaceAll("<[^>]+>", " ");
		
		txt = StringEscapeUtils.unescapeHtml4(txt);
		txt = StringUtils.normalizeSpace(txt);
		txt = StringUtils.trim(txt);
		
		return txt;
	}

	public static List<String> getSentences(String text) {
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);	
		
		List<String> sentences = new ArrayList<String>();
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> nlpSentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: nlpSentences) {
			sentences.add(sentence.toString());
		}
		
		return sentences;
	}
	
	
}
