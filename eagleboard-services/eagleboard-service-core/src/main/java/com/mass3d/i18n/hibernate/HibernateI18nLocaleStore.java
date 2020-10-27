package com.mass3d.i18n.hibernate;

import java.util.Locale;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.i18n.I18nLocaleStore;
import com.mass3d.i18n.locale.I18nLocale;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.i18n.I18nLocaleStore" )
public class HibernateI18nLocaleStore
    extends HibernateIdentifiableObjectStore<I18nLocale>
    implements I18nLocaleStore
{
    public HibernateI18nLocaleStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, I18nLocale.class, currentUserService, aclService, false );
    }

    @Override
    public I18nLocale getI18nLocaleByLocale( Locale locale )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "locale" ), locale.toString() ) ) );
    }

}
