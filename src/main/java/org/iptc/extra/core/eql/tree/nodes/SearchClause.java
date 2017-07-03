package org.iptc.extra.core.eql.tree;


public class SearchClause extends Clause {

	private Index index;
	
	private Relation relation;
	
	private SearchTerms searchTerms;

	public SearchClause() {
		
	}
	
	public SearchClause(SearchTerms searchTerms) {
		super();
		this.searchTerms = searchTerms;

		children.add(searchTerms);
	}
	
	public SearchClause(Index index, Relation relation, SearchTerms searchTerms) {
		super();
		this.index = index;
		this.relation = relation;
		this.searchTerms = searchTerms;
		
		children.add(index);
		children.add(relation);
		children.add(searchTerms);
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

	public SearchTerms getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerm(SearchTerms searchTerms) {
		this.searchTerms = searchTerms;
		children.add(searchTerms);
	}
	
	public boolean hasIndex() {
		return (index != null && relation != null);
	}
	
	@Override
	public String toString() {
		return (hasIndex() ? index + " " + relation + " " : "") + searchTerms.toString();
	}
}
