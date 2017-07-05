package org.iptc.extra.core.eql.tree.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author manos schinas
 *
 * Modified class represents nodes in the syntax tree that can be modified. 
 * There are two objects that can be modified: operators (org.iptc.extra.core.eql.tree.nodes.Operator) 
 * and relations (org.iptc.extra.core.eql.tree.nodes.Relation)
 * 
 * 
 *  Example: prox/unit=word/distance>2
 *	Operator prox is modified by two modifiers unit=word and distance>2
 *	These two modifiers are kept into modifiersMap
 *
 */
public class Modified extends Node {
	
	protected Map<String, Modifier> modifiersMap = new HashMap<String, Modifier>();
	
	public List<Modifier> getModifiers() {
		return new ArrayList<Modifier>(modifiersMap.values());
	}

	public void setModifiers(List<Modifier> modifiers) {
		for(Modifier modifier : modifiers) {
			modifiersMap.put(modifier.getModifier(), modifier);
		}
	}

	public boolean hasModifier(String modifier) {
		return modifiersMap.containsKey(modifier);
	}
	
	public Modifier getModifier(String modifier) {
		return modifiersMap.get(modifier);
	}
	
	@Override
	public boolean hasChildren() {
		return false;
	}

	public boolean isModified() {
		return !modifiersMap.isEmpty();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(Modifier modifier : modifiersMap.values()) {
			buffer.append(modifier);
		}
		
		return buffer.toString();
	}
	
}
