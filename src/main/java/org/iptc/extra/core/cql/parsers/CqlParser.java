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
		T__9=10, T__10=11, T__11=12, AND=13, OR=14, NOT=15, PROX=16, CHARS=17, 
		WS=18;
	public static final int
		RULE_prefixClause = 0, RULE_statement = 1, RULE_booleanOp = 2, RULE_searchClause = 3, 
		RULE_commentClause = 4, RULE_relation = 5, RULE_modifierList = 6, RULE_modifier = 7, 
		RULE_comparitor = 8, RULE_namedComparitor = 9, RULE_comparitorSymbol = 10, 
		RULE_modifierName = 11, RULE_modifierValue = 12, RULE_searchTerm = 13, 
		RULE_index = 14;
	public static final String[] ruleNames = {
		"prefixClause", "statement", "booleanOp", "searchClause", "commentClause", 
		"relation", "modifierList", "modifier", "comparitor", "namedComparitor", 
		"comparitorSymbol", "modifierName", "modifierValue", "searchTerm", "index"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'//'", "'/'", "'='", "'>'", "'<'", "'>='", "'<='", 
		"'<>'", "'=='", "'\"'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "AND", "OR", "NOT", "PROX", "CHARS", "WS"
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
			setState(30);
			match(T__0);
			setState(31);
			booleanOp();
			setState(33); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(32);
				statement();
				}
				}
				setState(35); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__0 || _la==T__2 );
			setState(37);
			match(T__1);
			setState(39);
			_la = _input.LA(1);
			if (_la==EOF) {
				{
				setState(38);
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
			setState(44);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(41);
				prefixClause();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(42);
				searchClause();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(43);
				commentClause();
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
			setState(62);
			switch (_input.LA(1)) {
			case AND:
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
				match(AND);
				setState(48);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(47);
					modifierList();
					}
				}

				}
				break;
			case OR:
				enterOuterAlt(_localctx, 2);
				{
				setState(50);
				match(OR);
				setState(52);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(51);
					modifierList();
					}
				}

				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 3);
				{
				setState(54);
				match(NOT);
				setState(56);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(55);
					modifierList();
					}
				}

				}
				break;
			case PROX:
				enterOuterAlt(_localctx, 4);
				{
				setState(58);
				match(PROX);
				setState(60);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(59);
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
			setState(64);
			match(T__0);
			setState(68);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(65);
				index();
				setState(66);
				relation();
				}
				break;
			}
			setState(70);
			searchTerm();
			setState(71);
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
			setState(73);
			match(T__2);
			setState(75); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(74);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==T__2) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(77); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << AND) | (1L << OR) | (1L << NOT) | (1L << PROX) | (1L << CHARS) | (1L << WS))) != 0) );
			setState(79);
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
		enterRule(_localctx, 10, RULE_relation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			comparitor();
			setState(83);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(82);
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
		enterRule(_localctx, 12, RULE_modifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(85);
				modifier();
				}
				}
				setState(88); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__3 );
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
		enterRule(_localctx, 14, RULE_modifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(T__3);
			setState(91);
			modifierName();
			setState(95);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10))) != 0)) {
				{
				setState(92);
				comparitorSymbol();
				setState(93);
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
		enterRule(_localctx, 16, RULE_comparitor);
		try {
			setState(99);
			switch (_input.LA(1)) {
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
				enterOuterAlt(_localctx, 1);
				{
				setState(97);
				comparitorSymbol();
				}
				break;
			case CHARS:
				enterOuterAlt(_localctx, 2);
				{
				setState(98);
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
		enterRule(_localctx, 18, RULE_namedComparitor);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(102); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(101);
					match(CHARS);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(104); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
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
		enterRule(_localctx, 20, RULE_comparitorSymbol);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10))) != 0)) ) {
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
		enterRule(_localctx, 22, RULE_modifierName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
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
		enterRule(_localctx, 24, RULE_modifierValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
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
		enterRule(_localctx, 26, RULE_searchTerm);
		int _la;
		try {
			setState(120);
			switch (_input.LA(1)) {
			case CHARS:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				match(CHARS);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				match(T__11);
				setState(115); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(114);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__11) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					setState(117); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << AND) | (1L << OR) | (1L << NOT) | (1L << PROX) | (1L << CHARS) | (1L << WS))) != 0) );
				setState(119);
				match(T__11);
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
		enterRule(_localctx, 28, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\24\177\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\2\6\2$\n\2"+
		"\r\2\16\2%\3\2\3\2\5\2*\n\2\3\3\3\3\3\3\5\3/\n\3\3\4\3\4\5\4\63\n\4\3"+
		"\4\3\4\5\4\67\n\4\3\4\3\4\5\4;\n\4\3\4\3\4\5\4?\n\4\5\4A\n\4\3\5\3\5\3"+
		"\5\3\5\5\5G\n\5\3\5\3\5\3\5\3\6\3\6\6\6N\n\6\r\6\16\6O\3\6\3\6\3\7\3\7"+
		"\5\7V\n\7\3\b\6\bY\n\b\r\b\16\bZ\3\t\3\t\3\t\3\t\3\t\5\tb\n\t\3\n\3\n"+
		"\5\nf\n\n\3\13\6\13i\n\13\r\13\16\13j\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3"+
		"\17\3\17\6\17v\n\17\r\17\16\17w\3\17\5\17{\n\17\3\20\3\20\3\20\2\2\21"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36\2\5\3\2\5\5\3\2\7\r\3\2\16\16"+
		"\u0083\2 \3\2\2\2\4.\3\2\2\2\6@\3\2\2\2\bB\3\2\2\2\nK\3\2\2\2\fS\3\2\2"+
		"\2\16X\3\2\2\2\20\\\3\2\2\2\22e\3\2\2\2\24h\3\2\2\2\26l\3\2\2\2\30n\3"+
		"\2\2\2\32p\3\2\2\2\34z\3\2\2\2\36|\3\2\2\2 !\7\3\2\2!#\5\6\4\2\"$\5\4"+
		"\3\2#\"\3\2\2\2$%\3\2\2\2%#\3\2\2\2%&\3\2\2\2&\'\3\2\2\2\')\7\4\2\2(*"+
		"\7\2\2\3)(\3\2\2\2)*\3\2\2\2*\3\3\2\2\2+/\5\2\2\2,/\5\b\5\2-/\5\n\6\2"+
		".+\3\2\2\2.,\3\2\2\2.-\3\2\2\2/\5\3\2\2\2\60\62\7\17\2\2\61\63\5\16\b"+
		"\2\62\61\3\2\2\2\62\63\3\2\2\2\63A\3\2\2\2\64\66\7\20\2\2\65\67\5\16\b"+
		"\2\66\65\3\2\2\2\66\67\3\2\2\2\67A\3\2\2\28:\7\21\2\29;\5\16\b\2:9\3\2"+
		"\2\2:;\3\2\2\2;A\3\2\2\2<>\7\22\2\2=?\5\16\b\2>=\3\2\2\2>?\3\2\2\2?A\3"+
		"\2\2\2@\60\3\2\2\2@\64\3\2\2\2@8\3\2\2\2@<\3\2\2\2A\7\3\2\2\2BF\7\3\2"+
		"\2CD\5\36\20\2DE\5\f\7\2EG\3\2\2\2FC\3\2\2\2FG\3\2\2\2GH\3\2\2\2HI\5\34"+
		"\17\2IJ\7\4\2\2J\t\3\2\2\2KM\7\5\2\2LN\n\2\2\2ML\3\2\2\2NO\3\2\2\2OM\3"+
		"\2\2\2OP\3\2\2\2PQ\3\2\2\2QR\7\5\2\2R\13\3\2\2\2SU\5\22\n\2TV\5\16\b\2"+
		"UT\3\2\2\2UV\3\2\2\2V\r\3\2\2\2WY\5\20\t\2XW\3\2\2\2YZ\3\2\2\2ZX\3\2\2"+
		"\2Z[\3\2\2\2[\17\3\2\2\2\\]\7\6\2\2]a\5\30\r\2^_\5\26\f\2_`\5\32\16\2"+
		"`b\3\2\2\2a^\3\2\2\2ab\3\2\2\2b\21\3\2\2\2cf\5\26\f\2df\5\24\13\2ec\3"+
		"\2\2\2ed\3\2\2\2f\23\3\2\2\2gi\7\23\2\2hg\3\2\2\2ij\3\2\2\2jh\3\2\2\2"+
		"jk\3\2\2\2k\25\3\2\2\2lm\t\3\2\2m\27\3\2\2\2no\7\23\2\2o\31\3\2\2\2pq"+
		"\7\23\2\2q\33\3\2\2\2r{\7\23\2\2su\7\16\2\2tv\n\4\2\2ut\3\2\2\2vw\3\2"+
		"\2\2wu\3\2\2\2wx\3\2\2\2xy\3\2\2\2y{\7\16\2\2zr\3\2\2\2zs\3\2\2\2{\35"+
		"\3\2\2\2|}\7\23\2\2}\37\3\2\2\2\23%).\62\66:>@FOUZaejwz";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}