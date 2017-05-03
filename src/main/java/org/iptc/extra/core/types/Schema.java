package org.iptc.extra.core.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("schemas")
@XmlRootElement
public class Schema {

	@Id
	protected String id;
	
	protected String name;
	
	protected List<Field> fields = new ArrayList<Field>();
	
	public Schema() {
		
	}
			
	public Schema(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void addField(String name, boolean textual) {
		if(hasField(name)) {
			Field field = new Field(name, textual);
			int index = fields.indexOf(field);
			fields.add(index, field);
		}
		else {
			Field field = new Field(name, textual);
			fields.add(field);
		}
	}
	
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public boolean hasField(String fieldName) {
		for(Field field : fields) {
			if(field.getName().equals(fieldName)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<String> getFieldNames() {
		Set<String> fieldsNames = new HashSet<String>();
		for(Field field : fields) {
			fieldsNames.add(field.name);
		}
		return fieldsNames;
	}
	
	@XmlRootElement(name = "fields")
	public static class Field {
		
		private String name;
		private boolean textual;	
		
		public Field() {
			
		}
		
		public Field(String name, boolean textual) {
			this.name = name;
			this.setTextual(textual);
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		public boolean isTextual() {
			return textual;
		}

		public void setTextual(boolean textual) {
			this.textual = textual;
		}

	}
	
}
