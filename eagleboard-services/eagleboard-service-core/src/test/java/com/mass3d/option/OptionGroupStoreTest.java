package com.mass3d.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OptionGroupStoreTest
    extends DhisSpringTest
{
    @Autowired
    private OptionGroupStore store;

    private OptionGroup optionGroupA;

    private OptionGroup optionGroupB;

    private OptionGroup optionGroupC;

    public void setUpTest()
    {
        optionGroupA = new OptionGroup( "OptionGroupA" );
        optionGroupA.setShortName( "ShortNameA" );

        optionGroupB = new OptionGroup( "OptionGroupB" );
        optionGroupB.setShortName( "ShortNameB" );

        optionGroupC = new OptionGroup( "OptionGroupC" );
        optionGroupC.setShortName( "ShortNameC" );
    }

    @Test
    public void tetAddOptionGroup()
    {
        store.save( optionGroupA );
        long idA = optionGroupA.getId();
        store.save( optionGroupB );
        long idB = optionGroupB.getId();
        store.save( optionGroupC );
        long idC = optionGroupC.getId();

        assertEquals( optionGroupA, store.get( idA ));
        assertEquals( optionGroupB, store.get( idB ));
        assertEquals( optionGroupC, store.get( idC ));
    }

    @Test
    public void testDeleteOptionGroup()
    {
        store.save( optionGroupA );
        long idA = optionGroupA.getId();
        store.save( optionGroupB );
        long idB = optionGroupB.getId();

        store.delete( optionGroupA );

        assertNull( store.get( idA ) );
        assertNotNull( store.get( idB ) );
    }

    @Test
    public void genericGetAll()
    {
        store.save( optionGroupA );
        store.save( optionGroupB );
        store.save( optionGroupC );

        Collection<OptionGroup> objects = store.getAll();

        assertNotNull( objects );
        assertEquals( 3, objects.size() );
        assertTrue( objects.contains( optionGroupA ) );
        assertTrue( objects.contains( optionGroupB ) );
        assertTrue( objects.contains( optionGroupC ) );
    }
}
