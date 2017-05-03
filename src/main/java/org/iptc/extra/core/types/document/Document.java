package org.iptc.extra.core.types;

import java.util.HashMap;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;


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
		return this.keySet();
	}
	
	public boolean containsField(String field) {
		return this.containsKey(field);
	}
	
}
