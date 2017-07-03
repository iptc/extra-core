package org.iptc.extra.core.eql.tree.nodes;

public class Modifier extends Node {
	
	private String modifier;
	private String comparitor;
	private String value;
	
	public Modifier(String modifier) {
		this.modifier = modifier;
	}
	
	public Modifier(String modifier, String comparitor, String value) {
		this.modifier = modifier;
		this.comparitor = comparitor;
		this.value = value;
	}
	
	public String getModifier() {
		return modifier;
	}
	
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	public String getComparitor() {
		return comparitor;
	}
	
	public void setComparitor(String comparitor) {
		this.comparitor = comparitor;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public boolean hasValue() {
		return (value != null && comparitor != null);
	}
	
	public boolean valueEquals(String value) {
		return value.equals(this.value);
	}
	
	public boolean isComparitorLT() {
		return hasValue() && comparitor.equals("<");
	}
	
	public boolean isComparitorLTE() {
		return hasValue() && comparitor.equals("<=");
	}

	public boolean isComparitorGT() {
		return hasValue() && comparitor.equals(">");
	}
	
	public boolean isComparitorGTE() {
		return hasValue() && comparitor.equals(">=");
	}
	
	public boolean isComparitorEQ() {
		return hasValue() && comparitor.equals("=");
	}
	
	@Override
	public String toString() {
		return "/" + (hasValue() ? modifier + comparitor + value : modifier);
	}

	@Override
	public boolean hasChildren() {
		return false;
	}
	
}
