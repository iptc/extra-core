package org.iptc.extra.core.eql;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.iptc.extra.core.eql.antlr.EqlBaseVisitor;
import org.iptc.extra.core.eql.antlr.EqlLexer;
import org.iptc.extra.core.eql.antlr.EqlParser;
import org.iptc.extra.core.eql.antlr.EqlParser.ModifierContext;
import org.iptc.extra.core.eql.antlr.EqlParser.StatementContext;
import org.iptc.extra.core.eql.tree.SyntaxError;
import org.iptc.extra.core.eql.tree.SyntaxTree;
import org.iptc.extra.core.eql.tree.extra.EQLOperator;
import org.iptc.extra.core.eql.tree.extra.EQLRelation;
import org.iptc.extra.core.eql.tree.nodes.Clause;
import org.iptc.extra.core.eql.tree.nodes.CommentClause;
import org.iptc.extra.core.eql.tree.nodes.ErrorMessageNode;
import org.iptc.extra.core.eql.tree.nodes.Index;
import org.iptc.extra.core.eql.tree.nodes.Modifier;
import org.iptc.extra.core.eql.tree.nodes.Node;
import org.iptc.extra.core.eql.tree.nodes.Operator;
import org.iptc.extra.core.eql.tree.nodes.PrefixClause;
import org.iptc.extra.core.eql.tree.nodes.ReferenceClause;
import org.iptc.extra.core.eql.tree.nodes.Relation;
import org.iptc.extra.core.eql.tree.nodes.SearchClause;
import org.iptc.extra.core.eql.tree.nodes.SearchTerm;

/**
 * @author manosetro - Manos Schinas
 * 
 * Extra Query Language (EQL) parser built upon Antlr 
 * 
 * That class is used to parse a rule expressed as an EQL string.
 * Generates the corresponding syntax tree (org.iptc.extra.core.eql.tree.SyntaxTree). 
 * 
 * This class is a wrapper of the Antlr parser in the package org.iptc.extra.core.eql.antlr
 * 
 * 
 */
public class EQLParser {
	
