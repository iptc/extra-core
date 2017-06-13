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
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.percolator.PercolateQueryBuilder;
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
		for(SearchHit hit : hits) {
			String source = hit.sourceAsString();
			Map<String, HighlightField> highlights = hit.getHighlightFields();
			Document doc = sourceToDocument(source, highlights, schema);
			if(doc != null) {
				documents.add(doc);
			}
		}
		
		ElasticSearchResponse<Document> resp = new ElasticSearchResponse<Document>();
		resp.setResults(documents);
		resp.setFound(hits.getTotalHits());
		
		return resp;
	}
	
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
	
	public boolean createPercolateIndex(String indexName) {
		PutMappingResponse response = client.admin().indices()
			.preparePutMapping(indexName)
			.setSource("query", "type=percolator")
			.get();
		
		return response.isAcknowledged();
	}
	
	public int submitRule(String id, QueryBuilder qb, String indexName) throws IOException {
		
		XContentBuilder query = XContentFactory.jsonBuilder()
				.startObject()
				.field("query", qb)
				.endObject();
		
		IndexResponse indexResponse = client.prepareIndex(indexName, "queries", id)
				.setSource(query)
				.setRefreshPolicy(RefreshPolicy.IMMEDIATE)
				.get();
		
		return indexResponse.status().getStatus();
	}
	
	public ElasticSearchResponse<String> findRules(Document document, String indexName, String docType, int page, int nPerPage) throws IOException {
		
		XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject();
		for(String fieldName : document.keySet()) {
			DocumentField field = document.get(fieldName);
			if(field instanceof StructuredTextField) {
				StructuredTextField structuredField = (StructuredTextField) field;
				docBuilder.field(fieldName, structuredField.getValue());
				
				docBuilder.startArray(fieldName + "_paragraphs");
				for(Paragraph paragraph : structuredField.getParagraphs()) {
					docBuilder.field("paragraph", paragraph.getParagraph());
				}
				docBuilder.endArray();
				
				docBuilder.startArray(fieldName + "_sentences");
				for(Sentence sentence : structuredField.getSentences()) {
					docBuilder.field("sentence", sentence.getText());
				}
				docBuilder.endArray();
				
			}
			else if(field instanceof TextField) {
				docBuilder.field(fieldName, ((TextField) field).getValue());
			}
			
		}
		docBuilder.endObject();
		
		PercolateQueryBuilder percolateQuery = new PercolateQueryBuilder("query", docType, docBuilder.bytes());
		
		Integer from = (page - 1) * nPerPage;
		Integer size = nPerPage;
		
		SearchResponse response = client.prepareSearch(indexName)
		        .setQuery(percolateQuery)
		        .setFrom(from)
		        .setSize(size)
		        .get();
		
		List<String> ruleIds = new ArrayList<String>();
		for(SearchHit hit : response.getHits()) {
			ruleIds.add(hit.getId());
		}
		
		ElasticSearchResponse<String> resp = new ElasticSearchResponse<String>();
		resp.setResults(ruleIds);
		resp.setFound(response.getHits().getTotalHits());
		
		return resp;
	}
		
}
