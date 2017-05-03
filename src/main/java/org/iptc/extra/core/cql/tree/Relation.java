package org.iptc.extra.core.cql.tree;

public class Relation extends Modified  {

	private String relation; 
	
	private boolean valid = true;
	
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

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
