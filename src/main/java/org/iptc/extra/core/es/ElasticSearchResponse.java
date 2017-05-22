package org.iptc.extra.core.es;

import java.util.ArrayList;
import java.util.List;

import org.iptc.extra.core.types.document.Document;

public class ElasticSearchResponse {

	private long found = 0;

	private List<Document> documents = new ArrayList<Document>();

	public long getFound() {
		return found;
	}

	public void setFound(long found) {
		this.found = found;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	
	
}
