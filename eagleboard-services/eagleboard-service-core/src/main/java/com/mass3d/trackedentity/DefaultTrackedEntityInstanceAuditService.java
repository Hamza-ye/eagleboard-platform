package com.mass3d.trackedentity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.audit.payloads.TrackedEntityInstanceAudit;
import com.mass3d.user.CurrentUserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.trackedentity.TrackedEntityInstanceAuditService" )
public class DefaultTrackedEntityInstanceAuditService
    implements TrackedEntityInstanceAuditService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private final TrackedEntityInstanceAuditStore trackedEntityInstanceAuditStore;
    private final TrackedEntityInstanceStore trackedEntityInstanceStore;
    private final TrackerAccessManager trackerAccessManager;
    private final CurrentUserService currentUserService;

    public DefaultTrackedEntityInstanceAuditService( TrackerAccessManager trackerAccessManager,
        TrackedEntityInstanceAuditStore trackedEntityInstanceAuditStore,
        TrackedEntityInstanceStore trackedEntityInstanceStore, CurrentUserService currentUserService )
    {
        checkNotNull( trackedEntityInstanceAuditStore );
        checkNotNull( trackedEntityInstanceStore );
        checkNotNull( trackerAccessManager );
        checkNotNull( currentUserService );

        this.trackedEntityInstanceAuditStore = trackedEntityInstanceAuditStore;
        this.trackedEntityInstanceStore = trackedEntityInstanceStore;
        this.trackerAccessManager = trackerAccessManager;
        this.currentUserService = currentUserService;
    }

    // -------------------------------------------------------------------------
    // TrackedEntityInstanceAuditService implementation
    // -------------------------------------------------------------------------

    @Override
    @Async
    @Transactional
    public void addTrackedEntityInstanceAudit( TrackedEntityInstanceAudit trackedEntityInstanceAudit )
    {
        trackedEntityInstanceAuditStore.addTrackedEntityInstanceAudit( trackedEntityInstanceAudit );
    }

    @Override
    @Transactional
    public void deleteTrackedEntityInstanceAudit( TrackedEntityInstance trackedEntityInstance )
    {
        trackedEntityInstanceAuditStore.deleteTrackedEntityInstanceAudit( trackedEntityInstance );
    }

    @Override
    @Transactional( readOnly = true )
    public List<TrackedEntityInstanceAudit> getTrackedEntityInstanceAudits(
        TrackedEntityInstanceAuditQueryParams params )
    {
        return trackedEntityInstanceAuditStore.getTrackedEntityInstanceAudits( params ).stream()
            .filter( a -> trackerAccessManager.canRead( currentUserService.getCurrentUser(),
                trackedEntityInstanceStore.getByUid( a.getTrackedEntityInstance() ) ).isEmpty() )
            .collect( Collectors.toList() );
    }

    @Override
    @Transactional(readOnly = true)
    public int getTrackedEntityInstanceAuditsCount( TrackedEntityInstanceAuditQueryParams params )
    {
        return trackedEntityInstanceAuditStore.getTrackedEntityInstanceAuditsCount( params );
    }
}