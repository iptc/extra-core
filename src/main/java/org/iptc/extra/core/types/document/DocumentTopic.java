package org.iptc.extra.core.types.document;

public class DocumentTopic {

	private String topicId;
	
	private String url;
	
	private String association;
	
	private String parentTopic;
	
	private String name;
	
	private boolean exclude;
	
	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAssociation() {
		return association;
	}

	public void setAssociation(String association) {
		this.association = association;
	}

	public String getParentTopic() {
		return parentTopic;
	}

	public void setParentTopic(String parentTopic) {
		this.parentTopic = parentTopic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExclude() {
		return exclude;
	}

	public void setExlude(boolean exclude) {
		this.exclude = exclude;
	}


}
