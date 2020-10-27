package com.mass3d.preheat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import org.springframework.stereotype.Component;

/**
 * This component is responsible for fetching all the unique attributes for a {@link IdentifiableObject} subclass.
 *
 */
@Component
public class SchemaToDataFetcher
{
    protected static final Log log = LogFactory.getLog( SchemaToDataFetcher.class );

    private final SessionFactory sessionFactory;

    public SchemaToDataFetcher( SessionFactory sessionFactory )
    {
        checkNotNull( sessionFactory );

        this.sessionFactory = sessionFactory;
    }

    /**
     * Executes a read-only query for the given Schema class and fetches only the fields
     * marked as "unique".
     *
     * @param schema a {@link Schema}
     * @return a List of objects corresponding to the "klass" of the given Schema
     */
    public List<? extends IdentifiableObject> fetch( Schema schema )
    {
        if ( schema == null )
        {
            return Collections.emptyList();
        }

        return mapUniqueFields(schema);
    }

    @SuppressWarnings("unchecked")
    private List<? extends IdentifiableObject> mapUniqueFields( Schema schema )
    {
        List<Property> uniqueProperties = schema.getUniqueProperties();

        List objects = new ArrayList();

        if ( !uniqueProperties.isEmpty() )
        {
            final String fields = extractUniqueFields( uniqueProperties );

            objects = sessionFactory.getCurrentSession()
                .createQuery( "SELECT " + fields + " from " + schema.getKlass().getSimpleName() )
                .setReadOnly( true )
                .getResultList();
        }

        // Hibernate returns a List containing an array of Objects if multiple columns are used in the query
        // or a "simple" List if only one columns is used in the query
        return uniqueProperties.size() == 1 ? handleSingleColumn( objects, uniqueProperties, schema )
            : handleMultipleColumn( objects, uniqueProperties, schema );

    }

    private List<IdentifiableObject> handleMultipleColumn( List<Object[]> objects, List<Property> uniqueProperties,
        Schema schema )
    {
        List<IdentifiableObject> resultsObjects = new ArrayList<>( objects.size() );

        for ( Object[] uniqueValuesArray : objects )
        {
            Map<String, Object> valuesMap = new HashMap<>();

            for ( int i = 0; i < uniqueValuesArray.length; i++ )
            {
                valuesMap.put( uniqueProperties.get( i ).getFieldName(), uniqueValuesArray[i] );
            }

            addToResult( schema, valuesMap, resultsObjects );
        }

        return resultsObjects;
    }

    private List<IdentifiableObject> handleSingleColumn( List<Object> objects, List<Property> uniqueProperties,
        Schema schema )
    {
        List<IdentifiableObject> resultsObjects = new ArrayList<>( objects.size() );
        for ( Object uniqueValue : objects )
        {
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put( uniqueProperties.get( 0 ).getFieldName(), uniqueValue );

            addToResult( schema, valuesMap, resultsObjects );
        }

        return resultsObjects;
    }

    private void addToResult( Schema schema, Map<String, Object> valuesMap, List<IdentifiableObject> resultsObjects )
    {
        try
        {
            IdentifiableObject identifiableObject = (IdentifiableObject) schema.getKlass().newInstance();
            BeanUtils.populate( identifiableObject, valuesMap );
            resultsObjects.add( identifiableObject );
        }
        catch ( Exception e )
        {
            log.error( "Error during dynamic population of object type: " + schema.getKlass().getSimpleName(), e );
        }
    }

    private String extractUniqueFields( List<Property> uniqueProperties )
    {
        return uniqueProperties.stream().map( Property::getFieldName ).collect( Collectors.joining( "," ) );
    }
}

