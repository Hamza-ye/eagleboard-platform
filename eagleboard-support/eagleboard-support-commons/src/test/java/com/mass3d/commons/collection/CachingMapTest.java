package com.mass3d.commons.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import org.junit.Test;

public class CachingMapTest
{
    private static final Callable<Animal> FN = () -> null;
    
    @Test
    public void testGetLoad()
    {
        Set<Animal> animals = new HashSet<>();
        animals.add( new Animal( 1, "horse" ) );
        animals.add( new Animal( 2, "dog" ) );
        animals.add( new Animal( 3, "cat" ) );

        CachingMap<Integer, Animal> cache = new CachingMap<Integer, Animal>().load( animals, a -> a.getId() );

        assertEquals( 3, cache.size() );
        assertEquals( "horse", cache.get( 1, FN ).getName() );
        assertEquals( "dog", cache.get( 2, FN ).getName() );
        assertEquals( "cat", cache.get( 3, FN ).getName() );        
        assertFalse( cache.containsKey( 5 ) );
    }
        
    @Test
    public void testLoadWithNull()
    {
        Set<Animal> animals = new HashSet<>();
        animals.add( new Animal( 1, "horse" ) );
        animals.add( new Animal( 2, null ) );
        animals.add( new Animal( 3, "cat" ) );

        CachingMap<String, Animal> cache = new CachingMap<String, Animal>().load( animals, a -> a.getName() );

        assertEquals( 2, cache.size() );
        assertEquals( 1, cache.get( "horse", FN ).getId() );       
        assertFalse( cache.containsKey( "dog" ) );
    }
    
    @Test
    public void testCacheHitMissCount()
    {
        CachingMap<Integer, Animal> cache = new CachingMap<Integer, Animal>();
        
        cache.put( 1, new Animal( 1, "horse" ) );
        cache.put( 2, new Animal( 2, "dog" ) );
        
        cache.get( 1, FN ); // Hit
        cache.get( 1, FN ); // Hit
        cache.get( 1, FN ); // Hit
        cache.get( 2, FN ); // Hit
        cache.get( 2, FN ); // Hit
        cache.get( 3, FN ); // Miss
        cache.get( 3, FN ); // Hit null-value
        cache.get( 4, FN ); // Miss
        cache.get( 4, FN ); // Hit null-value
        cache.get( 4, FN ); // Hit null-value
        cache.get( 5, FN ); // Miss
        
        assertEquals( 8, cache.getCacheHitCount() );
        assertEquals( 3, cache.getCacheMissCount() );
    }

    @Test
    public void testCacheLoadCount()
    {
        Set<Animal> animals = new HashSet<>();
        animals.add( new Animal( 1, "horse" ) );
        animals.add( new Animal( 2, "dog" ) );
        animals.add( new Animal( 3, "cat" ) );

        CachingMap<Integer, Animal> cache = new CachingMap<Integer, Animal>();
        
        assertEquals( 0, cache.getCacheLoadCount() );
        
        cache.load( animals, a -> a.getId() );

        assertEquals( 1, cache.getCacheLoadCount() );
    }

    @Test
    public void testIsCacheLoaded()
    {
        Set<Animal> animals = new HashSet<>();
        animals.add( new Animal( 1, "horse" ) );
        animals.add( new Animal( 3, "cat" ) );

        CachingMap<Integer, Animal> cache = new CachingMap<Integer, Animal>();
        
        assertFalse( cache.isCacheLoaded() );
        
        cache.load( animals, a -> a.getId() );

        assertTrue( cache.isCacheLoaded() );
    }

    /**
     * Only first get should create a miss, entry should be cached
     * event if value is null.
     */
    @Test
    public void testGetCacheNullValue()
    {
        Set<Animal> animals = new HashSet<>();
        animals.add( new Animal( 1, "horse" ) );
        animals.add( new Animal( 2, "dog" ) );
        
        CachingMap<Integer, Animal> cache = new CachingMap<Integer, Animal>().load( animals, a -> a.getId() );
        
        assertNull( cache.get( 5, FN ) ); // Miss
        assertNull( cache.get( 5, FN ) ); // Hit null-value
        assertNull( cache.get( 5, FN ) ); // Hit null-value
        
        assertEquals( 1, cache.getCacheMissCount() );
        assertEquals( 2, cache.getCacheHitCount() );
    }
    
    private class Animal
    {
        private int id;
        private String name;
        
        public Animal( int id, String name )
        {
            this.id = id;
            this.name = name;
        }
        
        public int getId()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }
    }
}
