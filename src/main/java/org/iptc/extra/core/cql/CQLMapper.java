package org.iptc.extra.core.cql;

import org.elasticsearch.index.query.QueryBuilder;

import org.iptc.extra.core.cql.tree.Node;
import org.iptc.extra.core.cql.tree.visitor.CQL2HTMLVisitor;
import org.iptc.extra.core.cql.tree.visitor.CQL2JSTreeVisitor;
import org.iptc.extra.core.cql.tree.visitor.EXTRA2ESQueryVisitor;
import org.iptc.extra.core.cql.tree.visitor.PretifyVisitor;
import org.iptc.extra.core.types.Schema;

/**
 * @author manosetro - Manos Schinas
 *	
 * A wrapper class that exposes several transformation of EXTRA rules.
 * 
 */
public class CQLMapper {

	/*
	 * Transform rule to elastic search query
	 */
	public QueryBuilder toElasticSearchQuery(Node root, Schema schema) {
		if(root == null) {
			return null;
		}	

		EXTRA2ESQueryVisitor visitor = new EXTRA2ESQueryVisitor(schema);
		QueryBuilder qb = visitor.visit(root);
		
		return qb;
	}

	/*
	 * Transform rule to elastic search highlight query
	 */
	public QueryBuilder toElasticSearchHighligh(Node root, Schema schema) {
		if(root == null) {
			return null;
		}	

		EXTRA2ESQueryVisitor visitor = new EXTRA2ESQueryVisitor(schema);
		QueryBuilder qb = visitor.visit(root);
		
		return qb;
	}
	
	/*
	 * Transform rule to html
	 */
	public String toHtml(Node root, String htmlTag) {
		CQL2HTMLVisitor visitor = new CQL2HTMLVisitor(htmlTag);	
		String html = visitor.visit(root);
		
		return html;
	}
	
	public String toString(Node root, String newline, String tab) {
		if(root == null) {
			return null;
		}
	
		PretifyVisitor visitor = new PretifyVisitor(newline, tab);
		return visitor.visit(root);
	}
	
	/*
	 * Transform rule to jstree
	 */
	public String toJSTree(Node root) {
		CQL2JSTreeVisitor visitor = new CQL2JSTreeVisitor();	
		String tree = visitor.visit(root);
		
		return "<ul><br/>" + tree + "</ul>";
	}

	
	
}
