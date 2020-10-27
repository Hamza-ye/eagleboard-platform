package com.mass3d.query;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.query.operators.MatchMode;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.query.QueryParser" )
public class DefaultQueryParser implements QueryParser
{
    private static final String IDENTIFIABLE = "identifiable";
    
    private static final String ORGANISATION_UNITS = "organisationUnits";

    private final SchemaService schemaService;
    
//    private final OrganisationUnitService organisationUnitService;
    
    private final CurrentUserService currentUserService;

    @Autowired
    public DefaultQueryParser( SchemaService schemaService, CurrentUserService currentUserService//,
//        OrganisationUnitService organisationUnitService
    )
    {
        checkNotNull( schemaService );

        this.schemaService = schemaService;
        this.currentUserService = currentUserService;
//        this.organisationUnitService = organisationUnitService;
    }

    @Override
    public Query parse( Class<?> klass, List<String> filters ) throws QueryParserException
    {
        return parse( klass, filters, Junction.Type.AND );
    }

    @Override
    public Query parse( Class<?> klass, List<String> filters, Junction.Type rootJunction ) throws QueryParserException
    {
        return parse( klass, filters, rootJunction, false );
    }
    
    @Override
    public Query parse( Class<?> klass, List<String> filters, Junction.Type rootJunction, boolean restrictToCaptureScope ) throws QueryParserException
    {
        Schema schema = schemaService.getDynamicSchema( klass );
        Query query = Query.from( schema, rootJunction );

        for ( String filter : filters )
        {
            String[] split = filter.split( ":" );

            if ( !(split.length >= 2) )
            {
                throw new QueryParserException( "Invalid filter => " + filter );
            }

            if ( split.length >= 3 )
            {
                int index = split[0].length() + ":".length() + split[1].length() + ":".length();

                if ( split[0].equals( IDENTIFIABLE ) && !schema.haveProperty( IDENTIFIABLE ) )
                {
                    handleIdentifiablePath( schema, split[1], filter.substring( index ), query.addDisjunction() );
                }
                else
                {
                    query.add( getRestriction( schema, split[0], split[1], filter.substring( index ) ) );
                }
            }
            else
            {
                query.add( getRestriction( schema, split[0], split[1], null ) );
            }
        }
        
        if ( restrictToCaptureScope && schema.haveProperty( ORGANISATION_UNITS ) )
        {
            User user = currentUserService.getCurrentUser();

            if ( user != null )
            {
//                handleCaptureScopeOuFiltering( schema, user, query.addDisjunction() );
                query.setUser( user );
            }
        }
        return query;
    }

//    private void handleCaptureScopeOuFiltering( Schema schema, User user, Disjunction disjunction )
//    {
//
//        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
//        params.setParents( user.getTodoTasks() );
//        params.setFetchChildren( true );
//
//        List<String> orgUnits = organisationUnitService.getOrganisationUnitsByQuery( params ).stream().map( orgUnit -> orgUnit.getUid() ).collect(
//            Collectors.toList() );
//
//        disjunction.add( getRestriction( schema, "organisationUnits.id", "in", "[" + String
//            .join( ",", orgUnits ) + "]" ) );
//        disjunction.add( getRestriction( schema, ORGANISATION_UNITS, "empty", null ) );
//    }
    
    private void handleIdentifiablePath( Schema schema, String operator, Object arg, Disjunction disjunction )
    {
        disjunction.add( getRestriction( schema, "id", operator, arg ) );
        disjunction.add( getRestriction( schema, "code", operator, arg ) );
        disjunction.add( getRestriction( schema, "name", operator, arg ) );

        if ( schema.haveProperty( "shortName" ) )
        {
            disjunction.add( getRestriction( schema, "shortName", operator, arg ) );
        }
    }

