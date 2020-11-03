package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.dataentryform.DataEntryFormService;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramDataEntryFormDeletionHandler" )
public class ProgramDataEntryFormDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final DataEntryFormService dataEntryFormService;

    private final ProgramStageService programStageService;

    public ProgramDataEntryFormDeletionHandler( DataEntryFormService dataEntryFormService,
        ProgramStageService programStageService )
    {
        checkNotNull( dataEntryFormService );
        checkNotNull( programStageService );
        this.dataEntryFormService = dataEntryFormService;
        this.programStageService = programStageService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return DataEntryForm.class.getSimpleName();
    }

    @Override
    public void deleteProgramStage( ProgramStage programStage )
    {
        DataEntryForm dataEntryForm = programStage.getDataEntryForm();

        if ( dataEntryForm != null )
        {
            boolean flag = false;
            
            Set<ProgramStage> programStages = programStage.getProgram().getProgramStages();
            
            programStages.remove( programStage );
            
            for ( ProgramStage stage : programStages )
            {
                if ( stage.getDataEntryForm() != null )
                {
                    programStage.setDataEntryForm( null );
                    programStageService.updateProgramStage( programStage );
                    flag = true;
                    break;
                }
            }

            if ( !flag )
            {
                dataEntryFormService.deleteDataEntryForm( dataEntryForm );
            }
        }
    }
}
