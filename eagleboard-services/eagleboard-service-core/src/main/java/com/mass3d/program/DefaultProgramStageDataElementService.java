package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.dataelement.DataElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramStageDataElementService" )
public class DefaultProgramStageDataElementService
    implements ProgramStageDataElementService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramStageDataElementStore programStageDataElementStore;

    public DefaultProgramStageDataElementService( ProgramStageDataElementStore programStageDataElementStore )
    {
        checkNotNull( programStageDataElementStore );

        this.programStageDataElementStore = programStageDataElementStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void addProgramStageDataElement( ProgramStageDataElement programStageDataElement )
    {
        programStageDataElementStore.save( programStageDataElement );
    }

    @Override
    @Transactional
    public void deleteProgramStageDataElement( ProgramStageDataElement programStageDataElement )
    {
        programStageDataElementStore.delete( programStageDataElement );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramStageDataElement> getAllProgramStageDataElements()
    {
        return programStageDataElementStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramStageDataElement get( ProgramStage programStage, DataElement dataElement )
    {
        return programStageDataElementStore.get( programStage, dataElement );
    }

    @Override
    @Transactional
    public void updateProgramStageDataElement( ProgramStageDataElement programStageDataElement )
    {
        programStageDataElementStore.update( programStageDataElement );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Set<String>> getProgramStageDataElementsWithSkipSynchronizationSetToTrue()
    {
        return programStageDataElementStore.getProgramStageDataElementsWithSkipSynchronizationSetToTrue();
    }
}
