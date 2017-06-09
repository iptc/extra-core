package org.iptc.extra.core.cql.tree.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiTermQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.SpanMultiTermQueryBuilder;
import org.elasticsearch.index.query.SpanNearQueryBuilder;
import org.elasticsearch.index.query.SpanNotQueryBuilder;
import org.elasticsearch.index.query.SpanOrQueryBuilder;
import org.elasticsearch.index.query.SpanQueryBuilder;
import org.elasticsearch.index.query.SpanTermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.iptc.extra.core.cql.SyntaxTree;
import org.iptc.extra.core.cql.tree.Clause;
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

public class EXTRA2ESVisitor extends SyntaxTreeVisitor<QueryBuilder> {
	
	private String indexSuffix = "";
	
	private boolean spanEnabled = false;
	
	private Schema schema;
	
	public EXTRA2ESVisitor() {
		
	}
	
	public EXTRA2ESVisitor(Schema schema) {
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
			return orToES(prefixClause);
		}

		if(extraOperator == ExtraOperator.MINIMUM_OCCURRENCE) {
			return orToES(prefixClause);
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
	
	private QueryBuilder andToES(PrefixClause prefixClause) {
		List<Clause> childrenClauses = prefixClause.getClauses();
		if(childrenClauses.size() == 1) {
			return visit(childrenClauses.get(0));
		}
		
		if(TreeUtils.areSearchTermClauses(childrenClauses)) {
			SearchTerms mergedSearchTerms = TreeUtils.mergeTerms(childrenClauses);
			
			QueryStringQueryBuilder queryBuilder = queryStringQuery(mergedSearchTerms.getSearchTerm());
			queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			if(mergedSearchTerms.hasWildcards()) {
				queryBuilder.defaultField("text_content");
				queryBuilder.analyzeWildcard(true);
			}
			else {
				queryBuilder.defaultField("stemmed_text_content");
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
		if(childrenClauses.size() == 1) {
			return visit(childrenClauses.get(0));
		}
		
		if(spanEnabled) {
			List<QueryBuilder> clausesQueries = getClausesQueries(childrenClauses);
			if(clausesQueries.size() < 1) {
				return null;
			}
			
			SpanOrQueryBuilder spanOrQb = spanOrQuery((SpanQueryBuilder) clausesQueries.get(0));
			for(int i = 1; i < clausesQueries.size(); i++) {
				
				spanOrQb.addClause((SpanQueryBuilder) clausesQueries.get(i));
			}	
			return spanOrQb;
		}
		else {
			if(TreeUtils.areSearchTermClauses(childrenClauses)) {
				SearchTerms mergedSearchTerms = TreeUtils.mergeTerms(childrenClauses);
			
				QueryStringQueryBuilder queryBuilder = queryStringQuery(mergedSearchTerms.getSearchTerm());
				if(mergedSearchTerms.hasWildcards()) {
					queryBuilder.defaultField("text_content");
					queryBuilder.analyzeWildcard(true);
				}
				else {
					queryBuilder.defaultField("stemmed_text_content");
				}
			
				return queryBuilder;
			}
			else {
				BoolQueryBuilder booleanQb = boolQuery();
				List<QueryBuilder> clausesQueries = getClausesQueries(childrenClauses);
				if(clausesQueries.isEmpty()) {
					return null;
				}
				for(QueryBuilder stqb : clausesQueries) {
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
		
		if(indices.size() != 1) {
			// the operator has clauses with multiple indices specified 
			return null;
		}
		
		String index = indices.toArray()[0] + "_paragraphs";
		if(searchClauses.size() == prefixClause.getClauses().size()) {
			BoolQueryBuilder booleanQb = boolQuery();
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
		
		if(searchClauses.size() == prefixClause.getClauses().size()) {
			BoolQueryBuilder booleanQb = boolQuery();
			if(indices.size() == 1) {
				for(SearchClause searchClause : searchClauses) {
					QueryBuilder clauseQueryBuilder = searchClausetoES(index + ".sentence", searchClause.getRelation(), searchClause.getSearchTerms());
					if(clauseQueryBuilder != null) {
						booleanQb.must(clauseQueryBuilder);
					}
				}
				NestedQueryBuilder nestedQb = nestedQuery(index, booleanQb, ScoreMode.Total);
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
				NestedQueryBuilder nestedQb = nestedQuery(index, booleanQb, ScoreMode.Total);
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
			}
		}
		
		NestedQueryBuilder nestedQb = sentenceToES(prefixClause);
		if(nestedQb != null) {
			booleanQb.mustNot(nestedQb);
		}
		
		return booleanQb;
	}

	private SpanNearQueryBuilder distanceToES(PrefixClause prefixClause, boolean inOrder) {
		
		Operator operator = prefixClause.getOperator();
		Modifier distanceModifier = operator.getModifier("distance");
		int slop = Integer.parseInt(distanceModifier.getValue());
		
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
			SpanNearQueryBuilder queryBuilder = spanNearQuery((SpanQueryBuilder) clauseQbs.get(0), slop);
			for(int i = 1; i < clauseQbs.size() ; i++) {
				queryBuilder.addClause((SpanQueryBuilder) clauseQbs.get(i));
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
				return null;
			}	
	
			for(QueryBuilder clauseQb : clausesQueries) {
				booleanQb.must(clauseQb);
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
				SpanQueryBuilder include = (SpanQueryBuilder) clauseSpanQbs.get(0);
				SpanQueryBuilder exclude = (SpanQueryBuilder) clauseSpanQbs.get(1);
				SpanNotQueryBuilder distanceQueryBuilder = spanNotQuery(include, exclude);
				distanceQueryBuilder.dist(dist);
			
				booleanQb.must(distanceQueryBuilder);
			
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
		
		boolean hasWildcards = searchTerms.hasWildcards();
		
		String query = searchTerms.getSearchTerm();
		if(hasWildcards && !relation.hasModifier("literal")) {
			return searchClauseWithWildcards(index, relation, searchTerms);
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
			String index = "text_content";
			return spanTermQuery(index, query);
		}
		else {
			String index = searchClause.getIndex().getName();
			Relation relation = searchClause.getRelation();
			
			if(relation.is("any") || relation.is("=")) {
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
	
	private QueryBuilder searchClauseWithWildcards(String index, Relation relation, SearchTerms searchTerms) {
		
		String query = searchTerms.getSearchTerm();
		
		if(relation.is("any") || relation.is("=") || relation.is("all")) {
			QueryStringQueryBuilder queryBuilder = queryStringQuery(query);
			queryBuilder.field(index);
			queryBuilder.analyzeWildcard(true);
			
			if(relation.is("all")) {
				queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
			
			return queryBuilder;
		}
		
		if(relation.is("==")) {
			return wildcardQuery(index, query);
		}
		
		if(relation.is("adj")) {
			query = query.toLowerCase().trim();
			
			String[] queryTerms = query.trim().split("\\s+");
			if(queryTerms.length > 1) {
				
				List<SpanQueryBuilder> qbs = new ArrayList<SpanQueryBuilder>();
				for(String term : queryTerms) {
					if(term.contains("*") || term.contains("?")) {
						MultiTermQueryBuilder multiTermQuery = (MultiTermQueryBuilder) wildcardQuery(index, term);
						
						SpanMultiTermQueryBuilder spanQuery = spanMultiTermQueryBuilder(multiTermQuery);
						qbs.add(spanQuery);
					}
					else {
						SpanTermQueryBuilder spanQuery = spanTermQuery(index, term);
						qbs.add(spanQuery);
					}
				}
				
				SpanNearQueryBuilder spanNear = spanNearQuery(qbs.get(0), 0);
				for(int i = 1 ; i < qbs.size() ; i++) {
					spanNear.addClause(qbs.get(i));
				}
				spanNear.inOrder(true);
				
				return spanNear;
						
			}
			else {
				return wildcardQuery(index, query);
			}			
		}
		
		return null;
	}
	
	@Override
	public QueryBuilder visitSearchTerms(SearchTerms searchTerms) {
		if(searchTerms.hasWildcards()) {
			
			String searchTerm = StringUtils.join(searchTerms.getTerms(), " ");
			WildcardQueryBuilder qb = wildcardQuery("text_content", searchTerm);
			
			return qb;
		}
		else {
			QueryBuilder qb;
			if(schema == null) {
				qb = matchQuery("text_content", searchTerms.getSearchTerm());
			}
			else {
				Set<String> fields = schema.getTextualFieldNames();
				qb = multiMatchQuery(searchTerms.getSearchTerm(), fields.toArray(new String[fields.size()]));
			}
			
			return qb;
		}
		
	}
}