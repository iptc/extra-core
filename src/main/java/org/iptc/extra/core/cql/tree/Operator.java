package org.iptc.extra.core.cql.tree;

public class Operator extends Modified  {

	private String operator; 
	
	private boolean valid = true;
	
	public Operator(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public boolean isOr() {
		return operator != null && operator.contentEquals("or");
	}
	
	public boolean isAnd() {
		return operator != null && operator.contentEquals("and");
	}
	
	public boolean isNot() {
		return operator != null && operator.contentEquals("not");
	}
	
	public boolean isProx() {
		return operator != null && operator.contentEquals("prox");
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(operator);
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
