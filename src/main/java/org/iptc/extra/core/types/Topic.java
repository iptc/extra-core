package org.iptc.extra.core.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity("topics")
@Indexes(@Index(fields = {@Field("topicId"), @Field(value = "taxonomyId")}))
@XmlRootElement()
public class Topic {
	
	@Id
	protected String id;

	protected String topicId;
	
	protected String name;
	
	protected String definition;
	
	protected String parentTopic;

	protected String taxonomyId;

	protected String label;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTopicId() {
		return topicId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public String getParentTopic() {
		return parentTopic;
	}

	public void setParentTopic(String parentTopic) {
		this.parentTopic = parentTopic;
	}
	
	public String getTaxonomyId() {
		return taxonomyId;
	}

	public void setTaxonomyId(String taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
