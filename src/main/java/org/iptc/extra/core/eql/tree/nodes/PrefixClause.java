package org.iptc.extra.core.eql.tree.nodes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.iptc.extra.core.eql.tree.extra.EQLOperator;

public class PrefixClause extends Clause {

	private Operator operator;
	private EQLOperator eqlOperator;
	
	private List<Clause> clauses = new ArrayList<Clause>();

	private boolean relaxed = false;
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
		children.add(operator);
	}

	public EQLOperator getEQLOperator() {
		return eqlOperator;
	}

	public void setEQLOperator(EQLOperator validOperator) {
		this.eqlOperator = validOperator;
	}
	
	public List<Clause> getClauses() {
		return clauses;
	}
	
	public Clause getClause(int index) {
		return clauses.get(index);
	}
	
	public List<SearchClause> getSearchClause() {
		 List<SearchClause> searchClauses = new ArrayList<SearchClause>();
		 for(Clause clause : clauses) {
			 if(clause instanceof SearchClause) {
				 searchClauses.add((SearchClause) clause);
			 }
		 }
		 return searchClauses;
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
