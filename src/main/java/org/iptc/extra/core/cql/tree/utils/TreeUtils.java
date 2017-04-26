package org.iptc.extra.core.cql.tree.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.Node;
import org.iptc.extra.core.cql.tree.visitor.SyntaxTreeVisitor;
import org.iptc.extra.core.types.Schema;

public class TreeUtils {
	
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
}
