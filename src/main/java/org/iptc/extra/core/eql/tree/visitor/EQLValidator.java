package org.iptc.extra.core.eql.tree.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iptc.extra.core.eql.tree.extra.EQLOperator;
import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.CommentClause;
import org.iptc.extra.core.eql.tree.nodes.ErrorMessageNode;
import org.iptc.extra.core.eql.tree.nodes.Index;
import org.iptc.extra.core.eql.tree.nodes.Node;
import org.iptc.extra.core.eql.tree.nodes.Operator;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;
import org.iptc.extra.core.eql.tree.nodes.Relation;
import org.iptc.extra.core.eql.tree.nodes.SearchClause;
import org.iptc.extra.core.eql.tree.nodes.SearchTerm;
import org.iptc.extra.core.eql.tree.utils.TreeUtils;
import org.iptc.extra.core.types.Schema;
import org.iptc.extra.core.types.Schema.Field;

public class EQLValidator extends SyntaxTreeVisitor<List<ErrorMessageNode>> {
	
	private Schema schema;

	public EQLValidator(Schema schema) {
		this.schema = schema;
	}

	public static List<ErrorMessageNode> validate(Node root, Schema schema) {
		EQLValidator validator = new EQLValidator(schema);
	
		List<ErrorMessageNode> invalidNodes = validator.visit(root);
		return invalidNodes;
	}
	
	@Override
	public List<ErrorMessageNode> visitPrefixClause(PrefixClause prefixClause) {
		
		List<ErrorMessageNode> invalidNodes = new ArrayList<ErrorMessageNode>();
		
		Operator operator = prefixClause.getOperator();
		EQLOperator extraOperator = prefixClause.getEQLOperator();
		
		if(extraOperator == EQLOperator.SENTENCE || extraOperator == EQLOperator.NOT_IN_SENTENCE ||
				extraOperator == EQLOperator.PARAGRAPH || extraOperator == EQLOperator.NOT_IN_PARAGRAPH || 
				extraOperator == EQLOperator.DISTANCE || extraOperator == EQLOperator.NOT_WITHIN_DISTANCE || 
				extraOperator == EQLOperator.ORDER || extraOperator == EQLOperator.ORDER_AND_DISTANCE || extraOperator == EQLOperator.NOT_IN_PHRASE) {
			
			if(prefixClause.getClauses().size() != 2) {
				if(prefixClause.getClauses().size() == 1 && (extraOperator == EQLOperator.SENTENCE || extraOperator == EQLOperator.NOT_IN_SENTENCE ||
						extraOperator == EQLOperator.PARAGRAPH || extraOperator == EQLOperator.NOT_IN_PARAGRAPH)) {
					Clause childClause = prefixClause.getClause(0);	
					
					if(!(childClause instanceof PrefixClause) || !EQLOperator.isWordDistanceOperator(((PrefixClause) childClause).getEQLOperator())) {
						ErrorMessageNode node = new ErrorMessageNode();
						node.setErrorMessage(operator.toString() + " (" + extraOperator + ") has invalid sub-statement. Only distance operators are permitted in single statements.");
					
						invalidNodes.add(node);
						operator.setValid(false);
					}
				}
				else {
					ErrorMessageNode node = new ErrorMessageNode();
					node.setErrorMessage(operator.toString() + " (" + extraOperator + ") has invalid number of statement. Only 2 statements are permitted.");
				
					invalidNodes.add(node);
					operator.setValid(false);
				}
			}
			
			int stemmedClauses = 0;
			Set<SearchClause> searchClauses = TreeUtils.getSearchClauses(prefixClause);
			for(SearchClause sc : searchClauses) {
				if(sc.getRelation() != null && sc.getRelation().hasModifier("stemming")) {
					stemmedClauses++;
				}
			}

			if(stemmedClauses > 0 && searchClauses.size() != stemmedClauses) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(operator.toString() + " (" + extraOperator + ") children mixes stemming and non-stemming.");
			
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
				 node.setErrorMessage(operator.toString() + " (" + extraOperator + ") cannot mix index and non-index statements");
				 
				 invalidNodes.add(node);
				 operator.setValid(false);
			}
			else {
				if(schema != null) {
					for(String index : indices) {
						
						Field field = schema.getField(index);
						if(field == null && index.contains("text_content")) {
							continue;
						}
						
						if(!field.hasSentences && (extraOperator == EQLOperator.SENTENCE || extraOperator == EQLOperator.NOT_IN_SENTENCE)) {
							ErrorMessageNode node = new ErrorMessageNode();
							node.setErrorMessage(operator.toString() + " (" + extraOperator + ") cannot be applied on a field (" + index + ") without sentences");
						 
							invalidNodes.add(node);
							operator.setValid(false);
						}
					
						if(!field.hasParagraphs && (extraOperator == EQLOperator.PARAGRAPH || extraOperator == EQLOperator.NOT_IN_PARAGRAPH)) {
							ErrorMessageNode node = new ErrorMessageNode();
							node.setErrorMessage(operator.toString() + " (" + extraOperator + ") cannot be applied on a field (" + index + ") without paragraphs");
						 
							invalidNodes.add(node);
							operator.setValid(false);
						}	
					}
				}
			}
		}
		
