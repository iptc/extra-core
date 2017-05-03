package org.iptc.extra.core.cql;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.iptc.extra.core.cql.parsers.CqlBaseVisitor;
import org.iptc.extra.core.cql.parsers.CqlLexer;
import org.iptc.extra.core.cql.parsers.CqlParser;
import org.iptc.extra.core.cql.parsers.CqlParser.ModifierContext;
import org.iptc.extra.core.cql.parsers.CqlParser.StatementContext;
import org.iptc.extra.core.cql.tree.Clause;
import org.iptc.extra.core.cql.tree.Index;
import org.iptc.extra.core.cql.tree.Modifier;
import org.iptc.extra.core.cql.tree.Node;
import org.iptc.extra.core.cql.tree.Operator;
import org.iptc.extra.core.cql.tree.PrefixClause;
import org.iptc.extra.core.cql.tree.Relation;
import org.iptc.extra.core.cql.tree.SearchClause;
import org.iptc.extra.core.cql.tree.SearchTerms;
import org.iptc.extra.core.cql.tree.extra.ExtraOperator;
import org.iptc.extra.core.cql.tree.extra.ExtraRelation;

public class CQLExtraParser {
	
	private static Node getRootNode(ParseTree tree) {
		
		CqlBaseVisitor<Node> visitor = new CqlBaseVisitor<Node>() {
			
			int depth = 0;
			
			@Override 
			public Node visitPrefixClause(CqlParser.PrefixClauseContext ctx) { 
			
				PrefixClause prefixClause = new PrefixClause();
				
				prefixClause.setDepth(depth);
				depth++;
				
				Node operator = visit(ctx.booleanOp()); 
				operator.setParent(prefixClause);
				
				prefixClause.setOperator((Operator) operator);
				
				List<Clause> clauses = new ArrayList<Clause>();
				for(StatementContext statement : ctx.statement()) {
					Node clause = visit(statement); 
					clause.setParent(prefixClause);
					
					clauses.add((Clause) clause);
				}
				prefixClause.setClauses(clauses);
				
				depth--;
				return prefixClause;				
			}
			
			@Override 
			public Node visitBooleanOp(CqlParser.BooleanOpContext ctx) { 
				
				String operatorName = null;
				if(ctx.OR() != null) {
					operatorName = ctx.OR().getText().toLowerCase();
				}
				else if(ctx.AND() != null) {
					operatorName = ctx.AND().getText().toLowerCase();
				}
				else if(ctx.NOT() != null) {
					operatorName = ctx.NOT().getText().toLowerCase();
				}
				else if(ctx.PROX() != null) {
					operatorName = ctx.PROX().getText().toLowerCase();
				}
				
				Operator operator = new Operator(operatorName);
				operator.setDepth(depth);
				depth++;
				
				if(ctx.modifierList() != null) {
					List<Modifier> modifiers = new ArrayList<Modifier>();
					for(ModifierContext modCtx : ctx.modifierList().modifier()) {
						Modifier modifier = new Modifier(modCtx.modifierName().getText());
						if(modCtx.comparitorSymbol() != null) {
							modifier.setComparitor(modCtx.comparitorSymbol().getText());
						}
						if(modCtx.modifierValue() != null) {
							modifier.setValue(modCtx.modifierValue().getText());
						}
						modifiers.add(modifier);
					}
					
					if(!modifiers.isEmpty()) {
						operator.setModifiers(modifiers);
					}
				}
				
				depth--;
				
				boolean valid = ExtraOperator.isValid(operator);
				operator.setValid(valid);
				
				return operator;
			}
			
			@Override 
			public Node visitSearchClause(CqlParser.SearchClauseContext ctx) { 
				
				SearchClause searchClause = new SearchClause();
				searchClause.setDepth(depth);
				depth++;
				
				Node searchTerms = visit(ctx.searchTerm());
				searchTerms.setParent(searchClause);
				searchClause.setSearchTerm((SearchTerms) searchTerms);
				
				if(ctx.index() != null) {
					if(ctx.relation() != null) {
						Index index = new Index(ctx.index().getText());
						index.setDepth(depth);
						index.setParent(searchClause);
						searchClause.setIndex(index);
						
						Node relation = visit(ctx.relation());
						relation.setParent(searchClause);
						searchClause.setRelation((Relation) relation);
					}
				}
				
				depth--;
				return searchClause;
			}
			
			@Override 
			public Node visitRelation(CqlParser.RelationContext ctx) { 
				String relationName = ctx.comparitor().getText();
				
				Relation relation = new Relation(relationName);
				relation.setDepth(depth);
				depth++;
				
				if(ctx.modifierList() != null) {
					List<Modifier> modifiers = new ArrayList<Modifier>();
					for(ModifierContext modCtx : ctx.modifierList().modifier()) {
						Modifier modifier = new Modifier(modCtx.modifierName().getText());
						if(modCtx.comparitorSymbol() != null) {
							modifier.setComparitor(modCtx.comparitorSymbol().getText());
						}
						if(modCtx.modifierValue() != null) {
							modifier.setValue(modCtx.modifierValue().getText());
						}
						modifiers.add(modifier);
					}
					
					if(!modifiers.isEmpty()) {
						relation.setModifiers(modifiers);
					}
				}
				
				depth--;
				
				boolean valid = ExtraRelation.isValid(relation);
				relation.setValid(valid);
				
				return relation;
			}
			
			@Override 
			public Node visitSearchTerm(CqlParser.SearchTermContext ctx) {
				List<String> terms = new ArrayList<String>();
				for(ParseTree child : ctx.children) {
					if(child.getText() != null && !child.getText().equals("\"") ) {
						terms.add(child.getText());
					}
				}
				
				SearchTerms searchTerms = new SearchTerms(terms);
				searchTerms.setDepth(depth);
				
				return searchTerms;
			}
		};
		
		Node root = visitor.visit(tree);
		return root;
	}
	
	
	public static SyntaxTree parse(String cql) {
		CharStream input = new ANTLRInputStream(cql);
		CqlLexer lexer = new CqlLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		CqlParser parser = new CqlParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners(); 
       
        SyntaxErrorsAggregator errorListener = new SyntaxErrorsAggregator();
        parser.addErrorListener(errorListener); 
        
		ParseTree tree = parser.prefixClause();		
		
		SyntaxTree syntaxTree = new SyntaxTree();
		syntaxTree.setErrors(errorListener.getErrors());
		
		if(!syntaxTree.hasErrors()) {
			Node root = getRootNode(tree);
			syntaxTree.setRootNode(root);
		}
		
		return syntaxTree;
	}
	
