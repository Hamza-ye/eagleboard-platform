package com.mass3d.query.planner;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import com.mass3d.query.Conjunction;
import com.mass3d.query.Criterion;
import com.mass3d.query.Disjunction;
import com.mass3d.query.Junction;
import com.mass3d.query.Order;
import com.mass3d.query.Query;
import com.mass3d.query.Restriction;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.query.planner.QueryPlanner" )
public class DefaultQueryPlanner implements QueryPlanner
{
    private final SchemaService schemaService;

    @Autowired
    public DefaultQueryPlanner( SchemaService schemaService )
    {
        checkNotNull( schemaService );

        this.schemaService = schemaService;
    }

    @Override
    public QueryPlan planQuery( Query query )
    {
        return planQuery( query, false );
    }

    @Override
    public QueryPlan planQuery( Query query, boolean persistedOnly )
    {
        // if only one filter, always set to Junction.Type AND
        Junction.Type junctionType = query.getCriterions().size() <= 1 ? Junction.Type.AND : query.getRootJunctionType();
        
        if ( (!isFilterOnPersistedFieldOnly( query ) || Junction.Type.OR == junctionType) && !persistedOnly )
        {
            return QueryPlan.QueryPlanBuilder.newBuilder()
                .persistedQuery( Query.from( query.getSchema() ).setPlannedQuery( true ) )
                .nonPersistedQuery( Query.from( query ).setPlannedQuery( true ) )
                .build();
        }

        Query npQuery = Query.from( query ).setUser( query.getUser() ).setPlannedQuery( true );

        Query pQuery = getQuery( npQuery, persistedOnly ).setUser( query.getUser() ).setPlannedQuery( true );

        // if there are any non persisted criterions left, we leave the paging to the in-memory engine
        if ( !npQuery.getCriterions().isEmpty() )
        {
            pQuery.setSkipPaging( true );
        }
        else
        {
            pQuery.setFirstResult( npQuery.getFirstResult() );
            pQuery.setMaxResults( npQuery.getMaxResults() );
        }

        return QueryPlan.QueryPlanBuilder
            .newBuilder()
            .persistedQuery( pQuery )
            .nonPersistedQuery( npQuery )
            .build();
    }

    @Override
    public QueryPath getQueryPath( Schema schema, String path )
    {
        Schema curSchema = schema;
        Property curProperty = null;
        boolean persisted = true;
        List<String> alias = new ArrayList<>();
        String[] pathComponents = path.split( "\\." );

        if ( pathComponents.length == 0 )
        {
            return null;
        }

        for ( int idx = 0; idx < pathComponents.length; idx++ )
        {
            String name = pathComponents[idx];
            curProperty = curSchema.getProperty( name );

            if ( curProperty == null )
            {
                throw new RuntimeException( "Invalid path property: " + name );
            }

            if ( !curProperty.isPersisted() )
            {
                persisted = false;
            }

            if ( (!curProperty.isSimple() && idx == pathComponents.length - 1) )
            {
                return new QueryPath( curProperty, persisted, alias.toArray( new String[]{} ) );
            }

            if ( curProperty.isCollection() )
            {
                curSchema = schemaService.getDynamicSchema( curProperty.getItemKlass() );
                alias.add( curProperty.getFieldName() );
            }
            else if ( !curProperty.isSimple() )
            {
                curSchema = schemaService.getDynamicSchema( curProperty.getKlass() );
                alias.add( curProperty.getFieldName() );
            }
            else
            {
                return new QueryPath( curProperty, persisted, alias.toArray( new String[]{} ) );
            }
        }

        return new QueryPath( curProperty, persisted, alias.toArray( new String[]{} ) );
    }

    @Override
    public Path<?> getQueryPath( Root<?> root, Schema schema, String path )
    {
        Schema curSchema = schema;
        Property curProperty;
        String[] pathComponents = path.split( "\\." );

        Path<?> currentPath = root;

        if ( pathComponents.length == 0 )
        {
            return null;
        }

        for ( int idx = 0; idx < pathComponents.length; idx++ )
        {
            String name = pathComponents[idx];
            curProperty = curSchema.getProperty( name );

            if ( curProperty == null )
            {
                throw new RuntimeException( "Invalid path property: " + name );
            }

            if ( (!curProperty.isSimple() && idx == pathComponents.length - 1) )
            {
                return root.join( curProperty.getFieldName() );
            }

            if ( curProperty.isCollection() )
            {
                currentPath = root.join( curProperty.getFieldName() );
                curSchema = schemaService.getDynamicSchema( curProperty.getItemKlass() );
            }
            else if ( !curProperty.isSimple() )
            {
                curSchema = schemaService.getDynamicSchema( curProperty.getKlass() );
                currentPath = root.join( curProperty.getFieldName() );
            }
            else
            {
                return currentPath.get( curProperty.getFieldName() );
            }
        }

        return currentPath;
    }

