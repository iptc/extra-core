package org.iptc.extra.core.cql.tree;

/**
 * @author manosetro - Manos Schinas
 *
 *	CommentClause corresponds to in-line comments in the rule.
 *	Every string between //...// is considered as a comment and mapped to a CommentClause object in the syntax tree. 
 *	
 */
public class CommentClause extends Clause {

	public CommentClause() {
		
	}
	
	public CommentClause(String comment) {
		this.comment = comment;
	}

	protected String comment;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}
	
	@Override
	public String toString() {
		return comment;
	}
}
