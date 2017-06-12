package org.iptc.extra.core.cql.tree.visitor;

import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.CommentClause;
import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.ReferenceClause;
import org.iptc.extra.core.cql.tree.Relation;
import org.iptc.extra.core.cql.tree.SearchClause;
import org.iptc.extra.core.cql.tree.SearchTerms;

public class CQL2HTMLVisitor extends SyntaxTreeVisitor<String> {
	
	private String htmlTag;

	public CQL2HTMLVisitor(String htmlTag) {
		this.htmlTag = htmlTag;
	}
	
	@Override
	public String visitPrefixClause(PrefixClause prefixClause) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<" + htmlTag + " class=\"prefixClause\" data-depth=\"" + prefixClause.getDepth() + "\">");
		buffer.append("(");
		
		buffer.append(visit(prefixClause.getOperator()));
		
		for(Clause clause : prefixClause.getClauses()) {
			buffer.append(visit(clause));
		}
		
		buffer.append(")");
		buffer.append("</" + htmlTag + ">");
		
		return buffer.toString();
	}
	
	@Override
	public String visitOperator(Operator operator) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<" + htmlTag + " class=\"booleanOp\" data-valid=\"" + operator.isValid() + "\" " 
				+ "data-depth=\"" + operator.getDepth() + "\">");
		buffer.append(operator);
		buffer.append("</" + htmlTag + ">");
		
		return buffer.toString();
	}
	
	@Override
	public String visitSearchClause(SearchClause searchClause) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + htmlTag + " class=\"searchClause\" data-depth=\"" + searchClause.getDepth() + "\">(");
		
		if(searchClause.hasIndex()) {
			buffer.append(visit(searchClause.getIndex()));
			buffer.append(visit(searchClause.getRelation()));
		}
		buffer.append(visit(searchClause.getSearchTerms()));

		buffer.append(") </" + htmlTag + "> ");
		return buffer.toString();
	}
	
	@Override
	public String visitCommentClause(CommentClause commentClause) {
		return "";
	}
	
	@Override
	public String visitReferenceClause(ReferenceClause referenceClause) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + htmlTag + " data-depth=\"" + referenceClause.getDepth() + "\"> (");
		
		buffer.append("<" + htmlTag + " class=\"referenceClause\" rule=\"" + referenceClause.getRuleId() + "\">");
		
		buffer.append("@ref == ");
		buffer.append(referenceClause.getRuleId());
		
		buffer.append("</" + htmlTag + ">");
		
		buffer.append(") </" + htmlTag + ">");
		return buffer.toString();
	}
	
	@Override
	public String visitIndex(Index index) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<" + htmlTag + " class=\"index\" data-valid=\"" + index.isValid() + "\" "
				+ "data-depth=\"" + index.getDepth() + "\"> ");
		buffer.append(index.getName());
		buffer.append(" </" + htmlTag + "> ");
		
		return buffer.toString();
	}
	
	@Override
	public String visitRelation(Relation relation) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<" + htmlTag + " class=\"relation\" data-valid=\"" + relation.isValid() + "\" "
				+ "data-depth=\"" + relation.getDepth() + "\"> ");
		buffer.append(relation);
		buffer.append(" </" + htmlTag + ">");	
		
		return buffer.toString();
	}
	
	@Override
	public String visitSearchTerms(SearchTerms searchTerm) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<" + htmlTag + " class=\"searchTerm\" data-depth=\"" + searchTerm.getDepth() + "\"> ");
		buffer.append(searchTerm);
		buffer.append(" </" + htmlTag + ">");
		
		return buffer.toString();
	}
}