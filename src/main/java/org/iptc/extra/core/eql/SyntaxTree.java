package org.iptc.extra.core.eql;

import java.util.ArrayList;
import java.util.List;

import org.iptc.extra.core.eql.tree.nodes.Node;

/**
 * @author manosetro
 *
 *	SyntaxTree class is the result of parsing of a given EQL rule. 
 *	
 */
public class SyntaxTree {

		private Node root;	// the root node of the syntax tree. Is the entry node for each class used to traverse the tree.
	
		private List<SyntaxError> errors = new ArrayList<SyntaxError>();	// a list of syntax errors produced by Antlr during parsing

		public List<SyntaxError> getErrors() {
			return errors;
		}

		public void setErrors(List<SyntaxError> errors) {
			this.errors = errors;
		}
		
	    public boolean hasErrors() {
	        return errors.size() > 0;
	    }

		public Node getRootNode() {
			return root;
		}

		public void setRootNode(Node root) {
			this.root = root;
		}
	    
	}