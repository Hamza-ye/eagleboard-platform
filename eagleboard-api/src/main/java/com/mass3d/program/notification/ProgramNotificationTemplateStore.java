package com.mass3d.program.notification;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.notification.ProgramNotificationTemplateStore" )
public interface ProgramNotificationTemplateStore
    extends IdentifiableObjectStore<ProgramNotificationTemplate>
{
    String ID = ProgramNotificationTemplate.class.getName();

    List<ProgramNotificationTemplate> getProgramNotificationByTriggerType(
        NotificationTrigger triggers);
}
