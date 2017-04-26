package org.iptc.extra.core.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("taxonomies")
@XmlRootElement()
public class Taxonomy {

	@Id
	protected String id;
	
	protected String name;

	protected String language;
	
	protected long topics;
	
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

	public long getTopics() {
		return topics;
	}

	public void setTopics(long topics) {
		this.topics = topics;
	}
	
}
