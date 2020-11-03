package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramStageSectionService" )
public class DefaultProgramStageSectionService
    implements ProgramStageSectionService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramStageSectionStore programStageSectionStore;

    public DefaultProgramStageSectionService( ProgramStageSectionStore programStageSectionStore )
    {
        checkNotNull( programStageSectionStore );

        this.programStageSectionStore = programStageSectionStore;
    }

    // -------------------------------------------------------------------------
    // ProgramStageSection implementation
    // -------------------------------------------------------------------------

    @Override
    public long saveProgramStageSection( ProgramStageSection programStageSection )
    {
        programStageSectionStore.save( programStageSection );
        return programStageSection.getId();
    }

    @Override
    public void deleteProgramStageSection( ProgramStageSection programStageSection )
    {
        programStageSectionStore.delete( programStageSection );
    }

    @Override
    public void updateProgramStageSection( ProgramStageSection programStageSection )
    {
        programStageSectionStore.update( programStageSection );
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramStageSection getProgramStageSection( long id )
    {
        return programStageSectionStore.get( id );
    }
}
