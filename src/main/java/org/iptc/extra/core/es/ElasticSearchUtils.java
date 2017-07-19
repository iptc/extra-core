package org.iptc.extra.core.es;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.iptc.extra.core.types.Schema;
import org.iptc.extra.core.types.Schema.Field;
import org.iptc.extra.core.types.document.Document;
import org.iptc.extra.core.types.document.DocumentField;
import org.iptc.extra.core.types.document.DocumentTopic;
import org.iptc.extra.core.types.document.Paragraph;
import org.iptc.extra.core.types.document.Sentence;
import org.iptc.extra.core.types.document.StructuredTextField;
import org.iptc.extra.core.types.document.TextField;
import org.iptc.extra.core.utils.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ElasticSearchUtils {

	public static XContentBuilder buildPercolateIndexSettings(String lang) throws IOException {
		
		XContentBuilder settingBuilder = XContentFactory.jsonBuilder().startObject();
		
		settingBuilder.startObject("settings").startObject("analysis");
		settingBuilder.startObject("filter");
		addIndexFilters(settingBuilder, lang);
		settingBuilder.endObject().startObject("analyzer");
		addIndexAnalyzers(settingBuilder, lang);
		settingBuilder.endObject();
		settingBuilder.endObject().endObject().endObject();
		
		return settingBuilder;
	}
	
	public static XContentBuilder buildCorporaIndexSettings(Schema schema) throws IOException {
		
		String lang = schema.getLanguage();
		XContentBuilder settingBuilder = XContentFactory.jsonBuilder().startObject();	
		
		settingBuilder.startObject("settings")
			.startObject("analysis")
				.startObject("filter");
					addIndexFilters(settingBuilder, lang);
				settingBuilder.endObject()
				.startObject("analyzer");
					addIndexAnalyzers(settingBuilder, lang);
				settingBuilder.endObject()
			.endObject()
		.endObject();
		
		XContentBuilder mappingsPropertiesBuilder = buildDocumentMapping(schema, true);
		
		settingBuilder.startObject("mappings")
			.rawField("documents", mappingsPropertiesBuilder.bytes())
		.endObject().endObject();

		return settingBuilder;
	}
	
	private static void addIndexFilters(XContentBuilder mappingBuilder, String lang) throws IOException {
		
		mappingBuilder.startObject(lang + "_stop");
		mappingBuilder.field("type", "stop");
		mappingBuilder.field("stopwords", "_" + lang + "_");
		mappingBuilder.endObject();
		
		if(lang.equals("english")) {
			mappingBuilder.startObject("english_possessive_stemmer");
			mappingBuilder.field("type", "stemmer");
			mappingBuilder.field("language", "possessive_english");
			mappingBuilder.endObject();
			
			mappingBuilder.startObject("english_stemmer");
			mappingBuilder.field("type", "stemmer");
			mappingBuilder.field("language", "english");
			mappingBuilder.endObject();
		}
		
		if(lang.equals("german")) {
			mappingBuilder.startObject("german_stemmer");
			mappingBuilder.field("type", "stemmer");
			mappingBuilder.field("language", "light_german");
			mappingBuilder.endObject();
		}
	}
	
	
	private static void addIndexAnalyzers(XContentBuilder mappingBuilder, String lang) throws IOException {
		
		if(lang.equals("english")) {
			mappingBuilder.startObject("english_stemming_analyzer");
			mappingBuilder.field("tokenizer", "standard");
			mappingBuilder.startArray("filter").value("english_possessive_stemmer").value("lowercase").value("english_stop").value("english_stemmer");
			mappingBuilder.endArray().endObject();
			
			mappingBuilder.startObject("english_non_stemming_analyzer");
			mappingBuilder.field("tokenizer", "standard");
			mappingBuilder.startArray("filter").value("lowercase").value("english_stop");
			mappingBuilder.endArray().endObject();
			
			mappingBuilder.startObject("english_case_sensitive_analyzer");
			mappingBuilder.field("tokenizer", "standard");
			mappingBuilder.startArray("filter").value("english_stop");
			mappingBuilder.endArray().endObject();
			
			mappingBuilder.startObject("english_literal_analyzer");
			mappingBuilder.field("tokenizer", "standard");
			mappingBuilder.startArray("filter").value("lowercase").value("english_stop");
			mappingBuilder.endArray().endObject();
		}
		
		if(lang.equals("german")) {
			mappingBuilder.startObject("german_stemming_analyzer");
			mappingBuilder.field("tokenizer", "standard");
			mappingBuilder.startArray("filter").value("lowercase").value("german_stop").value("german_normalization").value("german_stemmer");
			mappingBuilder.endArray().endObject();
			
			mappingBuilder.startObject("german_non_stemming_analyzer");
			mappingBuilder.field("tokenizer", "standard");
			mappingBuilder.startArray("filter").value("lowercase").value("german_stop").value("german_normalization");
			mappingBuilder.endArray().endObject();
			
			mappingBuilder.startObject("german_case_sensitive_analyzer");
			mappingBuilder.field("tokenizer", "standard");
			mappingBuilder.startArray("filter").value("german_stop").value("german_normalization");
			mappingBuilder.endArray().endObject();
			
			mappingBuilder.startObject("german_literal_analyzer");
			mappingBuilder.field("tokenizer", "whitespace");
			mappingBuilder.startArray("filter").value("lowercase").value("german_stop").value("german_normalization");
			mappingBuilder.endArray().endObject();
		}
		
	}
	
	public static XContentBuilder buildPercolateQuery(Document document) throws IOException {
		XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject();
		for(String fieldName : document.keySet()) {
			DocumentField field = document.get(fieldName);
			if(field instanceof StructuredTextField) {
				StructuredTextField structuredField = (StructuredTextField) field;
				docBuilder.field(fieldName, structuredField.getValue());
				
				docBuilder.startArray(fieldName + "_paragraphs");
				for(Paragraph paragraph : structuredField.getParagraphs()) {
					docBuilder.startObject()
						.field("paragraph", paragraph.getParagraph())
						.endObject();
				}
				docBuilder.endArray();
				
				docBuilder.startArray(fieldName + "_sentences");
				for(Sentence sentence : structuredField.getSentences()) {
					docBuilder.startObject()
						.field("sentence", sentence.getText())
						.endObject();
				}
				docBuilder.endArray();
				
			}
			else if(field instanceof TextField) {
				docBuilder.field(fieldName, ((TextField) field).getValue());
			}
		}
		docBuilder.endObject();
		
		return docBuilder;
	}
	
	public static XContentBuilder buildDocumentMapping(Schema schema, boolean addTopicsMapping) throws IOException {
		
		String lang = schema.getLanguage();
		
		XContentBuilder mappingBuilder = XContentFactory.jsonBuilder().startObject();
	
		mappingBuilder.startObject("_all").field("enabled", "false").endObject();
		
		mappingBuilder.startObject("properties");
		for(String fieldName : schema.getFieldNames()) {
			
			Field field = schema.getField(fieldName);
			if(field.textual) {
				addFieldMapping(mappingBuilder, fieldName, lang + "_non_stemming_analyzer");
				addFieldMapping(mappingBuilder, "literal_" + fieldName, lang + "_literal_analyzer");
				addFieldMapping(mappingBuilder, "stemmed_" + fieldName, lang + "_stemming_analyzer");
				addFieldMapping(mappingBuilder, "case_sensitive_" + fieldName, lang + "_case_sensitive_analyzer");
				addKeywordFieldMapping(mappingBuilder, "raw_" + fieldName);
				addKeywordFieldMapping(mappingBuilder, fieldName + "_tokens");
				addKeywordFieldMapping(mappingBuilder, "stemmed_" + fieldName + "_tokens");
				
				if(field.hasSentences) {
					addNestedFieldMapping(mappingBuilder, fieldName + "_sentences", "sentence", lang + "_non_stemming_analyzer");
					addNestedFieldMapping(mappingBuilder, "literal_" + fieldName + "_sentences", "sentence", lang + "_literal_analyzer");
					addNestedFieldMapping(mappingBuilder, "stemmed_" + fieldName + "_sentences", "sentence", lang + "_stemming_analyzer");
					addNestedFieldMapping(mappingBuilder, "case_sensitive_" + fieldName + "_sentences", "sentence", lang + "_case_sensitive_analyzer");
				}
				
				if(field.hasParagraphs) {
					addNestedFieldMapping(mappingBuilder, fieldName + "_paragraphs", "paragraph", lang + "_non_stemming_analyzer");
					addNestedFieldMapping(mappingBuilder, "literal_" + fieldName + "_paragraphs", "paragraph", lang + "_literal_analyzer");
					addNestedFieldMapping(mappingBuilder, "stemmed_" + fieldName + "_paragraphs", "paragraph", lang + "_stemming_analyzer");
					addNestedFieldMapping(mappingBuilder, "case_sensitive_" + fieldName + "_paragraphs", "paragraph", lang + "_case_sensitive_analyzer");
				}
			}
			else {
				String type = "keyword";
				if(field.numeric) {
					type = "long";
				}
				else if(field.date) {
					type = "date";
				}
				
				mappingBuilder.startObject(fieldName);
				mappingBuilder.field("type", type);
				mappingBuilder.endObject();
			}

		}
		
		if(addTopicsMapping) {
			addKeywordFieldMapping(mappingBuilder, "excluded");
			String[] topicFields = {"topicId", "name", "exclude", "parentTopic", "url", "association"};
			addNestedKeywordsMapping(mappingBuilder, "topics", topicFields);
		}
		
		//text_content field
		addFieldMapping(mappingBuilder, "text_content", lang + "_non_stemming_analyzer");
		addFieldMapping(mappingBuilder, "literal_text_content", lang + "_literal_analyzer");
		addFieldMapping(mappingBuilder, "stemmed_text_content", lang + "_stemming_analyzer");
		addFieldMapping(mappingBuilder, "case_sensitive_text_content", lang + "_case_sensitive_analyzer");
		addKeywordFieldMapping(mappingBuilder, "text_content_tokens");
		addKeywordFieldMapping(mappingBuilder, "stemmed_text_content_tokens");
		// sentences
		addNestedFieldMapping(mappingBuilder, "text_content_sentences", "sentence", lang + "_non_stemming_analyzer");
		addNestedFieldMapping(mappingBuilder, "literal_text_content_sentences", "sentence", lang + "_literal_analyzer");
		addNestedFieldMapping(mappingBuilder, "stemmed_text_content_sentences", "sentence", lang + "_stemming_analyzer");
		addNestedFieldMapping(mappingBuilder, "case_sensitive_text_content_sentences", "sentence", lang + "_case_sensitive_analyzer");
		// paragraphs
		addNestedFieldMapping(mappingBuilder, "text_content_paragraphs", "paragraph", lang + "_non_stemming_analyzer");
		addNestedFieldMapping(mappingBuilder, "literal_text_content_paragraphs", "paragraph", lang + "_literal_analyzer");
		addNestedFieldMapping(mappingBuilder, "stemmed_text_content_paragraphs", "paragraph", lang + "_stemming_analyzer");
		addNestedFieldMapping(mappingBuilder, "case_sensitive_text_content_paragraphs", "paragraph", lang + "_case_sensitive_analyzer");
		
		mappingBuilder.endObject();
		mappingBuilder.endObject();
		
		return mappingBuilder;
	}


	private static void addKeywordFieldMapping(XContentBuilder mappingBuilder, String fieldName) throws IOException {
		mappingBuilder.startObject(fieldName);
		mappingBuilder.field("type", "keyword");
		mappingBuilder.endObject();
	}
	
	private static void addFieldMapping(XContentBuilder mappingBuilder, String fieldName, String analyzer) throws IOException {
		mappingBuilder.startObject(fieldName);
		mappingBuilder.field("type", "text");
		mappingBuilder.field("analyzer", analyzer);
		mappingBuilder.endObject();
	}
	
	private static void addNestedFieldMapping(XContentBuilder mappingBuilder, String fieldName, String subFieldName, String analyzer) throws IOException {
		mappingBuilder.startObject(fieldName);
		mappingBuilder.field("type", "nested");
		mappingBuilder.startObject("properties");
		mappingBuilder.startObject(subFieldName);
		mappingBuilder.field("type", "text");
		mappingBuilder.field("analyzer", analyzer);
		mappingBuilder.endObject();
		mappingBuilder.endObject();
		mappingBuilder.endObject();
	}
	
	private static void addNestedKeywordsMapping(XContentBuilder mappingBuilder, String fieldName, String[] subFields) throws IOException {
		mappingBuilder.startObject(fieldName);
		mappingBuilder.field("type", "nested");
		mappingBuilder.startObject("properties");
		for(String subFieldName : subFields) {
			mappingBuilder.startObject(subFieldName);
			mappingBuilder.field("type", "keyword");
			mappingBuilder.endObject();
		}
		mappingBuilder.endObject();
		mappingBuilder.endObject();
	}
	
	/*
	 * Converts elastic search response to org.iptc.extra.core.types.document.Document
	 */
	public static Document sourceToDocument(String source, Map<String, HighlightField> highlights, Schema schema) {
		Gson gson = new Gson();
		Reader br = new StringReader(source);
		JsonObject sourceJson = gson.fromJson(br, JsonObject.class);
		
		Document doc = new Document();
		if(schema == null) {
			for(Entry<String, JsonElement> entry : sourceJson.entrySet()) {
				if(entry.getValue().isJsonPrimitive()) {
					doc.addField(entry.getKey(), entry.getValue().getAsString());
				}
			}
			return doc;
		}
		
		for(String fieldName : schema.getFieldNames()) {
			Field schemaField = schema.getField(fieldName);
			JsonElement fieldValue = sourceJson.get(fieldName);
			if(fieldValue instanceof JsonPrimitive) {
				String value = fieldValue.getAsString();
				
				if(highlights != null) {
					if(highlights.containsKey(fieldName)) {
						Text[] fragments = highlights.get(fieldName).fragments();
						if(fragments.length > 0) {
							value = fragments[0].string();
						}
					}
					else if(highlights.containsKey("stemmed_" + fieldName)) {
						Text[] fragments = highlights.get("stemmed_" + fieldName).fragments();
						if(fragments.length > 0) {
							value = fragments[0].string();
						}
					}
					else if(highlights.containsKey("case_sensitive_" + fieldName)) {
						Text[] fragments = highlights.get("case_sensitive_" + fieldName).fragments();
						if(fragments.length > 0) {
							value = fragments[0].string();
						}
					}
					else if(highlights.containsKey("literal_" + fieldName)) {
						Text[] fragments = highlights.get("literal_" + fieldName).fragments();
						if(fragments.length > 0) {
							value = fragments[0].string();
						}
					}
					else if(highlights.containsKey("raw_" + fieldName)) {
						Text[] fragments = highlights.get("raw_" + fieldName).fragments();
						if(fragments.length > 0) {
							value = fragments[0].string();
						}
					}				
				}
				
				if(schemaField.hasParagraphs) {
					StructuredTextField bodyField = new StructuredTextField();
					bodyField.setValue(value);
					
					JsonElement paragraphsArray = sourceJson.get(fieldName + "_paragraphs");
					if(paragraphsArray != null && paragraphsArray.isJsonArray()) {
						for(JsonElement paragraphElement : paragraphsArray.getAsJsonArray()) {
							String paragraph = paragraphElement.getAsJsonObject().get("paragraph").getAsString();
							bodyField.addParagraph(paragraph);
						}
					}
					
					doc.addField(fieldName, bodyField);
				}
				else {
					doc.addField(fieldName, value);
				}
			}
		}
		
		return doc;
	}
	
	public static XContentBuilder documentToSource(Document document, Schema schema) throws IOException {
		
		XContentBuilder docBuilder = XContentFactory.jsonBuilder()
			.startObject()
			.field("id", document.getId());
		
		docBuilder.startArray("topics");
		for(DocumentTopic topic : document.getTopics()) {
			docBuilder.startObject()
				.field("topicId",  topic.getTopicId())
				.field("url",  topic.getUrl())
				.field("association",  topic.getAssociation())
				.field("parentTopic",  topic.getParentTopic())
				.field("name",  topic.getName())
				.field("exclude",  topic.isExclude())
			.endObject();
		}
		docBuilder.endArray();
		
		StringBuffer textContentBuffer = new StringBuffer();
		for(String fieldName : document.keySet()) {
			Field fieldType = schema.getField(fieldName);
			if(fieldType != null) {
				DocumentField docField = document.get(fieldName);
				if(docField instanceof StructuredTextField) {
					//TODO: handle structured text fields
				}
				else if(docField instanceof TextField) {
					TextField textField = (TextField) docField;
					String fieldValue = textField.getValue();
					
					docBuilder.field(fieldName, textField.getValue());
					
					if(fieldType.textual) {
						textContentBuffer.append(fieldValue + " ");
						
						docBuilder.field("stemmed_" + fieldName, fieldValue);
						docBuilder.field("literal_" + fieldName, fieldValue);
						docBuilder.field("case_sensitive_" + fieldName, fieldValue);
						docBuilder.field("raw_" + fieldName, textField.getValue());
					}
					
					if(fieldType.hasParagraphs) {
						List<String> paragraphs = TextUtils.getParagraphs(textField.getValue());
						
						addParagraphs(docBuilder, fieldName + "_paragraphs", paragraphs);
						addParagraphs(docBuilder, "stemmed_" + fieldName + "_paragraphs", paragraphs);
						addParagraphs(docBuilder, "literal_" + fieldName + "_paragraphs", paragraphs);
						addParagraphs(docBuilder, "case_sensitive_" + fieldName + "_paragraphs",  paragraphs);
						
						List<Sentence> sentences = new ArrayList<Sentence>();		
						for(String paragraph : paragraphs) {
							sentences.addAll(TextUtils.getSentences(paragraph));
						}
						
						addSentences(docBuilder, fieldName + "_sentences", sentences);
						addSentences(docBuilder, "stemmed_" + fieldName + "_sentences", sentences);
						addSentences(docBuilder, "literal_" + fieldName + "_sentences", sentences);
						addSentences(docBuilder, "case_sensitive_" + fieldName + "_sentences", sentences);
						
					}
					else {
						if(fieldType.hasSentences) {
							List<Sentence> sentences = TextUtils.getSentences(textField.getValue());
							
							addSentences(docBuilder, fieldName + "_sentences", sentences);
							addSentences(docBuilder, "stemmed_" + fieldName + "_sentences", sentences);
							addSentences(docBuilder, "literal_" + fieldName + "_sentences", sentences);
							addSentences(docBuilder, "case_sensitive_" + fieldName + "_sentences", sentences);
						}
					}
				}
				
			}
		}
		
		String textContent = textContentBuffer.toString();
		if(textContent.length() > 0) {
			docBuilder.field("text_content", textContent);
			docBuilder.field("stemmed_text_content", textContent);
			docBuilder.field("literal_text_content", textContent);
			docBuilder.field("case_sensitive_text_content", textContent);
			docBuilder.field("raw_text_content", textContent);
			
			List<Sentence> sentences = TextUtils.getSentences(textContent);
			addSentences(docBuilder, "text_content_sentences", sentences);
			addSentences(docBuilder, "stemmed_text_content_sentences", sentences);
			addSentences(docBuilder, "literal_text_content_sentences", sentences);
			addSentences(docBuilder, "case_sensitive_text_content_sentences", sentences);
			
			List<String> paragraphs = TextUtils.getParagraphs(textContent);
			addParagraphs(docBuilder, "text_content_sentences", paragraphs);
			addParagraphs(docBuilder, "stemmed_text_content_sentences", paragraphs);
			addParagraphs(docBuilder, "literal_text_content_sentences", paragraphs);
			addParagraphs(docBuilder, "case_sensitive_text_content_sentences", paragraphs);
			
		}
		
		docBuilder.endObject();
		
		return docBuilder;
	}
	
	private static void addParagraphs(XContentBuilder builder, String fieldName, List<String> paragraphs) throws IOException {
		builder.startArray(fieldName);
		for(String paragraph : paragraphs) {
			builder.startObject().field("paragraph", paragraph).endObject();
		}
		builder.endArray();
	}
	
	private static void addSentences(XContentBuilder builder, String fieldName, List<Sentence> sentences) throws IOException {
		builder.startArray(fieldName);
		for(Sentence sentence : sentences) {
			builder.startObject().field("sentence", sentence.getText()).endObject();
		}
		builder.endArray();
	}
}
