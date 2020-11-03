package com.mass3d.trackedentity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.trackedentity.TrackedEntityTypeService" )
public class DefaultTrackedEntityTypeService
    implements TrackedEntityTypeService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final IdentifiableObjectStore<TrackedEntityType> trackedEntityTypeStore;

    public DefaultTrackedEntityTypeService( IdentifiableObjectStore<TrackedEntityType> trackedEntityTypeStore )
    {
        checkNotNull( trackedEntityTypeStore );

        this.trackedEntityTypeStore = trackedEntityTypeStore;
    }

    // -------------------------------------------------------------------------
    // TrackedEntityType
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        trackedEntityTypeStore.save( trackedEntityType );

        return trackedEntityType.getId();
    }

    @Override
    @Transactional
    public void deleteTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        trackedEntityTypeStore.delete( trackedEntityType );
    }

    @Override
    @Transactional
    public void updateTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        trackedEntityTypeStore.update( trackedEntityType );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityType getTrackedEntityType( long id )
    {
        return trackedEntityTypeStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityType getTrackedEntityType( String uid )
    {
        return trackedEntityTypeStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityType getTrackedEntityByName( String name )
    {
        return trackedEntityTypeStore.getByName( name );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityType> getAllTrackedEntityType()
    {
        return trackedEntityTypeStore.getAll();
    }
}
