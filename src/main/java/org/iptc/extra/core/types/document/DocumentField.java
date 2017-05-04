package org.iptc.extra.core.types.document;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class DocumentField {

	protected String value;

	public DocumentField() {
		
	}
	
	public DocumentField(String value) {
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
	
}
