package com.mass3d.keyjsonvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class KeyJsonValueServiceTest
    extends DhisSpringTest
{
    private final String namespace = "DOGS";
    
    @Autowired
    private KeyJsonValueService keyJsonValueService;
    
    @Test
    public void testAddGetObject()
    {
        Dog dogA = new Dog( "1", "Fido", "Brown" );
        Dog dogB = new Dog( "2", "Aldo", "Black" );
        
        keyJsonValueService.addValue( namespace, dogA.getId(), dogA );
        keyJsonValueService.addValue( namespace, dogB.getId(), dogB );
        
        dogA = keyJsonValueService.getValue( namespace, dogA.getId(), Dog.class );
        dogB = keyJsonValueService.getValue( namespace, dogB.getId(), Dog.class );
        
        assertNotNull( dogA );        
        assertEquals( "1", dogA.getId() );
        assertEquals( "Fido", dogA.getName() );
        assertNotNull( dogB );
        assertEquals( "2", dogB.getId() );
        assertEquals( "Aldo", dogB.getName() );
    }

    @Test
    public void testAddUpdateObject()
    {
        Dog dogA = new Dog( "1", "Fido", "Brown" );
        Dog dogB = new Dog( "2", "Aldo", "Black" );
        
        keyJsonValueService.addValue( namespace, dogA.getId(), dogA );
        keyJsonValueService.addValue( namespace, dogB.getId(), dogB );

        dogA = keyJsonValueService.getValue( namespace, dogA.getId(), Dog.class );
        dogB = keyJsonValueService.getValue( namespace, dogB.getId(), Dog.class );

        assertEquals( "Fido", dogA.getName() );
        assertEquals( "Aldo", dogB.getName() );
        
        dogA.setName( "Lilly" );
        dogB.setName( "Teddy" );
        
        keyJsonValueService.updateValue( namespace, dogA.getId(), dogA );
        keyJsonValueService.updateValue( namespace, dogB.getId(), dogB );

        dogA = keyJsonValueService.getValue( namespace, dogA.getId(), Dog.class );
        dogB = keyJsonValueService.getValue( namespace, dogB.getId(), Dog.class );

        assertEquals( "Lilly", dogA.getName() );
        assertEquals( "Teddy", dogB.getName() );
    }
}
