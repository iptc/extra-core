package org.iptc.extra.core.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

@Entity("corpora")
@XmlRootElement
public class Corpus {

	@Id
	protected String id;
	
	protected String name;
	
	protected String schemaId;

	@Transient 
	protected Schema schema;
	
	protected String taxonomyId;
	
	@Transient 
	protected Taxonomy taxonomy;
	
	protected String language;
	
	protected long documents = 0;
	
	public Corpus() {
		
	}
	
	public Corpus(String id, String name, String schemaId, String taxonomyId) {
		super();
		this.id = id;
		this.name = name;
		this.schemaId = schemaId;
		this.taxonomyId = taxonomyId;
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

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	
	public String getTaxonomyId() {
		return taxonomyId;
	}

	public void setTaxonomyId(String taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public long getDocuments() {
		return documents;
	}

	public void setDocuments(long documents) {
		this.documents = documents;
	}
	
	
}
