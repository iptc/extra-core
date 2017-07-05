package org.iptc.extra.core.eql.tree.nodes;

/**
 * @author manosetro - Manos Schinas
 *
 *	CommentClause corresponds to in-line comments in the rule.
 *	Every string between //...// is considered as a comment and mapped to a CommentClause object in the syntax tree. 
 *
 *	(or
 *		(title any "term1 term2 term2")
 *		//this is a comment that will be mapped to a ReferenceClause//
 *	)
 *	
 */
public class CommentClause extends Clause {

	protected String comment;
	
	public CommentClause() {
		
	}
	
	public CommentClause(String comment) {
		this.comment = comment;
	}
	
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
