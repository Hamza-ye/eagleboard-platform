package com.mass3d.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import com.mass3d.node.annotation.NodeComplex;
import com.mass3d.node.annotation.NodeSimple;
import org.junit.Before;
import org.junit.Test;

class Value
{
    @NodeSimple
    private String value;
}

class ComplexFields
{
    @NodeComplex
    private Value property;

    @NodeComplex( value = "renamedProperty" )
    private Value propertyToBeRenamed;

    @NodeComplex( isReadable = true, isWritable = false )
    private Value readOnly;

    @NodeComplex( isReadable = false, isWritable = true )
    private Value writeOnly;

    @NodeComplex( namespace = "http://ns.example.org" )
    private Value propertyWithNamespace;

    public Value getProperty()
    {
        return property;
    }

    public void setProperty( Value property )
    {
        this.property = property;
    }
}

public class FieldComplexNodePropertyIntrospectorServiceTest
{
    private Map<String, Property> propertyMap;

    @Before
    public void setup()
    {
        propertyMap = new NodePropertyIntrospectorService().scanClass( ComplexFields.class );
    }

    @Test
    public void testContainsKey()
    {
        assertTrue( propertyMap.containsKey( "property" ) );
        assertFalse( propertyMap.containsKey( "propertyToBeRenamed" ) );
        assertTrue( propertyMap.containsKey( "renamedProperty" ) );
        assertTrue( propertyMap.containsKey( "readOnly" ) );
        assertTrue( propertyMap.containsKey( "writeOnly" ) );
        assertTrue( propertyMap.containsKey( "propertyWithNamespace" ) );
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
