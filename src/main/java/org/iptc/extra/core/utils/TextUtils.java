package org.iptc.extra.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.iptc.extra.core.types.document.Sentence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

	public static List<Sentence> getSentences(String text) {
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);	
		
		List<Sentence> sentences = new ArrayList<Sentence>();
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> nlpSentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: nlpSentences) {
			Sentence s = new Sentence(sentence.toString());
			sentences.add(s);
		}
		
		return sentences;
	}
	
	public static List<String> getParagraphs(String text) {
		List<String> paragraphs = new ArrayList<String>();

		org.jsoup.nodes.Document doc = Jsoup.parse(text);
		Elements pElements = doc.select("p");
		for (Element pElement : pElements) {
			String paragraph = pElement.text();
			paragraphs.add(paragraph);
		}
		return paragraphs;
	}
	
}
