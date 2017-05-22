package org.iptc.extra.core.es;

import java.io.IOException;
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
import org.iptc.extra.core.types.document.Document;

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
	
	public ElasticSearchResponse findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage) throws IOException {
		return findDocuments(qb, indexName, page, nPerPage, null);
	}
	
	public ElasticSearchResponse findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage, List<String> highlightFields) throws IOException {
		
		Integer from = (page - 1) * nPerPage;
		Integer size = nPerPage;
		
		SearchRequestBuilder request = client.prepareSearch(indexName)
		        .setTypes("documents")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(qb)              
		        .setFrom(from).setSize(size)
		        .setExplain(false);
		
		if(highlightFields != null && !highlightFields.isEmpty()) {
			HighlightBuilder hlBuilder = new HighlightBuilder();
			for(String field : highlightFields) {
				hlBuilder.field(field)
					.fragmentSize(0)
					.numOfFragments(0)
					.requireFieldMatch(false);
			}
			hlBuilder.preTags("<span class=\"highlight\">");
			hlBuilder.postTags("</span>");
			hlBuilder.requireFieldMatch(false);
			
			request.highlighter(hlBuilder);
		}
		
		SearchResponse response = request.get();
		
		List<Document> documents = new ArrayList<Document>();
		SearchHits hits = response.getHits();
		for(SearchHit hit : hits) {
			Document doc = new Document();
			doc.setId(hit.getId());
		
			Map<String, HighlightField> highlights = hit.getHighlightFields();
			Map<String, Object> source = hit.getSource();
			
			for(String field : source.keySet()) {
				if(field.equals("id")) {
					continue;
				}
				
				String value =  source.get(field).toString();
				if(highlights.containsKey(field)) {
					Text[] fragments = highlights.get(field).fragments();
					if(fragments.length > 0) {
						value = fragments[0].string();
					}
				}
				
				doc.addField(field, value);	
			}
		}
		
		ElasticSearchResponse resp = new ElasticSearchResponse();
		resp.setDocuments(documents);
		resp.setFound(hits.getTotalHits());
		
		return resp;
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
		
		IndexResponse indexReponse = client.prepareIndex(indexName, "query", id)
				.setSource(query)
				.setRefreshPolicy(RefreshPolicy.IMMEDIATE)
				.get();
		
		return indexReponse.status().getStatus();
	}
	
	public List<String> findRules(Document document, Schema schema, String indexName, int page, int nPerPage) throws IOException {
		XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject();
		
		docBuilder.endObject();
		
		PercolateQueryBuilder percolateQuery = new PercolateQueryBuilder("query", schema.getName(), docBuilder.bytes());
		
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
		
		return ruleIds;
	}
}
