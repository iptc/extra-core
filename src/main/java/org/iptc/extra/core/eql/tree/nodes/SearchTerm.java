package org.iptc.extra.core.eql.tree.nodes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SearchTerm extends Node {
	
	private static String[] regexpCharacters =  {".", "+", "|", "{", "}", "[", "]", "(", ")", "\"", "\\"};
	
	private static String[] wildcards =  {"+", "*"};
	
	private List<String> terms = new ArrayList<String>();

	public SearchTerm() {
		
	}
	
	public SearchTerm(String term) {
		this.terms.add(term);
	}

	public SearchTerm(List<String> terms) {
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

	public String getQueryString(String prefix) {
		List<String> queryParts = new ArrayList<String>();
		for(String term : terms) {
			if(isRegexp(term)) {
				queryParts.add(prefix + "(/" + term + "/)");
			}
			else {
				if(term.matches(".+\\s+.+")) {
					List<String> termParts = new ArrayList<String>();
					for(String termPart : term.split("\\s+")) {
						termParts.add("+" + termPart);
					}
					term = "(" + StringUtils.join(termParts, " ") +")";
				}
				queryParts.add(prefix + term);
			}
		}

		return StringUtils.join(queryParts, " ");
	}
	
	@Override
	public boolean hasChildren() {
		return false;
	}
	
	@Override
	public String toString() {
		if(isRegexp()) {
			return "\"" + getRegexp(true)  + "\"";
		}
		
		return "\"" + getSearchTerm() + "\"";
	}
	
	public int numberOfTerms() {
		return terms.size();
	}
	
	public String getTerm(int index) {
		return terms.get(index);
	}
	
	// Does search term contains wild-cards?
	public boolean hasWildCards() {
		for(String term : terms) {
			boolean has = hasWildCards(term);
			if(has) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasWildCards(String term) {
		for(String regexCharacter : wildcards) {
			if(term.contains(regexCharacter)) {
				return true;
			}
		}
		
		return false;
	}
	
	// Is search term a regular expression?
	public boolean isRegexp() {
		for(String term : terms) {
			boolean isRegex = isRegexp(term);
			if(isRegex) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isRegexp(String term) {
		for(String regexCharacter : regexpCharacters) {
			if(term.contains(regexCharacter)) {
				return true;
			}
		}
		
		return false;
	}
	
	// check whether the regular expression contains a whitespace character
	public boolean doesRegexpContainWhitespaces() {
		String regexp = StringUtils.join(terms, "");
		if(regexp.contains("\\s")) {
			return true;
		}
		
		return false;
	}
	
	// Get regular expression string 
	public String getRegexp(boolean predefinedCharacterClasses) {
		String regexp = StringUtils.join(terms, "");
		
		if(!predefinedCharacterClasses && regexp != null) {
			regexp = regexp
					.replace("\\d", "[0-9]")
					.replace("\\D", "[^0-9]")
					.replace("\\s", "[ \\t\\n\\x0B\\f\\r]")
					.replace("\\S", "[^ \\t\\n\\x0B\\f\\r]")
					.replace("\\w", "[a-zA-Z_0-9]")
					.replace("\\w", "[^a-zA-Z_0-9]");
			
			regexp = ".*" + regexp + ".*";
		}
		
		return regexp;
	}
	
}
