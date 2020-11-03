package com.mass3d.trackedentityattributevalue;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.common.AuditType;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityAttributeService;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.user.CurrentUserService;
import org.springframework.stereotype.Service;

@Service( "com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueAuditService" )
public class DefaultTrackedEntityAttributeValueAuditService
    implements TrackedEntityAttributeValueAuditService
{
    private final TrackedEntityAttributeValueAuditStore trackedEntityAttributeValueAuditStore;
    private final TrackedEntityAttributeService trackedEntityAttributeService;
    private final CurrentUserService currentUserService;

    public DefaultTrackedEntityAttributeValueAuditService(
        TrackedEntityAttributeValueAuditStore trackedEntityAttributeValueAuditStore,
        TrackedEntityAttributeService trackedEntityAttributeService,
        CurrentUserService currentUserService )
    {
        checkNotNull( trackedEntityAttributeValueAuditStore );
        checkNotNull( trackedEntityAttributeService );
        checkNotNull( currentUserService );

        this.trackedEntityAttributeValueAuditStore = trackedEntityAttributeValueAuditStore;
        this.trackedEntityAttributeService = trackedEntityAttributeService;
        this.currentUserService = currentUserService;
    }

    @Override
    public void addTrackedEntityAttributeValueAudit( TrackedEntityAttributeValueAudit trackedEntityAttributeValueAudit )
    {
        trackedEntityAttributeValueAuditStore.addTrackedEntityAttributeValueAudit( trackedEntityAttributeValueAudit );
    }

    @Override
    public List<TrackedEntityAttributeValueAudit> getTrackedEntityAttributeValueAudits(
        List<TrackedEntityAttribute> trackedEntityAttributes, List<TrackedEntityInstance> trackedEntityInstances,
        AuditType auditType )
    {
        return aclFilter( trackedEntityAttributeValueAuditStore
            .getTrackedEntityAttributeValueAudits( trackedEntityAttributes, trackedEntityInstances, auditType ) );
    }

    @Override
    public List<TrackedEntityAttributeValueAudit> getTrackedEntityAttributeValueAudits( List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType, int first, int max )
    {
        return aclFilter( trackedEntityAttributeValueAuditStore.getTrackedEntityAttributeValueAudits(
            trackedEntityAttributes, trackedEntityInstances, auditType, first, max ) );
    }

    private List<TrackedEntityAttributeValueAudit> aclFilter(
        List<TrackedEntityAttributeValueAudit> trackedEntityAttributeValueAudits )
    {
        // Fetch all the Tracked Entity Instance Attributes this user has access to
        // (only store UIDs) - not a very efficient solution, but at the moment we do not
        // have ACL api to check TEI Attributes
        Set<String> allUserReadableTrackedEntityAttributes = trackedEntityAttributeService
            .getAllUserReadableTrackedEntityAttributes( currentUserService.getCurrentUser() ).stream()
            .map( BaseIdentifiableObject::getUid ).collect( Collectors.toSet() );

        return trackedEntityAttributeValueAudits.stream()
            .filter( audit -> allUserReadableTrackedEntityAttributes.contains( audit.getAttribute().getUid() ) )
            .collect( Collectors.toList() );
    }

    @Override
    public int countTrackedEntityAttributeValueAudits( List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType )
    {
        return trackedEntityAttributeValueAuditStore.countTrackedEntityAttributeValueAudits( trackedEntityAttributes, trackedEntityInstances, auditType );
    }

    @Override
    public void deleteTrackedEntityAttributeValueAudits( TrackedEntityInstance trackedEntityInstance )
    {
        trackedEntityAttributeValueAuditStore.deleteTrackedEntityAttributeValueAudits( trackedEntityInstance );
    }
}
