package com.mass3d.category.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryComboStore;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.category.CategoryComboStore" )
public class HibernateCategoryComboStore
    extends HibernateIdentifiableObjectStore<CategoryCombo>
    implements CategoryComboStore
{
    public HibernateCategoryComboStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
        CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, CategoryCombo.class, currentUserService, aclService, true );
    }

    @Override
    public List<CategoryCombo> getCategoryCombosByDimensionType( DataDimensionType dataDimensionType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder,  newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "dataDimensionType" ), dataDimensionType ) )
            .addPredicate( root -> builder.equal( root.get( "name" ), "default" ) ) );
    }
}
