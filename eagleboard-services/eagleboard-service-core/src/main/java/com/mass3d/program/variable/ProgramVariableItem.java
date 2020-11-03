package com.mass3d.program.variable;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_ANALYTICS_PERIOD_END;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_ANALYTICS_PERIOD_START;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_COMPLETED_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_CREATION_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_CURRENT_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_DUE_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_ENROLLMENT_COUNT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_ENROLLMENT_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_ENROLLMENT_STATUS;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_EVENT_COUNT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_EVENT_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_EXECUTION_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_INCIDENT_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_ORG_UNIT_COUNT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_PROGRAM_STAGE_ID;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_PROGRAM_STAGE_NAME;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_SYNC_DATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_TEI_COUNT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_VALUE_COUNT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.V_ZERO_POS_VALUE_COUNT;

import com.google.common.collect.ImmutableMap;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;

/**
 * Program indicator variable expression item
 *
 */
public class ProgramVariableItem
    extends ProgramExpressionItem
{
    private final static ImmutableMap<Integer, ProgramVariable> PROGRAM_VARIABLES = ImmutableMap.<Integer, ProgramVariable>builder()
        .put( V_ANALYTICS_PERIOD_END, new vAnalyticsPeriodEnd() )
        .put( V_ANALYTICS_PERIOD_START, new vAnalyticsPeriodStart() )
        .put( V_CREATION_DATE, new vCreationDate() )
        .put( V_CURRENT_DATE, new vCurrentDate() )
        .put( V_COMPLETED_DATE, new vCompletedDate() )
        .put( V_DUE_DATE, new vDueDate() )
        .put( V_ENROLLMENT_COUNT, new vEnrollmentCount() )
        .put( V_ENROLLMENT_DATE, new vEnrollmentDate() )
        .put( V_ENROLLMENT_STATUS, new vEnrollmentStatus() )
        .put( V_EVENT_COUNT, new vEventCount() )
        .put( V_EXECUTION_DATE, new vEventDate() ) // Same as event date
        .put( V_EVENT_DATE, new vEventDate() )
        .put( V_INCIDENT_DATE, new vIncidentDate() )
        .put( V_ORG_UNIT_COUNT, new vOrgUnitCount() )
        .put( V_PROGRAM_STAGE_ID, new vProgramStageId() )
        .put( V_PROGRAM_STAGE_NAME, new vProgramStageName() )
        .put( V_SYNC_DATE, new vSyncDate() )
        .put( V_TEI_COUNT, new vTeiCount() )
        .put( V_VALUE_COUNT, new vValueCount() )
        .put( V_ZERO_POS_VALUE_COUNT, new vZeroPosValueCount() )
        .build();

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String variableName = visitor.getI18n().getString( ctx.programVariable().getText() );

        visitor.getItemDescriptions().put( ctx.getText(), variableName );

        ProgramVariable programVariable = getProgramVariable ( ctx );

        return programVariable.defaultVariableValue();
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        ProgramVariable programVariable = getProgramVariable ( ctx );

        return programVariable.getSql( visitor );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private ProgramVariable getProgramVariable( ExprContext ctx )
    {
        ProgramVariable programVariable = PROGRAM_VARIABLES.get( ctx.programVariable().var.getType() );

        if ( programVariable == null )
        {
            throw new ParserExceptionWithoutContext( "Can't find program variable " + ctx.programVariable().var.getText() );
        }

        return programVariable;
    }
}
