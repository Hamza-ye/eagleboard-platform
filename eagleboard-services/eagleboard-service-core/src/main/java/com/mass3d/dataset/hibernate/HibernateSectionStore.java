package com.mass3d.dataset.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.Section;
import com.mass3d.dataset.SectionStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @version $Id$
 */
@Repository( "com.mass3d.dataset.hibernate.HibernateSectionStore" )
public class HibernateSectionStore
    extends HibernateIdentifiableObjectStore<Section> implements SectionStore
{
    public HibernateSectionStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, Section.class, currentUserService, aclService, true );
    }

    @Override
    public Section getSectionByName( String name, DataSet dataSet )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "name" ), name ) )
            .addPredicate( root -> builder.equal( root.get( "dataSet" ), dataSet ) ) );
    }
}
