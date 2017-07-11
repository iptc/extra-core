package org.iptc.extra.core.eql.tree.visitor;

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
import org.iptc.extra.core.eql.tree.SyntaxTree;
import org.iptc.extra.core.eql.tree.extra.EQLOperator;
import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.CommentClause;
import org.iptc.extra.core.eql.tree.nodes.Index;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;
import org.iptc.extra.core.eql.tree.nodes.ReferenceClause;
import org.iptc.extra.core.eql.tree.nodes.Relation;
import org.iptc.extra.core.eql.tree.nodes.SearchClause;
import org.iptc.extra.core.eql.tree.nodes.SearchTerm;
import org.iptc.extra.core.types.Schema;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class EQL2HighlightVisitor extends SyntaxTreeVisitor<QueryBuilder> {
	
	private Schema schema;
	
	public EQL2HighlightVisitor(Schema schema) {
		this.schema = schema;
	}

	@Override
	public QueryBuilder visitPrefixClause(PrefixClause prefixClause) {
		EQLOperator extraOperator = prefixClause.getEQLOperator();
		
		if(extraOperator == null) {
			return null;
		}
		
		if(extraOperator == EQLOperator.AND) {
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
		
		BoolQueryBuilder booleanQb = boolQuery();
			
		List<QueryBuilder> mustClauses = new ArrayList<QueryBuilder>();
		List<QueryBuilder> mustNotClauses = new ArrayList<QueryBuilder>();
		for(Clause clause : childrenClauses) {
			if(EQLOperator.isEQLOperatorClause(clause, EQLOperator.NOT)) {
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
	
	private QueryBuilder orToES(PrefixClause prefixClause) {
		
		List<Clause> childrenClauses = prefixClause.getClauses();
		childrenClauses = childrenClauses.stream().filter(clause -> !(clause instanceof CommentClause)).collect(Collectors.toList());
		
		if(childrenClauses.size() == 1) {
			return visit(childrenClauses.get(0));
		}

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
			SearchTerm searchTerm = searchClause.getSearchTerm();
		
			String indexName = index.getName();
			return searchClausetoES(indexName, relation, searchTerm);
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
	
	private QueryBuilder searchClausetoES(String index, Relation relation, SearchTerm searchTerm) {
		
		if(index.equals("text_content")) {
			BoolQueryBuilder booleanQb = boolQuery();
			for(String field : schema.getTextualFieldNames()) {
				QueryBuilder fieldQb = searchClausetoES(field, relation, searchTerm);
				if(fieldQb != null) {
					booleanQb.should(fieldQb);
				}
			}
			
			return booleanQb;
		}
	
		boolean isRegexp = searchTerm.isRegexp();
		boolean hasWildcards = searchTerm.hasWildCards();
		
		String query = searchTerm.getSearchTerm();
		if(isRegexp && !relation.hasModifier("literal")) {
			return regexpSearchClause(index, relation, searchTerm);
		}
		
		if(hasWildcards && !relation.hasModifier("literal")) {
			return wildcardsSearchClause(index, relation, searchTerm);
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
		else if(relation.is("within") && searchTerm.numberOfTerms() == 2) {
			RangeQueryBuilder qb = rangeQuery(index);	
			return qb.gte(searchTerm.getTerm(0)).lte(searchTerm.getTerm(1));
		}
		
		return null;	
	}
	
	private QueryBuilder wildcardsSearchClause(String index, Relation relation, SearchTerm searchTerm) {
		
		String query = searchTerm.getSearchTerm();
		
		if(relation.is("any") || relation.is("=") || relation.is("all") || relation.is("adj")) {
			QueryStringQueryBuilder queryBuilder = queryStringQuery(query);
			
			queryBuilder.analyzeWildcard(true);
			
			if(index.equals("")) {
				for(String field : schema.getTextualFieldNames()) {
					queryBuilder.field(field);
				}
			}
			else {
				queryBuilder.defaultField(index);
			}
			
			if(relation.is("all") || relation.is("adj")) {
				queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
			
			return queryBuilder;
		}
		
		if(relation.is("==")) {
			return wildcardQuery(index, query);
		}
		
		return null;
	}
	
	private QueryBuilder regexpSearchClause(String index, Relation relation, SearchTerm searchTerm) {
		
		String query = searchTerm.getSearchTerm();
		
		if(relation.is("any") || relation.is("=") || relation.is("all")) {
			query = StringUtils.join(searchTerm.getTerms(), "");
			QueryStringQueryBuilder queryBuilder = queryStringQuery("/" + query + "/");
			
			queryBuilder.analyzeWildcard(true);
			
			if(index.equals("")) {
				for(String field : schema.getTextualFieldNames()) {
					queryBuilder.field(field);
				}
			}
			else {
				queryBuilder.defaultField(index);
			}
			
			if(relation.is("all")) {
				queryBuilder.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
			
			return queryBuilder;
		}
		
		if(relation.is("==")) {
			if(relation.hasModifier("regexp")) {
				query = StringUtils.join(searchTerm.getTerms(), "");
				return regexpQuery(index, query);
			}
			else {
				return wildcardQuery(index, query);
			}
		}
		
		if(relation.is("adj")) {
			query = StringUtils.join(searchTerm.getTerms(), "");
			return regexpQuery(index, query);		
		}
		
		return null;
	}
	
	@Override
	public QueryBuilder visitSearchTerm(SearchTerm searchTerm) {
		Set<String> fields = schema.getTextualFieldNames();
		String[] fieldNames = fields.toArray(new String[fields.size()]);
				
		MultiMatchQueryBuilder qb = multiMatchQuery(searchTerm.getSearchTerm(), fieldNames);
		qb.operator(org.elasticsearch.index.query.Operator.AND);
			
		return qb;
	}
	
}