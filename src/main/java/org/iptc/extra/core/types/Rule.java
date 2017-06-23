package org.iptc.extra.core.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("rules")
@XmlRootElement()
public class Rule {
	
	@Id
	protected String id;
	
	protected String name;
	
	protected String query;

	protected String status;
	
	protected long createdAt;
	
	protected long updatedAt;
	
	protected String uid;
	
	protected String parentRule;

	protected String taxonomy;
	
	protected String topicId;
	
	protected String topicName;

	protected List<String> group = new ArrayList<String>();
	
	public List<String> getGroup() {
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	public Rule() {
		
	}
			
	public Rule(String id) {
		this.id = id;
	}
	
	public Rule(String id, String query) {
		this.id = id;
		this.query = query;
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
	
	public static void main(String...args) {
		Rule r = new Rule("1", "test");
		System.out.println(r);
	}
}
