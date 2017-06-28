package org.iptc.extra.core.cql.tree.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiTermQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.index.query.SpanNearQueryBuilder;
import org.elasticsearch.index.query.SpanNotQueryBuilder;
import org.elasticsearch.index.query.SpanOrQueryBuilder;
import org.elasticsearch.index.query.SpanQueryBuilder;
import org.elasticsearch.index.query.SpanTermQueryBuilder;
import org.elasticsearch.script.Script;
import org.iptc.extra.core.cql.SyntaxTree;
import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.CommentClause;
import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.Modifier;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.ReferenceClause;
import org.iptc.extra.core.cql.tree.Relation;
import org.iptc.extra.core.cql.tree.SearchClause;
import org.iptc.extra.core.cql.tree.SearchTerms;
import org.iptc.extra.core.cql.tree.extra.ExtraOperator;
import org.iptc.extra.core.cql.tree.utils.TreeUtils;
import org.iptc.extra.core.types.Schema;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author manosetro - Manos Schinas
 * 
 *	EXTRA2ESQueryVisitor performs a depth-first traversal of the syntax tree 
 *	and generates the equivalent Elastic Search Query. 
 */
public class EXTRA2ESQueryVisitor extends SyntaxTreeVisitor<QueryBuilder> {
	
	private String indexSuffix = "";
	
	private boolean spanEnabled = false;
	
	private Schema schema;
	
	public EXTRA2ESQueryVisitor() {
		
	}
	
	public EXTRA2ESQueryVisitor(Schema schema) {
		this.schema = schema;
	}

	@Override
	public QueryBuilder visitPrefixClause(PrefixClause prefixClause) {
		
		ExtraOperator extraOperator = prefixClause.getExtraOperator();
	
		if(extraOperator == ExtraOperator.AND) {
			return andToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.OR) {
			return orToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.NOT) {
			return notToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.MINIMUM) {
			return minimumToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.SENTENCE) {
			return sentenceToES(prefixClause);
		}

		if(extraOperator == ExtraOperator.NOT_IN_SENTENCE) {
			return notInSentenceToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.PARAGRAPH) {
			return paragraphToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.NOT_IN_PARAGRAPH) {
			return notInParagraphToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.DISTANCE) {
			return distanceToES(prefixClause, false);
		}
		
		if(extraOperator == ExtraOperator.NOT_WITHIN_DISTANCE) {
			return notWithiDistanceToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.MAXIMUM_OCCURRENCE) {
			return occurenceToES(prefixClause);
		}

		if(extraOperator == ExtraOperator.MINIMUM_OCCURRENCE) {
			return occurenceToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.ORDER) {
			return orderToES(prefixClause);
		}
		
		if(extraOperator == ExtraOperator.ORDER_AND_DISTANCE) {
			return distanceToES(prefixClause, true);
		}

		if(extraOperator == ExtraOperator.NOT_IN_PHRASE) {
			return notInPhraseToES(prefixClause);
		}
		
		return null;
		
	}
	
	private QueryBuilder occurenceToES(PrefixClause prefixClause) {
		
		BoolQueryBuilder booleanQb = boolQuery();
		
		QueryBuilder orQb = orToES(prefixClause);
		if(orQb != null) {
			booleanQb.must(orQb);
		}
		
		Operator operator = prefixClause.getOperator();
		Modifier modifier = operator.getModifier("count");
		String comparitor = modifier.getComparitor();
		String value = modifier.getValue();
		
		if(prefixClause.getSearchClause().size() == 1) {
			
			SearchClause searchClause = prefixClause.getSearchClause().get(0);
			String field = (searchClause.getIndex() == null) ? "text_content" : searchClause.getIndex().getName();
			SearchTerms searchTerms = searchClause.getSearchTerms();
			
			String code = getScriptCode(field + "_tokens", comparitor, value, searchTerms.getTerms());
			Script script = new Script(code);
			ScriptQueryBuilder scriptQuery = scriptQuery(script);
			booleanQb.must(scriptQuery);
		}
		else {
			Map<String, List<String>> fieldTerms = new HashMap<String, List<String>>();
			for(SearchClause searchClause : prefixClause.getSearchClause()) {
				Relation relation = searchClause.getRelation();
				String field = (searchClause.getIndex() == null) ? "text_content" : searchClause.getIndex().getName();
				SearchTerms searchTerms = searchClause.getSearchTerms();
			
				String fieldPrefix = "";
				if(relation != null && relation.hasModifier("stemming")) {
					fieldPrefix = "stemmed_";
				}
				
				String tokensField = fieldPrefix + field + "_tokens";
				
				List<String> terms = fieldTerms.get(tokensField);
				if(terms == null) {
					terms = new ArrayList<String>();
					fieldTerms.put(field + "_tokens", terms);
				}
				
				if(relation == null || relation.is("=") || relation.is("any")) {
					terms.addAll(searchTerms.getTerms());
				}
				else if(relation.is("adj")) {
					terms.add(searchTerms.getSearchTerm().toLowerCase());
				}
			}
		
			String code = getScriptCode(comparitor, value, fieldTerms);
			Script script = new Script(code);
			ScriptQueryBuilder scriptQuery = scriptQuery(script);
			booleanQb.must(scriptQuery);
		}
		
		return booleanQb;
	}
	
