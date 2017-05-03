package org.iptc.extra.core.cql.tree.visitor;

import org.apache.commons.lang3.StringUtils;
import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.CommentClause;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.SearchClause;

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
}

