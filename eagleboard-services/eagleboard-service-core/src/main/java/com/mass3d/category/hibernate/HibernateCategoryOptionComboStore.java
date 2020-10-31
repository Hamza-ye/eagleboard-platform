package com.mass3d.category.hibernate;

import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.category.CategoryOptionComboStore;
import com.mass3d.common.ObjectDeletionRequestedEvent;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dbms.DbmsManager;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.category.CategoryOptionComboStore" )
public class HibernateCategoryOptionComboStore
    extends HibernateIdentifiableObjectStore<CategoryOptionCombo>
    implements CategoryOptionComboStore
{
    private DbmsManager dbmsManager;

    public HibernateCategoryOptionComboStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService,
        AclService aclService, DbmsManager dbmsManager )
    {
        super( sessionFactory, jdbcTemplate, publisher, CategoryOptionCombo.class, currentUserService, aclService, true );
        this.dbmsManager = dbmsManager;
    }

    @Override
    public CategoryOptionCombo getCategoryOptionCombo( CategoryCombo categoryCombo, Set<CategoryOption> categoryOptions )
    {
        String hql = "from CategoryOptionCombo co where co.categoryCombo = :categoryCombo";

        for ( CategoryOption option : categoryOptions )
        {
            hql += " and :option" + option.getId() + " in elements (co.categoryOptions)";
        }

        Query<CategoryOptionCombo> query = getQuery( hql );

        query.setParameter( "categoryCombo", categoryCombo );

        for ( CategoryOption option : categoryOptions )
        {
            query.setParameter( "option" + option.getId(), option );
        }

        return query.uniqueResult();
    }

    @Override
    public void updateNames()
    {
        List<CategoryOptionCombo> categoryOptionCombos = getQuery( "from CategoryOptionCombo co where co.name is null" ).list();
        int counter = 0;

        Session session = getSession();

        for ( CategoryOptionCombo coc : categoryOptionCombos )
        {
            session.update( coc );

            if ( ( counter % 400 ) == 0 )
            {
                dbmsManager.clearSession();
            }
        }
    }

    @Override
    public List<CategoryOptionCombo> getCategoryOptionCombosByGroupUid( String groupUid )
    {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<CategoryOptionCombo> query = builder.createQuery( CategoryOptionCombo.class );
        Root<CategoryOptionCombo> root = query.from( CategoryOptionCombo.class );
        Join<Object, Object> joinCatOption = root.join( "categoryOptions", JoinType.INNER );
        Join<Object, Object> joinCatOptionGroup = joinCatOption.join( "groups", JoinType.INNER );
        query.where( builder.equal( joinCatOptionGroup.get( "uid" ), groupUid ) );
        return getSession().createQuery( query ).list();
    }

    @Override
    public void deleteNoRollBack( CategoryOptionCombo categoryOptionCombo )
    {
        ObjectDeletionRequestedEvent event = new ObjectDeletionRequestedEvent( categoryOptionCombo );
        event.setShouldRollBack( false );

        publisher.publishEvent( event );

        getSession().delete( categoryOptionCombo );
    }
}