    @Override
    public Restriction getRestriction( Schema schema, String path, String operator, Object arg ) throws QueryParserException
    {
        Property property = getProperty( schema, path );

        if ( property == null )
        {
            throw new QueryParserException( "Unknown path property: " + path );
        }

        switch ( operator )
        {
            case "eq":
            {
                return Restrictions.eq( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "!eq":
            {
                return Restrictions.ne( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "ne":
            {
                return Restrictions.ne( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "neq":
            {
                return Restrictions.ne( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "gt":
            {
                return Restrictions.gt( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "lt":
            {
                return Restrictions.lt( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "gte":
            {
                return Restrictions.ge( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "ge":
            {
                return Restrictions.ge( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "lte":
            {
                return Restrictions.le( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "le":
            {
                return Restrictions.le( path, QueryUtils.parseValue( property.getKlass(), arg ) );
            }
            case "like":
            {
                return Restrictions.like( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.ANYWHERE );
            }
            case "!like":
            {
                return Restrictions.notLike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.ANYWHERE );
            }
            case "$like":
            {
                return Restrictions.like( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.START );
            }
            case "!$like":
            {
                return Restrictions.notLike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.START );
            }
            case "like$":
            {
                return Restrictions.like( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.END );
            }
            case "!like$":
            {
                return Restrictions.notLike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.END );
            }
            case "ilike":
            {
                return Restrictions.ilike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.ANYWHERE );
            }
            case "!ilike":
            {
                return Restrictions.notIlike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.ANYWHERE );
            }
            case "startsWith":
            case "$ilike":
            {
                return Restrictions.ilike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.START );
            }
            case "!$ilike":
            {
                return Restrictions.notIlike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.START );
            }
            case "token":
            {
                return Restrictions.token( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.START );
            }
            case "!token":
            {
                return Restrictions.notToken( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.START );
            }
            case "endsWith":
            case "ilike$":
            {
                return Restrictions.ilike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.END );
            }
            case "!ilike$":
            {
                return Restrictions.notIlike( path, QueryUtils.parseValue( property.getKlass(), arg ), MatchMode.END );
            }
            case "in":
            {
                Collection<?> values = null;

                if ( property.isCollection() )
                {
                    values = QueryUtils.parseValue( Collection.class, property.getItemKlass(), arg );
                }
                else
                {
                    values = QueryUtils.parseValue( Collection.class, property.getKlass(), arg );
                }

                if ( values == null || values.isEmpty() )
                {
                    throw new QueryParserException( "Invalid argument `" + arg + "` for in operator." );
                }

                return Restrictions.in( path, values );
            }
            case "!in":
            {
                Collection<?> values = null;

                if ( property.isCollection() )
                {
                    values = QueryUtils.parseValue( Collection.class, property.getItemKlass(), arg );
                }
                else
                {
                    values = QueryUtils.parseValue( Collection.class, property.getKlass(), arg );
                }

                if ( values == null || values.isEmpty() )
                {
                    throw new QueryParserException( "Invalid argument `" + arg + "` for in operator." );
                }

                return Restrictions.notIn( path, values );
            }
            case "null":
            {
                return Restrictions.isNull( path );
            }
            case "!null":
            {
                return Restrictions.isNotNull( path );
            }
            case "empty":
            {
                return Restrictions.isEmpty( path );
            }
            default:
            {
                throw new QueryParserException( "`" + operator + "` is not a valid operator." );
            }
        }
    }

    @Override
    public Property getProperty( Schema schema, String path ) throws QueryParserException
    {
        String[] paths = path.split( "\\." );
        Schema currentSchema = schema;
        Property currentProperty = null;

        for ( int i = 0; i < paths.length; i++ )
        {
            if ( !currentSchema.haveProperty( paths[i] ) )
            {
                return null;
            }

            currentProperty = currentSchema.getProperty( paths[i] );

            if ( currentProperty == null )
            {
                throw new QueryParserException( "Unknown path property: " + paths[i] + " (" + path + ")" );
            }

            if ( (currentProperty.isSimple() && !currentProperty.isCollection()) && i != (paths.length - 1) )
            {
                throw new QueryParserException( "Simple type was found before finished parsing path expression, please check your path string." );
            }

            if ( currentProperty.isCollection() )
            {
                currentSchema = schemaService.getDynamicSchema( currentProperty.getItemKlass() );
            }
            else
            {
                currentSchema = schemaService.getDynamicSchema( currentProperty.getKlass() );
            }
        }

        return currentProperty;
    }
}
