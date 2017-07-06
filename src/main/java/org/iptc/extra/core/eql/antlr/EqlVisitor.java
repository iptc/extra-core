// Generated from /home/manosetro/IdeaProjects/cql-parser/parser/Eql.g4 by ANTLR 4.5.3

package org.iptc.extra.core.eql.antlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link EqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface EqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link EqlParser#prefixClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixClause(EqlParser.PrefixClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(EqlParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#booleanOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanOp(EqlParser.BooleanOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#searchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchClause(EqlParser.SearchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#commentClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentClause(EqlParser.CommentClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#referenceClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceClause(EqlParser.ReferenceClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#referencedRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferencedRule(EqlParser.ReferencedRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef(EqlParser.RefContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(EqlParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#modifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifierList(EqlParser.ModifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier(EqlParser.ModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#comparitor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparitor(EqlParser.ComparitorContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#namedComparitor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedComparitor(EqlParser.NamedComparitorContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#comparitorSymbol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparitorSymbol(EqlParser.ComparitorSymbolContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#modifierName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifierName(EqlParser.ModifierNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#modifierValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifierValue(EqlParser.ModifierValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#searchTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchTerm(EqlParser.SearchTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link EqlParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(EqlParser.IndexContext ctx);
}