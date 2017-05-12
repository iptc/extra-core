package org.iptc.extra.core.types.document;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TextField extends DocumentField {

	protected String value;
	
	public TextField() {
		
	}
	
	public TextField(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public JsonElement toJson() {
		return new JsonPrimitive(value);
	}
	
	public String toString() {
		return value;
	}
}
