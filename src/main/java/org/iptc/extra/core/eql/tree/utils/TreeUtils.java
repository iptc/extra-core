package org.iptc.extra.core.eql.tree.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iptc.extra.core.eql.EQLParser;
import org.iptc.extra.core.eql.SyntaxTree;
import org.iptc.extra.core.eql.tree.Clause;
import org.iptc.extra.core.eql.tree.CommentClause;
import org.iptc.extra.core.eql.tree.Index;
import org.iptc.extra.core.eql.tree.Node;
import org.iptc.extra.core.eql.tree.Operator;
import org.iptc.extra.core.eql.tree.PrefixClause;
import org.iptc.extra.core.eql.tree.ReferenceClause;
import org.iptc.extra.core.eql.tree.Relation;
import org.iptc.extra.core.eql.tree.SearchClause;
import org.iptc.extra.core.eql.tree.SearchTerms;
import org.iptc.extra.core.eql.tree.visitor.SyntaxTreeVisitor;
import org.iptc.extra.core.types.Rule;
import org.iptc.extra.core.types.Schema;

import edu.stanford.nlp.util.StringUtils;

public class TreeUtils {
	
	public static boolean isTreeValid(Node root) {
		for(Relation relation : getRelations(root)) {
			if(!relation.isValid()) {
				return false;
			}
		}
		for(Operator operator : getOperators(root)) {
			if(!operator.isValid()) {
				return false;
			}
		}
		return true;
	}
	
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
	
	public static SearchTerms mergeSearchTerms(List<Clause> clauses) {
		SearchTerms mergedSearchTerms = new SearchTerms();
		
		List<String> mergedTerms = new ArrayList<String>();
		for(Clause clause : clauses) {
			if(clause instanceof SearchClause) {
				SearchClause searchClause = (SearchClause) clause;
				
				SearchTerms searchTerms = searchClause.getSearchTerms();
				if(searchTerms.isRegexp()) {
					mergedTerms.add(searchTerms.getRegexp(false));
				}
				else {
					List<String> terms = searchTerms.getTerms();
					mergedTerms.add(StringUtils.join(terms, " "));
				}
			}
		}
		
		mergedSearchTerms.setTerms(mergedTerms);
		return mergedSearchTerms;
	}
	
	public static List<String> getIndices(List<SearchClause> searchClauses) {
		Set<String> set = new HashSet<String>();
		for(SearchClause searchClause : searchClauses) {
			if(searchClause.hasIndex()) {
				set.add(searchClause.getIndex().getName());
			}
		}
		
		return new ArrayList<String>(set);
	}
	
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