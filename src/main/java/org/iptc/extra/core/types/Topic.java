package org.iptc.extra.core.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

/**
 * 
 * @author manos schinas
 * 
 * That class represents a topic
 *
 */
@Entity("topics")
@Indexes(@Index(fields = {@Field("topicId"), @Field(value = "taxonomyId")}))
@XmlRootElement()
public class Topic {
	
	@Id
	protected String id;			// unique identifier, produced by the concatenation of taxonomy and topic id

	protected String topicId;		// unique topic id inside the taxonomy
	
	protected String name;			// the name of the topic
	
	protected String definition;	// a definition - description of the topic
	
	protected String parentTopic;	// the id of the parent of the topic (if any)

	protected String taxonomyId;	// the taxonomy to which the topic belongs to

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
