package com.mass3d.trackedentity.hibernate;

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.QueryFilter;
import com.mass3d.common.QueryItem;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.commons.util.SqlHelper;
import com.mass3d.jdbc.StatementBuilder;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramTrackedEntityAttribute;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityAttributeStore;
import com.mass3d.trackedentity.TrackedEntityInstanceQueryParams;
import com.mass3d.trackedentity.TrackedEntityTypeAttribute;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentity.TrackedEntityAttributeStore" )
public class HibernateTrackedEntityAttributeStore
    extends HibernateIdentifiableObjectStore<TrackedEntityAttribute>
    implements TrackedEntityAttributeStore
{
    private final StatementBuilder statementBuilder;

    public HibernateTrackedEntityAttributeStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService,
        AclService aclService, StatementBuilder statementBuilder )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityAttribute.class, currentUserService, aclService, true );
        this.statementBuilder = statementBuilder;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public List<TrackedEntityAttribute> getByDisplayOnVisitSchedule( boolean displayOnVisitSchedule )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "displayOnVisitSchedule" ), displayOnVisitSchedule ) ) );
    }

    @Override
    public List<TrackedEntityAttribute> getDisplayInListNoProgram()
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "displayInListNoProgram" ), true ) ) );
    }

    @Override
    public Optional<String> getTrackedEntityInstanceUidWithUniqueAttributeValue( TrackedEntityInstanceQueryParams params )
    {
        // ---------------------------------------------------------------------
        // Select clause
        // ---------------------------------------------------------------------

        SqlHelper hlp = new SqlHelper( true );

        String hql = "select tei.uid from TrackedEntityInstance tei ";

        if ( params.hasOrganisationUnits() )
        {
            String orgUnitUids = params.getOrganisationUnits().stream()
                .map( OrganisationUnit::getUid )
                .collect( Collectors.joining( ", ", "'", "'" ) );

            hql += "inner join tei.organisationUnit as ou ";
            hql += hlp.whereAnd() + " ou.uid in (" + orgUnitUids + ") ";
        }

        for ( QueryItem item : params.getAttributes() )
        {
            for ( QueryFilter filter : item.getFilters() )
            {
                final String encodedFilter = filter.getSqlFilter( statementBuilder.encode( StringUtils
                    .lowerCase( filter.getFilter() ), false ) );

                hql += hlp.whereAnd() + " exists (from TrackedEntityAttributeValue teav where teav.entityInstance=tei";
                hql += " and teav.attribute.uid='" + item.getItemId() + "'";

                if ( item.isNumeric() )
                {
                    hql += " and teav.plainValue " + filter.getSqlOperator() + encodedFilter + ")";
                }
                else
                {
                    hql += " and lower(teav.plainValue) " + filter.getSqlOperator() + encodedFilter + ")";
                }
            }
        }

        if ( !params.isIncludeDeleted() )
        {
            hql += hlp.whereAnd() + " tei.deleted is false";
        }

        Query<String> query = getTypedQuery( hql );
        query.setMaxResults( 1 );

        Iterator<String> it = query.iterate();

        if ( it.hasNext() )
        {
            return Optional.of( it.next());
        }

        return Optional.empty();
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Set<TrackedEntityAttribute> getTrackedEntityAttributesByTrackedEntityTypes()
    {
        Query query = sessionFactory.getCurrentSession()
                .createQuery( "select trackedEntityTypeAttributes from TrackedEntityType" );

        Set<TrackedEntityTypeAttribute> trackedEntityTypeAttributes = new HashSet<>( query.list() );

        return trackedEntityTypeAttributes.stream()
            .map( TrackedEntityTypeAttribute::getTrackedEntityAttribute )
            .collect( Collectors.toSet() );
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<Program, Set<TrackedEntityAttribute>> getTrackedEntityAttributesByProgram()
    {
        Map<Program, Set<TrackedEntityAttribute>> result = new HashMap<>();

        Query query = sessionFactory.getCurrentSession().createQuery( "select p.programAttributes from Program p" );

        List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = query.list();

        for ( ProgramTrackedEntityAttribute programTrackedEntityAttribute : programTrackedEntityAttributes )
        {
            if ( !result.containsKey( programTrackedEntityAttribute.getProgram() ) )
            {
                result.put( programTrackedEntityAttribute.getProgram(), Sets.newHashSet( programTrackedEntityAttribute.getAttribute() ) );
            }
            else
            {
                result.get( programTrackedEntityAttribute.getProgram() ).add( programTrackedEntityAttribute.getAttribute() );
            }
        }
        return result;
    }
}
