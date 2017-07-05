package org.iptc.extra.core.eql.tree.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iptc.extra.core.eql.EQLParser;
import org.iptc.extra.core.eql.tree.SyntaxTree;
import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.CommentClause;
import org.iptc.extra.core.eql.tree.nodes.Index;
import org.iptc.extra.core.eql.tree.nodes.Node;
import org.iptc.extra.core.eql.tree.nodes.Operator;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;
import org.iptc.extra.core.eql.tree.nodes.ReferenceClause;
import org.iptc.extra.core.eql.tree.nodes.Relation;
import org.iptc.extra.core.eql.tree.nodes.SearchClause;
import org.iptc.extra.core.eql.tree.nodes.SearchTerm;
import org.iptc.extra.core.eql.tree.visitor.SyntaxTreeVisitor;
import org.iptc.extra.core.types.Rule;
import org.iptc.extra.core.types.Schema;

import edu.stanford.nlp.util.StringUtils;

/**
 * 
 * @author manos schinas
 *
 * This class contains a set of static methods, for the processing of syntax true produced by EQL parser
 * 
 */
public class TreeUtils {
	
	// Checks whether the tree starting to root node is valid
	public static boolean isTreeValid(Node root) {
		// iterate over relation nodes. Return false (rule is invalid) if any relation is invalid
		for(Relation relation : getRelations(root)) {
			if(!relation.isValid()) {
				return false;
			}
		}
		
		// iterate over operator nodes. Return false (rule is invalid) if any operator is invalid
		for(Operator operator : getOperators(root)) {
			if(!operator.isValid()) {
				return false;
			}
		}
		return true;
	}
	
	// Iterate over relation and operator nodes and return any invalid node
	public static List<Node> getInvalidNodes(Node root) {
		List<Node> nodes = new ArrayList<Node>();
		for(Relation relation : getRelations(root)) {
			if(!relation.isValid()) {
				nodes.add(relation);
			}
		}
		for(Operator operator : getOperators(root)) {
			if(!operator.isValid()) {
				nodes.add(operator);
			}
		}
		
		return nodes;
	}
	
