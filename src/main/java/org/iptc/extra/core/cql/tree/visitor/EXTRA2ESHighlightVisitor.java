package org.iptc.extra.core.cql.tree.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

import org.iptc.extra.core.cql.SyntaxTree;
import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.CommentClause;
import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.ReferenceClause;
import org.iptc.extra.core.cql.tree.Relation;
import org.iptc.extra.core.cql.tree.SearchClause;
import org.iptc.extra.core.cql.tree.SearchTerms;
import org.iptc.extra.core.cql.tree.extra.ExtraOperator;
import org.iptc.extra.core.cql.tree.utils.TreeUtils;
import org.iptc.extra.core.types.Schema;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class EXTRA2ESHighlightVisitor extends SyntaxTreeVisitor<QueryBuilder> {
	
	private Schema schema;
	
	public EXTRA2ESHighlightVisitor(Schema schema) {
		this.schema = schema;
	}

	@Override
	public QueryBuilder visitPrefixClause(PrefixClause prefixClause) {
		ExtraOperator extraOperator = prefixClause.getExtraOperator();
		
		if(extraOperator == null) {
			return null;
		}
		
		if(extraOperator == ExtraOperator.AND) {
			return andToES(prefixClause);
		}
		
		return orToES(prefixClause);
	}
	
	private QueryBuilder andToES(PrefixClause prefixClause) {
		List<Clause> childrenClauses = prefixClause.getClauses();
		childrenClauses = childrenClauses.stream().filter(clause -> !(clause instanceof CommentClause)).collect(Collectors.toList());
		
		if(childrenClauses.size() == 1) {
			return visit(childrenClauses.get(0));
		}
		
		if(TreeUtils.areSearchTermClauses(childrenClauses)) {
			SearchTerms mergedSearchTerms = TreeUtils.mergeSearchTerms(childrenClauses);
			if(mergedSearchTerms.isRegexp()) {
				QueryStringQueryBuilder queryBuilder = queryStringQuery(mergedSearchTerms.getSearchTerm());
				queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
				queryBuilder.analyzeWildcard(true);
				
				for(String field : schema.getTextualFieldNames()) {
					queryBuilder.field(field);
				}
				
				return queryBuilder;
			}
			else {
				Set<String> fields = schema.getTextualFieldNames();
				MultiMatchQueryBuilder qb = multiMatchQuery(mergedSearchTerms.getSearchTerm(), fields.toArray(new String[fields.size()]));
				
				qb.operator(org.elasticsearch.index.query.Operator.AND);
				return qb;
			}
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
		
		if(TreeUtils.areSearchTermClauses(childrenClauses)) {	
			SearchTerms mergedSearchTerms = TreeUtils.mergeSearchTerms(childrenClauses);

			QueryStringQueryBuilder queryBuilder = queryStringQuery(mergedSearchTerms.getSearchTerm());
			for(String field : schema.getTextualFieldNames()) {
				queryBuilder.field(field);
			}
			
			if(mergedSearchTerms.isRegexp()) {	
				queryBuilder.analyzeWildcard(true);
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
		if(searchClause.hasIndex()) {
			Index index = searchClause.getIndex();
			Relation relation = searchClause.getRelation();
			SearchTerms searchTerms = searchClause.getSearchTerms();
		
			String indexName = index.getName();
			return searchClausetoES(indexName, relation, searchTerms);
		}
		else {
			QueryBuilder qb = visitChildren(searchClause);
			return qb;
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
		
		boolean hasWildcards = searchTerms.isRegexp();
		
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
	
	private QueryBuilder searchClauseWithWildcards(String index, Relation relation, SearchTerms searchTerms) {
		
		String query = searchTerms.getSearchTerm();
		
		if(relation.is("any") || relation.is("=") || relation.is("all")) {
			query = StringUtils.join(searchTerms.getTerms(), "");
			QueryStringQueryBuilder queryBuilder = queryStringQuery("/" + query + "/");
			queryBuilder.field(index);
			queryBuilder.analyzeWildcard(true);
			
			if(relation.is("all")) {
				queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
			
			return queryBuilder;
		}
		
		if(relation.is("==")) {
			if(relation.hasModifier("regexp")) {
				query = StringUtils.join(searchTerms.getTerms(), "");
				return regexpQuery(index, query);
			}
			else {
				return wildcardQuery(index, query);
			}
		}
		
		if(relation.is("adj")) {
			query = StringUtils.join(searchTerms.getTerms(), "");
			return regexpQuery(index, query);		
		}
		
		return null;
	}
	
	@Override
	public QueryBuilder visitSearchTerms(SearchTerms searchTerms) {
		Set<String> fields = schema.getTextualFieldNames();
		String[] fieldNames = fields.toArray(new String[fields.size()]);
				
		MultiMatchQueryBuilder qb = multiMatchQuery(searchTerms.getSearchTerm(), fieldNames);
		qb.operator(org.elasticsearch.index.query.Operator.AND);
			
		return qb;
	}
	
}