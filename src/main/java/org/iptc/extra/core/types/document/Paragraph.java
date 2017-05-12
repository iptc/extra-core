package org.iptc.extra.core.types.document;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {

	private String paragraph;
	private List<Sentence> sentences = new ArrayList<Sentence>();

	public Paragraph(String paragraph) {
		this.paragraph = paragraph;
		String[] splits = paragraph.split("\\.|\\?|\\!");
		for(String split : splits) {
			split = split.trim();
			Sentence sentence = new Sentence(split);
			sentences.add(sentence);
		}
	}
	
	public Sentence getSentence(int index) {
		return sentences.get(index);
	}
	
	public int getNumberOfSentences() {
		return sentences.size();
	}
	
	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}
	
	public String toString() {		
		return paragraph;
	}

	public String getParagraph() {
		return paragraph;
	}

	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}
}
