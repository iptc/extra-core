package org.iptc.extra.core.eql.tree.nodes;

import org.iptc.extra.core.eql.tree.SyntaxTree;
import org.iptc.extra.core.types.Rule;

/**
 * 
 * @author manos schinas
 * 
 * 	A reference clause corresponds to another referenced rule 
 * 
 * 	Example:
 * 	(or	
 * 		(title any "term1 term2")
 * 		(@ref == 5954d2231bac0c2f382b7ca5)
 * 	)
 *
 */
public class ReferenceClause extends Clause {

	private String ruleId;	// the id of the referenced rule

	private Rule rule;	// the referenced rule
	
	private SyntaxTree ruleSyntaxTree;	// the syntax tree of the referenced rule
	
	
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public SyntaxTree getRuleSyntaxTree() {
		return ruleSyntaxTree;
	}

	public void setRuleSyntaxTree(SyntaxTree ruleSyntaxTree) {
		this.ruleSyntaxTree = ruleSyntaxTree;
	}
	
	@Override
	public String toString() {
		return "( @ref == " + ruleId + " )";
	}
}
