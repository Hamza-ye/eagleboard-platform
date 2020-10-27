package com.mass3d.deduplication.hibernate;

import java.math.BigInteger;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.deduplication.PotentialDuplicate;
import com.mass3d.deduplication.PotentialDuplicateQuery;
import com.mass3d.deduplication.PotentialDuplicateStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.deduplication.PotentialDuplicateStore" )
public class HibernatePotentialDuplicateStore
    extends HibernateIdentifiableObjectStore<PotentialDuplicate>
    implements PotentialDuplicateStore
{
    public HibernatePotentialDuplicateStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, PotentialDuplicate.class, currentUserService,
            aclService, false );
    }

    @Override
    public int getCountByQuery( PotentialDuplicateQuery query )
    {
        if ( query.getTeis() != null && query.getTeis().size() > 0 )
        {
            Query<Long> hibernateQuery = getTypedQuery(
                "select count(*) from PotentialDuplicate pr where pr.teiA in (:uids)  or pr.teiB in (:uids)" );
            hibernateQuery.setParameterList( "uids", query.getTeis() );
            return hibernateQuery.getSingleResult().intValue();
        }
        else
        {
            return getCount();
        }
    }

    @Override
    public List<PotentialDuplicate> getAllByQuery( PotentialDuplicateQuery query )
    {
        if ( query.getTeis() != null && query.getTeis().size() > 0 )
        {
            Query<PotentialDuplicate> hibernateQuery = getTypedQuery(
                "from PotentialDuplicate pr where pr.teiA in (:uids)  or pr.teiB in (:uids)" );
            hibernateQuery.setParameterList( "uids", query.getTeis() );
            return hibernateQuery.getResultList();
        }
        else
        {
            return getAll();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean exists( PotentialDuplicate potentialDuplicate )
    {
        NativeQuery<BigInteger> query;
        if ( potentialDuplicate.getTeiA() == null )
        {
            return false;
        }

        if ( potentialDuplicate.getTeiB() == null )
        {
            query = getSession().createNativeQuery( "select count(potentialduplicateid) from potentialduplicate pd " +
                "where pd.teiA = :teia limit 1" );
            query.setParameter( "teia", potentialDuplicate.getTeiA() );
        }
        else
        {
            query = getSession().createNativeQuery( "select count(potentialduplicateid) from potentialduplicate pd " +
                "where (pd.teiA = :teia and pd.teiB = :teib) or (pd.teiA = :teib and pd.teiB = :teia) limit 1" );

            query.setParameter( "teia", potentialDuplicate.getTeiA() );
            query.setParameter( "teib", potentialDuplicate.getTeiB() );
        }

        return query.getSingleResult().intValue() != 0;
    }
}
