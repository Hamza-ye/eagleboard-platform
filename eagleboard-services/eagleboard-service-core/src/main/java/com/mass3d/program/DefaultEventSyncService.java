package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service( "com.mass3d.program.EventSyncService" )
public class DefaultEventSyncService implements EventSyncService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final EventSyncStore eventSyncStore;

    public DefaultEventSyncService( EventSyncStore eventSyncStore )
    {
        checkNotNull( eventSyncStore );
        this.eventSyncStore = eventSyncStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------
    
    @Override
    public List<ProgramStageInstance> getEvents( List<String> uids )
    {
        return eventSyncStore.getEvents( uids );
    }

    @Override
    public ProgramStageInstance getEvent( String uid )
    {
        return eventSyncStore.getEvent( uid );
    }

    @Override
    public ProgramInstance getEnrollment( String uid )
    {
        return eventSyncStore.getEnrollment( uid );
    }
}