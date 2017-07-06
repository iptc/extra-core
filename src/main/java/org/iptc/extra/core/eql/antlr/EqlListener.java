// Generated from /home/manosetro/IdeaProjects/cql-parser/parser/Eql.g4 by ANTLR 4.5.3

package org.iptc.extra.core.eql.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EqlParser}.
 */
public interface EqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link EqlParser#prefixClause}.
	 * @param ctx the parse tree
	 */
	void enterPrefixClause(EqlParser.PrefixClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#prefixClause}.
	 * @param ctx the parse tree
	 */
	void exitPrefixClause(EqlParser.PrefixClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(EqlParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(EqlParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#booleanOp}.
	 * @param ctx the parse tree
	 */
	void enterBooleanOp(EqlParser.BooleanOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#booleanOp}.
	 * @param ctx the parse tree
	 */
	void exitBooleanOp(EqlParser.BooleanOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#searchClause}.
	 * @param ctx the parse tree
	 */
	void enterSearchClause(EqlParser.SearchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#searchClause}.
	 * @param ctx the parse tree
	 */
	void exitSearchClause(EqlParser.SearchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#commentClause}.
	 * @param ctx the parse tree
	 */
	void enterCommentClause(EqlParser.CommentClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#commentClause}.
	 * @param ctx the parse tree
	 */
	void exitCommentClause(EqlParser.CommentClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#referenceClause}.
	 * @param ctx the parse tree
	 */
	void enterReferenceClause(EqlParser.ReferenceClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#referenceClause}.
	 * @param ctx the parse tree
	 */
	void exitReferenceClause(EqlParser.ReferenceClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#referencedRule}.
	 * @param ctx the parse tree
	 */
	void enterReferencedRule(EqlParser.ReferencedRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#referencedRule}.
	 * @param ctx the parse tree
	 */
	void exitReferencedRule(EqlParser.ReferencedRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#ref}.
	 * @param ctx the parse tree
	 */
	void enterRef(EqlParser.RefContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#ref}.
	 * @param ctx the parse tree
	 */
	void exitRef(EqlParser.RefContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(EqlParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(EqlParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#modifierList}.
	 * @param ctx the parse tree
	 */
	void enterModifierList(EqlParser.ModifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#modifierList}.
	 * @param ctx the parse tree
	 */
	void exitModifierList(EqlParser.ModifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(EqlParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(EqlParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#comparitor}.
	 * @param ctx the parse tree
	 */
	void enterComparitor(EqlParser.ComparitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#comparitor}.
	 * @param ctx the parse tree
	 */
	void exitComparitor(EqlParser.ComparitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#namedComparitor}.
	 * @param ctx the parse tree
	 */
	void enterNamedComparitor(EqlParser.NamedComparitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#namedComparitor}.
	 * @param ctx the parse tree
	 */
	void exitNamedComparitor(EqlParser.NamedComparitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#comparitorSymbol}.
	 * @param ctx the parse tree
	 */
	void enterComparitorSymbol(EqlParser.ComparitorSymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#comparitorSymbol}.
	 * @param ctx the parse tree
	 */
	void exitComparitorSymbol(EqlParser.ComparitorSymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#modifierName}.
	 * @param ctx the parse tree
	 */
	void enterModifierName(EqlParser.ModifierNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#modifierName}.
	 * @param ctx the parse tree
	 */
	void exitModifierName(EqlParser.ModifierNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#modifierValue}.
	 * @param ctx the parse tree
	 */
	void enterModifierValue(EqlParser.ModifierValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#modifierValue}.
	 * @param ctx the parse tree
	 */
	void exitModifierValue(EqlParser.ModifierValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#searchTerm}.
	 * @param ctx the parse tree
	 */
	void enterSearchTerm(EqlParser.SearchTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#searchTerm}.
	 * @param ctx the parse tree
	 */
	void exitSearchTerm(EqlParser.SearchTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link EqlParser#index}.
	 * @param ctx the parse tree
	 */
	void enterIndex(EqlParser.IndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link EqlParser#index}.
	 * @param ctx the parse tree
	 */
	void exitIndex(EqlParser.IndexContext ctx);
}