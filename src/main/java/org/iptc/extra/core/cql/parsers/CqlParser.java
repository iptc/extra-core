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
		T__9=10, T__10=11, AND=12, OR=13, NOT=14, PROX=15, CHARS=16, WS=17;
	public static final int
		RULE_prefixClause = 0, RULE_statement = 1, RULE_booleanOp = 2, RULE_searchClause = 3, 
		RULE_relation = 4, RULE_modifierList = 5, RULE_modifier = 6, RULE_comparitor = 7, 
		RULE_namedComparitor = 8, RULE_comparitorSymbol = 9, RULE_modifierName = 10, 
		RULE_modifierValue = 11, RULE_searchTerm = 12, RULE_index = 13;
	public static final String[] ruleNames = {
		"prefixClause", "statement", "booleanOp", "searchClause", "relation", 
		"modifierList", "modifier", "comparitor", "namedComparitor", "comparitorSymbol", 
		"modifierName", "modifierValue", "searchTerm", "index"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'/'", "'='", "'>'", "'<'", "'>='", "'<='", "'<>'", 
		"'=='", "'\"'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"AND", "OR", "NOT", "PROX", "CHARS", "WS"
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
			setState(28);
			match(T__0);
			setState(29);
			booleanOp();
			setState(31); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(30);
				statement();
				}
				}
				setState(33); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__10) | (1L << CHARS))) != 0) );
			setState(35);
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

	public static class StatementContext extends ParserRuleContext {
		public PrefixClauseContext prefixClause() {
			return getRuleContext(PrefixClauseContext.class,0);
		}
		public SearchClauseContext searchClause() {
			return getRuleContext(SearchClauseContext.class,0);
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
			setState(39);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(37);
				prefixClause();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(38);
				searchClause();
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
			setState(57);
			switch (_input.LA(1)) {
			case AND:
				enterOuterAlt(_localctx, 1);
				{
				setState(41);
				match(AND);
				setState(43);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(42);
					modifierList();
					}
				}

				}
				break;
			case OR:
				enterOuterAlt(_localctx, 2);
				{
				setState(45);
				match(OR);
				setState(47);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(46);
					modifierList();
					}
				}

				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 3);
				{
				setState(49);
				match(NOT);
				setState(51);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(50);
					modifierList();
					}
				}

				}
				break;
			case PROX:
				enterOuterAlt(_localctx, 4);
				{
				setState(53);
				match(PROX);
				setState(55);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(54);
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
			setState(74);
			switch (_input.LA(1)) {
			case T__10:
			case CHARS:
				enterOuterAlt(_localctx, 1);
				{
				setState(62);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(59);
					index();
					setState(60);
					relation();
					}
					break;
				}
				setState(64);
				searchTerm();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				match(T__0);
				setState(69);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(66);
					index();
					setState(67);
					relation();
					}
					break;
				}
				setState(71);
				searchTerm();
				setState(72);
				match(T__1);
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
		enterRule(_localctx, 8, RULE_relation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			comparitor();
			setState(78);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(77);
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
		enterRule(_localctx, 10, RULE_modifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(80);
				modifier();
				}
				}
				setState(83); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__2 );
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
		enterRule(_localctx, 12, RULE_modifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(T__2);
			setState(86);
			modifierName();
			setState(90);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9))) != 0)) {
				{
				setState(87);
				comparitorSymbol();
				setState(88);
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
		enterRule(_localctx, 14, RULE_comparitor);
		try {
			setState(94);
			switch (_input.LA(1)) {
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(92);
				comparitorSymbol();
				}
				break;
			case CHARS:
				enterOuterAlt(_localctx, 2);
				{
				setState(93);
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
		enterRule(_localctx, 16, RULE_namedComparitor);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(97); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(96);
					match(CHARS);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(99); 
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
		enterRule(_localctx, 18, RULE_comparitorSymbol);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9))) != 0)) ) {
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
		enterRule(_localctx, 20, RULE_modifierName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
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
		enterRule(_localctx, 22, RULE_modifierValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
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
		enterRule(_localctx, 24, RULE_searchTerm);
		int _la;
		try {
			setState(115);
			switch (_input.LA(1)) {
			case CHARS:
				enterOuterAlt(_localctx, 1);
				{
				setState(107);
				match(CHARS);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(108);
				match(T__10);
				setState(110); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(109);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__10) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					setState(112); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << AND) | (1L << OR) | (1L << NOT) | (1L << PROX) | (1L << CHARS) | (1L << WS))) != 0) );
				setState(114);
				match(T__10);
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
		enterRule(_localctx, 26, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\23z\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\6\2\"\n\2\r\2\16\2#\3\2"+
		"\3\2\3\3\3\3\5\3*\n\3\3\4\3\4\5\4.\n\4\3\4\3\4\5\4\62\n\4\3\4\3\4\5\4"+
		"\66\n\4\3\4\3\4\5\4:\n\4\5\4<\n\4\3\5\3\5\3\5\5\5A\n\5\3\5\3\5\3\5\3\5"+
		"\3\5\5\5H\n\5\3\5\3\5\3\5\5\5M\n\5\3\6\3\6\5\6Q\n\6\3\7\6\7T\n\7\r\7\16"+
		"\7U\3\b\3\b\3\b\3\b\3\b\5\b]\n\b\3\t\3\t\5\ta\n\t\3\n\6\nd\n\n\r\n\16"+
		"\ne\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\6\16q\n\16\r\16\16\16r\3"+
		"\16\5\16v\n\16\3\17\3\17\3\17\2\2\20\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\2\4\3\2\6\f\3\2\r\r~\2\36\3\2\2\2\4)\3\2\2\2\6;\3\2\2\2\bL\3\2\2\2"+
		"\nN\3\2\2\2\fS\3\2\2\2\16W\3\2\2\2\20`\3\2\2\2\22c\3\2\2\2\24g\3\2\2\2"+
		"\26i\3\2\2\2\30k\3\2\2\2\32u\3\2\2\2\34w\3\2\2\2\36\37\7\3\2\2\37!\5\6"+
		"\4\2 \"\5\4\3\2! \3\2\2\2\"#\3\2\2\2#!\3\2\2\2#$\3\2\2\2$%\3\2\2\2%&\7"+
		"\4\2\2&\3\3\2\2\2\'*\5\2\2\2(*\5\b\5\2)\'\3\2\2\2)(\3\2\2\2*\5\3\2\2\2"+
		"+-\7\16\2\2,.\5\f\7\2-,\3\2\2\2-.\3\2\2\2.<\3\2\2\2/\61\7\17\2\2\60\62"+
		"\5\f\7\2\61\60\3\2\2\2\61\62\3\2\2\2\62<\3\2\2\2\63\65\7\20\2\2\64\66"+
		"\5\f\7\2\65\64\3\2\2\2\65\66\3\2\2\2\66<\3\2\2\2\679\7\21\2\28:\5\f\7"+
		"\298\3\2\2\29:\3\2\2\2:<\3\2\2\2;+\3\2\2\2;/\3\2\2\2;\63\3\2\2\2;\67\3"+
		"\2\2\2<\7\3\2\2\2=>\5\34\17\2>?\5\n\6\2?A\3\2\2\2@=\3\2\2\2@A\3\2\2\2"+
		"AB\3\2\2\2BM\5\32\16\2CG\7\3\2\2DE\5\34\17\2EF\5\n\6\2FH\3\2\2\2GD\3\2"+
		"\2\2GH\3\2\2\2HI\3\2\2\2IJ\5\32\16\2JK\7\4\2\2KM\3\2\2\2L@\3\2\2\2LC\3"+
		"\2\2\2M\t\3\2\2\2NP\5\20\t\2OQ\5\f\7\2PO\3\2\2\2PQ\3\2\2\2Q\13\3\2\2\2"+
		"RT\5\16\b\2SR\3\2\2\2TU\3\2\2\2US\3\2\2\2UV\3\2\2\2V\r\3\2\2\2WX\7\5\2"+
		"\2X\\\5\26\f\2YZ\5\24\13\2Z[\5\30\r\2[]\3\2\2\2\\Y\3\2\2\2\\]\3\2\2\2"+
		"]\17\3\2\2\2^a\5\24\13\2_a\5\22\n\2`^\3\2\2\2`_\3\2\2\2a\21\3\2\2\2bd"+
		"\7\22\2\2cb\3\2\2\2de\3\2\2\2ec\3\2\2\2ef\3\2\2\2f\23\3\2\2\2gh\t\2\2"+
		"\2h\25\3\2\2\2ij\7\22\2\2j\27\3\2\2\2kl\7\22\2\2l\31\3\2\2\2mv\7\22\2"+
		"\2np\7\r\2\2oq\n\3\2\2po\3\2\2\2qr\3\2\2\2rp\3\2\2\2rs\3\2\2\2st\3\2\2"+
		"\2tv\7\r\2\2um\3\2\2\2un\3\2\2\2v\33\3\2\2\2wx\7\22\2\2x\35\3\2\2\2\23"+
		"#)-\61\659;@GLPU\\`eru";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}