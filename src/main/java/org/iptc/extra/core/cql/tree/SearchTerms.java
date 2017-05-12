package org.iptc.extra.core.cql.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SearchTerms extends Node {
	
	private static String[] regex =  {".", "?", "+", "*", "|", "{", "}", "[", "]", "(", ")", "\"", "\\"};
	
	private List<String> terms = new ArrayList<String>();

	public SearchTerms() {
		
	}
	
	public SearchTerms(List<String> terms) {
		this.terms.addAll(terms);
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	
	public String getSearchTerm() {
		String searchTerm = StringUtils.join(terms, " ");
		if(searchTerm != null && searchTerm.contains("/")) {
			searchTerm = searchTerm.replaceAll(" / ", "/");
		}
		
		return searchTerm;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}
	
	@Override
	public String toString() {
		return "\"" + getSearchTerm() + "\"";
	}
	
	public int numberOfTerms() {
		return terms.size();
	}
	
	public String getTerm(int index) {
		return terms.get(index);
	}
	
	public boolean hasWildcards() {
		for(String term : terms) {
			for(String regexCharacter : regex) {
				if(term.contains(regexCharacter)) {
					return true;
				}
			}
		}
		return false;
	}
}