	/*
	 * Given a rule expressed as an EQL query, that method returns a syntax tree (org.iptc.extra.core.eql.SyntaxTree)
	 * 
	 */
	public static SyntaxTree parse(String eql) {
		
		CharStream input = new ANTLRInputStream(eql);
		EqlLexer lexer = new EqlLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		EqlParser parser = new EqlParser(tokens);	// Antlr parser used to parse EQL 
		parser.setBuildParseTree(true);
		parser.removeErrorListeners(); 
       
		// add custom error listeners
        SyntaxErrorsAggregator errorListener = new SyntaxErrorsAggregator();
        parser.addErrorListener(errorListener); 
        
		ParseTree tree = parser.prefixClause();	// get Antlr ParseTree 
		
		SyntaxTree syntaxTree = new SyntaxTree();	
		syntaxTree.setErrors(errorListener.getErrors());
		try {
			Node root = getRootNode(tree);	
			syntaxTree.setRootNode(root);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return syntaxTree;
	}
	
	/* 
	 * transforms parse tree (org.antlr.v4.runtime.tree.ParseTree) returned by Antlr parser to 
	 * org.iptc.extra.core.eql.tree.Node
	 */
	private static Node getRootNode(ParseTree tree) {
		
		EqlBaseVisitor<Node> visitor = new EqlBaseVisitor<Node>() {
			
			int depth = 0;
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.PrefixClauseContext and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.PrefixClause
			 */
			@Override 
			public Node visitPrefixClause(EqlParser.PrefixClauseContext ctx) { 
				
				PrefixClause prefixClause = new PrefixClause();
				
				prefixClause.setDepth(depth);
				depth++;
				
				for(ParseTree child : ctx.children) {
					if(child instanceof ErrorNode) {
						Node node = visit(child);
						if(node != null && node instanceof ErrorNode) {
							prefixClause.addError(node);
						}
					}
				}
				
				Node operator = visit(ctx.booleanOp()); 
				operator.setParent(prefixClause);
				
				EQLOperator extraOperator = EQLOperator.getEQLOperator((Operator) operator);
				prefixClause.setEQLOperator(extraOperator);
				prefixClause.setOperator((Operator) operator);
				
				List<Clause> clauses = new ArrayList<Clause>();
				for(StatementContext statement : ctx.statement()) {
					Node node = visit(statement); 
					if(node == null) {
						continue;
					}	
					node.setParent(prefixClause);
					
					if(node instanceof Clause) {
						clauses.add((Clause) node);
					}
					else if(node instanceof ErrorNode) {
						prefixClause.addError(node);
					}
				}
				prefixClause.setClauses(clauses);
				
				depth--;
				return prefixClause;				
			}
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.BooleanOpContext and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.Operator
			 */
			@Override 
			public Node visitBooleanOp(EqlParser.BooleanOpContext ctx) { 
				
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
				
				boolean valid = EQLOperator.isValid(operator);
				operator.setValid(valid);
				
				return operator;
			}
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.SearchClauseContext and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.SearchClause
			 */
			@Override 
			public Node visitSearchClause(EqlParser.SearchClauseContext ctx) { 
				
				SearchClause searchClause = new SearchClause();
				searchClause.setDepth(depth);
				depth++;
				
				Node searchTerms = visit(ctx.searchTerm());
				searchTerms.setParent(searchClause);
				searchClause.setSearchTerm((SearchTerm) searchTerms);
				
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
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.CommentClauseContext and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.CommentClause
			 */
			@Override 
			public Node visitCommentClause(EqlParser.CommentClauseContext ctx) {
				List<String> terms = new ArrayList<String>();
				for(ParseTree child : ctx.children) {
					terms.add(child.getText());
				}
				
				String comment = StringUtils.join(terms, " ");
				CommentClause commentClause = new CommentClause(comment);
				commentClause.setDepth(depth);
				
				return commentClause; 
			}
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.RelationContext and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.Relation
			 */
			@Override 
			public Node visitRelation(EqlParser.RelationContext ctx) { 
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
				
				boolean valid = EQLRelation.isValid(relation);
				relation.setValid(valid);
				
				return relation;
			}
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.SearchTermContext and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.SearchTerm
			 */
			@Override 
			public Node visitSearchTerm(EqlParser.SearchTermContext ctx) {
				List<String> terms = new ArrayList<String>();
				for(ParseTree child : ctx.children) {
					if(child.getText() != null && !child.getText().equals("\"") ) {
						terms.add(child.getText());
					}
				}
				
				SearchTerm searchTerm = new SearchTerm(terms);
				searchTerm.setDepth(depth);
				
				return searchTerm;
			}
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.ReferenceClauseContext and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.ReferenceClause
			 */
			@Override 
			public Node visitReferenceClause(EqlParser.ReferenceClauseContext ctx) {
				String ruleId = ctx.referencedRule().getText();
				
				ReferenceClause clause = new ReferenceClause();
				clause.setRuleId(ruleId);
				clause.setDepth(depth);
				
				return clause;
			}
			
			/*
			 * Visits org.iptc.extra.core.eql.parsers.EqlParser.ErrorNode and produces the  
			 * equivalent org.iptc.extra.core.eql.tree.nodes.ErrorMessageNode
			 */
			@Override
			public Node visitErrorNode(ErrorNode node) {
				ErrorMessageNode errorMsgNode = new ErrorMessageNode();
				errorMsgNode.setErrorMessage(node.getText());
				errorMsgNode.setDepth(depth);

				return errorMsgNode;
			}

		};
		
		Node root = visitor.visit(tree);
		return root;
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
	
	public static void main(String...args) {
		EQLMapper mapper = new EQLMapper();
		
		String cql = "(and "
				+ "(title any \"this is a test\")"
				+ "(body adj \"phrase to match\")"
				+ "(or "
				+ "(title any \"term2\")"
				+ "(title any/stemming \"term3\")"
				+ "// this is a comment //"
				+ ")"
				+ ")";
		
		
		SyntaxTree result = EQLParser.parse(cql);
		System.out.println(mapper.toString(result.getRootNode(), "\n", "\t"));
		
		System.out.println(mapper.toHtml(result.getRootNode(), "div"));
		
	}
	
}
