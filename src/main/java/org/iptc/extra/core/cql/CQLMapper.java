package org.iptc.extra.core.cql;


import org.elasticsearch.index.query.QueryBuilder;

import org.iptc.extra.core.cql.tree.Node;
import org.iptc.extra.core.cql.tree.visitor.CQL2HTMLVisitor;
import org.iptc.extra.core.cql.tree.visitor.CQL2JSTreeVisitor;
import org.iptc.extra.core.cql.tree.visitor.EXTRA2ESVisitor;
import org.iptc.extra.core.cql.tree.visitor.PretifyVisitor;

public class CQLMapper {

	public QueryBuilder toElasticSearch(Node root) {
		if(root == null) {
			return null;
		}	

		EXTRA2ESVisitor visitor = new EXTRA2ESVisitor();
		
		QueryBuilder qb = visitor.visit(root);
		
		return qb;
	}

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
	
	
	public String toJSTree(Node root) {
		CQL2JSTreeVisitor visitor = new CQL2JSTreeVisitor();	
		String tree = visitor.visit(root);
		
		return "<ul><br/>" + tree + "</ul>";
	}

	
	
}
