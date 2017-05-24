package org.iptc.extra.core.es;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchResponse<K> {

	private long found = 0;

	private List<K> results = new ArrayList<K>();

	public long getFound() {
		return found;
	}

	public void setFound(long found) {
		this.found = found;
	}

	public List<K> getResults() {
		return results;
	}

	public void setResults(List<K> results) {
		this.results = results;
	}
	
	
}
