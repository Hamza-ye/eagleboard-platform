package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Iterator;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.trackedentity.TrackedEntityInstance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramInstanceDeletionHandler" )
public class ProgramInstanceDeletionHandler
    extends
    DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private final JdbcTemplate jdbcTemplate;

    private final ProgramInstanceService programInstanceService;

    public ProgramInstanceDeletionHandler( JdbcTemplate jdbcTemplate, ProgramInstanceService programInstanceService )
    {
        checkNotNull( programInstanceService );
        checkNotNull( jdbcTemplate );
        this.programInstanceService = programInstanceService;
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramInstance.class.getSimpleName();
    }

    @Override
    public void deleteTrackedEntityInstance( TrackedEntityInstance trackedEntityInstance )
    {
        for ( ProgramInstance programInstance : trackedEntityInstance.getProgramInstances() )
        {
            programInstanceService.deleteProgramInstance( programInstance );
        }
    }

    @Override
    public String allowDeleteProgram( Program program )
    {
        if ( program.isWithoutRegistration() )
        {
            return null;
        }

        String sql = "SELECT COUNT(*) FROM programinstance where programid = " + program.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }

    @Override
    public void deleteProgram( Program program )
    {
        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( program );

        if ( programInstances != null )
        {
            Iterator<ProgramInstance> iterator = programInstances.iterator();
            while ( iterator.hasNext() )
            {
                ProgramInstance programInstance = iterator.next();
                iterator.remove();
                programInstanceService.hardDeleteProgramInstance( programInstance );
            }
        }
    }
}
