package com.mass3d.trackedentity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component
public class TrackedEntityDataElementDimensionDeletionHandler
    extends DeletionHandler
{
    private final SessionFactory sessionFactory;

    public TrackedEntityDataElementDimensionDeletionHandler( SessionFactory sessionFactory )
    {
        checkNotNull( sessionFactory );
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected String getClassName()
    {
        return TrackedEntityDataElementDimension.class.getSimpleName();
    }

//    @Override
//    @SuppressWarnings( "unchecked" )
//    public void deleteLegendSet( LegendSet legendSet )
//    {
//        //TODO Move this get-method to service layer
//
//        Query query = sessionFactory.getCurrentSession()
//            .createQuery( "FROM TrackedEntityDataElementDimension WHERE legendSet=:legendSet" );
//        query.setParameter( "legendSet", legendSet );
//
//        List<TrackedEntityDataElementDimension> dataElementDimensions = query.list();
//
//        for ( TrackedEntityDataElementDimension dataElementDimension : dataElementDimensions )
//        {
//            dataElementDimension.setLegendSet( null );
//            sessionFactory.getCurrentSession().update( dataElementDimension );
//        }
//    }
}
