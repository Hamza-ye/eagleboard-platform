package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.List;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramStageDeletionHandler" )
public class ProgramStageDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramStageService programStageService;

    private final JdbcTemplate jdbcTemplate;

    public ProgramStageDeletionHandler( ProgramStageService programStageService, JdbcTemplate jdbcTemplate )
    {
        checkNotNull( programStageService );
        checkNotNull( jdbcTemplate );
        this.programStageService = programStageService;
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    protected String getClassName()
    {
        return ProgramStage.class.getSimpleName();
    }

    @Override
    public void deleteProgram( Program program )
    {
        Iterator<ProgramStage> iterator = program.getProgramStages().iterator();

        while ( iterator.hasNext() )
        {
            ProgramStage programStage = iterator.next();
            iterator.remove();
            programStageService.deleteProgramStage( programStage );
        }
    }

    @Override
    public void deleteDataEntryForm( DataEntryForm dataEntryForm )
    {
        List<ProgramStage> associatedProgramStages = programStageService.getProgramStagesByDataEntryForm( dataEntryForm );

        for ( ProgramStage programStage : associatedProgramStages )
        {
            programStage.setDataEntryForm( null );
            programStageService.updateProgramStage( programStage );
        }
    }

    @Override
    public String allowDeleteDataElement( DataElement dataElement )
    {
        String sql = "SELECT COUNT(*) FROM programstagedataelement WHERE dataelementid=" + dataElement.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
}
