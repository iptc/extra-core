package org.iptc.extra.core.eql.tree.extra;

import java.util.List;

import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.Modifier;
import org.iptc.extra.core.eql.tree.nodes.Operator;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;

/**
 * 
 * @author manos schinas
 * 
 * A set of valid operators in EQL, built upon modified or, and, not, prox
 *
 */
public enum EQLOperator {
	AND, 					// and
	OR, 					// or
	NOT, 					// not
	MINIMUM, 				// or/countunique>n
	DISTANCE, 				// prox/unit=word/distance<n
	NOT_WITHIN_DISTANCE, 	// prox/unit=word/distance>n
	MINIMUM_OCCURRENCE, 	// or/count>n
	MAXIMUM_OCCURRENCE,		// or/count>n
	ORDER, 					// prox/ordered
	SENTENCE, 				// prox/unit=sentence/distance=1
	PARAGRAPH, 				// prox/unit=paragraph/distance=1
	NOT_IN_PHRASE,			// prox/unit=word/distance>0
	NOT_IN_SENTENCE,		// prox/unit=sentence/distance>1
	NOT_IN_PARAGRAPH,		// prox/unit=paragraph/distance>1
	ORDER_AND_DISTANCE,		// prox/unit=word/distance<n/ordered
	FROM_START,				
	FROM_END,
	MAXIMUM_SENTENCES,
	MAXIMUM_PARAGRAPHS,
	PARAGRAPH_POSITION;

	public static EQLOperator getEQLOperator(Operator operator) {
		
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
		EQLOperator extraOperator = getEQLOperator(operator);
		return extraOperator != null;
	}
	
	public static boolean isEQLOperatorClause(Clause clause, EQLOperator extraOperator) {
		if(clause instanceof PrefixClause) {
			PrefixClause prefixClause = (PrefixClause) clause;
			if(prefixClause.getEQLOperator() != null && prefixClause.getEQLOperator() == extraOperator) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isWordDistanceOperator(EQLOperator extraOperator) {
		return (extraOperator == EQLOperator.DISTANCE || extraOperator == EQLOperator.NOT_WITHIN_DISTANCE || 
				extraOperator == EQLOperator.ORDER || extraOperator == EQLOperator.ORDER_AND_DISTANCE || 
				extraOperator == EQLOperator.NOT_IN_PHRASE);
	}
}