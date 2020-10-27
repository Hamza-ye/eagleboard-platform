package com.mass3d.parser.expression;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.AMPERSAND_2;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.AND;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.C_BRACE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.DIV;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.EQ;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.EXCLAMATION_POINT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.FIRST_NON_NULL;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.GEQ;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.GREATEST;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.GT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.IF;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.IS_NOT_NULL;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.IS_NULL;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.LEAST;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.LEQ;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.LOG;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.LOG10;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.LT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.MINUS;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.MOD;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.MUL;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.NE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.NOT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.OR;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.PAREN;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.PERIOD_OFFSET;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.PLUS;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.POWER;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.VERTICAL_BAR_2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.parser.expression.dataitem.ItemConstant;
import com.mass3d.parser.expression.function.*;
import com.mass3d.parser.expression.operator.*;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;

/**
 * Utilities for ANTLR parsing
 *
 */
public class ParserUtils
{
    public final static double DOUBLE_VALUE_IF_NULL = 0.0;

    public final static ImmutableMap<Integer, ExpressionItem> COMMON_EXPRESSION_ITEMS = ImmutableMap
        .<Integer, ExpressionItem> builder()

        // Non-comparison operators

        .put( PAREN, new OperatorGroupingParentheses() )
        .put( PLUS, new OperatorMathPlus() )
        .put( MINUS, new OperatorMathMinus() )
        .put( POWER, new OperatorMathPower() )
        .put( MUL, new OperatorMathMultiply() )
        .put( DIV, new OperatorMathDivide() )
        .put( MOD, new OperatorMathModulus() )
        .put( NOT, new OperatorLogicalNot() )
        .put( EXCLAMATION_POINT, new OperatorLogicalNot() )
        .put( AND, new OperatorLogicalAnd() )
        .put( AMPERSAND_2, new OperatorLogicalAnd() )
        .put( OR, new OperatorLogicalOr() )
        .put( VERTICAL_BAR_2, new OperatorLogicalOr() )

        // Comparison operators

        .put( EQ, new OperatorCompareEqual() )
        .put( NE, new OperatorCompareNotEqual() )
        .put( GT, new OperatorCompareGreaterThan() )
        .put( LT, new OperatorCompareLessThan() )
        .put( GEQ, new OperatorCompareGreaterThanOrEqual() )
        .put( LEQ, new OperatorCompareLessThanOrEqual() )

        // Functions

        .put( FIRST_NON_NULL, new FunctionFirstNonNull() )
        .put( GREATEST, new FunctionGreatest() )
        .put( IF, new FunctionIf() )
        .put( IS_NOT_NULL, new FunctionIsNotNull() )
        .put( IS_NULL, new FunctionIsNull() )
        .put( LEAST, new FunctionLeast() )
        .put( LOG, new FunctionLog() )
        .put( LOG10, new FunctionLog10() )
        .put( PERIOD_OFFSET, new PeriodOffset() )

        // Data items

        .put( C_BRACE, new ItemConstant() )

        .build();

    public final static ExpressionItemMethod ITEM_GET_DESCRIPTIONS = ExpressionItem::getDescription;

    public final static ExpressionItemMethod ITEM_GET_IDS = ExpressionItem::getItemId;

    public final static ExpressionItemMethod ITEM_GET_ORG_UNIT_GROUPS = ExpressionItem::getOrgUnitGroup;

    public final static ExpressionItemMethod ITEM_EVALUATE = ExpressionItem::evaluate;

    public final static ExpressionItemMethod ITEM_GET_SQL = ExpressionItem::getSql;

    public final static ExpressionItemMethod ITEM_REGENERATE = ExpressionItem::regenerate;

    /**
     * Used for syntax checking when we don't have a list of actual periods for
     * collecting samples.
     */
    public final static List<Period> DEFAULT_SAMPLE_PERIODS = ImmutableList.of(
        PeriodType.getPeriodFromIsoString( "20010101" ) );

    /**
     * Assume that an item of the form #{...} has a syntax that could be used in a
     * program indicator expression for #{programStageUid.dataElementUid}
     *
     * @param ctx the item context
     */
    public static void assumeStageElementSyntax( ExprContext ctx )
    {
        if ( ctx.uid0 == null || ctx.uid1 == null || ctx.uid2 != null || ctx.wild2 != null )
        {
            throw new ParserExceptionWithoutContext( "Invalid Program Stage / DataElement syntax: " + ctx.getText() );
        }
    }

    /**
     * Assume that an item of the form A{...} has a syntax that could be used in an
     * expression for A{progamUid.attributeUid}
     *
     * @param ctx the item context
     */
    public static void assumeExpressionProgramAttribute( ExprContext ctx )
    {
        if ( ctx.uid0 == null || ctx.uid1 == null )
        {
            throw new ParserExceptionWithoutContext( "Program attribute must have two UIDs: " + ctx.getText() );
        }
    }

    /**
     * Assume that an item of the form A{...} has a syntax that could be used be
     * used in an program expression for A{attributeUid}
     *
     * @param ctx the item context
     */
    public static void assumeProgramExpressionProgramAttribute( ExprContext ctx )
    {
        if ( ctx.uid0 == null || ctx.uid1 != null )
        {
            throw new ParserExceptionWithoutContext( "Program attribute must have one UID: " + ctx.getText() );
        }
    }
}
