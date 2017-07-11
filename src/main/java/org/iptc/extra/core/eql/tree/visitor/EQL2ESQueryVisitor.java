package org.iptc.extra.core.eql.tree.visitor;

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
import org.iptc.extra.core.eql.tree.SyntaxTree;
import org.iptc.extra.core.eql.tree.extra.EQLOperator;
import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.CommentClause;
import org.iptc.extra.core.eql.tree.nodes.Index;
import org.iptc.extra.core.eql.tree.nodes.Modifier;
import org.iptc.extra.core.eql.tree.nodes.Operator;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;
import org.iptc.extra.core.eql.tree.nodes.ReferenceClause;
import org.iptc.extra.core.eql.tree.nodes.Relation;
import org.iptc.extra.core.eql.tree.nodes.SearchClause;
import org.iptc.extra.core.eql.tree.nodes.SearchTerm;
import org.iptc.extra.core.eql.tree.utils.TreeUtils;
import org.iptc.extra.core.types.Schema;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author manosetro - Manos Schinas
 * 
 *	EXTRA2ESQueryVisitor performs a depth-first traversal of the syntax tree 
 *	and generates the equivalent Elastic Search Query. 
 */
public class EQL2ESQueryVisitor extends SyntaxTreeVisitor<QueryBuilder> {
	
	private String indexSuffix = "";		// suffix on a field used to target other versions of the field: e.g. title -> title_sentences
	
	private boolean spanEnabled = false;	// when enabled, the elastic search query is a span query
	
	private Schema schema;					// the schema used to map EQL to ES
	
	public EQL2ESQueryVisitor() {
		
	}
	
	public EQL2ESQueryVisitor(Schema schema) {
		this.schema = schema;
	}