	protected static class UnderlineListener extends BaseErrorListener {
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
	        System.err.println("line "+line+":"+charPositionInLine+" "+msg);
	        underlineError(recognizer,(Token)offendingSymbol, line, charPositionInLine);
	    }

	    protected void underlineError(Recognizer<?, ?> recognizer, Token offendingToken, int line, int charPositionInLine) {
	    	
	        CommonTokenStream tokens = (CommonTokenStream)recognizer.getInputStream();
	        String input = tokens.getTokenSource().getInputStream().toString();
	        String[] lines = input.split("\n");
	        String errorLine = lines[line - 1];
	        System.err.println(errorLine);
	        for (int i=0; i<charPositionInLine; i++) System.err.print(" ");
	        int start = offendingToken.getStartIndex();
	        int stop = offendingToken.getStopIndex();
	        if ( start>=0 && stop>=0 ) {
	            for (int i=start; i<=stop; i++) System.err.print("^");
	        }
	        System.err.println();
	    }
	}
	
	protected static class SyntaxErrorsAggregator extends BaseErrorListener {
		
	    private final List<SyntaxError> errors = new ArrayList<SyntaxError>();
	    
	    public List<SyntaxError> getErrors() {
	        return errors;
	    }
	    
	    @Override
	    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
	    	errors.add(new SyntaxError(line, charPositionInLine, (Token) offendingSymbol));
	    }

	}
	
	
}
