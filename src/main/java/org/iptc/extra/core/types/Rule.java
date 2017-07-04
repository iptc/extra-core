package org.iptc.extra.core.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 
 * @author manos schinas
 * 
 * This class represents an extra rule, that can be used for retrieval and classification of documents.  
 *
 */
@Entity("rules")
@XmlRootElement()
public class Rule {
	
	@Id
	protected String id;	// the unique identifier of the rule
	
	protected String name;	// a representative name for the rule
	
	protected String query;	// the actual rule, expressed as an EQL query 

	protected String status;	// the status of the rule. Can take three values: new, draft, submitted
	
	protected long createdAt;	// creation date of the rule
	
	protected long updatedAt;	// update date of the rule
	
	protected long submittedAt = 0;	// date of the last submission
	
	protected String uid;			// the id of the user that created the ruled
	
	protected String parentRule;	// the id of the parent rule, if any

	protected String taxonomy;		// the id of the taxonomy associated with that rule
	
	protected String topicId;		// the id of the topic associated with that rule
	
	protected String topicName;		

	protected List<String> group = new ArrayList<String>();		// a list of group ids used to split rules into subsets
		
	protected List<String> schemas = new ArrayList<String>();	// a list of schemas to which the rule is associated

	public Rule() {
		
	}
			
	public Rule(String id) {
		this.id = id;
	}
	
	public Rule(String id, String query) {
		this.id = id;
		this.query = query;
	}
	
	// Getters/ Setters
	
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
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	
	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public long getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(long submittedAt) {
		this.submittedAt = submittedAt;
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getParentRule() {
		return parentRule;
	}

	public void setParentRule(String parentRule) {
		this.parentRule = parentRule;
	}

	public String getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	
	public List<String> getGroup() {
		if(group == null) {
			return new ArrayList<String>();
		}
		
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}
	
	public List<String> getSchemas() {
		if(schemas == null) {
			return new ArrayList<String>();
		}
		
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

}
