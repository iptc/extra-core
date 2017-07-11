package org.iptc.extra.core.eql.tree.visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iptc.extra.core.daos.RulesDAO;
import org.iptc.extra.core.eql.EQLParser;
import org.iptc.extra.core.eql.tree.SyntaxTree;
import org.iptc.extra.core.eql.tree.nodes.ErrorMessageNode;
import org.iptc.extra.core.eql.tree.nodes.ReferenceClause;
import org.iptc.extra.core.types.Rule;

public class ReferenceClausesVisitor extends SyntaxTreeVisitor<List<ErrorMessageNode>> {

	private RulesDAO dao;

	private Set<String> ruleIds = new HashSet<String>();
	
	public ReferenceClausesVisitor(RulesDAO dao, String rootRuleId) {
		this.dao = dao;
		ruleIds.add(rootRuleId);
	}
	
	@Override
	public List<ErrorMessageNode> visitReferenceClause(ReferenceClause referenceClause) {
		
		List<ErrorMessageNode> errors = new ArrayList<ErrorMessageNode>();
		
		String ruleId = referenceClause.getRuleId();
		if(ruleIds.contains(ruleId)) {
			
			referenceClause.setValid(false);
			
			ErrorMessageNode errorNode = new ErrorMessageNode();
			errorNode.setErrorMessage("Cyclic reference: " + ruleIds + " - " + ruleId);

			errors.add(errorNode);
		}
		else {
			ruleIds.add(ruleId);
			Rule rule = dao.get(ruleId);
			if(rule != null) {
				referenceClause.setRule(rule);
			
				String referencedEql = rule.getQuery();
				SyntaxTree referencedSyntaxTree = EQLParser.parse(referencedEql);
				referenceClause.setRuleSyntaxTree(referencedSyntaxTree);
				
				if(!referencedSyntaxTree.hasErrors() && referencedSyntaxTree.getRootNode() != null) {
					visit(referencedSyntaxTree.getRootNode());
				}
				else {
					referenceClause.setValid(false);
					
					ErrorMessageNode errorNode = new ErrorMessageNode();
					errorNode.setErrorMessage("Referenced rule " + ruleId + " has invalid syntax.");

					errors.add(errorNode);
				}
			}
			else {
				referenceClause.setValid(false);
				
				ErrorMessageNode errorNode = new ErrorMessageNode();
				errorNode.setErrorMessage("Referenced rule " + ruleId + " does not exist.");

				errors.add(errorNode);
			}
		}
		
		return errors;
	}
	
	protected List<ErrorMessageNode> aggregateResult(List<ErrorMessageNode> aggregate, List<ErrorMessageNode> nextResult) {
		aggregate.addAll(nextResult);
		return aggregate;
	}
	
	protected List<ErrorMessageNode> defaultResult() {
		return new ArrayList<ErrorMessageNode>();
	}
}
