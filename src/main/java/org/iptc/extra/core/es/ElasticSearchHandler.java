package org.iptc.extra.core.es;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.iptc.extra.core.types.Document;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ElasticSearchHandler {

	protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	protected RestClient restClient;
	
	public ElasticSearchHandler(String host, int port) {
		restClient = RestClient.builder(new HttpHost(host, port, "http")).build();
	}
	
	public int countDocuments(QueryBuilder qb, String indexName) throws IOException {
		
		String query = "{ \"query\": " + qb.toString() + "}";
		
		Gson gson = new Gson();
		JsonObject queryObject = gson.fromJson(query, JsonObject.class);
		queryObject.addProperty("size", 0);
		
		HttpEntity entity = new NStringEntity(queryObject.toString(), ContentType.APPLICATION_JSON);
		
		String endpoint = indexName + "/articles/_search";
		Response response = restClient.performRequest(
				"POST", endpoint, 
				Collections.<String, String>emptyMap(), 
				entity, 
				new Header[0]);
		
		JsonObject responseObject = parseResponse(response);
		
		JsonObject hits = responseObject.getAsJsonObject("hits");
		int total = hits.get("total").getAsInt();
		
		return total;
	}
	
	public Pair<Integer, List<Document>> findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage) throws IOException {
		return findDocuments(qb, indexName, page, nPerPage, null);
	}

	public Pair<Integer, List<Document>> findDocuments(QueryBuilder qb, String indexName, int page, int nPerPage, List<String> highlightFields) throws IOException {
	
		String query = "{ \"query\": " + qb.toString() + "}";
		
		Gson gson = new Gson();
		JsonObject queryObject = gson.fromJson(query, JsonObject.class);
		
		if(highlightFields != null && !highlightFields.isEmpty()) {
			JsonArray fields = new JsonArray();
			for(String hf : highlightFields) {
				JsonObject field = new JsonObject();
				JsonObject fragment = new JsonObject();
				fragment.add("fragment_size", new JsonPrimitive(0));
				fragment.add("number_of_fragments", new JsonPrimitive(0));
				
				field.add(hf, fragment);
				fields.add(field);
			}

			JsonObject highlight = new JsonObject();
			highlight.add("fields", fields);
			highlight.add("pre_tags", new JsonPrimitive("<span class=\"highlight\">"));
			highlight.add("post_tags", new JsonPrimitive("</span>"));
			
			queryObject.add("highlight", highlight);
		}
		
		Integer from = (page - 1) * nPerPage;
		Integer size = nPerPage;
		
		queryObject.addProperty("from", from);
		queryObject.addProperty("size", size);
		
		HttpEntity entity = new NStringEntity(queryObject.toString(), ContentType.APPLICATION_JSON);
		
		String endpoint = indexName + "/articles/_search";
		Response response = restClient.performRequest(
				"POST", endpoint, 
				Collections.<String, String>emptyMap(), 
				entity, 
				new Header[0]);
		
		List<Document> documents = new ArrayList<Document>();
		JsonObject responseObject = parseResponse(response);

		JsonObject hits = responseObject.getAsJsonObject("hits");
		int total = hits.get("total").getAsInt();
		for(JsonElement hitElement : hits.getAsJsonArray("hits")) {
			
			JsonObject hit = hitElement.getAsJsonObject();
			JsonObject source =  hit.getAsJsonObject("_source");
			
			JsonObject highlight = new JsonObject();
			if(hit.has("highlight")) {
				highlight = hit.getAsJsonObject("highlight");
			}
			
			Document doc = new Document();
			doc.setId(source.get("id").getAsString());
				
			doc.addField("title", source.get("title").getAsString());
			if(highlight.has("title")) {
				JsonArray ar = highlight.getAsJsonArray("title");
				if(ar.size() > 0) {
					doc.addField("title", ar.get(0).getAsString());
				}
			}
			
			doc.addField("description", source.get("description").getAsString());
			doc.addField("body", source.get("body").getAsString());
			if(highlight.has("body")) {
				JsonArray ar = highlight.getAsJsonArray("body");
				if(ar.size() > 0) {
					doc.addField("body", ar.get(0).getAsString());
				}
			}
			
			doc.addField("contentCreated", source.get("contentCreated").getAsString());
			doc.addField("slugline", source.get("slugline").getAsString());

			documents.add(doc);
		}
		
		return Pair.of(total, documents);
	}
	
	public void close() throws IOException {
		restClient.close();
	}
	
	private JsonObject parseResponse(Response response) {
		HttpEntity responseEntity = response.getEntity();
		
		InputStream inputStream = null;
		JsonObject responseObject = null;
		try {
			Gson gson = new Gson();
			inputStream = responseEntity.getContent();
			Reader br = new InputStreamReader(inputStream);
			responseObject = gson.fromJson(br, JsonObject.class);
			
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}		
		finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return responseObject;
	}
}