	@Override
	public QueryBuilder visitPrefixClause(PrefixClause prefixClause) {
		
		EQLOperator eqlOperator = prefixClause.getEQLOperator();

		// check EQL operator
		if(eqlOperator == EQLOperator.AND) {
			return andToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.OR) {
			return orToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.NOT) {
			return notToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.MINIMUM) {
			return minimumToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.SENTENCE) {
			return sentenceToES(prefixClause);
		}

		if(eqlOperator == EQLOperator.NOT_IN_SENTENCE) {
			return notInSentenceToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.PARAGRAPH) {
			return paragraphToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.NOT_IN_PARAGRAPH) {
			return notInParagraphToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.DISTANCE) {
			return distanceToES(prefixClause, false);
		}
		
		if(eqlOperator == EQLOperator.NOT_WITHIN_DISTANCE) {
			return notWithiDistanceToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.MAXIMUM_OCCURRENCE) {
			return occurenceToES(prefixClause);
		}

		if(eqlOperator == EQLOperator.MINIMUM_OCCURRENCE) {
			return occurenceToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.ORDER) {
			return orderToES(prefixClause);
		}
		
		if(eqlOperator == EQLOperator.ORDER_AND_DISTANCE) {
			return distanceToES(prefixClause, true);
		}

		if(eqlOperator == EQLOperator.NOT_IN_PHRASE) {
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
			SearchTerm searchTerms = searchClause.getSearchTerm();
			
			String code = getScriptCode(field + "_tokens", comparitor, value, searchTerms.getTerms());
			Script script = new Script(code);
			ScriptQueryBuilder scriptQuery = scriptQuery(script);
			booleanQb.must(scriptQuery);
		}
		else {
			Map<String, List<String>> fieldTerms = new HashMap<String, List<String>>();
			for(SearchClause searchClause : prefixClause.getSearchClause()) {
				
				Relation relation = searchClause.getRelation();
				
				// if index is not specified use text_content field
				String field = (searchClause.getIndex() == null) ? "text_content" : searchClause.getIndex().getName();
				
				SearchTerm searchTerm = searchClause.getSearchTerm();
			
				String fieldPrefix = "";
				if(relation != null && relation.hasModifier("stemming")) {
					fieldPrefix = "stemmed_";
				}
				
				String tokensField = fieldPrefix + field + "_tokens";
				
				List<String> terms = fieldTerms.get(tokensField);
				if(terms == null) {
					terms = new ArrayList<String>();
					fieldTerms.put(tokensField, terms);
				}
				
				if(relation == null || relation.is("=") || relation.is("any")) {
					terms.addAll(searchTerm.getTerms());
				}
				else if(relation.is("adj")) {
					terms.add(searchTerm.getSearchTerm().toLowerCase().trim());
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
			if(terms.isEmpty()) {
				continue;
			}
			
			String termsArray = StringUtils.join(terms, "', '").replaceAll("-", " ");
			
			buffer.append("for (int i = 0; i < doc['" + field + "'].length; ++i) { ");
			buffer.append("String t = doc['" + field + "'][i]; ");
			buffer.append("String token = t.substring(0, t.indexOf('_')).trim(); ");
			buffer.append("def terms = ['" + termsArray + "']; ");
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
			SearchTerm mergedSearchTerm = TreeUtils.mergeSearchTerm(childrenClauses);
			String queryString = mergedSearchTerm.getQueryString("");
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
			
			if(mergedSearchTerm.hasWildCards()) {	
				queryBuilder.analyzeWildcard(true);
			}	
			
			return queryBuilder;
		}
		else {
			BoolQueryBuilder booleanQb = boolQuery();
			
			List<QueryBuilder> mustClauses = new ArrayList<QueryBuilder>();
			List<QueryBuilder> mustNotClauses = new ArrayList<QueryBuilder>();
			for(Clause clause : childrenClauses) {
				if(EQLOperator.isEQLOperatorClause(clause, EQLOperator.NOT)) {
					PrefixClause notClause = (PrefixClause) clause;
					for(Clause notChild : notClause.getClauses()) {
						QueryBuilder queryBuilder = visit(notChild);
						if(queryBuilder == null) {
							continue;
						}
						mustNotClauses.add(queryBuilder);
					}
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
				SearchTerm mergedSearchTerm = TreeUtils.mergeSearchTerm(childrenClauses);
				String queryString = mergedSearchTerm.getQueryString("");
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
				
				if(mergedSearchTerm.hasWildCards()) {	
					queryBuilder.analyzeWildcard(true);
				}	
				
				return queryBuilder;
			}
			else {
				// Sub-statement are a mix of search clauses and other prefix clauses
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
	
	/*
	 * or/countunique>N
	 */
	private QueryBuilder minimumToES(PrefixClause prefixClause) {
		
		QueryBuilder queryBuilder = orToES(prefixClause);	// get OR query
		if(queryBuilder == null) {
			return null;
		}
		
		Operator operator = prefixClause.getOperator();
		Modifier modifier = operator.getModifier("countunique");
		Integer minimumShouldMatch = Integer.parseInt(modifier.getValue());
		if(modifier.isComparitorGT()) {
			// if comparitor is >= then set minimumShouldMatch to minimumShouldMatch + 1
			minimumShouldMatch += 1;
		}
		
		// Set minimumShouldMatch parameter
		if(queryBuilder instanceof QueryStringQueryBuilder) {
			((QueryStringQueryBuilder) queryBuilder).minimumShouldMatch(minimumShouldMatch.toString());
		}
		else if (queryBuilder instanceof BoolQueryBuilder) {
			((BoolQueryBuilder) queryBuilder).minimumShouldMatch(minimumShouldMatch);
		}
		else if(queryBuilder instanceof MultiMatchQueryBuilder) {
			((MultiMatchQueryBuilder) queryBuilder).minimumShouldMatch(minimumShouldMatch.toString());
		}
		
		return queryBuilder;
	}
	
	private BoolQueryBuilder notToES(PrefixClause prefixClause) {
		
		BoolQueryBuilder booleanQb = boolQuery();	// create boolean query builder
		List<QueryBuilder> childrenQueries = getClausesQueries(prefixClause.getClauses());
		if(childrenQueries.isEmpty()) {
			// there is no child query, return null
			return null;
		}
		
		for(QueryBuilder childQuery : childrenQueries) {
			// child query must not match
			booleanQb.mustNot(childQuery);
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
					QueryBuilder clauseQb = searchClausetoElasticSearchQuery(index + ".paragraph", searchClause.getRelation(), searchClause.getSearchTerm());
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
					QueryBuilder clauseQueryBuilder = searchClausetoElasticSearchQuery(index + ".paragraph", relation, searchClause.getSearchTerm());
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
			// the operator has clauses with more than one indices specified - cannot mix 
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
					QueryBuilder clauseQueryBuilder = searchClausetoElasticSearchQuery(index + ".sentence", searchClause.getRelation(), searchClause.getSearchTerm());
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
					QueryBuilder clauseQueryBuilder = searchClausetoElasticSearchQuery(index + ".sentence", relation, searchClause.getSearchTerm());
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
	
		List<SearchClause> searchClauses = prefixClause.getSearchClause();
		if(searchClauses.size() == 2) {
			this.spanEnabled = true;
			
			SearchClause includeSearchClause = searchClauses.get(0);
			SpanQueryBuilder include = (SpanQueryBuilder) visit(includeSearchClause);
			
			SearchClause excludeSearchClause = searchClauses.get(1);
			SearchTerm searchTerm = excludeSearchClause.getSearchTerm();
			
			SpanQueryBuilder exclude = null;
			if(searchTerm.isRegexp()) {
				exclude = (SpanQueryBuilder) visit(excludeSearchClause);
			}
			else {
				List<SearchClause> splittedSearchClauses = excludeSearchClause.splitSearchClause();
				
				if(splittedSearchClauses.size() > 1) {
					SpanNearQueryBuilder near = spanNearQuery((SpanQueryBuilder) visit(splittedSearchClauses.get(0)), 0);
					for(int i = 1; i < splittedSearchClauses.size() ; i++) {
						near.addClause((SpanQueryBuilder) visit(splittedSearchClauses.get(i)));
					}
					near.inOrder(false);

					exclude = near;
				}
				else {
					exclude = (SpanQueryBuilder) visit(includeSearchClause);
				}
			}
			this.spanEnabled = false;
			
			SpanNotQueryBuilder distanceQueryBuilder = spanNotQuery(include, exclude);

			return distanceQueryBuilder;
		}
		else {
			List<Clause> clauses = prefixClause.getPrefixOrSearchClause();
			if(clauses.size() == 2) {
				if(clauses.get(0) instanceof SearchClause && clauses.get(1) instanceof PrefixClause) {
					this.spanEnabled = true;
					SpanQueryBuilder include = (SpanQueryBuilder) visit(clauses.get(0));
					
					SpanQueryBuilder exclude = (SpanQueryBuilder) visit(clauses.get(1));
					
					this.spanEnabled = false;
					
					SpanNotQueryBuilder distanceQueryBuilder = spanNotQuery(include, exclude);
					return distanceQueryBuilder;
				}
			}
		}
		
		return null;
	}
	
	/*
	 * Iterate over a list of clauses anf return a list of Elastic Search QueryBuilder objects
	 */
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
	
	@Override
	public QueryBuilder visitSearchClause(SearchClause searchClause) {
		if(spanEnabled) {
			return searchClausetoSpanQuery(searchClause);
		}
		else {
			if(searchClause.hasIndex()) {
				Index index = searchClause.getIndex();
				Relation relation = searchClause.getRelation();
				SearchTerm searchTerm = searchClause.getSearchTerm();
		
				String indexName = index.getName() + indexSuffix;
				return searchClausetoElasticSearchQuery(indexName, relation, searchTerm);
			}
			else {
				QueryBuilder qb = visitChildren(searchClause);
				return qb;
			}	
		}
	}
	
	private QueryBuilder searchClausetoElasticSearchQuery(String index, Relation relation, SearchTerm searchTerm) {
		
		boolean isRegex = searchTerm.isRegexp();
		
		String query = searchTerm.getSearchTerm();
		if(isRegex && !relation.hasModifier("literal")) {
			return searchClauseToRegexQuery(index, relation, searchTerm);
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
			
			if(searchTerm.hasWildCards() && !isRegex) {
				return queryStringQuery(query)
						.field(index)
						.analyzeWildcard(true);
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
			
			if(searchTerm.hasWildCards() && !isRegex) {
				
				List<SpanQueryBuilder> spanQueries = new ArrayList<SpanQueryBuilder>();
				
				query = query.toLowerCase();
				for(String term : query.split("\\s+")) {
					if(term.contains("*") || term.contains("?")) {
						MultiTermQueryBuilder qb = (MultiTermQueryBuilder) wildcardQuery(index, term);
						spanQueries.add(spanMultiTermQueryBuilder(qb));
					}
					else {
						SpanTermQueryBuilder qb = spanTermQuery(index, term);
						spanQueries.add(qb);
					}
				}
				
				if(spanQueries.size() > 1) {
					SpanNearQueryBuilder spanNearQb = spanNearQuery(spanQueries.get(0), 1);
					for(int i = 1; i < spanQueries.size() ; i++) {
						spanNearQb.addClause(spanQueries.get(i));
					}
					
					return spanNearQb;
				}
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
		else if(relation.is("within") && searchTerm.numberOfTerms() == 2) {
			RangeQueryBuilder qb = rangeQuery(index);	
			return qb.gte(searchTerm.getTerm(0)).lte(searchTerm.getTerm(1));
		}
		
		return null;	
	}
	
	private SpanQueryBuilder searchClausetoSpanQuery(SearchClause searchClause) {
		
		SearchTerm searchTerm = searchClause.getSearchTerm();
		String query = searchTerm.getSearchTerm();
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
					
					SpanQueryBuilder initialSpan = null;
					if(queryTerms[0].contains("*") || queryTerms[0].contains("?")) {
						initialSpan = spanMultiTermQueryBuilder((MultiTermQueryBuilder) wildcardQuery(index, queryTerms[0]));
					}
					else {
						initialSpan = spanTermQuery(index, queryTerms[0]);
					}

					SpanOrQueryBuilder spanOr = spanOrQuery(initialSpan);
					for(int i = 1 ; i < queryTerms.length; i++) {
						String term = queryTerms[i];
						if(term.contains("*") || term.contains("?")) {
							MultiTermQueryBuilder qb = (MultiTermQueryBuilder) wildcardQuery(index, term);
							spanOr.addClause(spanMultiTermQueryBuilder(qb));
						}
						else {
							spanOr.addClause(spanTermQuery(index, term));
						}
					}
					
					return spanOr;
				}
				
				if(query.contains("*") || query.contains("?")) {
					MultiTermQueryBuilder qb = (MultiTermQueryBuilder) wildcardQuery(index, query);
					return spanMultiTermQueryBuilder(qb);
				}
				
				return spanTermQuery(index, query);
			}
			else if (relation.is("==")) {
				MultiTermQueryBuilder qb = (MultiTermQueryBuilder) termQuery(index, query);
				return spanMultiTermQueryBuilder(qb);

			}
			else if (relation.is("all")) {
				String[] queryTerms = query.trim().split("\\s+");
				if(queryTerms.length > 1) {
					SpanQueryBuilder initialSpan = null;
					if(queryTerms[0].contains("*") || queryTerms[0].contains("?")) {
						initialSpan = spanMultiTermQueryBuilder((MultiTermQueryBuilder) wildcardQuery(index, queryTerms[0]));
					}
					else {
						initialSpan = spanTermQuery(index, queryTerms[0]);
					}
					
					SpanNearQueryBuilder spanNear = spanNearQuery(initialSpan, Integer.MAX_VALUE);
					for(int i = 1 ; i < queryTerms.length; i++) {
						if(queryTerms[i].contains("*") || queryTerms[i].contains("?")) {
							MultiTermQueryBuilder qb = (MultiTermQueryBuilder) wildcardQuery(index, queryTerms[i]);
							spanNear.addClause(spanMultiTermQueryBuilder(qb));
						}
						else {
							spanNear.addClause(spanTermQuery(index, queryTerms[i]));
						}
					}
					
					return spanNear;
				}
				
				if(query.contains("*") || query.contains("?")) {
					MultiTermQueryBuilder qb = (MultiTermQueryBuilder) wildcardQuery(index, query);
					return spanMultiTermQueryBuilder(qb);
				}
				
				return spanTermQuery(index, query);
				
			}
			else if (relation.is("adj")) {
				
				if(relation.hasModifier("regexp")) {	
					RegexpQueryBuilder regexpQb = regexpQuery(index, searchTerm.getRegexp(false));
					return spanMultiTermQueryBuilder(regexpQb);
				}
				
				String[] queryTerms = query.trim().split("\\s+");
				if(queryTerms.length > 1) {
					SpanQueryBuilder initialSpan = null;
					if(queryTerms[0].contains("*") || queryTerms[0].contains("?")) {
						initialSpan = spanMultiTermQueryBuilder((MultiTermQueryBuilder) wildcardQuery(index, queryTerms[0]));
					}
					else {
						initialSpan = spanTermQuery(index, queryTerms[0]);
					}
					
					SpanNearQueryBuilder spanNear = spanNearQuery(initialSpan, 0);
					for(int i = 1 ; i < queryTerms.length; i++) {
						if(queryTerms[i].contains("*") || queryTerms[i].contains("?")) {
							MultiTermQueryBuilder qb = (MultiTermQueryBuilder) wildcardQuery(index, queryTerms[i]);
							spanNear.addClause(spanMultiTermQueryBuilder(qb));
						}
						else {
							spanNear.addClause(spanTermQuery(index, queryTerms[i]));
						}
					}
					
					return spanNear;
				}
				else {
					if(query.contains("*") || query.contains("?")) {
						MultiTermQueryBuilder qb = (MultiTermQueryBuilder) wildcardQuery(index, query);
						return spanMultiTermQueryBuilder(qb);
					}
					
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
			else if(relation.is("within") && searchTerm.numberOfTerms() == 2) {
				RangeQueryBuilder qb = rangeQuery(index).gte(searchTerm.getTerm(0)).lte(searchTerm.getTerm(1));
				
				return spanMultiTermQueryBuilder((MultiTermQueryBuilder) qb);
			}
		}
		
		return null;
	}
	
	private QueryBuilder searchClauseToRegexQuery(String index, Relation relation, SearchTerm searchTerm) {
		
		String query = searchTerm.getSearchTerm();
		
		if(relation.is("any") || relation.is("=") || relation.is("all")) {
			query = searchTerm.getRegexp(false);
			QueryStringQueryBuilder queryBuilder = queryStringQuery("/" + query + "/");
			queryBuilder.field("raw_" + index);
			queryBuilder.analyzeWildcard(true);
			
			if(relation.is("all")) {
				queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
			
			return queryBuilder;
		}
		
		if(relation.is("==")) {
			if(relation.hasModifier("regexp") || searchTerm.isRegexp()) {
				return regexpQuery("raw_" + index, searchTerm.getRegexp(false));
			}
			else {
				return wildcardQuery(index, searchTerm.getSearchTerm());
			}
		}
		
		if(relation.is("adj")) {
			return regexpQuery("raw_" + index, searchTerm.getRegexp(false));			
		}
		
		return null;
	}
	
	/*
	 * SearchTerm is visited in cases that there is no defined index and relation.
	 * 
	 */
	@Override
	public QueryBuilder visitSearchTerm(SearchTerm searchTerm) {
		if(searchTerm.isRegexp()) {
			// search term is a regexp, thus regexp elastic search query is used
			return regexpQuery("text_content", searchTerm.getRegexp(false));
		}
		else {
			if(searchTerm.hasWildCards()) {
				// search term has wild-cards, thus enable wildcards in query string builder
				QueryStringQueryBuilder queryBuilder = queryStringQuery(searchTerm.getQueryString("+")).analyzeWildcard(true);
				queryBuilder.enablePositionIncrements(false);
	
				if(schema == null || !indexSuffix.equals("")) {
					// target text_content
					queryBuilder.defaultField("text_content" + indexSuffix);
				}
				else {
					// target every textual field in the schema
					for(String field : schema.getTextualFieldNames()) {
						queryBuilder.field(field);
					}
				}
				
				return queryBuilder;
			}
			else {
				QueryBuilder qb;
				if(schema == null || !indexSuffix.equals("")) {
					// schema is not defined, thus text_content field is used
					qb = matchQuery("text_content" + indexSuffix, searchTerm.getSearchTerm())
							.operator(org.elasticsearch.index.query.Operator.AND);
				}
				else {
					// schema is defined, thus use a multi-match query in elastic search 
					Set<String> fields = schema.getTextualFieldNames();
					String[] fieldNames = fields.toArray(new String[fields.size()]);
				
					qb = multiMatchQuery(searchTerm.getSearchTerm(), fieldNames).operator(org.elasticsearch.index.query.Operator.AND);
				}
			
				return qb;
			}
		}
	}
	
	@Override
	public QueryBuilder visitReferenceClause(ReferenceClause referenceClause) {
		
		SyntaxTree syntaxTree = referenceClause.getRuleSyntaxTree();
		if(syntaxTree != null && !syntaxTree.hasErrors() && syntaxTree.getRootNode() != null) {
			// visit root node of referenced rule
			return visit(syntaxTree.getRootNode());
		}
		
		return null;
	}
}