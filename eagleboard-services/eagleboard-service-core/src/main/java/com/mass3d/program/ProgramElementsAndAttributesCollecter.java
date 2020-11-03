package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.parser.expression.ParserUtils.*;
import static com.mass3d.parser.expression.ParserUtils.assumeProgramExpressionProgramAttribute;
import static com.mass3d.parser.expression.ParserUtils.assumeStageElementSyntax;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.A_BRACE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.HASH_BRACE;

import java.util.Set;
import org.hisp.dhis.parser.expression.antlr.ExpressionBaseListener;

/**
 * Traverse the ANTLR4 parse tree for a program expression to collect the
 * UIDs for data elements and program attributes.
 * <p/>
 * Uses the ANTLR4 listener pattern.
 *
 */
public class ProgramElementsAndAttributesCollecter
    extends ExpressionBaseListener
{
    private Set<String> items;

    private AnalyticsType analyticsType;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ProgramElementsAndAttributesCollecter( Set<String> items, AnalyticsType analyticsType )
    {
        checkNotNull( items );
        checkNotNull( analyticsType );

        this.items = items;
        this.analyticsType = analyticsType;
    }

    // -------------------------------------------------------------------------
    // ANTLR Listener methods
    // -------------------------------------------------------------------------

    @Override
    public void enterExpr( ExprContext ctx )
    {
        if ( ctx.it == null )
        {
            return;
        }

        switch ( ctx.it.getType() )
        {
            case HASH_BRACE:
                assumeStageElementSyntax( ctx );

                String programStageId = ctx.uid0.getText();
                String dataElementId = ctx.uid1.getText();

                if ( AnalyticsType.ENROLLMENT.equals( analyticsType ) )
                {
                    items.add( programStageId + "_" + dataElementId );
                }
                else
                {
                    items.add( dataElementId );
                }
                break;

            case A_BRACE:
                assumeProgramExpressionProgramAttribute( ctx );

                String attributeId = ctx.uid0.getText();

                items.add( attributeId );

                break;

            default:
                break;
        }
    }
}
