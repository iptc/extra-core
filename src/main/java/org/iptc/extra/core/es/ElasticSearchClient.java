package org.iptc.extra.core.es;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.percolator.PercolateQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.iptc.extra.core.types.Schema;
import org.iptc.extra.core.types.Schema.Field;
import org.iptc.extra.core.types.document.Document;
import org.iptc.extra.core.types.document.DocumentField;
import org.iptc.extra.core.types.document.Paragraph;
import org.iptc.extra.core.types.document.Sentence;
import org.iptc.extra.core.types.document.StructuredTextField;
import org.iptc.extra.core.types.document.TextField;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class ElasticSearchClient {

	private TransportClient client;

	@SuppressWarnings("resource")
	public ElasticSearchClient(String host, int port) throws UnknownHostException {
		client = new PreBuiltTransportClient(Settings.EMPTY)
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
	}
	
	public void close() {
		client.close();
	}
	
	public ElasticSearchResponse<Document> findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage) throws IOException {
		return findDocuments(qb, indexName, page, nPerPage, null, null);
	}
	
	public ElasticSearchResponse<Document> findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage, Schema schema) throws IOException {
		return findDocuments(qb, indexName, page, nPerPage, schema, null);
	}
	
	public ElasticSearchResponse<Document> findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage, QueryBuilder highlightQuery) throws IOException {
		return findDocuments(qb, indexName, page, nPerPage, null, highlightQuery);
	}
	
	/**
	 * 
	 * @param qb				The query expressed in Elastic search DSL
	 * @param indexName			The name of the index in elastic search
	 * @param page				Page number
	 * @param nPerPage			Number of documents per page 
	 * @param schema			The schema of the documents in the index 
	 * @param highlightQuery	The query used to highlight the results
	 * 
	 * @return ElasticSearchResponse<Document>
	 * 	
	 * @throws IOException
	 */
	public ElasticSearchResponse<Document> findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage, Schema schema, QueryBuilder highlightQuery) throws IOException {
		
		Integer from = (page - 1) * nPerPage;
		Integer size = nPerPage;
		
		SearchRequestBuilder request = client.prepareSearch(indexName)
		        .setTypes("documents")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(qb)              
		        .setFrom(from)
		        .setSize(size)
		        .setExplain(false);
		
		if(schema != null) {
			HighlightBuilder hlBuilder = new HighlightBuilder();
			for(String field : schema.getTextualFieldNames()) {
				hlBuilder.field(field).fragmentSize(0).numOfFragments(0);
				hlBuilder.field("stemmed_" + field).fragmentSize(0).numOfFragments(0);
				hlBuilder.field("case_sensitive_" + field).fragmentSize(0).numOfFragments(0);
				hlBuilder.field("literal_" + field).fragmentSize(0).numOfFragments(0);
			}
			
			hlBuilder.preTags("<span class=\"highlight\">");
			hlBuilder.postTags("</span>");
			if(highlightQuery != null) {
				hlBuilder.highlightQuery(highlightQuery);
			}
			
			request.highlighter(hlBuilder);
		}
		
		SearchResponse response = request.get();

		List<Document> documents = new ArrayList<Document>();
		SearchHits hits = response.getHits();
		float maxScore = hits.getMaxScore() > 0 ? hits.getMaxScore() : 1;
		for(SearchHit hit : hits) {
			float hitScore = hit.getScore();
			String source = hit.sourceAsString();
			Map<String, HighlightField> highlights = hit.getHighlightFields();
			Document doc = sourceToDocument(source, highlights, schema);
			if(doc != null) {
				String score = Float.toString(hitScore/maxScore);
				doc.addField("score", score);
				documents.add(doc);
			}
		}
		
		ElasticSearchResponse<Document> resp = new ElasticSearchResponse<Document>();
		resp.setResults(documents);
		resp.setFound(hits.getTotalHits());
		
		return resp;
	}
	
	/*
	 * Converts elastic search response to org.iptc.extra.core.types.document.Document
	 */
	public Document sourceToDocument(String source, Map<String, HighlightField> highlights, Schema schema) {
		Gson gson = new Gson();
		Reader br = new StringReader(source);
		JsonObject sourceJson = gson.fromJson(br, JsonObject.class);
		
		Document doc = new Document();
		for(String fieldName : schema.getFieldNames()) {
			Field schemaField = schema.getField(fieldName);
			JsonElement fieldValue = sourceJson.get(fieldName);
			if(fieldValue instanceof JsonPrimitive) {
				String value = fieldValue.getAsString();
				
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
	
	/**
	 * Count number of documents that match the query
	 * 
	 * @param qb				The query expressed in Elastic search DSL
	 * @param indexName			The name of the index in elastic search
	 * @return	number of matched documents long
	 * 
	 * @throws IOException
	 */
	public long countDocuments(QueryBuilder qb, String indexName) throws IOException {
		SearchRequestBuilder request = client.prepareSearch(indexName)
		        .setTypes("documents")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(qb)              
		        .setSize(0)
		        .setExplain(false);
		
		SearchResponse response = request.get();
		SearchHits hits = response.getHits();
		
		return hits.getTotalHits();
	}
	
	public int submitRule(String id, QueryBuilder qb, String indexName) throws IOException {
		return submitRule(id, qb, indexName, null);
	}
	
	/**
	 * Submit a rule into percolate index
	 * 
	 * @param id	-	The id of the rule
	 * @param qb	-	The Elastic Search query generated by the rule
	 * @param indexName	-	The index name of the percolate index
	 * @param groupId	-	A group id if that rule is part of a subset
	 * @return
	 * @throws IOException
	 */
	public int submitRule(String id, QueryBuilder qb, String indexName, String groupId) throws IOException {
		GetResponse response = client.prepareGet(indexName, "queries", id).setRefresh(true).execute().actionGet();
		if(response.isExists()) {
			
			UpdateResponse updateResponse = client.prepareUpdate(indexName, "queries", id)
					.setDoc(XContentFactory.jsonBuilder()               
		            .startObject()
		                .field("query", qb)
		            .endObject())
					.get();
			
			if(groupId != null && !groupId.equals("")) {
				Map<String, Object> source = response.getSourceAsMap();
				List<?> groups = (List<?>) source.get("group");
				if(groups == null || !groups.contains(groupId)) {
					Script script = new Script("if(!ctx._source.containsKey(\"group\")) { ctx._source.group = []; } ctx._source.group.add(\"" + groupId + "\");");
					updateResponse = client.prepareUpdate(indexName, "queries", id).setScript(script).get();
				}	
			}

			return updateResponse.status().getStatus();
		}
		else {
			XContentBuilder query = XContentFactory.jsonBuilder().startObject().field("query", qb);
			if(groupId != null && !groupId.equals("")) {
				query.array("group", groupId);
			}
			query.endObject();
			
			IndexResponse indexResponse = client.prepareIndex(indexName, "queries", id)
					.setSource(query)
					.setRefreshPolicy(RefreshPolicy.IMMEDIATE)
					.get();	
			
			return indexResponse.status().getStatus();
		}
		
		
	}
	
	/**
	 * Delete a rule with from percolate index
	 */
	public int deleteRule(String id, String indexName) throws IOException {	
		DeleteResponse deleteResponse = client.prepareDelete(indexName, "queries", id).get();
		return deleteResponse.status().getStatus();
		
	}
	
	/**
	 * Given a document, retrieve rules indexed into percolate index
	 * 
	 * @param document	The document used to query percolate index
	 * @param indexName	The name of the percolate index
	 * @param page		Page number
	 * @param nPerPage	Number of rules per page
	 * @return
	 * @throws IOException
	 */
	public ElasticSearchResponse<String> findRules(Document document, String indexName, int page, int nPerPage) throws IOException {
		return findRules(document, indexName, null, page, nPerPage);
	}
	
	/**
	 * Given a document, retrieve rules indexed into percolate index
	 * 
	 * @param document	The document used to query percolate index
	 * @param indexName	The name of the percolate index
	 * @param group		The if of a group in order to get rules that match the document under the specific group 
	 * @param page		Page number
	 * @param nPerPage	Number of rules per page
	 * @return
	 * @throws IOException
	 */
	
	public ElasticSearchResponse<String> findRules(Document document, String indexName, String group, int page, int nPerPage) throws IOException {
		
		XContentBuilder docBuilder = buildPercolateQuery(document);
		
		QueryBuilder query;
		if(group != null && !group.equals("")) {
			query = boolQuery()
					.must(new PercolateQueryBuilder("query", "doc", docBuilder.bytes()))
					.must(termQuery("group", group));
		}
		else {
			query = new PercolateQueryBuilder("query", "doc", docBuilder.bytes());
		}
		
		Integer from = (page - 1) * nPerPage;
		Integer size = nPerPage;
		
		ElasticSearchResponse<String> resp = new ElasticSearchResponse<String>();
		try {
			SearchResponse response = client.prepareSearch(indexName)
		        .setQuery(query)
		        .setFrom(from)
		        .setSize(size)
		        .get();
		
			List<String> ruleIds = new ArrayList<String>();
			for(SearchHit hit : response.getHits()) {
				ruleIds.add(hit.getId());
			}
			resp.setResults(ruleIds);
			resp.setFound(response.getHits().getTotalHits());
		}
		catch(Exception e) {
			
		}
		
		return resp;
	}
		
	private XContentBuilder buildPercolateQuery(Document document) throws IOException {
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
	
	private boolean createSchemaPercolateMapping(Schema schema) throws IOException {
		
		IndicesAdminClient indicesClient = client.admin().indices();
		boolean exists = indicesClient.prepareExists(schema.getId()).execute().actionGet().isExists();
		if(!exists) {
			
			XContentBuilder settingBuilder = XContentFactory.jsonBuilder().startObject();
			
			settingBuilder.startObject("settings").startObject("analysis");
			settingBuilder.startObject("filter");
			addIndexFilters(settingBuilder, "english");
			addIndexFilters(settingBuilder, "german");
			settingBuilder.endObject().startObject("analyzer");
			addIndexAnalyzers(settingBuilder, "english");
			addIndexAnalyzers(settingBuilder, "german");
			settingBuilder.endObject();
			settingBuilder.endObject().endObject().endObject();
			
			indicesClient.prepareCreate(schema.getId())
				.setSource(settingBuilder)
				.get();
		}
		
		XContentBuilder mappingBuilder = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("properties")
						.startObject("query").field("type", "percolator").endObject()
						.startObject("group").field("type", "keyword").endObject()
					.endObject()
				.endObject();
		
		PutMappingResponse mappingResponse = indicesClient.preparePutMapping(schema.getId())
				.setType("queries")
				.setSource(mappingBuilder)
				.get();

		return mappingResponse.isAcknowledged();
	}
	
	public boolean createSchemaMapping(Schema schema) throws IOException {
		
		createSchemaPercolateMapping(schema);
		
		String lang = schema.getLanguage();
		
		XContentBuilder mappingBuilder = XContentFactory.jsonBuilder().startObject();
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
				mappingBuilder.startObject(fieldName);
				mappingBuilder.field("type", "keyword");
				mappingBuilder.endObject();
			}

		}
		mappingBuilder.endObject();
		mappingBuilder.endObject();
		
		PutMappingResponse mappingResponse = client.admin().indices().preparePutMapping(schema.getId())
			.setType("doc")
			.setSource(mappingBuilder)
			.get();

		return mappingResponse.isAcknowledged();
	}

	private void addKeywordFieldMapping(XContentBuilder mappingBuilder, String fieldName) throws IOException {
		mappingBuilder.startObject(fieldName);
		mappingBuilder.field("type", "keyword");
		mappingBuilder.endObject();
	}
	
	private void addFieldMapping(XContentBuilder mappingBuilder, String fieldName, String analyzer) throws IOException {
		mappingBuilder.startObject(fieldName);
		mappingBuilder.field("type", "text");
		mappingBuilder.field("analyzer", analyzer);
		mappingBuilder.endObject();
	}
	
	private void addNestedFieldMapping(XContentBuilder mappingBuilder, String fieldName, String subFieldName, String analyzer) throws IOException {
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
	
	private void addIndexFilters(XContentBuilder mappingBuilder, String lang) throws IOException {
		
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
	
	
	private void addIndexAnalyzers(XContentBuilder mappingBuilder, String lang) throws IOException {
		
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
			mappingBuilder.startArray("filter").value("lowercase").value("german_stop").value("german_normalization").value("english_stemmer");
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
	
	
	public static void main(String...args) throws IOException {
		ElasticSearchClient client = new ElasticSearchClient("160.40.50.207", 9300);
		
		Schema schema =  new Schema();
		schema.setLanguage("german");
		schema.addField("title", true, true, false);
		schema.addField("subtitle", true, true, false);
		schema.addField("body", true, true, true);
		schema.addField("slugline", false, false, false);
		
		client.createSchemaMapping(schema);
	}
}
