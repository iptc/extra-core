package org.iptc.extra.core.cql.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PrefixClause extends Clause {

	private Operator operator;
	
	private List<Clause> clauses = new ArrayList<Clause>();

	private boolean relaxed = false;
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
		children.add(operator);
	}

	public List<Clause> getClauses() {
		return clauses;
	}

	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
		
		children.addAll(clauses);
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		buffer.append(operator);
		buffer.append(" ");
		buffer.append(StringUtils.join(clauses, " "));
		buffer.append(")");
		return buffer.toString();
	}

	public boolean isRelaxed() {
		return relaxed;
	}

	public void setRelaxed(boolean relaxed) {
		this.relaxed = relaxed;
	}
	
}
