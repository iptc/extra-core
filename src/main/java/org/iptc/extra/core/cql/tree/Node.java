package org.iptc.extra.core.cql.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
	
	protected Node parent;
	
	protected List<Node> children = new ArrayList<Node>();
	
	protected List<Node> errors = new ArrayList<Node>();
	
	protected int depth;
	
	protected boolean valid = true;
	
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
