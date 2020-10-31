package com.mass3d.category.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryStore;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.category.CategoryStore" )
public class HibernateCategoryStore
    extends HibernateIdentifiableObjectStore<Category>
    implements CategoryStore
{
    public HibernateCategoryStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, Category.class, currentUserService, aclService, true );
    }

    @Override
    public List<Category> getCategoriesByDimensionType( DataDimensionType dataDimensionType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate( root -> builder.equal( root.get( "dataDimensionType" ), dataDimensionType ) ) );
    }

    @Override
    public List<Category> getCategories( DataDimensionType dataDimensionType, boolean dataDimension )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate( root -> builder.equal( root.get( "dataDimensionType" ), dataDimensionType ) )
            .addPredicate( root -> builder.equal( root.get( "dataDimension" ), dataDimension ) ) );
    }

    @Override
    public List<Category> getCategoriesNoAcl( DataDimensionType dataDimensionType, boolean dataDimension )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "dataDimensionType" ), dataDimensionType ) )
            .addPredicate( root -> builder.equal( root.get( "dataDimension" ), dataDimension ) ) );
    }
}
