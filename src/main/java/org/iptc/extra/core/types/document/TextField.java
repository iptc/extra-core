package org.iptc.extra.core.types.document;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class TextField extends DocumentField {

	private List<Paragraph> paragraphs = new ArrayList<Paragraph>();

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs.addAll(paragraphs);
	}
	
	public void addParagraph(Paragraph paragraph) {
		this.paragraphs.add(paragraph);
	}
	
	public void addParagraph(String paragraph) {
		Paragraph p = new Paragraph(paragraph);
		this.paragraphs.add(p);
	}
	
	public List<Sentence> getSentences() {
		List<Sentence> sentences = new ArrayList<Sentence>();
		for(Paragraph paragraph : paragraphs) {
			List<Sentence> paragraphSentences = paragraph.getSentences();
			sentences.addAll(paragraphSentences);
		}
		return sentences;
	}
	
	public String getValue() {
		StringBuffer buffer = new StringBuffer();
		for(Paragraph paragraph : paragraphs) {
			buffer.append(paragraph);
		}
		
		return buffer.toString();
	}
	
	public JsonElement toJson() {
		JsonArray arr = new JsonArray();
		for(Paragraph paragraph : paragraphs) {
			arr.add(paragraph.toString());
		}
		return arr;
	}
}
