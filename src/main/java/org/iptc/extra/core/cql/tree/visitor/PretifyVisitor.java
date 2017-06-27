package org.iptc.extra.core.cql.tree.visitor;

import org.apache.commons.lang3.StringUtils;
import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.CommentClause;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.ReferenceClause;
import org.iptc.extra.core.cql.tree.SearchClause;

/**
 * @author manosetro - Manos Schinas
 *
 *	The class extends SyntaxTreeVisitor class to create a pretified version of the rule, expressed as a sytnax tree.
 *	Namely, this visitor visits each node of the syntax tree in a depth first fashion, 
 *	concatenates the string representation of each node into a single string, 
 *	while adds newlines and tab characters to pretify it. 
 *
 */
public class PretifyVisitor extends SyntaxTreeVisitor<String> {
	
	private String newline;
	private String tab;

	public PretifyVisitor(String newline, String tab) {
		this.newline = newline;
		this.tab = tab;
	}
	
	@Override
	public String visitPrefixClause(PrefixClause prefixClause) {	
		StringBuffer buffer = new StringBuffer();
			
		buffer.append(StringUtils.repeat(tab, prefixClause.getDepth()));
		buffer.append("(");
			
		Operator operator = prefixClause.getOperator();
		buffer.append(operator.toString());
		buffer.append(newline);
			
		for(Clause clause : prefixClause.getClauses()) {
			buffer.append(visit(clause));
		}
			
		buffer.append(StringUtils.repeat(tab, prefixClause.getDepth()));
		buffer.append(")");
		buffer.append(newline);
		
		return  buffer.toString();
	}
	
	@Override
	public String visitSearchClause(SearchClause searchClause) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.repeat(tab, searchClause.getDepth()));
		buffer.append("(");
		if(searchClause.hasIndex()) {
			buffer.append(searchClause.getIndex());
			buffer.append(" ");
			buffer.append(searchClause.getRelation());
			buffer.append(" ");
		}
		buffer.append(searchClause.getSearchTerms());
		buffer.append(")");
		buffer.append(newline);
			
		return buffer.toString();
	}
	
	@Override
	public String visitCommentClause(CommentClause commentClause) {	
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.repeat(tab, commentClause.getDepth()));
		buffer.append(commentClause.getComment());
		buffer.append(newline);
		return buffer.toString();
	}
	
	@Override
	public String visitReferenceClause(ReferenceClause referenceClause) {	
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.repeat(tab, referenceClause.getDepth()));
		buffer.append("(@ref == " + referenceClause.getRuleId() + ")");
		buffer.append(newline);
		return buffer.toString();
	}
	
}

