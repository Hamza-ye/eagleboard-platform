package com.mass3d.trackedentityfilter;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.program.Program;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.trackedentityfilter.TrackedEntityInstanceFilterService" )
public class DefaultTrackedEntityInstanceFilterService
    implements TrackedEntityInstanceFilterService
{
    
    private final TrackedEntityInstanceFilterStore trackedEntityInstanceFilterStore;

    public DefaultTrackedEntityInstanceFilterService(
        TrackedEntityInstanceFilterStore trackedEntityInstanceFilterStore )
    {
        checkNotNull(trackedEntityInstanceFilterStore);

        this.trackedEntityInstanceFilterStore = trackedEntityInstanceFilterStore;
    }

    // -------------------------------------------------------------------------
    // TrackedEntityInstanceFilterService implementation
    // -------------------------------------------------------------------------
    
    @Override
    @Transactional
    public long add( TrackedEntityInstanceFilter trackedEntityInstanceFilter )
    {        
        trackedEntityInstanceFilterStore.save( trackedEntityInstanceFilter );
        return trackedEntityInstanceFilter.getId();
    }
    
    @Override
    @Transactional
    public void delete( TrackedEntityInstanceFilter trackedEntityInstanceFilter )
    {
        trackedEntityInstanceFilterStore.delete( trackedEntityInstanceFilter );
    }

    @Override
    @Transactional
    public void update( TrackedEntityInstanceFilter trackedEntityInstanceFilter )
    {
        trackedEntityInstanceFilterStore.update( trackedEntityInstanceFilter );
    }
    
    @Override
    @Transactional(readOnly = true)
    public TrackedEntityInstanceFilter get( long id )
    {
        return trackedEntityInstanceFilterStore.get( id );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityInstanceFilter> getAll()
    {
        return trackedEntityInstanceFilterStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityInstanceFilter> get( Program program )
    {
        return trackedEntityInstanceFilterStore.get( program );
    }
}
