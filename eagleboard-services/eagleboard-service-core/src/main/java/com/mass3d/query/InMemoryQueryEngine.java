package com.mass3d.query;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.PagerUtils;
import com.mass3d.hibernate.HibernateUtils;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.security.acl.AclService;
import com.mass3d.system.util.ReflectionUtils;
import com.mass3d.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.query.InMemoryQueryEngine" )
public class InMemoryQueryEngine<T extends IdentifiableObject>
    implements QueryEngine<T>
{
    private final SchemaService schemaService;
    private final AclService aclService;
    private final CurrentUserService currentUserService;

    @Autowired
    public InMemoryQueryEngine( SchemaService schemaService, AclService aclService, CurrentUserService currentUserService )
    {
        checkNotNull( schemaService );
        checkNotNull( aclService );
        checkNotNull( currentUserService );

        this.schemaService = schemaService;
        this.aclService = aclService;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<T> query( Query query )
    {
        validateQuery( query );
        List<T> list = runQuery( query );
        list = runSorter( query, list );

        return PagerUtils.pageCollection( list, query.getFirstResult(), query.getMaxResults() );
    }

    @Override
    public int count( Query query )
    {
        validateQuery( query );
        List<T> list = runQuery( query );

        return list.size();
    }

    private void validateQuery( Query query )
    {
        if ( query.getUser() == null )
        {
            query.setUser( currentUserService.getCurrentUser() );
        }

        if ( query.getSchema() == null )
        {
            throw new QueryException( "Invalid Query object, does not contain Schema" );
        }

        if ( query.getObjects() == null )
        {
            throw new QueryException( "InMemoryQueryEngine requires an existing object list to work on." );
        }
    }

    @SuppressWarnings( "unchecked" )
    private List<T> runQuery( Query query )
    {
        return query.getObjects().stream()
            .filter( object -> test( query, (T) object ) )
            .map( object -> (T) object )
            .collect( Collectors.toList() );
    }

    private List<T> runSorter( Query query, List<T> objects )
    {
        List<T> sorted = new ArrayList<>( objects );

        sorted.sort( ( o1, o2 ) ->
        {
            for ( Order order : query.getOrders() )
            {
                int result = order.compare( o1, o2 );
                if ( result != 0 ) return result;
            }

            return 0;
        } );

        return sorted;
    }

    private boolean test( Query query, T object )
    {
        List<Boolean> testResults = new ArrayList<>();

        for ( Criterion criterion : query.getCriterions() )
        {
            Boolean testResult = false;

            // normal Restriction, just assume Conjunction
            if ( Restriction.class.isInstance( criterion ) )
            {
                Restriction restriction = (Restriction) criterion;
                testResult = testAnd( query, object, Lists.newArrayList( restriction ) );
            }
            else if ( Conjunction.class.isInstance( criterion ) )
            {
                Conjunction conjunction = (Conjunction) criterion;
                testResult = testAnd( query, object, conjunction.getCriterions() );
            }
            else if ( Disjunction.class.isInstance( criterion ) )
            {
                Disjunction disjunction = (Disjunction) criterion;
                testResult = testOr( query, object, disjunction.getCriterions() );
            }

            testResults.add( testResult );
        }

        if ( query.getRootJunctionType() == Junction.Type.OR )
        {
            return testResults.contains( Boolean.TRUE );
        }

        return !testResults.contains( Boolean.FALSE );
    }

    private boolean testAnd( Query query, T object, List<Criterion> criterions )
    {
        for ( Criterion criterion : criterions )
        {
            if ( Restriction.class.isInstance( criterion ) )
            {
                Restriction restriction = (Restriction) criterion;
                Object value = getValue( query, object, restriction.getPath() );

                if ( !Collection.class.isInstance( value ) )
                {
                    if ( !restriction.getOperator().test( value ) )
                    {
                        return false;
                    }
                }
                else
                {
                    Collection<?> collection = (Collection<?>) value;

                    for ( Object item : collection )
                    {
                        if ( restriction.getOperator().test( item ) )
                        {
                            return true;
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private boolean testOr( Query query, T object, List<Criterion> criterions )
    {
        for ( Criterion criterion : criterions )
        {
            if ( Restriction.class.isInstance( criterion ) )
            {
                Restriction restriction = (Restriction) criterion;
                Object value = getValue( query, object, restriction.getPath() );

                if ( !Collection.class.isInstance( value ) )
                {
                    if ( restriction.getOperator().test( value ) )
                    {
                        return true;
                    }
                }
                else
                {
                    Collection<?> collection = (Collection<?>) value;

                    for ( Object item : collection )
                    {
                        if ( restriction.getOperator().test( item ) )
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @SuppressWarnings( "unchecked" )
    private Object getValue( Query query, Object object, String path )
    {
        String[] paths = path.split( "\\." );
        Schema currentSchema = query.getSchema();

        if ( path.contains( "access" ) && query.getSchema().isIdentifiableObject() )
        {
            ((BaseIdentifiableObject) object).setAccess( aclService.getAccess( (T) object, query.getUser() ) );
        }

        for ( int i = 0; i < paths.length; i++ )
        {
            Property property = currentSchema.getProperty( paths[i] );

            if ( property == null )
            {
                throw new QueryException( "No property found for path " + path );
            }

            if ( property.isCollection() )
            {
                currentSchema = schemaService.getDynamicSchema( property.getItemKlass() );
            }
            else
            {
                currentSchema = schemaService.getDynamicSchema( property.getKlass() );
            }

            object = collect( object, property );

            if ( path.contains( "access" ) && property.isIdentifiableObject() )
            {
                if ( property.isCollection() )
                {
                    for ( Object item : ((Collection<?>) object) )
                    {
                        ((BaseIdentifiableObject) item).setAccess( aclService.getAccess( (T) item, query.getUser() ) );
                    }
                }
                else
                {
                    ((BaseIdentifiableObject) object).setAccess( aclService.getAccess( (T) object, query.getUser() ) );
                }
            }

            if ( i == (paths.length - 1) )
            {
                if ( property.isCollection() )
                {
                    return Lists.newArrayList( object );
                }

                return object;
            }

        }

        throw new QueryException( "No values found for path " + path );
    }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    private Object collect( Object object, Property property )
    {
        object = HibernateUtils.unwrap( object );

        if ( Collection.class.isInstance( object ) )
        {
            Collection<?> collection = (Collection<?>) object;
            List<Object> items = new ArrayList<>();

            for ( Object item : collection )
            {
                Object collect = collect( item, property );

                if ( Collection.class.isInstance( collect ) )
                {
                    items.addAll( ((Collection) collect) );
                }
                else
                {
                    items.add( collect );
                }
            }

            return items;
        }

        return ReflectionUtils.invokeMethod( object, property.getGetterMethod() );
    }
}
