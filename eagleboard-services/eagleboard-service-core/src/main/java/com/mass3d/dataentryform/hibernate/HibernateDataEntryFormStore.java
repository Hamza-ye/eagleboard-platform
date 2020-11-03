package com.mass3d.dataentryform.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.dataentryform.DataEntryFormStore;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.dataentryform.DataEntryFormStore" )
public class HibernateDataEntryFormStore
    extends HibernateIdentifiableObjectStore<DataEntryForm>
    implements DataEntryFormStore
{
    public HibernateDataEntryFormStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
         CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataEntryForm.class, currentUserService, aclService, false );
    }

    // -------------------------------------------------------------------------
    // DataEntryFormStore implementation
    // -------------------------------------------------------------------------

    @Override
    public DataEntryForm getDataEntryFormByName( String name )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<DataEntryForm> parameters = new JpaQueryParameters<DataEntryForm>()
            .addPredicate( root -> builder.equal( root.get( "name" ), name ) );

        return getSingleResult( builder, parameters );
    }
}
