// Generated from /home/manosetro/IdeaProjects/cql-parser/parser/Cql.g4 by ANTLR 4.5.3

package org.iptc.extra.core.cql.parsers;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CqlParser#prefixClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixClause(CqlParser.PrefixClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(CqlParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#booleanOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanOp(CqlParser.BooleanOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#searchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchClause(CqlParser.SearchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#commentClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentClause(CqlParser.CommentClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(CqlParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#modifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifierList(CqlParser.ModifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier(CqlParser.ModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#comparitor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparitor(CqlParser.ComparitorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#namedComparitor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedComparitor(CqlParser.NamedComparitorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#comparitorSymbol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparitorSymbol(CqlParser.ComparitorSymbolContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#modifierName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifierName(CqlParser.ModifierNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#modifierValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifierValue(CqlParser.ModifierValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#searchTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchTerm(CqlParser.SearchTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link CqlParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(CqlParser.IndexContext ctx);
}