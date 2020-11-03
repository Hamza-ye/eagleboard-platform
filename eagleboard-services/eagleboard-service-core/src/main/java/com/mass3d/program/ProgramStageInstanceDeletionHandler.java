package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.dataelement.DataElement;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramStageInstanceDeletionHandler" )
public class ProgramStageInstanceDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final JdbcTemplate jdbcTemplate;

    private final ProgramStageInstanceService programStageInstanceService;

    public ProgramStageInstanceDeletionHandler( JdbcTemplate jdbcTemplate,
        ProgramStageInstanceService programStageInstanceService )
    {
        checkNotNull( jdbcTemplate );
        checkNotNull( programStageInstanceService );
        this.jdbcTemplate = jdbcTemplate;
        this.programStageInstanceService = programStageInstanceService;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramStageInstance.class.getSimpleName();
    }

    @Override
    public String allowDeleteProgramStage( ProgramStage programStage )
    {
        String sql = "SELECT COUNT(*) FROM programstageinstance WHERE programstageid = " + programStage.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }

    @Override
    public void deleteProgramInstance( ProgramInstance programInstance )
    {
        for ( ProgramStageInstance programStageInstance : programInstance.getProgramStageInstances() )
        {
            programStageInstanceService.deleteProgramStageInstance( programStageInstance );
        }
    }

    @Override
    public String allowDeleteProgram( Program program )
    {
        String sql = "SELECT COUNT(*) FROM programstageinstance psi join programinstance pi on pi.programinstanceid=psi.programinstanceid where pi.programid = " + program.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }

    @Override
    public String allowDeleteDataElement( DataElement dataElement )
    {
        String sql = "select count(*) from programstageinstance where eventdatavalues ? '" + dataElement.getUid() + "'";

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
}
