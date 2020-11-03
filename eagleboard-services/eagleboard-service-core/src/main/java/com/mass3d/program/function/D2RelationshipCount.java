package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.trimQuotes;
import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;
import com.mass3d.relationship.RelationshipType;

/**
 * Program indicator function: d2 relationship count
 *
 */
public class D2RelationshipCount
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        if ( ctx.QUOTED_UID() != null )
        {
            String relationshipId = trimQuotes( ctx.QUOTED_UID().getText() );

            RelationshipType relationshipType = visitor.getRelationshipTypeService().getRelationshipType( relationshipId );

            if ( relationshipType == null )
            {
                throw new ParserExceptionWithoutContext( "Relationship type " + relationshipId + " not found" );
            }

            visitor.getItemDescriptions().put( ctx.QUOTED_UID().getText(), relationshipType.getDisplayName() );
        }

        return DEFAULT_DOUBLE_VALUE;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String relationshipIdConstraint = "";

        if ( ctx.QUOTED_UID() != null )
        {
            String relationshipId = trimQuotes( ctx.QUOTED_UID().getText() );

            relationshipIdConstraint =
                " join relationshiptype rt on r.relationshiptypeid = rt.relationshiptypeid and rt.uid = '"
                    + relationshipId + "'";
        }

        return "(select count(*) from relationship r" + relationshipIdConstraint +
            " join relationshipitem rifrom on rifrom.relationshipid = r.relationshipid" +
            " join trackedentityinstance tei on rifrom.trackedentityinstanceid = tei.trackedentityinstanceid and tei.uid = ax.tei)";
    }
}
