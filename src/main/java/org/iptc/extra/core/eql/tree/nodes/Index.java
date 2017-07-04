package org.iptc.extra.core.eql.tree.nodes;

/**
 * 
 * @author manos schinas
 *
 *	Part of search clauses:
 *
 * 	Index		Relation		SearchTerm
 *	|			|				|
 *	title 		any 			"term1 term2"
 *
 */
public class Index extends Node {

	private String name;
	
	public Index(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public String toString() {
		return name;
	}
}
