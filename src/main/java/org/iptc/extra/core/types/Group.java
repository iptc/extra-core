package org.iptc.extra.core.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("groups")
@XmlRootElement()
public class Group {
	
	@Id
	protected String id;
		
	protected String name;

	public Group() {
		
	}
	
	public Group(String id, String name) {
		super();
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
	
}
