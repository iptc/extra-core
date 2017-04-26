package org.iptc.extra.core.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("dictionaries")
@XmlRootElement()
public class Dictionary {

	@Id
	protected String id;

	protected String language;

	protected List<String> terms = new ArrayList<String>();
	
	public Dictionary() {
		
	}
			
	public Dictionary(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	
}
