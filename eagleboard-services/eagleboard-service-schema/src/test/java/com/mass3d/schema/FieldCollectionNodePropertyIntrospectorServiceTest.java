package com.mass3d.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.mass3d.node.annotation.NodeCollection;
import com.mass3d.node.annotation.NodeRoot;
import com.mass3d.node.annotation.NodeSimple;
import org.junit.Before;
import org.junit.Test;

@NodeRoot( value = "collectionItem" ) class Item
{
    @NodeSimple
    private String value;
}

class CollectionFields
{
    @NodeCollection
    private List<String> property = new ArrayList<>();

    @NodeCollection( value = "renamedProperty" )
    private List<String> propertyToBeRenamed = new ArrayList<>();

    @NodeCollection( isReadable = true, isWritable = false )
    private List<String> readOnly = new ArrayList<>();

    @NodeCollection( isReadable = false, isWritable = true )
    private List<String> writeOnly = new ArrayList<>();

    @NodeCollection( namespace = "http://ns.example.org" )
    private List<String> propertyWithNamespace = new ArrayList<>();

    public List<String> getProperty()
    {
        return property;
    }

    public void setProperty( List<String> property )
    {
        this.property = property;
    }

    @NodeCollection
    private List<Item> items1 = new ArrayList<>();

    @NodeCollection( value = "items", itemName = "item" )
    private List<Item> items2 = new ArrayList<>();
}

public class FieldCollectionNodePropertyIntrospectorServiceTest
{
    private Map<String, Property> propertyMap;

    @Before
    public void setup()
    {
        propertyMap = new NodePropertyIntrospectorService().scanClass( CollectionFields.class );
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
        assertTrue( propertyMap.containsKey( "items1" ) );
        assertTrue( propertyMap.containsKey( "items" ) );
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
        assertEquals( "items2", propertyMap.get( "items" ).getFieldName() );
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

    @Test
    public void testItemName()
    {
        assertEquals( "collectionItem", propertyMap.get( "items1" ).getName() );
        assertEquals( "items1", propertyMap.get( "items1" ).getCollectionName() );

        assertEquals( "item", propertyMap.get( "items" ).getName() );
        assertEquals( "items", propertyMap.get( "items" ).getCollectionName() );
    }

    @Test
    public void testItemKlass()
    {
        assertTrue( Item.class.equals( propertyMap.get( "items1" ).getItemKlass() ) );
    }
}
