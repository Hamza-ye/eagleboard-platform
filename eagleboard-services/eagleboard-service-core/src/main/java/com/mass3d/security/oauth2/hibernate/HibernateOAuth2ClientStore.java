package com.mass3d.security.oauth2.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.security.oauth2.OAuth2Client;
import com.mass3d.security.oauth2.OAuth2ClientStore;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.security.oauth2.OAuth2ClientStore" )
public class HibernateOAuth2ClientStore
    extends HibernateIdentifiableObjectStore<OAuth2Client>
        implements OAuth2ClientStore
{
    public HibernateOAuth2ClientStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, OAuth2Client.class, currentUserService, aclService,
            true );
    }

    @Override
    public OAuth2Client getByClientId( String cid )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters().addPredicate( root -> builder.equal( root.get( "cid" ), cid ) ) );
    }
}
