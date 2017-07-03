package org.iptc.extra.core.eql.tree.nodes;

public class ErrorMessageNode extends Node {

	private String errorMessage;
	
	@Override
	public boolean hasChildren() {
		return false;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String toString() {
		return errorMessage;
	}
}
