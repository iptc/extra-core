// Generated from /home/manosetro/IdeaProjects/cql-parser/parser/Cql.g4 by ANTLR 4.5.3

package org.iptc.extra.core.cql.parsers;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, AND=14, OR=15, NOT=16, PROX=17, 
		CHARS=18, WS=19;
	public static final int
		RULE_prefixClause = 0, RULE_statement = 1, RULE_booleanOp = 2, RULE_searchClause = 3, 
		RULE_commentClause = 4, RULE_referenceClause = 5, RULE_referencedRule = 6, 
		RULE_ref = 7, RULE_relation = 8, RULE_modifierList = 9, RULE_modifier = 10, 
		RULE_comparitor = 11, RULE_namedComparitor = 12, RULE_comparitorSymbol = 13, 
		RULE_modifierName = 14, RULE_modifierValue = 15, RULE_searchTerm = 16, 
		RULE_index = 17;
	public static final String[] ruleNames = {
		"prefixClause", "statement", "booleanOp", "searchClause", "commentClause", 
		"referenceClause", "referencedRule", "ref", "relation", "modifierList", 
		"modifier", "comparitor", "namedComparitor", "comparitorSymbol", "modifierName", 
		"modifierValue", "searchTerm", "index"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'//'", "'@ref'", "'=='", "'/'", "'='", "'>'", "'<'", 
		"'>='", "'<='", "'<>'", "'\"'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "AND", "OR", "NOT", "PROX", "CHARS", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Cql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class PrefixClauseContext extends ParserRuleContext {
		public BooleanOpContext booleanOp() {
			return getRuleContext(BooleanOpContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode EOF() { return getToken(CqlParser.EOF, 0); }
		public PrefixClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterPrefixClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitPrefixClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitPrefixClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixClauseContext prefixClause() throws RecognitionException {
		PrefixClauseContext _localctx = new PrefixClauseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prefixClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			match(T__0);
			setState(37);
			booleanOp();
			setState(39); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(38);
				statement();
				}
				}
				setState(41); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__0 || _la==T__2 );
			setState(43);
			match(T__1);
			setState(45);
			_la = _input.LA(1);
			if (_la==EOF) {
				{
				setState(44);
				match(EOF);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public PrefixClauseContext prefixClause() {
			return getRuleContext(PrefixClauseContext.class,0);
		}
		public SearchClauseContext searchClause() {
			return getRuleContext(SearchClauseContext.class,0);
		}
		public CommentClauseContext commentClause() {
			return getRuleContext(CommentClauseContext.class,0);
		}
		public ReferenceClauseContext referenceClause() {
			return getRuleContext(ReferenceClauseContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(51);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(47);
				prefixClause();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(48);
				searchClause();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(49);
				commentClause();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(50);
				referenceClause();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanOpContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(CqlParser.AND, 0); }
		public ModifierListContext modifierList() {
			return getRuleContext(ModifierListContext.class,0);
		}
		public TerminalNode OR() { return getToken(CqlParser.OR, 0); }
		public TerminalNode NOT() { return getToken(CqlParser.NOT, 0); }
		public TerminalNode PROX() { return getToken(CqlParser.PROX, 0); }
		public BooleanOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterBooleanOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitBooleanOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitBooleanOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanOpContext booleanOp() throws RecognitionException {
		BooleanOpContext _localctx = new BooleanOpContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_booleanOp);
		int _la;
		try {
			setState(69);
			switch (_input.LA(1)) {
			case AND:
				enterOuterAlt(_localctx, 1);
				{
				setState(53);
				match(AND);
				setState(55);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(54);
					modifierList();
					}
				}

				}
				break;
			case OR:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				match(OR);
				setState(59);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(58);
					modifierList();
					}
				}

				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 3);
				{
				setState(61);
				match(NOT);
				setState(63);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(62);
					modifierList();
					}
				}

				}
				break;
			case PROX:
				enterOuterAlt(_localctx, 4);
				{
				setState(65);
				match(PROX);
				setState(67);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(66);
					modifierList();
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SearchClauseContext extends ParserRuleContext {
		public SearchTermContext searchTerm() {
			return getRuleContext(SearchTermContext.class,0);
		}
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public RelationContext relation() {
			return getRuleContext(RelationContext.class,0);
		}
		public SearchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_searchClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterSearchClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitSearchClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitSearchClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SearchClauseContext searchClause() throws RecognitionException {
		SearchClauseContext _localctx = new SearchClauseContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_searchClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(T__0);
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(72);
				index();
				setState(73);
				relation();
				}
				break;
			}
			setState(77);
			searchTerm();
			setState(78);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentClauseContext extends ParserRuleContext {
		public CommentClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterCommentClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitCommentClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitCommentClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentClauseContext commentClause() throws RecognitionException {
		CommentClauseContext _localctx = new CommentClauseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_commentClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(T__2);
			setState(82); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(81);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==T__2) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(84); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << AND) | (1L << OR) | (1L << NOT) | (1L << PROX) | (1L << CHARS) | (1L << WS))) != 0) );
			setState(86);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReferenceClauseContext extends ParserRuleContext {
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public ReferencedRuleContext referencedRule() {
			return getRuleContext(ReferencedRuleContext.class,0);
		}
		public ReferenceClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_referenceClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterReferenceClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitReferenceClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitReferenceClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferenceClauseContext referenceClause() throws RecognitionException {
		ReferenceClauseContext _localctx = new ReferenceClauseContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_referenceClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(T__0);
			setState(89);
			ref();
			setState(90);
			referencedRule();
			setState(91);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReferencedRuleContext extends ParserRuleContext {
		public List<TerminalNode> CHARS() { return getTokens(CqlParser.CHARS); }
		public TerminalNode CHARS(int i) {
			return getToken(CqlParser.CHARS, i);
		}
		public ReferencedRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_referencedRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterReferencedRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitReferencedRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitReferencedRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferencedRuleContext referencedRule() throws RecognitionException {
		ReferencedRuleContext _localctx = new ReferencedRuleContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_referencedRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(93);
				match(CHARS);
				}
				}
				setState(96); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CHARS );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RefContext extends ParserRuleContext {
		public RefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ref; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefContext ref() throws RecognitionException {
		RefContext _localctx = new RefContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_ref);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(T__3);
			setState(99);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationContext extends ParserRuleContext {
		public ComparitorContext comparitor() {
			return getRuleContext(ComparitorContext.class,0);
		}
		public ModifierListContext modifierList() {
			return getRuleContext(ModifierListContext.class,0);
		}
		public RelationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterRelation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitRelation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitRelation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		RelationContext _localctx = new RelationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_relation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			comparitor();
			setState(103);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(102);
				modifierList();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifierListContext extends ParserRuleContext {
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public ModifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifierList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterModifierList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitModifierList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitModifierList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModifierListContext modifierList() throws RecognitionException {
		ModifierListContext _localctx = new ModifierListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_modifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(105);
				modifier();
				}
				}
				setState(108); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__5 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifierContext extends ParserRuleContext {
		public ModifierNameContext modifierName() {
			return getRuleContext(ModifierNameContext.class,0);
		}
		public ComparitorSymbolContext comparitorSymbol() {
			return getRuleContext(ComparitorSymbolContext.class,0);
		}
		public ModifierValueContext modifierValue() {
			return getRuleContext(ModifierValueContext.class,0);
		}
		public ModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitModifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModifierContext modifier() throws RecognitionException {
		ModifierContext _localctx = new ModifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_modifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(T__5);
			setState(111);
			modifierName();
			setState(115);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) {
				{
				setState(112);
				comparitorSymbol();
				setState(113);
				modifierValue();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparitorContext extends ParserRuleContext {
		public ComparitorSymbolContext comparitorSymbol() {
			return getRuleContext(ComparitorSymbolContext.class,0);
		}
		public NamedComparitorContext namedComparitor() {
			return getRuleContext(NamedComparitorContext.class,0);
		}
		public ComparitorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparitor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterComparitor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitComparitor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitComparitor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparitorContext comparitor() throws RecognitionException {
		ComparitorContext _localctx = new ComparitorContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_comparitor);
		try {
			setState(119);
			switch (_input.LA(1)) {
			case T__4:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
				enterOuterAlt(_localctx, 1);
				{
				setState(117);
				comparitorSymbol();
				}
				break;
			case CHARS:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				namedComparitor();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NamedComparitorContext extends ParserRuleContext {
		public List<TerminalNode> CHARS() { return getTokens(CqlParser.CHARS); }
		public TerminalNode CHARS(int i) {
			return getToken(CqlParser.CHARS, i);
		}
		public NamedComparitorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedComparitor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterNamedComparitor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitNamedComparitor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitNamedComparitor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedComparitorContext namedComparitor() throws RecognitionException {
		NamedComparitorContext _localctx = new NamedComparitorContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_namedComparitor);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(122); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(121);
					match(CHARS);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(124); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparitorSymbolContext extends ParserRuleContext {
		public ComparitorSymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparitorSymbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterComparitorSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitComparitorSymbol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitComparitorSymbol(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparitorSymbolContext comparitorSymbol() throws RecognitionException {
		ComparitorSymbolContext _localctx = new ComparitorSymbolContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_comparitorSymbol);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifierNameContext extends ParserRuleContext {
		public TerminalNode CHARS() { return getToken(CqlParser.CHARS, 0); }
		public ModifierNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifierName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterModifierName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitModifierName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitModifierName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModifierNameContext modifierName() throws RecognitionException {
		ModifierNameContext _localctx = new ModifierNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_modifierName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			match(CHARS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifierValueContext extends ParserRuleContext {
		public TerminalNode CHARS() { return getToken(CqlParser.CHARS, 0); }
		public ModifierValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifierValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterModifierValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitModifierValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitModifierValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModifierValueContext modifierValue() throws RecognitionException {
		ModifierValueContext _localctx = new ModifierValueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_modifierValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(CHARS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SearchTermContext extends ParserRuleContext {
		public TerminalNode CHARS() { return getToken(CqlParser.CHARS, 0); }
		public SearchTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_searchTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterSearchTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitSearchTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitSearchTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SearchTermContext searchTerm() throws RecognitionException {
		SearchTermContext _localctx = new SearchTermContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_searchTerm);
		int _la;
		try {
			setState(140);
			switch (_input.LA(1)) {
			case CHARS:
				enterOuterAlt(_localctx, 1);
				{
				setState(132);
				match(CHARS);
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				match(T__12);
				setState(135); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(134);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__12) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					setState(137); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << AND) | (1L << OR) | (1L << NOT) | (1L << PROX) | (1L << CHARS) | (1L << WS))) != 0) );
				setState(139);
				match(T__12);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexContext extends ParserRuleContext {
		public TerminalNode CHARS() { return getToken(CqlParser.CHARS, 0); }
		public IndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).enterIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CqlListener ) ((CqlListener)listener).exitIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CqlVisitor ) return ((CqlVisitor<? extends T>)visitor).visitIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexContext index() throws RecognitionException {
		IndexContext _localctx = new IndexContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(CHARS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\25\u0093\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\3\2\3\2\6\2*\n\2\r\2\16\2+\3\2\3\2\5\2\60\n\2\3\3\3\3\3"+
		"\3\3\3\5\3\66\n\3\3\4\3\4\5\4:\n\4\3\4\3\4\5\4>\n\4\3\4\3\4\5\4B\n\4\3"+
		"\4\3\4\5\4F\n\4\5\4H\n\4\3\5\3\5\3\5\3\5\5\5N\n\5\3\5\3\5\3\5\3\6\3\6"+
		"\6\6U\n\6\r\6\16\6V\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\6\ba\n\b\r\b\16\b"+
		"b\3\t\3\t\3\t\3\n\3\n\5\nj\n\n\3\13\6\13m\n\13\r\13\16\13n\3\f\3\f\3\f"+
		"\3\f\3\f\5\fv\n\f\3\r\3\r\5\rz\n\r\3\16\6\16}\n\16\r\16\16\16~\3\17\3"+
		"\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\6\22\u008a\n\22\r\22\16\22\u008b"+
		"\3\22\5\22\u008f\n\22\3\23\3\23\3\23\2\2\24\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$\2\5\3\2\5\5\4\2\7\7\t\16\3\2\17\17\u0096\2&\3\2\2\2\4"+
		"\65\3\2\2\2\6G\3\2\2\2\bI\3\2\2\2\nR\3\2\2\2\fZ\3\2\2\2\16`\3\2\2\2\20"+
		"d\3\2\2\2\22g\3\2\2\2\24l\3\2\2\2\26p\3\2\2\2\30y\3\2\2\2\32|\3\2\2\2"+
		"\34\u0080\3\2\2\2\36\u0082\3\2\2\2 \u0084\3\2\2\2\"\u008e\3\2\2\2$\u0090"+
		"\3\2\2\2&\'\7\3\2\2\')\5\6\4\2(*\5\4\3\2)(\3\2\2\2*+\3\2\2\2+)\3\2\2\2"+
		"+,\3\2\2\2,-\3\2\2\2-/\7\4\2\2.\60\7\2\2\3/.\3\2\2\2/\60\3\2\2\2\60\3"+
		"\3\2\2\2\61\66\5\2\2\2\62\66\5\b\5\2\63\66\5\n\6\2\64\66\5\f\7\2\65\61"+
		"\3\2\2\2\65\62\3\2\2\2\65\63\3\2\2\2\65\64\3\2\2\2\66\5\3\2\2\2\679\7"+
		"\20\2\28:\5\24\13\298\3\2\2\29:\3\2\2\2:H\3\2\2\2;=\7\21\2\2<>\5\24\13"+
		"\2=<\3\2\2\2=>\3\2\2\2>H\3\2\2\2?A\7\22\2\2@B\5\24\13\2A@\3\2\2\2AB\3"+
		"\2\2\2BH\3\2\2\2CE\7\23\2\2DF\5\24\13\2ED\3\2\2\2EF\3\2\2\2FH\3\2\2\2"+
		"G\67\3\2\2\2G;\3\2\2\2G?\3\2\2\2GC\3\2\2\2H\7\3\2\2\2IM\7\3\2\2JK\5$\23"+
		"\2KL\5\22\n\2LN\3\2\2\2MJ\3\2\2\2MN\3\2\2\2NO\3\2\2\2OP\5\"\22\2PQ\7\4"+
		"\2\2Q\t\3\2\2\2RT\7\5\2\2SU\n\2\2\2TS\3\2\2\2UV\3\2\2\2VT\3\2\2\2VW\3"+
		"\2\2\2WX\3\2\2\2XY\7\5\2\2Y\13\3\2\2\2Z[\7\3\2\2[\\\5\20\t\2\\]\5\16\b"+
		"\2]^\7\4\2\2^\r\3\2\2\2_a\7\24\2\2`_\3\2\2\2ab\3\2\2\2b`\3\2\2\2bc\3\2"+
		"\2\2c\17\3\2\2\2de\7\6\2\2ef\7\7\2\2f\21\3\2\2\2gi\5\30\r\2hj\5\24\13"+
		"\2ih\3\2\2\2ij\3\2\2\2j\23\3\2\2\2km\5\26\f\2lk\3\2\2\2mn\3\2\2\2nl\3"+
		"\2\2\2no\3\2\2\2o\25\3\2\2\2pq\7\b\2\2qu\5\36\20\2rs\5\34\17\2st\5 \21"+
		"\2tv\3\2\2\2ur\3\2\2\2uv\3\2\2\2v\27\3\2\2\2wz\5\34\17\2xz\5\32\16\2y"+
		"w\3\2\2\2yx\3\2\2\2z\31\3\2\2\2{}\7\24\2\2|{\3\2\2\2}~\3\2\2\2~|\3\2\2"+
		"\2~\177\3\2\2\2\177\33\3\2\2\2\u0080\u0081\t\3\2\2\u0081\35\3\2\2\2\u0082"+
		"\u0083\7\24\2\2\u0083\37\3\2\2\2\u0084\u0085\7\24\2\2\u0085!\3\2\2\2\u0086"+
		"\u008f\7\24\2\2\u0087\u0089\7\17\2\2\u0088\u008a\n\4\2\2\u0089\u0088\3"+
		"\2\2\2\u008a\u008b\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c"+
		"\u008d\3\2\2\2\u008d\u008f\7\17\2\2\u008e\u0086\3\2\2\2\u008e\u0087\3"+
		"\2\2\2\u008f#\3\2\2\2\u0090\u0091\7\24\2\2\u0091%\3\2\2\2\24+/\659=AE"+
		"GMVbinuy~\u008b\u008e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}