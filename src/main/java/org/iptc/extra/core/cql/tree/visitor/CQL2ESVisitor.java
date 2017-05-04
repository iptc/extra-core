package org.iptc.extra.core.cql.tree.visitor;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.Modifier;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.Relation;
import org.iptc.extra.core.cql.tree.SearchClause;
import org.iptc.extra.core.cql.tree.SearchTerms;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class CQL2ESVisitor extends SyntaxTreeVisitor<QueryBuilder> {
	
	@Override
	public QueryBuilder visitPrefixClause(PrefixClause prefixClause) {
	
		BoolQueryBuilder booleanQb = boolQuery();
		
		Operator operator = prefixClause.getOperator();
		
		List<QueryBuilder> clausesQueries = new ArrayList<QueryBuilder>();
		for(Clause clause : prefixClause.getClauses()) {
			QueryBuilder queryBuilder = visit(clause);
			if(queryBuilder == null) {
				continue;
			}
			clausesQueries.add(queryBuilder);
		}
		
		if(clausesQueries.isEmpty()) {
			return null;
		}
		
		if(operator.isAnd()) {
			for(QueryBuilder stqb : clausesQueries) {
				booleanQb.must(stqb);
			}
		}
		else if(operator.isOr()) {
			for(QueryBuilder stqb : clausesQueries) {
				booleanQb.should(stqb);
			}

			if(operator.hasModifier("countunique")) {
				Modifier modifier = operator.getModifier("countunique");
				if(modifier.isComparitorLT() || modifier.isComparitorLTE()) {
						booleanQb.minimumShouldMatch(modifier.getValue());
				}
			}
		}
		else if(operator.isNot()) {
			for(QueryBuilder stqb : clausesQueries) {
				booleanQb.mustNot(stqb);
			}
		}
		else if(operator.isProx()) {
			for(QueryBuilder stqb : clausesQueries) {
				booleanQb.must(stqb);
			}
			
			if(operator.hasModifier("distance")) {
				//Modifier distanceModifier = operator.getModifier("distance");
			}
		}
		
		return booleanQb;
	}

	@Override
	public QueryBuilder visitSearchClause(SearchClause searchClause) {
		
		if(searchClause.hasIndex()) {
		
			Index index = searchClause.getIndex();
			Relation relation = searchClause.getRelation();
			SearchTerms searchTerms = searchClause.getSearchTerms();
			
			String query = searchTerms.getSearchTerm();
		
			if(relation.is("any") || relation.is("=")) {
				if(relation.hasModifier("stemming")) {
					return matchQuery("stemmed_" + index.getName(), query);
				}
				return matchQuery(index.getName(), query);
			}
			else if (relation.is("==")) {
				return termQuery(index.getName(), query);
			}
			else if (relation.is("all")) {
				MatchQueryBuilder qb;
				if(relation.hasModifier("stemming")) {
					qb = matchQuery("stemmed_" + index.getName(), query);
				}
				else {
					qb = matchQuery(index.getName(), query);
				}
				qb.operator(org.elasticsearch.index.query.Operator.AND);
				
				return qb;
			}
			else if (relation.is("adj")) {
				return matchPhraseQuery(index.getName(), query);
			}
			else if(relation.is(">")) {
				RangeQueryBuilder qb = rangeQuery(index.getName());
				return qb.gt(query);
			}
			else if(relation.is(">=")) {
				RangeQueryBuilder qb = rangeQuery(index.getName());
				return qb.gte(query);
			}
			else if(relation.is("<")) {
				RangeQueryBuilder qb = rangeQuery(index.getName());
				return qb.lt(query);
			}
			else if(relation.is("<=")) {
				RangeQueryBuilder qb = rangeQuery(index.getName());
				return qb.lte(query);
			}
			else if(relation.is("within") && searchTerms.numberOfTerms() == 2) {
				RangeQueryBuilder qb = rangeQuery(index.getName());	
				return qb.gte(searchTerms.getTerm(0)).lte(searchTerms.getTerm(1));
			}
			
			return null;
		}
		else {
			QueryBuilder qb = visitChildren(searchClause);
			return qb;
		}	
	}

	@Override
	public QueryBuilder visitSearchTerms(SearchTerms searchTerm) {
		QueryBuilder qb = matchQuery("_all", searchTerm.getSearchTerm());
		return qb;
	}
}