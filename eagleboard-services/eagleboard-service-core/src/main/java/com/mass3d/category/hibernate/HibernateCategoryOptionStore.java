package com.mass3d.category.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryOptionStore;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.category.CategoryOptionStore" )
public class HibernateCategoryOptionStore
    extends HibernateIdentifiableObjectStore<CategoryOption>
    implements CategoryOptionStore
{
    public HibernateCategoryOptionStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, CategoryOption.class, currentUserService, aclService, true );
    }

    @Override
    public List<CategoryOption> getCategoryOptions( Category category )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate( root -> builder.equal( root.join( "categories" ).get( "id" ), category.getId() ) ) );

    }
}
