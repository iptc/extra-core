package org.iptc.extra.core.eql.tree.nodes;

/**
 * 
 * @author manos schinas
 * 
 * A node that corresponds to a syntax error produced during eql parsing by antlr
 *
 */
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
