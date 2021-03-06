package org.iptc.extra.core.eql.tree.visitor;

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
*	EXTRA2ESQueryVisitor performs a depth-first traversal of the syntax tree and generates an HTML representation. 
*	
*	Example rule:
*	
*	(or
*		(title any "term1 term2")
*		(and
*			(body adj "term3 term4")
*			(body any "term5")
*		)
*	)
*
*	Transformed to the following HTML section:
*
*	<div class="prefixClause" data-depth="0" operator="OR"> (
*		<div class="booleanOp" data-valid="true" data-depth="1"> or </div>
*		<div class="searchClause" data-depth="1"> (
*			<div class="index" data-valid="true" data-depth="2"> title </div> 
*			<div class="relation" data-valid="true" data-depth="2"> any </div>
*			<div class="searchTerm" data-depth="2"> "term1 term2" </div>
*		)</div> 
*		<div class="prefixClause" data-depth="1" operator="AND"> (
*			<div class="booleanOp" data-valid="true" data-depth="2">and</div>
*			<div class="searchClause" data-depth="2">(
*				<div class="index" data-valid="true" data-depth="3"> body </div> 
*				<div class="relation" data-valid="true" data-depth="3"> adj </div>
*				<div class="searchTerm" data-depth="3"> "term3 term4" </div>
*			)</div> 
*			<div class="searchClause" data-depth="2"> (
*				<div class="index" data-valid="true" data-depth="3"> body </div> 
*				<div class="relation" data-valid="true" data-depth="3"> any </div>
*				<div class="searchTerm" data-depth="3"> "term5" </div>
*			)</div> 
*		)</div>
*	)</div>
*/
public class EQL2HTMLVisitor extends SyntaxTreeVisitor<String> {
	
	private String htmlTag;

	public EQL2HTMLVisitor(String htmlTag) {
		this.htmlTag = htmlTag;
	}
	
	@Override
	public String visitPrefixClause(PrefixClause prefixClause) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<" + htmlTag + " class=\"prefixClause\" data-depth=\"" + prefixClause.getDepth() + "\"" +
				(prefixClause.getEQLOperator()!=null ? " operator=\"" + prefixClause.getEQLOperator() + "\"" : "") + 
				">");
		
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
				+ "data-depth=\"" + operator.getDepth() +  "\">");
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
		buffer.append(visit(searchClause.getSearchTerm()));

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
		
		buffer.append("<" + htmlTag + " class=\"referenceClause\" rule=\"" + referenceClause.getRuleId() + "\""
				+ "data-valid=\"" + referenceClause.isValid() + "\">");
		
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
	public String visitSearchTerm(SearchTerm searchTerm) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<" + htmlTag + " class=\"searchTerm\" data-depth=\"" + searchTerm.getDepth() + "\"> ");
		buffer.append(searchTerm);
		buffer.append(" </" + htmlTag + ">");
		
		return buffer.toString();
	}
}