package org.iptc.extra.core.es;

import java.io.IOException;
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
import org.iptc.extra.core.types.Corpus;
import org.iptc.extra.core.types.Schema;
import org.iptc.extra.core.types.document.Document;


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
	
	
	public boolean createCorporaIndex(Corpus corpus) {
		Schema schema = corpus.getSchema();
		if(schema != null) {
			try {
				XContentBuilder settingBuilder = ElasticSearchUtils.buildDocumentMapping(schema);
				
				IndicesAdminClient indicesClient = client.admin().indices();
				indicesClient.prepareCreate(corpus.getId())
					.setSource(settingBuilder)
					.get();
				
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return false;
	}
	
	public boolean indexDocument(String indexName, Document document, Schema schema) {
		try {
			XContentBuilder source = ElasticSearchUtils.documentToSource(document, schema);
			client.prepareIndex()
					.setIndex(indexName)
					.setType("documents")
					.setId(document.getId())
					.setSource(source)
					.get();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	public Document getDocument(String documentId, String indexName, Schema schema) throws IOException {
		
		GetResponse response = client.prepareGet(indexName, "documents", documentId).get();
		if(!response.isSourceEmpty()) {
			Document doc = ElasticSearchUtils.sourceToDocument(response.getSourceAsString(), null, schema);
			
			return doc;
		}
		
		return null;
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
	 * @return ElasticSearchResponse<Document> A set of documents that match the query
	 * 	
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
			Document doc = ElasticSearchUtils.sourceToDocument(source, highlights, schema);
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
	
	/**
	 * Count number of documents that match the query
	 * 
	 * @param qb				The query expressed in Elastic search DSL
	 * @param indexName			The name of the index in elastic search
	 * 
	 * @return	number of matched documents long
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
	 * 
	 * @return A code that indicates whether the submission was successful or not
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
	 * 
	 * @return A set of rule ids that match the input document
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
	 * 
	 * @return	A set of rule ids that match the input document
	 */
	
	public ElasticSearchResponse<String> findRules(Document document, String indexName, String group, int page, int nPerPage) throws IOException {
		
		XContentBuilder docBuilder = ElasticSearchUtils.buildPercolateQuery(document);
		
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
	
	public boolean createPercolateIndex(Schema schema) throws IOException {
		
		IndicesAdminClient indicesClient = client.admin().indices();
		boolean exists = indicesClient.prepareExists(schema.getId()).execute().actionGet().isExists();
		if(!exists) {
			XContentBuilder settingBuilder = ElasticSearchUtils.buildPercolateIndexSettings(schema.getLanguage());
			indicesClient.prepareCreate(schema.getId())
				.setSource(settingBuilder)
				.get();
			
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

		return false;
	}
	
	public boolean createPercolateIndexMapping(Schema schema) throws IOException {
		
		//Create Percolate Index, if not exist
		createPercolateIndex(schema);
		
		//Create Document Mapping based on the specified schema
		XContentBuilder mappingBuilder = ElasticSearchUtils.buildDocumentMapping(schema);
		PutMappingResponse mappingResponse = client.admin().indices().preparePutMapping(schema.getId())
			.setType("doc")
			.setSource(mappingBuilder)
			.get();

		return mappingResponse.isAcknowledged();
	}
	
}
