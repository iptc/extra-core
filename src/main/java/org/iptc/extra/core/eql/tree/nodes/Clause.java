package org.iptc.extra.core.eql.tree.nodes;

/**
 * 
 * @author manos schinas
 * 
 * 	Clause class corresponds to a statement in an EQL query
 * 
 * 	Can be one of the following:
 * 		prefix clause (org.iptc.extra.core.eql.tree.nodes.PrefixClause)
 * 		search clause (org.iptc.extra.core.eql.tree.nodes.SearchClause)
 * 		comment clause (org.iptc.extra.core.eql.tree.nodes.CommentClause)
 * 		reference clause (org.iptc.extra.core.eql.tree.nodes.ReferenceClause)
 *		
 */
public class Clause extends Node {

	@Override
	public boolean hasChildren() {
		return true;
	}

}