    /**
     * @param query Query
     * @return Query instance
     */
    private Query getQuery( Query query, boolean persistedOnly )
    {
        Query pQuery = Query.from( query.getSchema(), query.getRootJunctionType() );
        Iterator<Criterion> iterator = query.getCriterions().iterator();

        while ( iterator.hasNext() )
        {
            com.mass3d.query.Criterion criterion = iterator.next();

            if ( Junction.class.isInstance( criterion ) )
            {
                Junction junction = handleJunction( pQuery, (Junction) criterion, persistedOnly );

                if ( !junction.getCriterions().isEmpty() )
                {
                    pQuery.getAliases().addAll( junction.getAliases() );
                    pQuery.add( junction );
                }

                if ( ((Junction) criterion).getCriterions().isEmpty() )
                {
                    iterator.remove();
                }
            }
            else if ( Restriction.class.isInstance( criterion ) )
            {
                Restriction restriction = (Restriction) criterion;
                restriction.setQueryPath( getQueryPath( query.getSchema(), restriction.getPath() ) );

                if ( restriction.getQueryPath().isPersisted() && !restriction.getQueryPath().haveAlias() )
                {
                    pQuery.getAliases().addAll( Arrays
                        .asList( ((Restriction) criterion).getQueryPath().getAlias() ) );
                    pQuery.getCriterions().add( criterion );
                    iterator.remove();
                }
            }
        }

        if ( query.ordersPersisted() )
        {
            pQuery.addOrders( query.getOrders() );
            query.clearOrders();
        }

        return pQuery;
    }

    private Junction handleJunction( Query query, Junction queryJunction, boolean persistedOnly )
    {
        Iterator<com.mass3d.query.Criterion> iterator = queryJunction.getCriterions().iterator();
        Junction criteriaJunction = Disjunction.class.isInstance( queryJunction ) ?
            new Disjunction( query.getSchema() ) : new Conjunction( query.getSchema() );

        while ( iterator.hasNext() )
        {
            com.mass3d.query.Criterion criterion = iterator.next();

            if ( Junction.class.isInstance( criterion ) )
            {
                Junction junction = handleJunction( query, (Junction) criterion, persistedOnly );

                if ( !junction.getCriterions().isEmpty() )
                {
                    criteriaJunction.getAliases().addAll( junction.getAliases() );
                    criteriaJunction.add( junction );
                }

                if ( ((Junction) criterion).getCriterions().isEmpty() )
                {
                    iterator.remove();
                }
            }
            else if ( Restriction.class.isInstance( criterion ) )
            {
                Restriction restriction = (Restriction) criterion;
                restriction.setQueryPath( getQueryPath( query.getSchema(), restriction.getPath() ) );

                if ( restriction.getQueryPath().isPersisted() && !restriction.getQueryPath().haveAlias( 1 ) )
                {
                    criteriaJunction.getAliases().addAll( Arrays
                        .asList( ((Restriction) criterion).getQueryPath().getAlias() ) );
                    criteriaJunction.getCriterions().add( criterion );
                    iterator.remove();
                }
                else if ( persistedOnly )
                {
                    throw new RuntimeException( "Path " + restriction.getQueryPath().getPath() +
                        " is not fully persisted, unable to build persisted only query plan." );
                }
            }
        }

        return criteriaJunction;
    }

    /**
     * Check if all the criteria for the given query are associated to "persisted" properties
     *
     * @param query a {@see Query} object
     * @return true, if all criteria are on persisted properties
     */
    private boolean isFilterOnPersistedFieldOnly( Query query )
    {
        Set<String> persistedFields = query.getSchema().getPersistedProperties().keySet();
        if ( nonPersistedFieldExistsInCriterions( persistedFields, query.getCriterions() ) )
        {
            return false;
        }

        for ( Order order : query.getOrders() )
        {

            if ( !persistedFields.contains( order.getProperty().getName() ) )
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Recursive function that checks if any of the criterions or subcriterions are associated with fields that are not persisted. 
     *
     * @param persistedFields The set of persistedFields in the schema
     * @param criterions List of criterions
     * @return true if there is any non persisted field in any of the criteria at any level. false otherwise.
     */
    private boolean nonPersistedFieldExistsInCriterions( Set<String> persistedFields, List<Criterion> criterions )
    {
        for ( Criterion criterion : criterions )
        {
            if ( criterion instanceof Restriction )
            {
                Restriction restriction = (Restriction) criterion;
                if ( !persistedFields.contains( restriction.getPath() ) )
                {
                    return true;
                }
            }
            else if ( criterion instanceof Junction )
            {
                if ( nonPersistedFieldExistsInCriterions( persistedFields, ((Junction) criterion).getCriterions() ) )
                {
                    return true;
                }
            }
        }
        return false;
    }
}
