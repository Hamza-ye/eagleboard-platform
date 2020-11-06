package com.mass3d.dataelement.hibernate;

import com.mass3d.category.CategoryCombo;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.ValueType;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementDomain;
import com.mass3d.dataelement.DataElementStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.dataelement.DataElementStore" )
public class HibernateDataElementStore
    extends HibernateIdentifiableObjectStore<DataElement>
    implements DataElementStore
{
    public HibernateDataElementStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataElement.class, currentUserService, aclService, false );
    }

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    @Override
    public List<DataElement> getDataElementsByDomainType( DataElementDomain domainType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters().addPredicate( root -> builder.equal( root.get( "domainType" ), domainType ) ) );
    }

    @Override
    public List<DataElement> getDataElementsByValueType( ValueType valueType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters().addPredicate( root -> builder.equal( root.get( "valueType" ), valueType ) ) );
    }

    @Override
    public List<DataElement> getDataElementByCategoryCombo( CategoryCombo categoryCombo )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters().addPredicate( root -> builder.equal( root.get( "categoryCombo" ), categoryCombo ) ) );
    }

    @Override
    public List<DataElement> getDataElementsByZeroIsSignificant( boolean zeroIsSignificant )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "zeroIsSignificant" ), zeroIsSignificant ) )
            .addPredicate( root -> root.get( "valueType" ).in( ValueType.NUMERIC_TYPES ) ) );
    }

    @Override
    public List<DataElement> getDataElementsWithoutGroups()
    {
        String hql = "from DataElement d where size(d.groups) = 0";

        return getQuery( hql ).setCacheable( true ).list();
    }

    @Override
    public List<DataElement> getDataElementsWithoutDataSets()
    {
        String hql = "from DataElement d where size(d.dataSetElements) = 0 and d.domainType =:domainType";

        return getQuery( hql ).setParameter( "domainType", DataElementDomain.AGGREGATE ).setCacheable( true ).list();
    }

    @Override
    public List<DataElement> getDataElementsWithDataSets()
    {
        String hql = "from DataElement d where size(d.dataSetElements) > 0";

        return getQuery( hql ).setCacheable( true ).list();
    }

    @Override
    public List<DataElement> getDataElementsByAggregationLevel( int aggregationLevel )
    {
        String hql = "from DataElement de join de.aggregationLevels al where al = :aggregationLevel";

        return getQuery( hql ).setParameter( "aggregationLevel", aggregationLevel ).list();
    }
}
