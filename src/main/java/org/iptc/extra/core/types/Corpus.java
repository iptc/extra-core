package org.iptc.extra.core.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("corpora")
@XmlRootElement
public class Corpus {

	@Id
	protected String id;
	
	protected String name;
	
	protected String schemaId;
	
	protected long documents = 0;
	
	
	public Corpus() {
		
	}
	
	public Corpus(String id, String name, String schemaId) {
		super();
		this.id = id;
		this.name = name;
		this.schemaId = schemaId;
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

	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public long getDocuments() {
		return documents;
	}

	public void setDocuments(long documents) {
		this.documents = documents;
	}
	
	
}
