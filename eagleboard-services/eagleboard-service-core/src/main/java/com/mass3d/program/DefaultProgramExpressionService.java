package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.program.ProgramExpression.DUE_DATE;
import static com.mass3d.program.ProgramExpression.OBJECT_PROGRAM_STAGE;
import static com.mass3d.program.ProgramExpression.OBJECT_PROGRAM_STAGE_DATAELEMENT;
import static com.mass3d.program.ProgramExpression.REPORT_DATE;
import static com.mass3d.program.ProgramExpression.SEPARATOR_ID;
import static com.mass3d.program.ProgramExpression.SEPARATOR_OBJECT;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mass3d.common.GenericStore;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramExpressionService" )
public class DefaultProgramExpressionService
    implements ProgramExpressionService
{
    private static final String REGEXP = "\\[(" + OBJECT_PROGRAM_STAGE_DATAELEMENT + "|" + OBJECT_PROGRAM_STAGE + ")"
        + SEPARATOR_OBJECT + "([a-zA-Z0-9\\- ]+[" + SEPARATOR_ID + "([a-zA-Z0-9\\- ]|" + DUE_DATE + "|" + REPORT_DATE
        + ")+]*)\\]";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final GenericStore<ProgramExpression> programExpressionStore;

    private final ProgramStageService programStageService;

    private final DataElementService dataElementService;

    public DefaultProgramExpressionService(
        @Qualifier( "com.mass3d.program.ProgramExpressionStore" ) GenericStore<ProgramExpression> programExpressionStore,
        ProgramStageService programStageService, DataElementService dataElementService )
    {
        checkNotNull( programExpressionStore );
        checkNotNull( programStageService );
        checkNotNull( dataElementService );

        this.programExpressionStore = programExpressionStore;
        this.programStageService = programStageService;
        this.dataElementService = dataElementService;
    }

    // -------------------------------------------------------------------------
    // ProgramExpression CRUD operations
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addProgramExpression( ProgramExpression programExpression )
    {
        programExpressionStore.save( programExpression );
        return programExpression.getId();
    }

    @Override
    @Transactional
    public void updateProgramExpression( ProgramExpression programExpression )
    {
        programExpressionStore.update( programExpression );
    }

    @Override
    @Transactional
    public void deleteProgramExpression( ProgramExpression programExpression )
    {
        programExpressionStore.delete( programExpression );
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramExpression getProgramExpression( long id )
    {
        return programExpressionStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public String getExpressionDescription( String programExpression )
    {
        StringBuffer description = new StringBuffer();

        Pattern pattern = Pattern.compile( REGEXP );
        Matcher matcher = pattern.matcher( programExpression );
        int countFormula = 0;
        
        while ( matcher.find() )
        {
            countFormula++;
            
            String match = matcher.group();
            String key = matcher.group(1);
            match = match.replaceAll( "[\\[\\]]", "" );

            String[] info = match.split( SEPARATOR_OBJECT );
            String[] ids = info[1].split( SEPARATOR_ID );

            ProgramStage programStage = programStageService.getProgramStage( ids[0] );
            String name = ids[1];
            
            if ( programStage == null )
            {
                return INVALID_CONDITION;
            }
            else if ( !name.equals( DUE_DATE ) && !name.equals( REPORT_DATE )  )
            {
                DataElement dataElement = dataElementService.getDataElement( name );
                
                if ( dataElement == null )
                {
                    return INVALID_CONDITION;
                }
                else
                {
                    name = dataElement.getDisplayName();
                }
            }

            matcher.appendReplacement( description,
                "[" + key + ProgramExpression.SEPARATOR_OBJECT + programStage.getDisplayName() + SEPARATOR_ID + name + "]" );
        }

        StringBuffer tail = new StringBuffer();
        matcher.appendTail( tail );
        
        if ( countFormula > 1 || !tail.toString().isEmpty() || ( countFormula == 0 && !tail.toString().isEmpty() ) )
        {
            return INVALID_CONDITION;
        }        

        return description.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<DataElement> getDataElements( String programExpression )
    {
        Collection<DataElement> dataElements = new HashSet<>();

        Pattern pattern = Pattern.compile( REGEXP );
        Matcher matcher = pattern.matcher( programExpression );
        
        while ( matcher.find() )
        {
            String match = matcher.group();
            match = match.replaceAll( "[\\[\\]]", "" );

            String[] info = match.split( SEPARATOR_OBJECT );
            String[] ids = info[1].split( SEPARATOR_ID );
            
            DataElement dataElement = dataElementService.getDataElement( ids[1] );
            dataElements.add( dataElement );
        }

        return dataElements;
    }
}
