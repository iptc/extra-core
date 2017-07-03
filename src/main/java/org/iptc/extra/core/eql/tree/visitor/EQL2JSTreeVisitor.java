package org.iptc.extra.core.eql.tree.visitor;

import org.iptc.extra.core.eql.SyntaxTree;
import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.CommentClause;
import org.iptc.extra.core.eql.tree.nodes.Index;
import org.iptc.extra.core.eql.tree.nodes.Operator;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;
import org.iptc.extra.core.eql.tree.nodes.ReferenceClause;
import org.iptc.extra.core.eql.tree.nodes.Relation;
import org.iptc.extra.core.eql.tree.nodes.SearchClause;
import org.iptc.extra.core.eql.tree.nodes.SearchTerm;

/**
 * @author manosetro - Manos Schinas
 * 
 *	EXTRA2ESQueryVisitor performs a depth-first traversal of the syntax tree 
 *	and generates an HTML representation that can be used by the jsTree plugin of jQuery (https://www.jstree.com/). 
 */

public class EQL2JSTreeVisitor extends SyntaxTreeVisitor<String> {
		
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
			buffer.append(visit(searchClause.getSearchTerm()));

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
			
			SyntaxTree syntaxTree = referenceClause.getRuleSyntaxTree();
			if(syntaxTree != null && syntaxTree.getRootNode() != null) {
				buffer.append("<ul>");
				buffer.append(visit(syntaxTree.getRootNode()));
				buffer.append("</ul>");
			}
			
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
		public String visitSearchTerm(SearchTerm searchTerm) {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<span class=\"searchTerm\"> ");
			buffer.append(searchTerm);
			buffer.append("</span>");
			
			return buffer.toString();
		}
	}