	/* 
	 * checks whether the tree, starting at root, matches the given schema. Returns invalid fields/indices
	 */
	public static Set<String> validateSchema(Node root, Schema schema) {
		SyntaxTreeVisitor<Set<String>> visitor = new SyntaxTreeVisitor<Set<String>>() {
			public Set<String> visitIndex(Index index) {
				Set<String> indices = new HashSet<String>();
				
				if(!schema.getFieldNames().contains(index.getName()) && !"text_content".equals(index.getName())) {
					index.setValid(false);
					indices.add(index.getName());
				}
				return indices;
			}
			
			protected Set<String> aggregateResult(Set<String> aggregate, Set<String> nextResult) {
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
			protected Set<String> defaultResult() {
				return new HashSet<String>();
			}
		};
		
		if(schema == null) {
			return new HashSet<String>();
		}
		
		Set<String> indices = visitor.visit(root);
		return indices;
		
	}
	
	/*
	 * Validate tree across multiple schemas
	 */
	public static Map<String, Set<String>> validateSchema(Node root, List<Schema> schemas) {
		
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		for(Schema schema : schemas) {
			Set<String> fields = schema.getFieldNames();
			Set<String> indices = getIndices(root);
		
			indices.retainAll(fields);
			
			map.put(schema.getId(), indices);
		}
		
		return map;
	}
	
	/* 
	 * Traverse the tree and returns a set of indices
	 */
	public static Set<String> getIndices(Node root) {
		SyntaxTreeVisitor<Set<String>> visitor = new SyntaxTreeVisitor<Set<String>>() {
			
			public Set<String> visitIndex(Index index) {
				Set<String> indices = new HashSet<String>();
				indices.add(index.getName());
			
				return indices;
			}
			
			@Override
			protected Set<String> aggregateResult(Set<String> aggregate, Set<String> nextResult) {	
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
			@Override
			protected Set<String> defaultResult() {
				return new HashSet<String>();
			}
			
		};
		
		Set<String> indices = visitor.visit(root);
		return indices;
	}
	
	/* 
	 * Traverse the tree and returns a set of relation nodes
	 */
	public static Set<Relation> getRelations(Node root) {
		SyntaxTreeVisitor<Set<Relation>> visitor = new SyntaxTreeVisitor<Set<Relation>>() {
			public Set<Relation> visitRelation(Relation relation) {
				Set<Relation> relations = new HashSet<Relation>();
				relations.add(relation);
				
				return relations;
			}
			
			protected Set<Relation> aggregateResult(Set<Relation> aggregate, Set<Relation> nextResult) {
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
			protected Set<Relation> defaultResult() {
				return new HashSet<Relation>();
			}
		};
		
		Set<Relation> relations = visitor.visit(root);
		return relations;
	}
	
	/* 
	 * Traverse the tree and returns a set of nodes, corresponding to search clauses:
	 * 
	 * SearchClause: Index Relation SearchTerm
	 * 
	 * e.g.	title any "term1 term2"
	 * 
	 */
	public static Set<SearchClause> getSearchClauses(Node root) {
		SyntaxTreeVisitor<Set<SearchClause>> visitor = new SyntaxTreeVisitor<Set<SearchClause>>() {
			public Set<SearchClause> visitSearchClause(SearchClause searchClause) {
				Set<SearchClause> searchClauses = new HashSet<SearchClause>();
				searchClauses.add(searchClause);
				
				return searchClauses;
			}
			
			protected Set<SearchClause> aggregateResult(Set<SearchClause> aggregate, Set<SearchClause> nextResult) {
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
			protected Set<SearchClause> defaultResult() {
				return new HashSet<SearchClause>();
			}
		};
		
		Set<SearchClause> searchClauses = visitor.visit(root);
		return searchClauses;
	}
	
	/* 
	 * Traverse the tree and returns a set of nodes, corresponding to search clauses having no idnex and relation
	 * 
	 * e.g. "term1 term2"
	 * 
	 */
	public static Set<SearchClause> getSearchTermClauses(Node root) {
		SyntaxTreeVisitor<Set<SearchClause>> visitor = new SyntaxTreeVisitor<Set<SearchClause>>() {
			public Set<SearchClause> visitSearchClause(SearchClause searchClause) {
				Set<SearchClause> searchClauses = new HashSet<SearchClause>();
				
				if(!searchClause.hasIndex()) {
					searchClauses.add(searchClause);
				}
				
				return searchClauses;
			}
			
			protected Set<SearchClause> aggregateResult(Set<SearchClause> aggregate, Set<SearchClause> nextResult) {
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
			protected Set<SearchClause> defaultResult() {
				return new HashSet<SearchClause>();
			}
		};
		
		Set<SearchClause> searchClauses = visitor.visit(root);
		return searchClauses;
	}
	
	/* 
	 * Traverse the tree and returns a set of operator nodes
	 */
	public static Set<Operator> getOperators(Node root) {
		SyntaxTreeVisitor<Set<Operator>> visitor = new SyntaxTreeVisitor<Set<Operator>>() {
			public Set<Operator> visitOperator(Operator operator) {
				Set<Operator> operators = new HashSet<Operator>();
				operators.add(operator);
				
				return operators;
			}
			
			protected Set<Operator> aggregateResult(Set<Operator> aggregate, Set<Operator> nextResult) {
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
			protected Set<Operator> defaultResult() {
				return new HashSet<Operator>();
			}
		};
		
		Set<Operator> operators = visitor.visit(root);
		return operators;
	}
	
	/*
	 * iterates over a list of clauses and checks whether all of them are search clauses
	 */
	public static boolean areSearchClauses(Collection<Clause> clauses) {
		for(Clause clause : clauses) {
			if(clause instanceof CommentClause) {
				continue;
			}
			if(clause instanceof PrefixClause) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * iterates over a list of clauses and checks whether all of them are search clauses without index and relation
	 */
	public static boolean areSearchTermClauses(Collection<Clause> clauses) {
		for(Clause clause : clauses) {
			if(clause instanceof CommentClause) {
				continue;
			}
			if(clause instanceof PrefixClause) {
				return false;
			}
			if(clause instanceof ReferenceClause) {
				return false;
			}
			if(clause instanceof SearchClause) {
				SearchClause searchClause = (SearchClause) clause;
				if(searchClause.hasIndex()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/* 
	 * Iterates over a list of clauses and create a SearchTerm produced by the concatenation of underlying SearchTerms.
	 * 
	 * Clauses other than search clauses are ignored
	 * 
	 */
	public static SearchTerm mergeSearchTerm(List<Clause> clauses) {
		SearchTerm mergedSearchTerm = new SearchTerm();
		
		List<String> mergedTerms = new ArrayList<String>();
		for(Clause clause : clauses) {
			if(clause instanceof SearchClause) {	// use only SearchClause statements
				SearchClause searchClause = (SearchClause) clause;
				
				SearchTerm searchTerms = searchClause.getSearchTerm();
				if(searchTerms.isRegexp()) {
					mergedTerms.add(searchTerms.getRegexp(false));
				}
				else {
					List<String> terms = searchTerms.getTerms();
					mergedTerms.add(StringUtils.join(terms, " "));
				}
			}
		}
		
		mergedSearchTerm.setTerms(mergedTerms);
		return mergedSearchTerm;
	}
	
	/* 
	 * Traverse the tree and returns a set of index nodes
	 */
	public static List<String> getIndices(List<SearchClause> searchClauses) {
		Set<String> set = new HashSet<String>();
		for(SearchClause searchClause : searchClauses) {
			if(searchClause.hasIndex()) {
				set.add(searchClause.getIndex().getName());
			}
		}
		
		return new ArrayList<String>(set);
	}
	
	/*
	 * Traverse the tree and aggregate reference clauses
	 */
	public static List<ReferenceClause> getReferences(Node root) {
		SyntaxTreeVisitor<List<ReferenceClause>> visitor = new SyntaxTreeVisitor<List<ReferenceClause>>() {
			
			public List<ReferenceClause> visitReferenceClause(ReferenceClause reference) {
				List<ReferenceClause> references = new ArrayList<ReferenceClause>();
				references.add(reference);
			
				return references;
			}
			
			protected List<ReferenceClause> aggregateResult(List<ReferenceClause> aggregate, List<ReferenceClause> nextResult) {
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
			protected List<ReferenceClause> defaultResult() {
				return new ArrayList<ReferenceClause>();
			}
		};
		
		List<ReferenceClause> references = visitor.visit(root);
		return references;
	}
	
	// validate a rule references inside another rule 
	public static void validateReferenceRule(ReferenceClause reference, Schema schema) {
		
		Rule rule = reference.getRule();
		
		String query = rule.getQuery();	
		
		SyntaxTree syntaxTree = EQLParser.parse(query);
		reference.setRuleSyntaxTree(syntaxTree);
		
		//Node root = syntaxTree.getRootNode();
		
		//List<ErrorMessageNode> invalidNodes = ExtraValidator.validate(root, schema);
		//Set<String> unmatchedIndices = TreeUtils.validateSchema(root, schema);
		//List<ReferenceClause> references = TreeUtils.getReferences(root);
	}

	public static boolean isRuleValid(Rule rule, Schema schema) {
		String query = rule.getQuery();	
		
		SyntaxTree syntaxTree = EQLParser.parse(query);
		Node root = syntaxTree.getRootNode();
		
		if(syntaxTree.hasErrors() || root == null) {
			return false;
		}

		return true;
		
		//List<ErrorMessageNode> invalidNodes = ExtraValidator.validate(root, schema);
		//Set<String> unmatchedIndices = TreeUtils.validateSchema(root, schema);
		//List<ReferenceClause> references = TreeUtils.getReferences(root);
	}

}