	private String getScriptCode(String comparitor, String count, Map<String, List<String>> fieldTerms) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("int total = 0; ");
		for(String field : fieldTerms.keySet()) {
			List<String> terms = fieldTerms.get(field);
			buffer.append("for (int i = 0; i < doc['" + field + "'].length; ++i) { ");
			buffer.append("String t = doc['" + field + "'][i]; ");
			buffer.append("String token = t.substring(0, t.indexOf('_')); ");
			buffer.append("def terms = ['" + StringUtils.join(terms, "', '")+ "']; ");
			buffer.append("if(terms.contains(token)) { ");
			buffer.append("String f = t.substring(t.indexOf('_') + 1); ");
			buffer.append("total += Integer.parseInt(f); ");
			buffer.append("} ");
			buffer.append("} ");
		}
		buffer.append("total " + comparitor + " " + count + ";");
		return buffer.toString();
	}
	
	private String getScriptCode(String field, String comparitor, String count, List<String> terms) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("int total = 0; ");
		buffer.append("for (int i = 0; i < doc['" + field + "'].length; ++i) { ");
		buffer.append("String t = doc['" + field + "'][i]; ");
		buffer.append("String token = t.substring(0, t.indexOf('_')); ");
		buffer.append("def terms = ['" + StringUtils.join(terms, "', '")+ "']; ");
		buffer.append("if(terms.contains(token)) { ");
		buffer.append("String f = t.substring(t.indexOf('_') + 1); ");
		buffer.append("total += Integer.parseInt(f); ");
		buffer.append("} ");
		buffer.append("} ");
		buffer.append("total " + comparitor + " " + count + ";");
		return buffer.toString();
	}
	
	
	private QueryBuilder andToES(PrefixClause prefixClause) {
		List<Clause> childrenClauses = prefixClause.getClauses();
		childrenClauses = childrenClauses.stream().filter(clause -> !(clause instanceof CommentClause)).collect(Collectors.toList());
		
		if(childrenClauses.size() == 1) {
			return visit(childrenClauses.get(0));
		}
		
		if(TreeUtils.areSearchTermClauses(childrenClauses)) {
			// Αll the sub-statement are search clauses without specified index/relation
			SearchTerms mergedSearchTerms = TreeUtils.mergeSearchTerms(childrenClauses);
			String queryString = mergedSearchTerms.getQueryString("");
			QueryStringQueryBuilder queryBuilder = queryStringQuery(queryString);
			
			if(schema != null && !schema.getTextualFieldNames().isEmpty() && indexSuffix.equals("")) {
				for(String field : schema.getTextualFieldNames()) {
					queryBuilder.field(field);
				}
			}
			else {
				queryBuilder.defaultField("text_content" + indexSuffix);
			}
			
			queryBuilder.enablePositionIncrements(false);
			queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			
			if(mergedSearchTerms.hasWildCards()) {	
				queryBuilder.analyzeWildcard(true);
			}	
			
			return queryBuilder;
		}
		else {
			BoolQueryBuilder booleanQb = boolQuery();
			
			List<QueryBuilder> mustClauses = new ArrayList<QueryBuilder>();
			List<QueryBuilder> mustNotClauses = new ArrayList<QueryBuilder>();
			for(Clause clause : childrenClauses) {
				if(ExtraOperator.isExtraOperatorClause(clause, ExtraOperator.NOT)) {
					mustNotClauses.addAll(getChildrenClausesQueries((PrefixClause) clause));
				}
				else {
					QueryBuilder queryBuilder = visit(clause);
					if(queryBuilder != null) {
						mustClauses.add(queryBuilder);
					}
				}
			}

			if(mustClauses.isEmpty()) {
				return null;
			}	
		
			for(QueryBuilder stqb : mustClauses) {
				booleanQb.must(stqb);
			}	
			
			for(QueryBuilder stqb : mustNotClauses) {
				booleanQb.mustNot(stqb);
			}	
			
			return booleanQb;
		}
	}
	
	private QueryBuilder orToES(PrefixClause prefixClause) {
		
		List<Clause> childrenClauses = prefixClause.getClauses();
		childrenClauses = childrenClauses.stream().filter(clause -> !(clause instanceof CommentClause)).collect(Collectors.toList());
		
		if(childrenClauses.size() == 1) {
			return visit(childrenClauses.get(0));
		}
		
		if(spanEnabled) {
			// Span flag is enabled. That means that a parent node of the rule is a proximity operator.
			List<QueryBuilder> clausesQueries = getClausesQueries(childrenClauses);
			if(clausesQueries.size() < 1) {
				
				return null;
			}
			
			// Span query that matches the union of its clauses
			SpanOrQueryBuilder spanOrQb = spanOrQuery((SpanQueryBuilder) clausesQueries.get(0));
			for(int i = 1; i < clausesQueries.size(); i++) {
				spanOrQb.addClause((SpanQueryBuilder) clausesQueries.get(i));
			}	
			return spanOrQb;
		}
		else {
			if(TreeUtils.areSearchTermClauses(childrenClauses)) { 
				// Αll the sub-statement are search clauses without specified index/relation
				SearchTerms mergedSearchTerms = TreeUtils.mergeSearchTerms(childrenClauses);
				String queryString = mergedSearchTerms.getQueryString("");
				QueryStringQueryBuilder queryBuilder = queryStringQuery(queryString);
				
				if(schema != null && !schema.getTextualFieldNames().isEmpty() && indexSuffix.equals("")) {
					for(String field : schema.getTextualFieldNames()) {
						queryBuilder.field(field);
					}
				}
				else {
					queryBuilder.defaultField("text_content" + indexSuffix);
				}
				
				queryBuilder.enablePositionIncrements(false);
				
				if(mergedSearchTerms.hasWildCards()) {	
					queryBuilder.analyzeWildcard(true);
				}	
				
				return queryBuilder;
			}
			else {
				// Sub-statement are a mix of search clauses and other prefic clauses
				BoolQueryBuilder booleanQb = boolQuery();
				List<QueryBuilder> clausesQueries = getClausesQueries(childrenClauses);
				if(clausesQueries.isEmpty()) {
					return null;
				}
				for(QueryBuilder stqb : clausesQueries) {
					// The sub-statement should match (or) the document
					booleanQb.should(stqb);
				}	
				return booleanQb;
			}
		}
	}
	
	private QueryBuilder minimumToES(PrefixClause prefixClause) {
		QueryBuilder queryBuilder = orToES(prefixClause);
		if(queryBuilder == null) {
			return null;
		}
		
		Operator operator = prefixClause.getOperator();
		Modifier modifier = operator.getModifier("countunique");
		Integer mimimum = Integer.parseInt(modifier.getValue());
		if(modifier.isComparitorGT()) {
			mimimum += 1;
		}
		
		if(queryBuilder instanceof QueryStringQueryBuilder) {
			((QueryStringQueryBuilder) queryBuilder).minimumShouldMatch(mimimum.toString());
		}
		else if (queryBuilder instanceof BoolQueryBuilder) {
			((BoolQueryBuilder) queryBuilder).minimumShouldMatch(mimimum);
		}
		else if(queryBuilder instanceof MultiMatchQueryBuilder) {
			((MultiMatchQueryBuilder) queryBuilder).minimumShouldMatch(mimimum.toString());
		}
		
		return queryBuilder;
	}
	
	private BoolQueryBuilder notToES(PrefixClause prefixClause) {
		
		List<Clause> childrenClauses = prefixClause.getClauses();
		BoolQueryBuilder booleanQb = boolQuery();
		List<QueryBuilder> clausesQueries = getClausesQueries(childrenClauses);
		if(clausesQueries.isEmpty()) {
			return null;
		}
		
		for(QueryBuilder stqb : clausesQueries) {
			booleanQb.mustNot(stqb);
		}	
		return booleanQb;
	}
	
	private NestedQueryBuilder paragraphToES(PrefixClause prefixClause) {
		
		List<SearchClause> searchClauses = prefixClause.getSearchClause();
		Set<String> indices = TreeUtils.getIndices(prefixClause);
		
		if(indices.size() > 1) {
			// the operator has clauses with multiple indices specified 
			return null;
		}
		
		String index;
		if(indices.size() == 1) {
			index = indices.toArray()[0] + "_paragraphs";
		}
		else {
			index = "text_content_paragraphs";
		}

		String prefix = "";
		for(SearchClause searchClause : searchClauses) {
			Relation relation = searchClause.getRelation();
			if(relation != null && relation.hasModifier("stemming")) {
				prefix = "stemmed_";
				break;
			}
		}
		
		if(searchClauses.size() == prefixClause.getClauses().size()) {
			BoolQueryBuilder booleanQb = boolQuery();
			if(indices.size() == 1) {
				for(SearchClause searchClause : searchClauses) {
					QueryBuilder clauseQb = searchClausetoES(index + ".paragraph", searchClause.getRelation(), searchClause.getSearchTerms());
					if(clauseQb != null) {
						booleanQb.must(clauseQb);
					}
				}
				
				NestedQueryBuilder nestedQb = nestedQuery(index, booleanQb, ScoreMode.Total);
				return nestedQb;
				
			}	
			else {
				Relation relation = new Relation("="); 
				for(SearchClause searchClause : searchClauses) {
					QueryBuilder clauseQueryBuilder = searchClausetoES(index + ".paragraph", relation, searchClause.getSearchTerms());
					if(clauseQueryBuilder != null) {
						booleanQb.must(clauseQueryBuilder);
					}
				}
				
				NestedQueryBuilder nestedQb = nestedQuery(prefix + index, booleanQb, ScoreMode.Total);
				return nestedQb;
			}

		}
		else {
			indexSuffix = "_paragraphs.paragraph";
			BoolQueryBuilder booleanQb = boolQuery();
			for(Clause clause : prefixClause.getClauses()) {
				QueryBuilder clauseQueryBuilder = visit(clause);
				booleanQb.must(clauseQueryBuilder);
			}
			indexSuffix = "";
			
			NestedQueryBuilder nestedQb = nestedQuery(index, booleanQb, ScoreMode.Total);
			return nestedQb;
			
		}

	}
	
	private BoolQueryBuilder notInParagraphToES(PrefixClause prefixClause) {
		
		Set<String> indices = TreeUtils.getIndices(prefixClause);
		if(indices.size() != 1) {
			// the operator has clauses with multiple indices specified 
			return null;
		}

		BoolQueryBuilder booleanQb = boolQuery();
		for(Clause clause : prefixClause.getClauses()) {
			QueryBuilder clauseQueryBuilder = visit(clause);
			if(clauseQueryBuilder != null) {
				booleanQb.must(clauseQueryBuilder);
				break;
			}
		}
		
		NestedQueryBuilder nestedQb = paragraphToES(prefixClause);
		if(nestedQb != null) {
			booleanQb.mustNot(nestedQb);
		}
		
		return booleanQb;
	}
	
	private NestedQueryBuilder sentenceToES(PrefixClause prefixClause) {
		
		List<SearchClause> searchClauses = prefixClause.getSearchClause();
		Set<String> indices = TreeUtils.getIndices(prefixClause);
		
		if(indices.size() > 1) {
			// the operator has clauses with more than one indices specified 
			return null;
		}
		
		String index;
		if(indices.size() == 1) {
			index = indices.toArray()[0] + "_sentences";
		}
		else {
			index = "text_content_sentences";
		}
		
		String prefix = "";
		for(SearchClause searchClause : searchClauses) {
			Relation relation = searchClause.getRelation();
			if(relation != null && relation.hasModifier("stemming")) {
				prefix = "stemmed_";
				break;
			}
		}
		
		if(searchClauses.size() == prefixClause.getClauses().size()) {
			BoolQueryBuilder booleanQb = boolQuery();
			if(indices.size() == 1) {
				for(SearchClause searchClause : searchClauses) {
					QueryBuilder clauseQueryBuilder = searchClausetoES(index + ".sentence", searchClause.getRelation(), searchClause.getSearchTerms());
					if(clauseQueryBuilder != null) {
						booleanQb.must(clauseQueryBuilder);
					}
				}
				NestedQueryBuilder nestedQb = nestedQuery(prefix + index, booleanQb, ScoreMode.Total);
				return nestedQb;
			}
			else {
				Relation relation = new Relation("="); 
				for(SearchClause searchClause : searchClauses) {
					QueryBuilder clauseQueryBuilder = searchClausetoES(index + ".sentence", relation, searchClause.getSearchTerms());
					if(clauseQueryBuilder != null) {
						booleanQb.must(clauseQueryBuilder);
					}
				}
				NestedQueryBuilder nestedQb = nestedQuery(prefix + index, booleanQb, ScoreMode.Total);
				return nestedQb;
			}
		}
		else {
			indexSuffix = "_sentences.sentence";
			BoolQueryBuilder booleanQb = boolQuery();
			for(Clause clause : prefixClause.getClauses()) {
				QueryBuilder clauseQueryBuilder = visit(clause);
				if(clauseQueryBuilder != null) {
					booleanQb.must(clauseQueryBuilder);
				}
			}
			indexSuffix = "";
			
			NestedQueryBuilder nestedQb = nestedQuery(index, booleanQb, ScoreMode.Total);
			return nestedQb;
		}
	}
	
	private BoolQueryBuilder notInSentenceToES(PrefixClause prefixClause) {
		
		Set<String> indices = TreeUtils.getIndices(prefixClause);
		if(indices.size() != 1) {
			// the operator has clauses with more than one indices specified 
			return null;
		}
		
		BoolQueryBuilder booleanQb = boolQuery();
		for(Clause clause : prefixClause.getClauses()) {
			QueryBuilder clauseQueryBuilder = visit(clause);
			if(clauseQueryBuilder != null) {
				booleanQb.must(clauseQueryBuilder);
				break;
			}
		}
		
		NestedQueryBuilder nestedQb = sentenceToES(prefixClause);
		if(nestedQb != null) {
			booleanQb.mustNot(nestedQb);
		}
		
		return booleanQb;
	}

	private QueryBuilder distanceToES(PrefixClause prefixClause, boolean inOrder) {
		
		Operator operator = prefixClause.getOperator();
		Modifier distanceModifier = operator.getModifier("distance");
		int slop = Integer.parseInt(distanceModifier.getValue());
		
		/*
		BoolQueryBuilder booleanQb = boolQuery();
		for(Clause clause : prefixClause.getClauses()) {
			QueryBuilder clauseQb = visit(clause);
			if(clauseQb != null) {
				booleanQb.must(clauseQb);
			}
		}
		*/
		
		List<QueryBuilder> spanClauseQbs = new ArrayList<QueryBuilder>();
		this.spanEnabled = true;
		for(Clause clause : prefixClause.getClauses()) {
			QueryBuilder clauseQb = visit(clause);
			if(clauseQb != null) {
				spanClauseQbs.add(clauseQb);
			}
		}
		this.spanEnabled = false;
		
		if(spanClauseQbs.size() > 1) {
			SpanNearQueryBuilder queryBuilder = spanNearQuery((SpanQueryBuilder) spanClauseQbs.get(0), slop);
			for(int i = 1; i < spanClauseQbs.size() ; i++) {
				queryBuilder.addClause((SpanQueryBuilder) spanClauseQbs.get(i));
			}
			queryBuilder.inOrder(inOrder);
			
			return queryBuilder;
		}
		
		return null;
		
	}
	
	private BoolQueryBuilder notWithiDistanceToES(PrefixClause prefixClause) {
		if(prefixClause.getClauses().size() == 2) {
			Operator operator = prefixClause.getOperator();
			Modifier distanceModifier = operator.getModifier("distance");
			int dist = Integer.parseInt(distanceModifier.getValue());
			
			BoolQueryBuilder booleanQb = boolQuery();
			List<QueryBuilder> clausesQueries = getClausesQueries(prefixClause.getClauses());
			if(clausesQueries.isEmpty()) {
				// there must be at least one sub-statement
				return null;
			}	
	
			for(QueryBuilder clauseQb : clausesQueries) {
				booleanQb.must(clauseQb);
				break;
			}	
		
			
			List<QueryBuilder> clauseSpanQbs = new ArrayList<QueryBuilder>();
			this.spanEnabled = true;
			for(Clause clause : prefixClause.getClauses()) {
				QueryBuilder clauseSpanQb = visit(clause);
				if(clauseSpanQb != null) {
					clauseSpanQbs.add(clauseSpanQb);
				}
			}
			this.spanEnabled = false;
			
			if(clauseSpanQbs.size() == 2) {
				//SpanQueryBuilder include = (SpanQueryBuilder) clauseSpanQbs.get(0);
				//SpanQueryBuilder exclude = (SpanQueryBuilder) clauseSpanQbs.get(1);
				//SpanNotQueryBuilder distanceQueryBuilder = spanNotQuery(include, exclude);
				//distanceQueryBuilder.dist(dist);
			
				//booleanQb.must(distanceQueryBuilder);
			
				SpanNearQueryBuilder distanceQueryBuilder = spanNearQuery((SpanQueryBuilder) clauseSpanQbs.get(0), dist);
				for(int i = 1; i < clauseSpanQbs.size() ; i++) {
					distanceQueryBuilder.addClause((SpanQueryBuilder) clauseSpanQbs.get(i));
				}
				distanceQueryBuilder.inOrder(false);
				
				booleanQb.mustNot(distanceQueryBuilder);
				
				return booleanQb;
			}
		}
		
		return null;
	}
	
	private SpanNearQueryBuilder orderToES(PrefixClause prefixClause) {
		List<QueryBuilder> clauseQbs = new ArrayList<QueryBuilder>();
		this.spanEnabled = true;
		for(Clause clause : prefixClause.getClauses()) {
			QueryBuilder clauseQb = visit(clause);
			if(clauseQb != null) {
				clauseQbs.add(clauseQb);
			}
		}
		this.spanEnabled = false;
		
		if(clauseQbs.size() > 1) {
			SpanNearQueryBuilder queryBuilder = spanNearQuery((SpanQueryBuilder) clauseQbs.get(0), Integer.MAX_VALUE);
			for(int i = 1; i < clauseQbs.size() ; i++) {
				queryBuilder.addClause((SpanQueryBuilder) clauseQbs.get(i));
			}
			queryBuilder.inOrder(true);
			
			return queryBuilder;
		}
		return null;
	}

	private SpanNotQueryBuilder notInPhraseToES(PrefixClause prefixClause) {

		List<QueryBuilder> clauseSpanQbs = new ArrayList<QueryBuilder>();
		this.spanEnabled = true;
		for(Clause clause : prefixClause.getClauses()) {
			QueryBuilder clauseSpanQb = visit(clause);
			if(clauseSpanQb != null) {
				clauseSpanQbs.add(clauseSpanQb);
			}
		}
		this.spanEnabled = false;
			
		if(clauseSpanQbs.size() == 2) {
			SpanQueryBuilder include = (SpanQueryBuilder) clauseSpanQbs.get(0);
			SpanQueryBuilder exclude = (SpanQueryBuilder) clauseSpanQbs.get(1);
			
			SpanNotQueryBuilder distanceQueryBuilder = spanNotQuery(include, exclude);

			return distanceQueryBuilder;
		}
		
		return null;
	}
	
	private List<QueryBuilder> getClausesQueries(List<Clause> clauses) {
		List<QueryBuilder> clausesQueries = new ArrayList<QueryBuilder>();
		for(Clause clause : clauses) {
			QueryBuilder queryBuilder = visit(clause);
			if(queryBuilder == null) {
				continue;
			}
			clausesQueries.add(queryBuilder);
		}
		return clausesQueries;
	}
	
	private List<QueryBuilder> getChildrenClausesQueries(PrefixClause prefixClause) {
		List<Clause> childrenClauses = prefixClause.getClauses();
		List<QueryBuilder> clausesQueries = new ArrayList<QueryBuilder>();
		for(Clause clause : childrenClauses) {
			QueryBuilder queryBuilder = visit(clause);
			if(queryBuilder == null) {
				continue;
			}
			clausesQueries.add(queryBuilder);
		}
		return clausesQueries;
	}
	
	@Override
	public QueryBuilder visitSearchClause(SearchClause searchClause) {
		if(spanEnabled) {
			return searchClausetoSpan(searchClause);
		}
		else {
			if(searchClause.hasIndex()) {
				Index index = searchClause.getIndex();
				Relation relation = searchClause.getRelation();
				SearchTerms searchTerms = searchClause.getSearchTerms();
		
				String indexName = index.getName() + indexSuffix;
				return searchClausetoES(indexName, relation, searchTerms);
			}
			else {
				QueryBuilder qb = visitChildren(searchClause);
				return qb;
			}	
		}
	}

	@Override
	public QueryBuilder visitReferenceClause(ReferenceClause referenceClause) {
		
		SyntaxTree syntaxTree = referenceClause.getRuleSyntaxTree();
		if(syntaxTree != null && !syntaxTree.hasErrors() && syntaxTree.getRootNode() != null) {
			return visit(syntaxTree.getRootNode());
		}
		
		return null;
	}
	
	private QueryBuilder searchClausetoES(String index, Relation relation, SearchTerms searchTerms) {
		
		boolean isRegex = searchTerms.isRegexp();
		
		String query = searchTerms.getSearchTerm();
		if(isRegex && !relation.hasModifier("literal")) {
			return searchClauseWithRegex(index, relation, searchTerms);
		}
		
		if(relation.is("any") || relation.is("=")) {
			if(relation.hasModifier("stemming")) {
				index = "stemmed_" + index;
			}
			else if(relation.hasModifier("casesensitive")) {
				index = "case_sensitive_" + index;
			}
			else if(relation.hasModifier("literal")) {
				index = "literal_" + index;
			}
			
			return matchQuery(index, query);
			
		}
		else if (relation.is("==")) {
			return termQuery(index, query);
		}
		else if (relation.is("all")) {
			if(relation.hasModifier("stemming")) {
				index = "stemmed_" + index;
			}
			else if(relation.hasModifier("casesensitive")) {
				index = "case_sensitive_" + index;
			}
			else if(relation.hasModifier("literal")) {
				index = "literal_" + index;
			}
			
			MatchQueryBuilder queryBuilder = matchQuery(index, query);
			queryBuilder.operator(org.elasticsearch.index.query.Operator.AND);	
				
			return queryBuilder;
				
		}
		else if (relation.is("adj")) {
			if(relation.hasModifier("stemming")) {
				index = "stemmed_" + index;
			}
			else if(relation.hasModifier("casesensitive")) {
				index = "case_sensitive_" + index;
			}
			else if(relation.hasModifier("literal")) {
				index = "literal_" + index;
			}
			
			return matchPhraseQuery(index, query);
		}
		else if(relation.is(">")) {
			RangeQueryBuilder qb = rangeQuery(index);
			return qb.gt(query);
		}
		else if(relation.is(">=")) {
			RangeQueryBuilder qb = rangeQuery(index);
			return qb.gte(query);
		}
		else if(relation.is("<")) {
			RangeQueryBuilder qb = rangeQuery(index);
			return qb.lt(query);
		}
		else if(relation.is("<=")) {
			RangeQueryBuilder qb = rangeQuery(index);
			return qb.lte(query);
		}
		else if(relation.is("within") && searchTerms.numberOfTerms() == 2) {
			RangeQueryBuilder qb = rangeQuery(index);	
			return qb.gte(searchTerms.getTerm(0)).lte(searchTerms.getTerm(1));
		}
		
		return null;	
	}
	
	private SpanQueryBuilder searchClausetoSpan(SearchClause searchClause) {
		
		SearchTerms searchTerms = searchClause.getSearchTerms();
		String query = searchTerms.getSearchTerm();
		query = query.toLowerCase();
		
		if(!searchClause.hasIndex()) {
			return spanTermQuery("text_content" + indexSuffix, query);
		}
		else {
			String index = searchClause.getIndex().getName() + indexSuffix;
			Relation relation = searchClause.getRelation();
			
			if(relation.is("any") || relation.is("=")) {
				if(relation.hasModifier("regexp")) {
					MultiTermQueryBuilder qb = (MultiTermQueryBuilder) regexpQuery(index, query);
					return spanMultiTermQueryBuilder(qb);
				}
				
				String[] queryTerms = query.trim().split("\\s+");
				if(queryTerms.length > 1) {
					SpanTermQueryBuilder initialSpan = spanTermQuery(index, queryTerms[0]);
					SpanOrQueryBuilder spanOr = spanOrQuery(initialSpan);
					
					for(int i = 1 ; i < queryTerms.length; i++) {
						spanOr.addClause(spanTermQuery(index, queryTerms[i]));
					}
					
					return spanOr;
				}
				
				return spanTermQuery(index, query);
			}
			else if (relation.is("==")) {
				MultiTermQueryBuilder qb = (MultiTermQueryBuilder) termQuery(index, query);
				return spanMultiTermQueryBuilder(qb);

			}
			else if (relation.is("all")) {
				MatchQueryBuilder qb = matchQuery(index, query);
				qb.operator(org.elasticsearch.index.query.Operator.AND);
				
				return spanMultiTermQueryBuilder((MultiTermQueryBuilder) qb);
				
			}
			else if (relation.is("adj")) {
				if(relation.hasModifier("regexp")) {
					
					RegexpQueryBuilder regexpQb = regexpQuery(index, searchTerms.getRegexp(false));
					return spanMultiTermQueryBuilder(regexpQb);
				}
				String[] queryTerms = query.trim().split("\\s+");
				if(queryTerms.length > 1) {
					SpanTermQueryBuilder initialSpan = spanTermQuery(index, queryTerms[0]);
					SpanNearQueryBuilder spanNear = spanNearQuery(initialSpan, 0);
					
					for(int i = 1 ; i < queryTerms.length; i++) {
						spanNear.addClause(spanTermQuery(index, queryTerms[i]));
					}
					
					return spanNear;
				}
				else {
					return spanTermQuery(index, query);
				}
	
			}
			else if(relation.is(">")) {
				RangeQueryBuilder qb = rangeQuery(index).gt(query);
				return spanMultiTermQueryBuilder((MultiTermQueryBuilder) qb);
				
			}
			else if(relation.is(">=")) {
				RangeQueryBuilder qb = rangeQuery(index).gte(query);
				return spanMultiTermQueryBuilder((MultiTermQueryBuilder) qb);
			}
			else if(relation.is("<")) {
				RangeQueryBuilder qb = rangeQuery(index).lt(query);
				return spanMultiTermQueryBuilder((MultiTermQueryBuilder) qb);
			}
			else if(relation.is("<=")) {
				RangeQueryBuilder qb = rangeQuery(index).lte(query);
				return spanMultiTermQueryBuilder((MultiTermQueryBuilder) qb);
			}
			else if(relation.is("within") && searchTerms.numberOfTerms() == 2) {
				RangeQueryBuilder qb = rangeQuery(index).gte(searchTerms.getTerm(0)).lte(searchTerms.getTerm(1));
				
				return spanMultiTermQueryBuilder((MultiTermQueryBuilder) qb);
			}
		}
		
		return null;
	}
	
	private QueryBuilder searchClauseWithRegex(String index, Relation relation, SearchTerms searchTerms) {
		
		String query = searchTerms.getSearchTerm();
		
		if(relation.is("any") || relation.is("=") || relation.is("all")) {
			query = searchTerms.getRegexp(false);
			QueryStringQueryBuilder queryBuilder = queryStringQuery("/" + query + "/");
			queryBuilder.field("raw_" + index);
			queryBuilder.analyzeWildcard(true);
			
			if(relation.is("all")) {
				queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
			
			return queryBuilder;
		}
		
		if(relation.is("==")) {
			if(relation.hasModifier("regexp") || searchTerms.isRegexp()) {
				return regexpQuery("raw_" + index, searchTerms.getRegexp(false));
			}
			else {
				return wildcardQuery(index, searchTerms.getSearchTerm());
			}
		}
		
		if(relation.is("adj")) {
			return regexpQuery("raw_" + index, searchTerms.getRegexp(false));			
		}
		
		return null;
	}
	
	@Override
	public QueryBuilder visitSearchTerms(SearchTerms searchTerms) {
		if(searchTerms.isRegexp()) {
			return regexpQuery("text_content", searchTerms.getRegexp(false));
		}
		else {
			if(searchTerms.hasWildCards()) {
				QueryStringQueryBuilder queryBuilder = queryStringQuery(searchTerms.getQueryString("+")).analyzeWildcard(true);
				queryBuilder.enablePositionIncrements(false);
	
				if(schema == null || !indexSuffix.equals("")) {
					queryBuilder.field("text_content" + indexSuffix);
				}
				else {
					for(String field : schema.getTextualFieldNames()) {
						queryBuilder.field(field);
					}
				}
				return queryBuilder;
			}
			else {
				QueryBuilder qb;
				if(schema == null || !indexSuffix.equals("")) {
					qb = matchQuery("text_content" + indexSuffix, searchTerms.getSearchTerm()).operator(org.elasticsearch.index.query.Operator.AND);
				}
				else {
					Set<String> fields = schema.getTextualFieldNames();
					String[] fieldNames = fields.toArray(new String[fields.size()]);
				
					qb = multiMatchQuery(searchTerms.getSearchTerm(), fieldNames).operator(org.elasticsearch.index.query.Operator.AND);
				}
			
				return qb;
			}
		}
		
	}
}