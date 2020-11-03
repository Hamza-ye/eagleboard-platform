package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramStageSectionDeletionHandler" )
public class ProgramStageSectionDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    private final ProgramStageSectionService programStageSectionService;

    public ProgramStageSectionDeletionHandler( IdentifiableObjectManager idObjectManager,
        ProgramStageSectionService programStageSectionService )
    {
        checkNotNull( idObjectManager );
        checkNotNull( programStageSectionService );

        this.idObjectManager = idObjectManager;
        this.programStageSectionService = programStageSectionService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramStageSection.class.getSimpleName();
    }

    @Override
    public void deleteProgramIndicator( ProgramIndicator programIndicator )
    {
        Collection<ProgramStageSection> sections = idObjectManager.getAllNoAcl( ProgramStageSection.class );

        for ( ProgramStageSection section : sections )
        {
            if ( section.getProgramIndicators().remove( programIndicator ) )
            {
                idObjectManager.update( section );
            }
        }
    }

    @Override
    public void deleteProgramStage( ProgramStage programStage )
    {
        List<ProgramStageSection> programStageSections = new ArrayList<>( programStage.getProgramStageSections() );

        for ( ProgramStageSection programStageSection : programStageSections )
        {
            programStage.getProgramStageSections().remove( programStageSection );
            programStageSectionService.deleteProgramStageSection( programStageSection );
        }
    }
}
