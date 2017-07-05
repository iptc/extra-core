package org.iptc.extra.core.eql.tree.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 	@author manos schinas
 *	
 *	A search clause consists of 
 * 	Index		Relation		SearchTerm
 * 
 * 	Index and Relation are optional parts. 
 * 	When messing SearchTerm can match any field.
 * 
 */
public class SearchClause extends Clause {

	private Index index;
	
	private Relation relation;
	
	private SearchTerm searchTerm;

	public SearchClause() {
		
	}
	
	public SearchClause(SearchTerm searchTerm) {
		super();
		this.searchTerm = searchTerm;

		children.add(searchTerm);
	}
	
	public SearchClause(Index index, Relation relation, SearchTerm searchTerm) {
		super();
		this.index = index;
		this.relation = relation;
		this.searchTerm = searchTerm;
		
		children.add(index);
		children.add(relation);
		children.add(searchTerm);
	}
	
	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
		children.add(index);
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
		children.add(relation);
	}

	public SearchTerm getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(SearchTerm searchTerm) {
		this.searchTerm = searchTerm;
		children.add(searchTerm);
	}
	
	public boolean hasIndex() {
		return (index != null && relation != null);
	}
	
	@Override
	public String toString() {
		return (hasIndex() ? index + " " + relation + " " : "") + searchTerm.toString();
	}
	
	public List<SearchClause> splitSearchClause() {
		List<SearchClause> searchClauses = new ArrayList<SearchClause>();
		
		for(String term : searchTerm.getTerms()) {
			SearchTerm st = new SearchTerm(term);
			SearchClause searchClause = new SearchClause(index, relation, st);
			
			searchClauses.add(searchClause);
		}
		
		return searchClauses;
	}
}
