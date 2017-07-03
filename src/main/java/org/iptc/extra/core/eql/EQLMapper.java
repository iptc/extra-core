package org.iptc.extra.core.eql;

import org.elasticsearch.index.query.QueryBuilder;
import org.iptc.extra.core.eql.tree.nodes.Node;
import org.iptc.extra.core.eql.tree.visitor.EQL2ESQueryVisitor;
import org.iptc.extra.core.eql.tree.visitor.EQL2HTMLVisitor;
import org.iptc.extra.core.eql.tree.visitor.EQL2HighlightVisitor;
import org.iptc.extra.core.eql.tree.visitor.EQL2JSTreeVisitor;
import org.iptc.extra.core.eql.tree.visitor.PretifyVisitor;
import org.iptc.extra.core.types.Schema;

/**
 * @author manosetro - Manos Schinas
 *	
 * A wrapper class that exposes several transformation of EXTRA rules.
 * 
 */
public class EQLMapper {

	/*
	 * Transforms a rule to elastic search query
	 */
	public QueryBuilder toElasticSearchQuery(Node root, Schema schema) {
		if(root == null) {
			return null;
		}	

		EQL2ESQueryVisitor visitor = new EQL2ESQueryVisitor(schema);
		QueryBuilder qb = visitor.visit(root);
		
		return qb;
	}

	/*
	 * Transforms a rule to elastic search highlight query
	 * That's a relaxed version of the ES query. 
	 */
	public QueryBuilder toElasticSearchHighlight(Node root, Schema schema) {
		if(root == null) {
			return null;
		}	

		EQL2HighlightVisitor visitor = new EQL2HighlightVisitor(schema);
		QueryBuilder qb = visitor.visit(root);
		
		return qb;
	}
	
	/*
	 * Transforms a rule to html
	 */
	public String toHtml(Node root, String htmlTag) {
		EQL2HTMLVisitor visitor = new EQL2HTMLVisitor(htmlTag);	
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
	 * Transforms a rule to jstree
	 */
	public String toJSTree(Node root) {
		EQL2JSTreeVisitor visitor = new EQL2JSTreeVisitor();	
		String tree = visitor.visit(root);
		
		return "<ul><br/>" + tree + "</ul>";
	}

	
	
}
