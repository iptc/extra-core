package org.iptc.extra.core.eql.tree;

public class Relation extends Modified  {

	private String relation; 
	
	public Relation(String relation) {
		this.relation = relation;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	@Override
	public boolean hasChildren() {
		return false;
	}
	
	public boolean is(String relation) {
		return this.relation.equals(relation);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(relation);
		buffer.append(super.toString());
		return buffer.toString();
	}
}
