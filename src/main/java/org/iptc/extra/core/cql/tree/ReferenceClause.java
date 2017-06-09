package org.iptc.extra.core.cql.tree;

import org.iptc.extra.core.cql.SyntaxTree;
import org.iptc.extra.core.types.Rule;

public class ReferenceClause extends Clause {

	private String ruleId;

	private Rule rule;
	
	private SyntaxTree ruleSyntaxTree;
	
	
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
	
}
