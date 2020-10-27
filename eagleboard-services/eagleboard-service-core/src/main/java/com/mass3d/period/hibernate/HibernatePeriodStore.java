package com.mass3d.period.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import com.mass3d.cache.Cache;
import com.mass3d.cache.CacheProvider;
import com.mass3d.common.exception.InvalidIdentifierReferenceException;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.commons.util.DebugUtils;
import com.mass3d.commons.util.SystemUtils;
import com.mass3d.dbms.DbmsUtils;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodStore;
import com.mass3d.period.PeriodType;
import com.mass3d.period.RelativePeriods;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implements the PeriodStore interface.
 *
 * @version $Id: HibernatePeriodStore.java 5983 2008-10-17 17:42:44Z larshelg $
 */
@Repository( "com.mass3d.period.PeriodStore" )
@Slf4j
public class HibernatePeriodStore
    extends HibernateIdentifiableObjectStore<Period>
    implements PeriodStore
{
    private Environment env;

    private static Cache<Long> PERIOD_ID_CACHE;

    private CacheProvider cacheProvider;

    public HibernatePeriodStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService,
        Environment env, CacheProvider cacheProvider )
    {
        super( sessionFactory, jdbcTemplate, publisher, Period.class, currentUserService, aclService, true );

        transientIdentifiableProperties = true;

        this.env = env;
        this.cacheProvider = cacheProvider;
    }

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    @PostConstruct
    public void init()
    {
        PERIOD_ID_CACHE = cacheProvider.newCacheBuilder( Long.class )
            .forRegion( "periodIdCache" )
            .expireAfterWrite( 24, TimeUnit.HOURS )
            .withInitialCapacity( 200 )
            .forceInMemory()
            .withMaximumSize( SystemUtils.isTestRun( env.getActiveProfiles() ) ? 0 : 10000 )
            .build();
    }

    @Override
    public void addPeriod( Period period )
    {
        period.setPeriodType( reloadPeriodType( period.getPeriodType() ) );

        save( period );
    }

    @Override
    public Period getPeriod( Date startDate, Date endDate, PeriodType periodType )
    {
        String query = "from Period p where p.startDate =:startDate and p.endDate =:endDate and p.periodType =:periodType";

        return getSingleResult( getQuery( query )
            .setParameter( "startDate", startDate )
            .setParameter( "endDate", endDate )
            .setParameter( "periodType", reloadPeriodType( periodType ) ) );
    }

    @Override
    public List<Period> getPeriodsBetweenDates( Date startDate, Date endDate )
    {
        String query = "from Period p where p.startDate >=:startDate and p.endDate <=:endDate";

        Query<Period> typedQuery = getQuery( query )
            .setParameter( "startDate", startDate )
            .setParameter( "endDate", endDate );
        return getList( typedQuery );
    }

    @Override
    public List<Period> getPeriodsBetweenDates( PeriodType periodType, Date startDate, Date endDate )
    {
        String query = "from Period p where p.startDate >=:startDate and p.endDate <=:endDate and p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery( query )
            .setParameter( "startDate", startDate )
            .setParameter( "endDate", endDate )
            .setParameter( "periodType", reloadPeriodType( periodType ).getId() );
        return getList( typedQuery );
    }

    @Override
    public List<Period> getPeriodsBetweenOrSpanningDates( Date startDate, Date endDate )
    {
        String hql = "from Period p where ( p.startDate >= :startDate and p.endDate <= :endDate ) or ( p.startDate <= :startDate and p.endDate >= :endDate )";

        return getQuery( hql ).setParameter( "startDate", startDate ).setParameter( "endDate", endDate ).list();
    }

    @Override
    public List<Period> getIntersectingPeriodsByPeriodType( PeriodType periodType, Date startDate, Date endDate )
    {
        String query = "from Period p where p.startDate <=:endDate and p.endDate >=:startDate and p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery( query )
            .setParameter( "startDate", startDate )
            .setParameter( "endDate", endDate )
            .setParameter( "periodType", reloadPeriodType( periodType ).getId() );
        return getList( typedQuery );
    }

    @Override
    public List<Period> getIntersectingPeriods( Date startDate, Date endDate )
    {
        String query = "from Period p where p.startDate <=:endDate and p.endDate >=:startDate";

        Query<Period> typedQuery = getQuery( query )
            .setParameter( "startDate", startDate )
            .setParameter( "endDate", endDate );
        return getList( typedQuery );
    }

    @Override
    public List<Period> getPeriodsByPeriodType( PeriodType periodType )
    {
        String query = "from Period p where p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery( query )
            .setParameter( "periodType", reloadPeriodType( periodType ).getId() );
        return getList( typedQuery );
    }

    @Override
    public Period getPeriodFromDates( Date startDate, Date endDate, PeriodType periodType )
    {
        String query = "from Period p where p.startDate =:startDate and p.endDate =:endDate and p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery( query )
            .setParameter( "startDate", startDate )
            .setParameter( "endDate", endDate )
            .setParameter( "periodType", reloadPeriodType( periodType ).getId() );
        return getSingleResult( typedQuery );
    }

    @Override
    public Period reloadPeriod( Period period )
    {
        Session session = sessionFactory.getCurrentSession();

        if ( session.contains( period ) )
        {
            return period; // Already in session, no reload needed
        }

        Long id = PERIOD_ID_CACHE.get( period.getCacheKey(), key -> getPeriodId( period.getStartDate(), period.getEndDate(), period.getPeriodType() ) )
            .orElse( null );

        Period storedPeriod = id != null ? getSession().get( Period.class, id ) : null;

        return storedPeriod != null ? storedPeriod.copyTransientProperties( period ) : null;
    }

    private Long getPeriodId( Date startDate, Date endDate, PeriodType periodType )
    {
        Period period = getPeriod( startDate, endDate, periodType );

        return period != null ? period.getId() : null;
    }

    @Override
    public Period reloadForceAddPeriod( Period period )
    {
        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            addPeriod( period );

            return period;
        }

        return storedPeriod;
    }

    // -------------------------------------------------------------------------
    // PeriodType (do not use generic store which is linked to Period)
    // -------------------------------------------------------------------------

    @Override
    public int addPeriodType( PeriodType periodType )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( periodType );
    }

    @Override
    public void deletePeriodType( PeriodType periodType )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( periodType );
    }

    @Override
    public PeriodType getPeriodType( int id )
    {
        Session session = sessionFactory.getCurrentSession();

        return session.get( PeriodType.class, id );
    }

    @Override
    public PeriodType getPeriodType( Class<? extends PeriodType> periodType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<PeriodType> query = builder.createQuery( PeriodType.class );
        query.select( query.from( periodType ) );

        return getSession().createQuery( query ).setCacheable( true ).uniqueResult();
    }

    @Override
    public List<PeriodType> getAllPeriodTypes()
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<PeriodType> query = builder.createQuery( PeriodType.class );
        query.select( query.from( PeriodType.class ) );

        return getSession().createQuery( query ).setCacheable( true ).getResultList();
    }

    @Override
    public PeriodType reloadPeriodType( PeriodType periodType )
    {
        Session session = sessionFactory.getCurrentSession();

        if ( periodType == null || session.contains( periodType ) )
        {
            return periodType;
        }

        PeriodType reloadedPeriodType = getPeriodType( periodType.getClass() );

        if ( reloadedPeriodType == null )
        {
            throw new InvalidIdentifierReferenceException( "The PeriodType referenced by the Period is not in database: "
                + periodType.getName() );
        }

        return reloadedPeriodType;
    }

    @Override
    public Period insertIsoPeriodInStatelessSession( Period period )
    {
        StatelessSession session = sessionFactory.openStatelessSession();
        session.beginTransaction();
        try
        {
            Serializable id = session.insert( period );
            Period storedPeriod = (Period) session.get( Period.class, id );

            PERIOD_ID_CACHE.put( period.getCacheKey(), storedPeriod.getId() );

            return storedPeriod;
        }
        catch ( Exception exception )
        {
            log.error( DebugUtils.getStackTrace( exception ) );
        }
        finally
        {
            DbmsUtils.closeStatelessSession( session );
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // RelativePeriods (do not use generic store which is linked to Period)
    // -------------------------------------------------------------------------

    @Override
    public void deleteRelativePeriods( RelativePeriods relativePeriods )
    {
        sessionFactory.getCurrentSession().delete( relativePeriods );
    }
}
