package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.dataentryform.DataEntryForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramStageService" )
public class DefaultProgramStageService
    implements ProgramStageService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramStageStore programStageStore;

    public DefaultProgramStageService( ProgramStageStore programStageStore )
    {
        checkNotNull( programStageStore );
        this.programStageStore = programStageStore;
    }

    // -------------------------------------------------------------------------
    // ProgramStage implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long saveProgramStage( ProgramStage programStage )
    {
        programStageStore.save( programStage );
        return programStage.getId();
    }

    @Override
    @Transactional
    public void deleteProgramStage( ProgramStage programStage )
    {
        programStageStore.delete( programStage );
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramStage getProgramStage( long id )
    {
        return programStageStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramStage getProgramStage( String uid )
    {
        return programStageStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramStage> getProgramStagesByDataEntryForm( DataEntryForm dataEntryForm )
    {
        return programStageStore.getByDataEntryForm( dataEntryForm );
    }

    @Override
    @Transactional
    public void updateProgramStage( ProgramStage programStage )
    {
        programStageStore.update( programStage );
    }

}