		if(extraOperator == EQLOperator.MAXIMUM_OCCURRENCE || extraOperator == EQLOperator.MINIMUM_OCCURRENCE) {
			int clauses = 0;
			for(Clause clause : prefixClause.getClauses()) {
				if(!(clause instanceof CommentClause)) {
					clauses++;
				}
			}
			
			if(clauses != prefixClause.getSearchClause().size()) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(operator.toString() + " can be applied only to search clauses.");
				
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
		
		Index index = searchClause.getIndex();
		Relation relation = searchClause.getRelation();
		if(relation == null || index == null) {
			return invalidRelations;
		}
		
		if(relation != null && !relation.isValid()) {
			ErrorMessageNode node = new ErrorMessageNode();
			node.setErrorMessage(relation.toString() + " is not a valid EXTRA relation");
			
			invalidRelations.add(node);
		}
		
		SearchTerm searchTerm = searchClause.getSearchTerm();
		if(searchTerm.isRegexp()) {
			
			if(relation.hasModifier("stemming")) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(relation.toString() + ". Stemming cannot be mixed with regex: " + searchTerm);
				
				invalidRelations.add(node);
				relation.setValid(false);
			}
			
			if(relation.is(">") || relation.is(">=") || relation.is("<") || relation.is("<=") || relation.is("within") || relation.is(">")) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage(relation.getRelation() + " relation cannot be mixed with regex: " + searchTerm);
			
				invalidRelations.add(node);
				relation.setValid(false);
			}
			
		}
		
		if(relation.hasModifier("regexp") && !searchTerm.isRegexp()) {
			ErrorMessageNode node = new ErrorMessageNode();
			node.setErrorMessage(relation + " has regexp modifier but no regexp has been detected in search term: " + searchTerm);
		
			invalidRelations.add(node);
			relation.setValid(false);
		}
		
		if(relation.hasModifier("masked") && !searchTerm.hasWildCards()) {
			ErrorMessageNode node = new ErrorMessageNode();
			node.setErrorMessage(relation + " has masked modifier but no wildcards have been detected in search term: " + searchTerm);
		
			invalidRelations.add(node);
			relation.setValid(false);
		}
		
		if(relation.hasModifier("stemming") && (relation.is(">") || relation.is(">=") || relation.is("<") || relation.is("<=") || relation.is("within") || relation.is(">"))) {
			ErrorMessageNode node = new ErrorMessageNode();
			node.setErrorMessage(relation.getRelation() + " relation cannot has stemming modifier.");
		
			invalidRelations.add(node);
			relation.setValid(false);
		}
		else if(schema != null && relation.hasModifier("stemming")) {
			Field field = schema.getField(index.getName());
			if(field != null && !field.textual) {
				ErrorMessageNode node = new ErrorMessageNode();
				node.setErrorMessage("Stemming modifier cannot be applied on a non-textual index: " + index.getName());
			
				invalidRelations.add(node);
				relation.setValid(false);
			}
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
