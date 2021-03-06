package com.mass3d.keyjsonvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class KeyJsonValueStoreTest extends DhisSpringTest
{

    @Autowired
    private KeyJsonValueStore keyJsonValueStore;

    @Test
    public void testAddKeyJsonValue()
    {
        KeyJsonValue keyJsonValue = new KeyJsonValue();
        keyJsonValue.setNamespace( "A" );
        keyJsonValue.setKey( "1" );

        keyJsonValueStore.save( keyJsonValue );
        long id = keyJsonValue.getId();

        assertNotNull( keyJsonValue );
        assertEquals( keyJsonValue, keyJsonValueStore.get( id ) );
    }

    @Test
    public void testAddKeyJsonValuesAndGetNamespaces()
    {
        KeyJsonValue keyJsonValueA = new KeyJsonValue();
        keyJsonValueA.setNamespace( "A" );
        keyJsonValueA.setKey( "1" );
        keyJsonValueStore.save( keyJsonValueA );

        KeyJsonValue keyJsonValueB = new KeyJsonValue();
        keyJsonValueB.setNamespace( "B" );
        keyJsonValueB.setKey( "1" );
        keyJsonValueStore.save( keyJsonValueB );

        List<String> list = keyJsonValueStore.getNamespaces();

        assertTrue( list.contains( "A" ) );
        assertTrue( list.contains( "B" ) );
    }

    @Test
    public void testAddKeyJsonValuesAndGetKeysFromNamespace()
    {
        KeyJsonValue keyJsonValueA = new KeyJsonValue();
        keyJsonValueA.setNamespace( "A" );
        keyJsonValueA.setKey( "1" );
        keyJsonValueStore.save( keyJsonValueA );

        KeyJsonValue keyJsonValueB = new KeyJsonValue();
        keyJsonValueB.setNamespace( "A" );
        keyJsonValueB.setKey( "2" );
        keyJsonValueStore.save( keyJsonValueB );

        List<String> list = keyJsonValueStore.getKeysInNamespace( "A" );

        assertTrue( list.contains( "1" ) );
        assertTrue( list.contains( "2" ) );
    }

    @Test
    public void testAddKeyJsonValueAndGetKeyJsonValue()
    {
        KeyJsonValue keyJsonValueA = new KeyJsonValue();
        keyJsonValueA.setNamespace( "A" );
        keyJsonValueA.setKey( "1" );
        keyJsonValueStore.save( keyJsonValueA );

        assertEquals( keyJsonValueStore.getKeyJsonValue( "A", "1" ), keyJsonValueA );
    }

    @Test
    public void testGetKeyJsonValuesByNamespace()
    {
        KeyJsonValue keyJsonValueA = new KeyJsonValue();
        keyJsonValueA.setNamespace( "A" );
        keyJsonValueA.setKey( "1" );
        keyJsonValueStore.save( keyJsonValueA );

        KeyJsonValue keyJsonValueB = new KeyJsonValue();
        keyJsonValueB.setNamespace( "A" );
        keyJsonValueB.setKey( "2" );
        keyJsonValueStore.save( keyJsonValueB );

        KeyJsonValue keyJsonValueC = new KeyJsonValue();
        keyJsonValueC.setNamespace( "A" );
        keyJsonValueC.setKey( "3" );
        keyJsonValueStore.save( keyJsonValueC );


        List<KeyJsonValue> list = keyJsonValueStore.getKeyJsonValueByNamespace( "A" );

        assertTrue(list.contains( keyJsonValueA ));
        assertTrue(list.contains( keyJsonValueB ));
        assertTrue(list.contains( keyJsonValueC ));
    }
}
