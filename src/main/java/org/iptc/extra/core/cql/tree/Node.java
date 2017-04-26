package org.iptc.extra.core.cql.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
	
	protected Node parent;
	
	protected List<Node> children = new ArrayList<Node>();
	
	protected int depth;
	
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
}
