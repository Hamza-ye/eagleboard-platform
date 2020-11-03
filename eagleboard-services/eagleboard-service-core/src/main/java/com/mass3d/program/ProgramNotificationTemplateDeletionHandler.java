package com.mass3d.program;

import com.mass3d.program.notification.ProgramNotificationTemplate;
import com.mass3d.system.deletion.DeletionHandler;

public class ProgramNotificationTemplateDeletionHandler
    extends DeletionHandler
{
    @Override
    protected String getClassName()
    {
        return ProgramNotificationTemplate.class.getSimpleName();
    }
}
