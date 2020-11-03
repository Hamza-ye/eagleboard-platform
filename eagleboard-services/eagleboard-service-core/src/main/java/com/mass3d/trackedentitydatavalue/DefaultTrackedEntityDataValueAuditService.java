package com.mass3d.trackedentitydatavalue;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.mass3d.common.AuditType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.trackedentity.TrackerAccessManager;
import com.mass3d.user.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.trackedentitydatavalue.TrackedEntityDataValueAuditService" )
public class DefaultTrackedEntityDataValueAuditService
    implements TrackedEntityDataValueAuditService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final TrackedEntityDataValueAuditStore trackedEntityDataValueAuditStore;
    private Predicate<TrackedEntityDataValueAudit> aclFilter;

    public DefaultTrackedEntityDataValueAuditService( TrackedEntityDataValueAuditStore trackedEntityDataValueAuditStore,
        TrackerAccessManager trackerAccessManager, CurrentUserService currentUserService )
    {
        checkNotNull( trackedEntityDataValueAuditStore );
        checkNotNull( trackerAccessManager );
        checkNotNull( currentUserService );

        this.trackedEntityDataValueAuditStore = trackedEntityDataValueAuditStore;

        aclFilter = ( audit ) -> trackerAccessManager.canRead( currentUserService.getCurrentUser(),
            audit.getProgramStageInstance(), audit.getDataElement(), false ).isEmpty();
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void addTrackedEntityDataValueAudit( TrackedEntityDataValueAudit trackedEntityDataValueAudit )
    {
        trackedEntityDataValueAuditStore.addTrackedEntityDataValueAudit( trackedEntityDataValueAudit );
    }

    @Override
    @Transactional( readOnly = true )
    public List<TrackedEntityDataValueAudit> getTrackedEntityDataValueAudits( List<DataElement> dataElements,
        List<ProgramStageInstance> programStageInstances, AuditType auditType )
    {
        return trackedEntityDataValueAuditStore
            .getTrackedEntityDataValueAudits( dataElements, programStageInstances, auditType ).stream()
            .filter(aclFilter).collect( Collectors.toList() );
    }

    @Override
    @Transactional( readOnly = true )
    public List<TrackedEntityDataValueAudit> getTrackedEntityDataValueAudits( List<DataElement> dataElements,
        List<ProgramStageInstance> programStageInstances, AuditType auditType, int first, int max )
    {
        return trackedEntityDataValueAuditStore
            .getTrackedEntityDataValueAudits( dataElements, programStageInstances, auditType, first, max ).stream()
            .filter(aclFilter).collect( Collectors.toList() );
    }

    @Override
    @Transactional( readOnly = true )
    public int countTrackedEntityDataValueAudits( List<DataElement> dataElements,
        List<ProgramStageInstance> programStageInstances, AuditType auditType )
    {
        return trackedEntityDataValueAuditStore.countTrackedEntityDataValueAudits( dataElements, programStageInstances,
            auditType );
    }
}
