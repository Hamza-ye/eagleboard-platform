package com.mass3d.category.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import org.hibernate.SessionFactory;
import com.mass3d.category.CategoryOptionGroup;
import com.mass3d.category.CategoryOptionGroupSet;
import com.mass3d.category.CategoryOptionGroupStore;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.category.CategoryOptionGroupStore" )
public class HibernateCategoryOptionGroupStore
    extends HibernateIdentifiableObjectStore<CategoryOptionGroup>
    implements CategoryOptionGroupStore
{
    public HibernateCategoryOptionGroupStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, CategoryOptionGroup.class, currentUserService, aclService, true );
    }

    @Override
    public List<CategoryOptionGroup> getCategoryOptionGroups( CategoryOptionGroupSet groupSet )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<CategoryOptionGroup> parameters = newJpaParameters()
            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate( root -> {
                Join<Object, Object> groupSets = root.join( "groupSets" );

                return builder.or( builder.equal( groupSets.get( "id" ) , groupSet.getId() ),
                                    builder.isNull( groupSets.get( "id" ) ) );
            });

        return getList( builder, parameters );
    }
}
