package com.mass3d.query;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import com.mass3d.dataelement.DataElement;
import com.mass3d.query.operators.MatchMode;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import org.junit.Test;

public class QueryTest
{
    private Property createProperty( Class<?> klazz, String name, boolean simple, boolean persisted )
    {
        Property property = new Property( klazz );
        property.setName( name );
        property.setFieldName( name );
        property.setSimple( simple );
        property.setPersisted( persisted );

        return property;
    }

    private Schema createSchema()
    {
        Schema schema = new Schema( DataElement.class, "dataElement", "dataElements" );
        schema.addProperty( createProperty( String.class, "id", true, true ) );
        schema.addProperty( createProperty( String.class, "name", true, true ) );
        schema.addProperty( createProperty( String.class, "code", true, true ) );
        schema.addProperty( createProperty( Date.class, "created", true, true ) );
        schema.addProperty( createProperty( Date.class, "lastUpdated", true, true ) );

        schema.addProperty( createProperty( Integer.class, "int", true, true ) );
        schema.addProperty( createProperty( Long.class, "long", true, true ) );
        schema.addProperty( createProperty( Float.class, "float", true, true ) );
        schema.addProperty( createProperty( Double.class, "double", true, true ) );

        return schema;
    }

    @Test
    public void validRestrictionParameters()
    {
        Query query = Query.from( createSchema() );
        query.add( Restrictions.eq( "id", "anc" ) );
        query.add( Restrictions.like( "name", "anc", MatchMode.ANYWHERE ) );
        query.add( Restrictions.eq( "code", "anc" ) );

        assertEquals( 3, query.getCriterions().size() );
    }
}
