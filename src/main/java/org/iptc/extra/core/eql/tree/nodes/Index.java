package org.iptc.extra.core.eql.tree;

public class Index extends Node {

	private String name;
	
	public Index(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public String toString() {
		return name;
	}
}
