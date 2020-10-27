package com.mass3d.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import com.mass3d.node.annotation.NodeSimple;
import org.junit.Before;
import org.junit.Test;

class SimpleFields
{
    @NodeSimple( isAttribute = false )
    private String property;

    @NodeSimple( value = "renamedProperty", isAttribute = true )
    private String propertyToBeRenamed;

    @NodeSimple( isReadable = true, isWritable = false )
    private String readOnly;

    @NodeSimple( isReadable = false, isWritable = true )
    private boolean writeOnly;

    @NodeSimple( namespace = "http://ns.example.org" )
    private String propertyWithNamespace;

    public String getProperty()
    {
        return property;
    }

    public void setProperty( String property )
    {
        this.property = property;
    }
}

public class FieldSimpleNodePropertyIntrospectorServiceTest
{
    private Map<String, Property> propertyMap;

    @Before
    public void setup()
    {
        propertyMap = new NodePropertyIntrospectorService().scanClass( SimpleFields.class );
    }

    @Test
    public void testContainsKey()
    {
        assertTrue( propertyMap.containsKey( "property" ) );
        assertFalse( propertyMap.containsKey( "propertyToBeRenamed" ) );
        assertTrue( propertyMap.containsKey( "renamedProperty" ) );
        assertTrue( propertyMap.containsKey( "readOnly" ) );
        assertTrue( propertyMap.containsKey( "writeOnly" ) );
    }

    @Test
    public void testAttribute()
    {
        assertFalse( propertyMap.get( "property" ).isAttribute() );
        assertTrue( propertyMap.get( "renamedProperty" ).isAttribute() );
    }

    @Test
    public void testReadWrite()
    {
        assertTrue( propertyMap.get( "readOnly" ).isReadable() );
        assertFalse( propertyMap.get( "readOnly" ).isWritable() );

        assertFalse( propertyMap.get( "writeOnly" ).isReadable() );
        assertTrue( propertyMap.get( "writeOnly" ).isWritable() );

        assertNull( propertyMap.get( "readOnly" ).getSetterMethod() );
        assertNull( propertyMap.get( "writeOnly" ).getGetterMethod() );
    }

    @Test
    public void testFieldName()
    {
        assertEquals( "property", propertyMap.get( "property" ).getFieldName() );
        assertEquals( "propertyToBeRenamed", propertyMap.get( "renamedProperty" ).getFieldName() );
    }

    @Test
    public void testNamespace()
    {
        assertEquals( "http://ns.example.org", propertyMap.get( "propertyWithNamespace" ).getNamespace() );
    }

    @Test
    public void testGetter()
    {
        assertNotNull( propertyMap.get( "property" ).getGetterMethod() );
        assertNull( propertyMap.get( "renamedProperty" ).getGetterMethod() );
    }

    @Test
    public void testSetter()
    {
        assertNotNull( propertyMap.get( "property" ).getSetterMethod() );
        assertNull( propertyMap.get( "renamedProperty" ).getSetterMethod() );
    }
}
