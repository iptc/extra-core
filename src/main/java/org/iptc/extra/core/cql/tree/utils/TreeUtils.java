package org.iptc.extra.core.cql.tree.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.Node;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.Relation;
import org.iptc.extra.core.cql.tree.visitor.SyntaxTreeVisitor;
import org.iptc.extra.core.types.Schema;

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
	
	public static Set<String> validateSchema(Node root, Schema schema) {
		Set<String> fields = schema.getFieldNames();
		Set<String> indices = getIndices(root);
		
		indices.retainAll(fields);
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
			
			protected Set<String> aggregateResult(Set<String> aggregate, Set<String> nextResult) {
				aggregate.addAll(nextResult);
				return aggregate;
			}
			
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
	
}
