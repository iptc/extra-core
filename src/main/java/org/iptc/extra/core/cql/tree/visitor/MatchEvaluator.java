package org.iptc.extra.core.cql.tree.visitor;

import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.SearchClause;
import org.iptc.extra.core.cql.tree.visitor.MatchEvaluator.Match;
import org.iptc.extra.core.types.document.Document;
import org.iptc.extra.core.types.document.DocumentField;

public class MatchEvaluator extends SyntaxTreeVisitor<Match> {

	private Document document;

	public MatchEvaluator(Document document) {
		this.document = document;
	}
	
	@Override
	public Match visitSearchClause(SearchClause searchClause) {
		if(searchClause.hasIndex()) {
			Index index = searchClause.getIndex();
			DocumentField field = document.get(index);
			Match match = match(field, searchClause);
			
			return match;
		}
		else {
			Match totalMatch = new Match();
			for(String fieldName : document.getFieldNames()) {
				DocumentField field = document.get(fieldName);
				Match match = match(field, searchClause);
				
				totalMatch.merge(match);
			}
			
			return totalMatch;
		}
	}

	Match match(DocumentField field, SearchClause searchClause) {
		
		return null;
	}
	
	@Override
	public Match visitPrefixClause(PrefixClause prefixClause) {
		return null;
	}
	
	public static class Match {
		
		private int firstParagraph = -1;
		private int lastParagraph = -1;
		private int firstSentence = -1;
		private int lastSentence = -1;
		private int occurences = 0;
		
		public Match merge(Match other) {
			
			Match m = new Match();
			
			this.firstParagraph = Math.min(this.firstParagraph, other.firstParagraph);
			this.lastParagraph = Math.max(this.lastParagraph, other.lastParagraph);
			this.firstSentence = Math.min(this.firstSentence, other.firstSentence);
			this.lastSentence = Math.max(this.lastSentence, other.lastSentence);
			
			this.occurences += other.occurences;
			
			return m;
		}
	}
}
