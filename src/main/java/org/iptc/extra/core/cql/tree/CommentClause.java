package org.iptc.extra.core.cql.tree;

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
