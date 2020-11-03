package com.mass3d.trackedentitycomment;

import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class TrackedEntityCommentDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    TrackedEntityCommentService commentService;

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramInstance.class.getSimpleName();
    }

    @Override
    public void deleteProgramInstance( ProgramInstance programInstance )
    {
        for( TrackedEntityComment comment : programInstance.getComments())
        {
            commentService.deleteTrackedEntityComment( comment );
        }
    }

    @Override
    public void deleteProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        for( TrackedEntityComment comment : programStageInstance.getComments())
        {
            commentService.deleteTrackedEntityComment( comment );
        }
    }
}
