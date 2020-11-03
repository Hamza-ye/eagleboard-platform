package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramIndicatorDeletionHandler" )
public class ProgramIndicatorDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramIndicatorService programIndicatorService;

    public ProgramIndicatorDeletionHandler( ProgramIndicatorService programIndicatorService )
    {
        checkNotNull( programIndicatorService );
        this.programIndicatorService = programIndicatorService;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramIndicator.class.getSimpleName();
    }

    @Override
    public void deleteProgram( Program program )
    {
        Collection<ProgramIndicator> indicators = new HashSet<>( program.getProgramIndicators() );

        for (ProgramIndicator indicator : indicators) {
            programIndicatorService.deleteProgramIndicator(indicator);
        }
    }
}
