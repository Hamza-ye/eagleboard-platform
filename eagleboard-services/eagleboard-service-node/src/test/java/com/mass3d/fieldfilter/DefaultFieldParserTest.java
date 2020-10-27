package com.mass3d.fieldfilter;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link DefaultFieldParser}.
 *
 */
public class DefaultFieldParserTest
{
    private final DefaultFieldParser parser = new DefaultFieldParser();

    @Test
    public void parseWithTransformer()
    {
        final FieldMap fieldMap = parser.parse( "id,organisationUnits~pluck" );
        Assert.assertEquals( 2, fieldMap.size() );
        Assert.assertTrue( fieldMap.containsKey( "id" ) );
        Assert.assertTrue( fieldMap.containsKey( "organisationUnits~pluck" ) );
    }

    @Test
    public void parseWithTransformerArgAndFields()
    {
        final FieldMap fieldMap = parser.parse( "id,organisationUnits~pluck(name)[id,name]" );
        Assert.assertEquals( 2, fieldMap.size() );
        Assert.assertTrue( fieldMap.containsKey( "id" ) );
        Assert.assertTrue( fieldMap.containsKey( "organisationUnits~pluck(name)" ) );

        final FieldMap innerFieldMap = fieldMap.get( "organisationUnits~pluck(name)" );
        Assert.assertTrue( innerFieldMap.containsKey( "id" ) );
        Assert.assertTrue( innerFieldMap.containsKey( "name" ) );
    }
}