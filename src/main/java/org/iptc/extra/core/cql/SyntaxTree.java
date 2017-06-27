package org.iptc.extra.core.cql;

import java.util.ArrayList;
import java.util.List;

import org.iptc.extra.core.cql.tree.Node;

/**
 * @author manosetro
 *
 *	SyntaxTree class is the result of parsing of a given EXTRA rule. 
 *	
 */
public class SyntaxTree {

		private Node root;
	
		private List<SyntaxError> errors = new ArrayList<SyntaxError>();


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