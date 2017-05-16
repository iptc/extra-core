package org.iptc.extra.core.cql.tree.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iptc.extra.core.cql.tree.ErrorMessageNode;
import org.iptc.extra.core.cql.tree.Node;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.Relation;
import org.iptc.extra.core.cql.tree.SearchClause;
import org.iptc.extra.core.cql.tree.SearchTerms;
import org.iptc.extra.core.cql.tree.extra.ExtraOperator;
import org.iptc.extra.core.cql.tree.utils.TreeUtils;

public class ExtraValidator extends SyntaxTreeVisitor<List<ErrorMessageNode>> {
	
	public static List<ErrorMessageNode> validate(Node root) {
		ExtraValidator validator = new ExtraValidator();
	
		List<ErrorMessageNode> invalidNodes = validator.visit(root);
		return invalidNodes;
	}
	
	@Override
	public List<ErrorMessageNode> visitPrefixClause(PrefixClause prefixClause) {
		
		List<ErrorMessageNode> invalidNodes = new ArrayList<ErrorMessageNode>();
		
		Operator operator = prefixClause.getOperator();
		ExtraOperator extraOperator = prefixClause.getExtraOperator();
		
		if(extraOperator == ExtraOperator.SENTENCE || extraOperator == ExtraOperator.NOT_IN_SENTENCE ||
				extraOperator == ExtraOperator.PARAGRAPH || extraOperator == ExtraOperator.NOT_IN_PARAGRAPH || 
				extraOperator == ExtraOperator.DISTANCE || extraOperator == ExtraOperator.NOT_WITHIN_DISTANCE || 
				extraOperator == ExtraOperator.ORDER || extraOperator == ExtraOperator.ORDER_AND_DISTANCE || extraOperator == ExtraOperator.NOT_IN_PHRASE) {
			
			if(prefixClause.getClauses().size() != 2) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(operator.toString() + " (" + extraOperator + ") has invalid number of statement. Only 2 statements are permitted.");
				
				invalidNodes.add(node);
				operator.setValid(false);
			}
			
			Set<String> indices = TreeUtils.getIndices(prefixClause);
			Set<SearchClause> searchTermClauses = TreeUtils.getSearchTermClauses((prefixClause));
			
			if(indices.size() > 1) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(operator.toString() + " (" + extraOperator 
						+ ") has invalid number of indices: " + indices
						+ ". Only 1 or no index is permitted.");
				
				invalidNodes.add(node);
				operator.setValid(false);
			}
			else if(!indices.isEmpty() && !searchTermClauses.isEmpty()) {
				 ErrorMessageNode node = new ErrorMessageNode();
				 node.setErrorMessage(operator.toString() + " (" + extraOperator + ") connot mix index and non-index statements");
				 
				 invalidNodes.add(node);
				 operator.setValid(false);
			 }
		}
		
		if(extraOperator == null) {
			ErrorMessageNode node = new ErrorMessageNode();
			node.setErrorMessage(operator.toString() + " is not a valid EXTRA operator");
			
			invalidNodes.add(node);
			operator.setValid(false);
		}
		
		invalidNodes.addAll(visitChildren(prefixClause));
		
		return invalidNodes;
		
	}
	
	public List<ErrorMessageNode> visitSearchClause(SearchClause searchClause) {
		
		List<ErrorMessageNode> invalidRelations = new ArrayList<ErrorMessageNode>();
		
		Relation relation = searchClause.getRelation();
		if(relation != null && !relation.isValid()) {
			ErrorMessageNode node = new ErrorMessageNode();
			node.setErrorMessage(relation.toString() + " is not a valid EXTRA relation");
			
			invalidRelations.add(node);
		}
		
		SearchTerms searchTerms = searchClause.getSearchTerms();
		if(searchTerms.hasWildcards()) {
			
			if(relation.hasModifier("stemming")) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(relation.toString() + ". Stemming cannot be mixed with wildcards: " + searchTerms);
				
				invalidRelations.add(node);
				relation.setValid(false);
			}
			
			if(relation.is(">") || relation.is(">=") || relation.is("<") || relation.is("<=") || relation.is("within") || relation.is(">")) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(relation.getRelation() + " relation cannot be mixed with wildcards: " + searchTerms);
			
				invalidRelations.add(node);
				relation.setValid(false);
			}
			
		}
		
		if(relation.hasModifier("stemming") && (relation.is(">") || relation.is(">=") || relation.is("<") || relation.is("<=") || relation.is("within") || relation.is(">"))) {
			ErrorMessageNode node = new ErrorMessageNode();
			node.setErrorMessage(relation.getRelation() + " relation cannot has stemming modifier.");
		
			invalidRelations.add(node);
			relation.setValid(false);
		}
		
		return invalidRelations;
	}
	
	protected List<ErrorMessageNode> aggregateResult(List<ErrorMessageNode> aggregate, List<ErrorMessageNode> nextResult) {
		aggregate.addAll(nextResult);
		return aggregate;
	}
	
	protected List<ErrorMessageNode> defaultResult() {
		return new ArrayList<ErrorMessageNode>();
	}
	
}
