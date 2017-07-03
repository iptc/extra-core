package org.iptc.extra.core.eql.tree.visitor;

import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.CommentClause;
import org.iptc.extra.core.eql.tree.nodes.Index;
import org.iptc.extra.core.eql.tree.nodes.Node;
import org.iptc.extra.core.eql.tree.nodes.Operator;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;
import org.iptc.extra.core.eql.tree.nodes.ReferenceClause;
import org.iptc.extra.core.eql.tree.nodes.Relation;
import org.iptc.extra.core.eql.tree.nodes.SearchClause;
import org.iptc.extra.core.eql.tree.nodes.SearchTerm;

/**
 * @author manosetro - Manos Schinas
 *
 *	This class traverses the given syntax tree in a depth first fashion without performing any action. 
 *	That class should be extended by every visitor class. 
 *
 */
public class SyntaxTreeVisitor<T> {

	public T visit(Node node) {
		
		if (node instanceof Index) {
			return visitIndex((Index) node);
		}
		
		 if (node instanceof Relation) {
			 return visitRelation((Relation) node);
		 }
		 
		 if (node instanceof Operator) {
			 return visitOperator((Operator) node);
		 }
		 
		 if (node instanceof SearchTerm) {
			 return visitSearchTerm((SearchTerm) node);
		 }
		 
		 if (node instanceof CommentClause) {
			 return visitCommentClause((CommentClause) node);
		 }
		 
		 if (node instanceof SearchClause) {
			 return visitSearchClause((SearchClause) node);
		 }
		 
		 if (node instanceof ReferenceClause) {
			 return visitReferenceClause((ReferenceClause) node);
		 }
		 
		 if (node instanceof PrefixClause) {
			 return visitPrefixClause((PrefixClause) node);
		 }
		 
		 return null;
	}
	
	public T visitChildren(Node node) {
		T result = defaultResult();
		if(node.hasChildren()) {
			int n = node.getChildCount();
			for (int i=0; i<n; i++) {
				Node c = node.getChild(i);
				T childResult = visit(c);
				result = aggregateResult(result, childResult);
			}
		}
		
		return result;
	}
	
	public T visitIndex(Index index) {
		return visitChildren(index);
	}
	
	public T visitRelation(Relation relation) {
		return visitChildren(relation);
	}

	public T visitOperator(Operator operator) {
		return visitChildren(operator);
	}
	
	public T visitSearchTerm(SearchTerm searchTerm) {
		return visitChildren(searchTerm);
	}
	
	public T visitClause(Clause clause) {
		return visitChildren(clause);
	}
	
	public T visitCommentClause(CommentClause commentClause) {
		return visitChildren(commentClause);
	}
	
	public T visitSearchClause(SearchClause searchClause) {
		return visitChildren(searchClause);
	}

	public T visitReferenceClause(ReferenceClause referenceClause) {
		return visitChildren(referenceClause);
	}
	
	public T visitPrefixClause(PrefixClause prefixClause) {
		return visitChildren(prefixClause);
	}
	
	protected T aggregateResult(T aggregate, T nextResult) {
		return nextResult;
	}
	
	protected T defaultResult() {
		return null;
	}
}
