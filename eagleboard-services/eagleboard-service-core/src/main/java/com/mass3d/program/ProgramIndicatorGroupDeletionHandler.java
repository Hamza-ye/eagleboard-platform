package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramIndicatorGroupDeletionHandler" )
public class ProgramIndicatorGroupDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramIndicatorService programIndicatorService;

    public ProgramIndicatorGroupDeletionHandler( ProgramIndicatorService programIndicatorService )
    {
        checkNotNull( programIndicatorService );
        this.programIndicatorService = programIndicatorService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramIndicatorGroup.class.getName();
    }

    @Override
    public void deleteProgramIndicator( ProgramIndicator programIndicator)
    {
        for ( ProgramIndicatorGroup group : programIndicator.getGroups() )
        {
            group.getMembers().remove( programIndicator );
            programIndicatorService.updateProgramIndicatorGroup( group );
        }
    }
}
