package org.iptc.extra.core.cql.tree.extra;

import java.util.List;

import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.Modifier;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;

public enum ExtraOperator {
	
	AND, 
	OR, 
	NOT, 
	MINIMUM, 
	DISTANCE, 
	MINIMUM_OCCURRENCE, 
	MAXIMUM_OCCURRENCE,
	ORDER, 
	SENTENCE, 
	NOT_WITHIN_DISTANCE, 
	PARAGRAPH, 
	NOT_IN_PHRASE,
	NOT_IN_SENTENCE,
	NOT_IN_PARAGRAPH,
	ORDER_AND_DISTANCE,
	FROM_START,
	FROM_END,
	MAXIMUM_SENTENCES,
	MAXIMUM_PARAGRAPHS,
	PARAGRAPH_POSITION;

	public static ExtraOperator getExtraOperator(Operator operator) {
		
		List<Modifier> modifiers = operator.getModifiers();
		String op = operator.getOperator();
		if(op == null) {
			return null;
		}
		
		if(op.equals("and")) {
			if(modifiers == null || modifiers.isEmpty()) {
				return AND;
			}
		}
		else if (op.equals("or")) {
			if(modifiers == null || modifiers.isEmpty()) {
				return OR;
			}
			else {
				if(operator.hasModifier("count")) {
					Modifier modifier = operator.getModifier("count");
					if(modifier.isComparitorGT() || modifier.isComparitorGTE()) {
						return MINIMUM_OCCURRENCE;
					}
					
					if(modifier.isComparitorLT() || modifier.isComparitorLTE()) {
						return MAXIMUM_OCCURRENCE;
					}
				}
				if(operator.hasModifier("countunique")) {
					Modifier modifier = operator.getModifier("countunique");
					if(modifier.isComparitorGT() || modifier.isComparitorGTE()) {
						return MINIMUM;
					}
				}
			}
		}
		else if(op.equals("not")) {
			return NOT;
		}
		else if(op.equals("prox")) {
			if(modifiers != null && !modifiers.isEmpty()) {
				if(operator.hasModifier("distance") && operator.hasModifier("unit")) {
					Modifier distanceModifier = operator.getModifier("distance");
					Modifier unitModifier = operator.getModifier("unit");
					
					// word-level distances
					if(unitModifier.isComparitorEQ() && unitModifier.valueEquals("word")) {
						if(distanceModifier.isComparitorLTE() || distanceModifier.isComparitorLT()) {
							if(operator.hasModifier("ordered")) {
								return ORDER_AND_DISTANCE;
							}
							return DISTANCE;
						}
						
						if(distanceModifier.isComparitorGT()) {
							if(distanceModifier.valueEquals("0")) {
								return NOT_IN_PHRASE;
							}
							
							return NOT_WITHIN_DISTANCE;
						}
					}
					
					// sentence-level distances
					if(unitModifier.isComparitorEQ() && unitModifier.valueEquals("sentence")) {
						if(distanceModifier.isComparitorEQ() && distanceModifier.valueEquals("1")) {
							return SENTENCE;
						}
						if(distanceModifier.isComparitorGT()) {
							return NOT_IN_SENTENCE;
						}
					}
					
					// paragraph-level distances
					if(unitModifier.isComparitorEQ() && unitModifier.valueEquals("paragraph")) {
						if(distanceModifier.isComparitorEQ() && distanceModifier.valueEquals("1")) {
							return PARAGRAPH;
						}
						if(distanceModifier.isComparitorGT()) {
							return NOT_IN_PARAGRAPH;
						}
					}
					
				}
				if(operator.hasModifier("ordered")) {
					return ORDER;
				}
				
			}
		}
		
		return null;
	}
	
	public static boolean isValid(Operator operator) {
		ExtraOperator extraOperator = getExtraOperator(operator);
		return extraOperator != null;
	}
	
	public static boolean isExtraOperatorClause(Clause clause, ExtraOperator extraOperator) {
		if(clause instanceof PrefixClause) {
			PrefixClause prefixClause = (PrefixClause) clause;
			if(prefixClause.getExtraOperator() != null && prefixClause.getExtraOperator() == extraOperator) {
				return true;
			}
		}
		return false;
	}
	
}