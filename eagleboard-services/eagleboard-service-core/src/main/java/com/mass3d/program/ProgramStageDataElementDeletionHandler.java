package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementDomain;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramStageDataElementDeletionHandler" )
public class ProgramStageDataElementDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramStageDataElementService programStageDataElementService;

    public ProgramStageDataElementDeletionHandler( ProgramStageDataElementService programStageDataElementService )
    {
        checkNotNull( programStageDataElementService );

        this.programStageDataElementService = programStageDataElementService;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramStageDataElement.class.getSimpleName();
    }

    @Override
    public void deleteProgramStage( ProgramStage programStage )
    {
        List<ProgramStageDataElement> programStageDataElements = new ArrayList<>( programStage.getProgramStageDataElements() );

        for ( ProgramStageDataElement programStageDataElement : programStageDataElements )
        {
            programStage.getProgramStageDataElements().remove( programStageDataElement );
            programStageDataElementService.deleteProgramStageDataElement( programStageDataElement );
        }
    }

    @Override
    public void deleteDataElement( DataElement dataElement )
    {
        if ( DataElementDomain.TRACKER == dataElement.getDomainType() )
        {
            for (ProgramStageDataElement element : programStageDataElementService.getAllProgramStageDataElements()) {
                if (element.getDataElement() != null && element.getDataElement().equals(dataElement)) {
                    programStageDataElementService.deleteProgramStageDataElement(element);
                }
            }
        }
    }
}
