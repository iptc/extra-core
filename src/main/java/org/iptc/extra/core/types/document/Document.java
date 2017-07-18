package org.iptc.extra.core.types.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.iptc.extra.core.types.Schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@XmlRootElement()
public class Document extends HashMap<String, DocumentField> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2274574518570705042L;

	private String id;
	
	private List<DocumentTopic> topics = new ArrayList<DocumentTopic>();
			
	public Document() {
		
	}
	
	public Document(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void addField(String key, DocumentField value) {
		this.put(key, value);
	}
	
	public void addField(String key, String value) {
		DocumentField field = new TextField(value);
		this.put(key, field);
	}
	
	public Set<String> getFieldNames() {
		Set<String> fields = new HashSet<String>(this.keySet());
		return fields;
	}
	
	public boolean containsField(String field) {
		return this.containsKey(field);
	}
	
	public List<DocumentTopic> getTopics() {
		return topics;
	}

	public void setTopics(List<DocumentTopic> topics) {
		this.topics = topics;
	}

	public boolean matchSchema(Schema schema) {
		
		Set<String> schemaFields = schema.getFieldNames();
		Set<String> documentFields = getFieldNames();
		documentFields.removeAll(schemaFields);
		
		if(documentFields.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public JsonElement toJson() {
		JsonObject json = new JsonObject();
		for(String fieldName : keySet()) {
			DocumentField field = this.get(fieldName);
			json.add(fieldName, field.toJson());
		}
		return json;
	}
	
	public String toString() {
		JsonElement json = toJson();
		return json.toString();
	}
}
