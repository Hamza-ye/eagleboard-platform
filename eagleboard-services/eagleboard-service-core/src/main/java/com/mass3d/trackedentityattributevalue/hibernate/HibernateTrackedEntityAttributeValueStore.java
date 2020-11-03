package com.mass3d.trackedentityattributevalue.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.program.Program;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueStore" )
public class HibernateTrackedEntityAttributeValueStore
    extends HibernateGenericStore<TrackedEntityAttributeValue>
    implements TrackedEntityAttributeValueStore
{
    public HibernateTrackedEntityAttributeValueStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityAttributeValue.class, false );
    }
    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public void saveVoid( TrackedEntityAttributeValue attributeValue )
    {
        sessionFactory.getCurrentSession().save( attributeValue );
    }

    @Override
    public int deleteByTrackedEntityInstance( TrackedEntityInstance entityInstance )
    {
        Query<TrackedEntityAttributeValue> query = getQuery( "delete from TrackedEntityAttributeValue where entityInstance = :entityInstance" );
        query.setParameter( "entityInstance", entityInstance );
        return query.executeUpdate();
    }

    @Override
    public TrackedEntityAttributeValue get( TrackedEntityInstance entityInstance, TrackedEntityAttribute attribute )
    {
        String query = " from TrackedEntityAttributeValue v where v.entityInstance =:entityInstance and attribute =:attribute";

        Query<TrackedEntityAttributeValue> typedQuery = getQuery( query )
            .setParameter( "entityInstance", entityInstance )
            .setParameter( "attribute", attribute );

        return getSingleResult( typedQuery );
    }

    @Override
    public List<TrackedEntityAttributeValue> get( TrackedEntityInstance entityInstance )
    {
        String query = " from TrackedEntityAttributeValue v where v.entityInstance =:entityInstance";

        Query<TrackedEntityAttributeValue> typedQuery = getQuery( query ).setParameter( "entityInstance", entityInstance );

        return getList( typedQuery );
    }

    @Override
    public List<TrackedEntityAttributeValue> get( TrackedEntityAttribute attribute )
    {
        String query = " from TrackedEntityAttributeValue v where v.attribute =:attribute";

        Query<TrackedEntityAttributeValue> typedQuery = getQuery( query ).setParameter( "attribute", attribute );

        return getList( typedQuery );
    }

    @Override
    public List<TrackedEntityAttributeValue> get( Collection<TrackedEntityInstance> entityInstances )
    {
        if ( entityInstances == null || entityInstances.isEmpty() )
        {
            return new ArrayList<>();
        }

        String query = " from TrackedEntityAttributeValue v where v.entityInstance  in :entityInstances";

        Query<TrackedEntityAttributeValue> typedQuery = getQuery( query ).setParameter( "entityInstances", entityInstances );

        return getList( typedQuery );
    }

    @Override
    public List<TrackedEntityAttributeValue> searchByValue( TrackedEntityAttribute attribute, String searchText )
    {
        String query = " from TrackedEntityAttributeValue v where v.attribute =:attribute and lower(v.plainValue) like :searchText";

        Query<TrackedEntityAttributeValue> typedQuery = getQuery( query )
            .setParameter( "attribute", attribute )
            .setParameter( "searchText", "%" + StringUtils.lowerCase( searchText  ) + "%");

        return getList( typedQuery );
    }

    @Override
    public List<TrackedEntityAttributeValue> get( TrackedEntityAttribute attribute, String value )
    {
        String query = " from TrackedEntityAttributeValue v where v.attribute =:attribute and lower(v.plainValue) like :value";

        Query<TrackedEntityAttributeValue> typedQuery = getQuery( query )
            .setParameter( "attribute", attribute )
            .setParameter( "value", StringUtils.lowerCase( value ) );

        return getList( typedQuery );
    }

    @Override
    public List<TrackedEntityAttributeValue> get( TrackedEntityInstance entityInstance, Program program )
    {
        String query = " from TrackedEntityAttributeValue v where v.entityInstance =:entityInstance and v.attribute.program =:program";

        Query<TrackedEntityAttributeValue> typedQuery = getQuery( query );
        typedQuery.setParameter( "entityInstance", entityInstance );
        typedQuery.setParameter( "program", program );

        return getList( typedQuery );
    }

    @Override
    public int getCountOfAssignedTEAValues( TrackedEntityAttribute attribute )
    {
        Query query = getQuery( "select count(distinct c) from TrackedEntityAttributeValue c where c.attribute = :attribute" );
        query.setParameter( "attribute", attribute );

        return ((Long) query.getSingleResult()).intValue();
    }
}
