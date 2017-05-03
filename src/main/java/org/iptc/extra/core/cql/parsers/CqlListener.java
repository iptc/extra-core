// Generated from /home/manosetro/IdeaProjects/cql-parser/parser/Cql.g4 by ANTLR 4.5.3

package org.iptc.extra.core.cql.parsers;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CqlParser}.
 */
public interface CqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CqlParser#prefixClause}.
	 * @param ctx the parse tree
	 */
	void enterPrefixClause(CqlParser.PrefixClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#prefixClause}.
	 * @param ctx the parse tree
	 */
	void exitPrefixClause(CqlParser.PrefixClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(CqlParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(CqlParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#booleanOp}.
	 * @param ctx the parse tree
	 */
	void enterBooleanOp(CqlParser.BooleanOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#booleanOp}.
	 * @param ctx the parse tree
	 */
	void exitBooleanOp(CqlParser.BooleanOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#searchClause}.
	 * @param ctx the parse tree
	 */
	void enterSearchClause(CqlParser.SearchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#searchClause}.
	 * @param ctx the parse tree
	 */
	void exitSearchClause(CqlParser.SearchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#commentClause}.
	 * @param ctx the parse tree
	 */
	void enterCommentClause(CqlParser.CommentClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#commentClause}.
	 * @param ctx the parse tree
	 */
	void exitCommentClause(CqlParser.CommentClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(CqlParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(CqlParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#modifierList}.
	 * @param ctx the parse tree
	 */
	void enterModifierList(CqlParser.ModifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#modifierList}.
	 * @param ctx the parse tree
	 */
	void exitModifierList(CqlParser.ModifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(CqlParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(CqlParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#comparitor}.
	 * @param ctx the parse tree
	 */
	void enterComparitor(CqlParser.ComparitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#comparitor}.
	 * @param ctx the parse tree
	 */
	void exitComparitor(CqlParser.ComparitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#namedComparitor}.
	 * @param ctx the parse tree
	 */
	void enterNamedComparitor(CqlParser.NamedComparitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#namedComparitor}.
	 * @param ctx the parse tree
	 */
	void exitNamedComparitor(CqlParser.NamedComparitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#comparitorSymbol}.
	 * @param ctx the parse tree
	 */
	void enterComparitorSymbol(CqlParser.ComparitorSymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#comparitorSymbol}.
	 * @param ctx the parse tree
	 */
	void exitComparitorSymbol(CqlParser.ComparitorSymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#modifierName}.
	 * @param ctx the parse tree
	 */
	void enterModifierName(CqlParser.ModifierNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#modifierName}.
	 * @param ctx the parse tree
	 */
	void exitModifierName(CqlParser.ModifierNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#modifierValue}.
	 * @param ctx the parse tree
	 */
	void enterModifierValue(CqlParser.ModifierValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#modifierValue}.
	 * @param ctx the parse tree
	 */
	void exitModifierValue(CqlParser.ModifierValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#searchTerm}.
	 * @param ctx the parse tree
	 */
	void enterSearchTerm(CqlParser.SearchTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#searchTerm}.
	 * @param ctx the parse tree
	 */
	void exitSearchTerm(CqlParser.SearchTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link CqlParser#index}.
	 * @param ctx the parse tree
	 */
	void enterIndex(CqlParser.IndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link CqlParser#index}.
	 * @param ctx the parse tree
	 */
	void exitIndex(CqlParser.IndexContext ctx);
}