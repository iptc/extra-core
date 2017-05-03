package org.iptc.extra.core.types.document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.iptc.extra.core.types.Schema;


@XmlRootElement()
public class Document extends HashMap<String, Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2274574518570705042L;

	public Document() {
		
	}
	
	public Document(String id) {
		this.put("id", id);
	}

	public String getId() {
		return (String) this.get("id");
	}

	public void setId(String id) {
		this.put("id", id);
	}
	
	public void addField(String key, Object value) {
		this.put(key, value);
	}
	
	public Set<String> getFields() {
		Set<String> fields = new HashSet<String>(this.keySet());
		return fields;
	}
	
	public boolean containsField(String field) {
		return this.containsKey(field);
	}
	
	public boolean matchSchema(Schema schema) {
		
		Set<String> schemaFields = schema.getFieldNames();
		Set<String> documentFields = getFields();
		documentFields.removeAll(schemaFields);
		
		if(documentFields.isEmpty()) {
			return true;
		}
		
		return false;
	}
}
