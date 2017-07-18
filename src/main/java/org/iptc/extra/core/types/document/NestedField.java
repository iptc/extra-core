package org.iptc.extra.core.types.document;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NestedField extends DocumentField {

	private Map<String, DocumentField> subfields = new HashMap<String, DocumentField>();

	public void addField(String fieldName, DocumentField fieldValue) {
		subfields.put(fieldName, fieldValue);
	}
	
	public void addField(String fieldName, String fieldValue) {
		DocumentField field = new TextField(fieldValue);
		subfields.put(fieldName, field);
	}
	
	public JsonElement toJson() {
		JsonObject json = new JsonObject();
		for(String fieldName : subfields.keySet()) {
			DocumentField field = subfields.get(fieldName);
			json.add(fieldName, field.toJson());
		}
		return json;
	}

	@Override
	public String toString() {
		return toJson().toString();
	}
	
}
