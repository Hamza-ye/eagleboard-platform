package com.mass3d.system.util;

import static com.mass3d.system.util.ReflectionUtils.*;
import static com.mass3d.system.util.ReflectionUtils.getClassName;
import static com.mass3d.system.util.ReflectionUtils.getId;
import static com.mass3d.system.util.ReflectionUtils.getProperty;
import static com.mass3d.system.util.ReflectionUtils.isCollection;
import static com.mass3d.system.util.ReflectionUtils.setProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import com.mass3d.analytics.AggregationType;
import com.mass3d.dataelement.DataElement;
import org.junit.Before;
import org.junit.Test;

public class ReflectionUtilsTest
{
    private DataElement dataElementA;

    @Before
    public void before()
    {
        dataElementA = new DataElement();
        dataElementA.setId( 8 );
        dataElementA.setName( "NameA" );
        dataElementA.setAggregationType( AggregationType.SUM );
    }

    @Test
    public void testGetId()
    {
        assertEquals( 8, getId( dataElementA ) );
    }

    @Test
    public void testGetProperty()
    {
        assertEquals( "NameA", getProperty( dataElementA, "name" ) );
        assertNull( getProperty( dataElementA, "color" ) );
    }

    @Test
    public void testSetProperty()
    {
        setProperty( dataElementA, "shortName", "ShortNameA" );

        assertEquals( "ShortNameA", dataElementA.getShortName() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetPropertyException()
    {
        setProperty( dataElementA, "color", "Blue" );
    }

    @Test
    public void testGetClassName()
    {
        assertEquals( "DataElement", getClassName( dataElementA ) );
    }

    @Test
    public void testIsCollection()
    {
        List<Object> colA = new ArrayList<>();
        Collection<DataElement> colB = new HashSet<>();
        Collection<DataElement> colC = new ArrayList<>();

        assertTrue( isCollection( colA ) );
        assertTrue( isCollection( colB ) );
        assertTrue( isCollection( colC ) );
        assertFalse( isCollection( dataElementA ) );
    }
}
