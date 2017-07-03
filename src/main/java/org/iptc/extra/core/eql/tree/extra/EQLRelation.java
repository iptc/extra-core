package org.iptc.extra.core.eql.tree.extra;

import org.iptc.extra.core.eql.tree.nodes.Relation;

public enum EQLRelation {

	CONTAIN ("="), 
	EXACT ("=="), 
	NOT_EQUAL ("<>"), 
	LT ("<"), 
	LTE ("<="), 
	GT (">"), 
	GTE (">="), 
	ADJ ("adj"), 
	ALL ("all"), 
	ANY ("any"), 
	WITHIN ("within");
	
	private final String relation;
	
	EQLRelation(String relation) {
		this.relation = relation;
	};
	
	private String relation() { return relation; }
	
	public static boolean isValid(Relation relation) {
		String r = relation.getRelation();
		if(r == null) {
			return false;
		}
		
		r = r.toLowerCase();
		
		if(r.equals(CONTAIN.relation())) {
			return true;
		}
		if(r.equals(EXACT.relation())) {
			return true;
		}
		if(r.equals(NOT_EQUAL.relation())) {
			return true;
		}
		if(r.equals(LT.relation()) || r.equals(LTE.relation())) {
			return true;
		}
		if(r.equals(GT.relation()) || r.equals(GTE.relation())) {
			return true;
		}
		if(r.equals(ADJ.relation())) {
			return true;
		}
		if(r.equals(ALL.relation())) {
			return true;
		}
		if(r.equals(ANY.relation())) {
			return true;
		}
		if(r.equals(WITHIN.relation())) {
			return true;
		}
		
		return false;
	}
}