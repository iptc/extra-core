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

public class CQL2JSTreeVisitor extends SyntaxTreeVisitor<String> {
		
		@Override
		public String visitPrefixClause(PrefixClause prefixClause) {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<li>");
			
			
			buffer.append(visit(prefixClause.getOperator()));
			buffer.append("<ul>");
			for(Clause clause : prefixClause.getClauses()) {
				buffer.append(visit(clause));
			}
			buffer.append("</ul>");
			
			buffer.append("</li>");
			
			return buffer.toString();
		}
		
		@Override
		public String visitOperator(Operator operator) {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<span class=\"booleanOp\" data-valid=\"" + operator.isValid() + "\">");
			buffer.append(operator);
			buffer.append("</span>");
			
			return buffer.toString();
		}
		
		@Override
		public String visitSearchClause(SearchClause searchClause) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<li>");
			
			if(searchClause.hasIndex()) {
				buffer.append(visit(searchClause.getIndex()));
				buffer.append(visit(searchClause.getRelation()));
			}
			buffer.append(visit(searchClause.getSearchTerms()));

			buffer.append("</li> ");
			return buffer.toString();
		}
		
		@Override
		public String visitReferenceClause(ReferenceClause referenceClause) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<li>");
			
			buffer.append("<span class=\"referenceClause\">");
			buffer.append("@ref == ");
			buffer.append(referenceClause.getRuleId());
			buffer.append("</span>");
			
			buffer.append("</li> ");
			return buffer.toString();
		}
		
		
		@Override
		public String visitCommentClause(CommentClause commentClause) {
			return "";
		}
		
		@Override
		public String visitIndex(Index index) {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<span class=\"index\" data-valid=\"" + index.isValid() + "\"> ");
			buffer.append(index.getName());
			buffer.append("</span>");
			
			return buffer.toString();
		}
		
		@Override
		public String visitRelation(Relation relation) {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<span class=\"relation\" data-valid=\"" + relation.isValid() + "\"> ");
			buffer.append(relation);
			buffer.append("</span>");
			
			return buffer.toString();
		}
		
		@Override
		public String visitSearchTerms(SearchTerms searchTerm) {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<span class=\"searchTerm\"> ");
			buffer.append(searchTerm);
			buffer.append("</span>");
			
			return buffer.toString();
		}
	}