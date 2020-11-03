package com.mass3d.program.hibernate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.SoftDeleteHibernateObjectStore;
import com.mass3d.event.EventStatus;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.program.ProgramStageInstanceStore;
import com.mass3d.program.notification.NotificationTrigger;
import com.mass3d.program.notification.ProgramNotificationTemplate;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramStageInstanceStore" )
public class HibernateProgramStageInstanceStore
    extends SoftDeleteHibernateObjectStore<ProgramStageInstance>
    implements ProgramStageInstanceStore
{
    private final static Set<NotificationTrigger> SCHEDULED_PROGRAM_STAGE_INSTANCE_TRIGGERS =
        Sets.intersection(
            NotificationTrigger.getAllApplicableToProgramStageInstance(),
            NotificationTrigger.getAllScheduledTriggers()
        );

    public HibernateProgramStageInstanceStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramStageInstance.class, currentUserService,
            aclService, false );
    }

    @Override
    public ProgramStageInstance get( ProgramInstance programInstance, ProgramStage programStage )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        List<ProgramStageInstance> list = getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "programInstance" ), programInstance ) )
            .addPredicate( root -> builder.equal( root.get( "programStage" ), programStage ) )
            .addOrder( root -> builder.asc( root.get( "id" ) ) )
            .setMaxResults( 1 ) );

        return list.isEmpty() ? null : list.get( 0 );
    }

    @Override
    public List<ProgramStageInstance> get( Collection<ProgramInstance> programInstances, EventStatus status )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "status" ), status ) )
            .addPredicate( root -> root.get( "programInstance" ).in( programInstances ) ) );
    }

    @Override
    public List<ProgramStageInstance> get( TrackedEntityInstance entityInstance, EventStatus status )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "status" ), status ) )
            .addPredicate( root -> builder.equal( root.join( "programInstance" ).get( "entityInstance" ), entityInstance ) ) );
    }

    @Override
    public long getProgramStageInstanceCountLastUpdatedAfter( Date time )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getCount( builder, newJpaParameters()
            .addPredicate( root -> builder.greaterThanOrEqualTo( root.get( "lastUpdated" ), time ) )
            .count(builder::countDistinct) );
    }

    @Override
    public boolean exists( String uid )
    {
        if ( uid == null )
        {
            return false;
        }

        Query query = getSession().createNativeQuery(
            "select exists(select 1 from programstageinstance where uid=? and deleted is false)" );
        query.setParameter( 1, uid );

        return ((Boolean) query.getSingleResult()).booleanValue();
    }

    @Override
    public boolean existsIncludingDeleted( String uid )
    {
        if ( uid == null )
        {
            return false;
        }

        Query query = getSession().createNativeQuery(
            "select exists(select 1 from programstageinstance where uid=?)" );
        query.setParameter( 1, uid );

        return ((Boolean) query.getSingleResult()).booleanValue();
    }

    @Override
    public List<String> getUidsIncludingDeleted( List<String> uids )
    {
        String hql = "select psi.uid from ProgramStageInstance as psi where psi.uid in (:uids)";
        List<String> resultUids = new ArrayList<>();
        List<List<String>> uidsPartitions = Lists.partition( Lists.newArrayList( uids ), 20000 );

        for ( List<String> uidsPartition : uidsPartitions )
        {
            if ( !uidsPartition.isEmpty() )
            {
                resultUids.addAll( getSession().createQuery( hql, String.class ).setParameter( "uids", uidsPartition ).list() );
            }
        }

        return resultUids;
    }

    @Override
    public void updateProgramStageInstancesSyncTimestamp( List<String> programStageInstanceUIDs, Date lastSynchronized )
    {
        String hql = "update ProgramStageInstance set lastSynchronized = :lastSynchronized WHERE uid in :programStageInstances";

        getQuery( hql )
            .setParameter( "lastSynchronized", lastSynchronized )
            .setParameter( "programStageInstances", programStageInstanceUIDs )
            .executeUpdate();
    }

    @Override
    public List<ProgramStageInstance> getWithScheduledNotifications( ProgramNotificationTemplate template, Date notificationDate )
    {
        if ( notificationDate == null || !SCHEDULED_PROGRAM_STAGE_INSTANCE_TRIGGERS.contains( template.getNotificationTrigger() ) )
        {
            return Lists.newArrayList();
        }

        if ( template.getRelativeScheduledDays() == null )
        {
            return Lists.newArrayList();
        }

        Date targetDate = DateUtils.addDays( notificationDate, template.getRelativeScheduledDays() * -1 );

        String hql =
            "select distinct psi from ProgramStageInstance as psi " +
                "inner join psi.programStage as ps " +
                "where :notificationTemplate in elements(ps.notificationTemplates) " +
                "and psi.dueDate is not null " +
                "and psi.executionDate is null " +
                "and psi.status != :skippedEventStatus " +
                "and cast(:targetDate as date) = psi.dueDate " +
                "and psi.deleted is false";

        return getQuery( hql )
            .setParameter( "notificationTemplate", template )
            .setParameter( "skippedEventStatus", EventStatus.SKIPPED )
            .setParameter( "targetDate", targetDate ).list();
    }

    @Override
    protected void preProcessPredicates( CriteriaBuilder builder, List<Function<Root<ProgramStageInstance>, Predicate>> predicates )
    {
        predicates.add( root -> builder.equal( root.get( "deleted" ), false ) );
    }

    @Override
    protected ProgramStageInstance postProcessObject( ProgramStageInstance programStageInstance )
    {
        return (programStageInstance == null || programStageInstance.isDeleted()) ? null : programStageInstance;
    }
}
