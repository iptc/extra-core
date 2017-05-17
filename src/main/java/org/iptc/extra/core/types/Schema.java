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
	
	protected String language;
	
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public List<Field> getFields() {
		return fields;
	}

	public void addField(String name, boolean textual, boolean hasSentences, boolean hasParagraphs) {
		Field field = getField(name);
		if(field != null) {
			int index = fields.indexOf(field);
			field.textual = textual;
			field.hasSentences = hasSentences;
			field.hasParagraphs = hasParagraphs;
			
			fields.add(index, field);
		}
		else {
			field = new Field(name, textual);
			field.textual = textual;
			field.hasSentences = hasSentences;
			field.hasParagraphs = hasParagraphs;
			
			fields.add(field);
		}
	}
	
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public Field getField(String fieldName) {
		for(Field field : fields) {
			if(field.name.equals(fieldName)) {
				return field;
			}
		}
		return null;
	}
	
	public Set<String> getFieldNames() {
		Set<String> fieldsNames = new HashSet<String>();
		for(Field field : fields) {
			fieldsNames.add(field.name);
		}
		return fieldsNames;
	}
	
	public Set<String> getTextualFieldNames() {
		Set<String> fieldsNames = new HashSet<String>();
		for(Field field : fields) {
			if(field.textual) {
				fieldsNames.add(field.name);
			}
		}
		return fieldsNames;
	}
	
	@XmlRootElement(name = "fields")
	public static class Field {
		
		public String name;
		
		public boolean textual = false;	
		public boolean hasSentences = false;
		public boolean hasParagraphs = false;
		
		public Field() {
			
		}
		
		public Field(String name, boolean textual) {
			this.name = name;
			this.textual = textual;
		}

	}
	
}
