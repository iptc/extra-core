package org.iptc.extra.core.eql.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manosetro
 *
 *	Node class represents a single node in the syntax tree.
 *	
 */
public abstract class Node {
	
	protected Node parent;	// the parent node of the current node
	
	protected List<Node> children = new ArrayList<Node>();	// a list of children
	
	protected List<Node> errors = new ArrayList<Node>();	// a list of nodes, correspond to syntax error  
	
	protected int depth;	// the depth of that node in the syntax tree
	
	protected boolean valid = true;	// indicates whether that node is valid or not
	
	public abstract boolean hasChildren();

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	public Node getChild(int index) {
		return children.get(index);
	}
	
	public boolean addError(Node error) {
		return errors.add(error);
	}
	
	public List<Node> getErrors() {
		return errors;
	}
	
	public Node getError(int index) {
		return errors.get(index);
	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	 
}
