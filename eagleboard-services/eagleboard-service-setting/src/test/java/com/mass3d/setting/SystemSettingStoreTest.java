package com.mass3d.setting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemSettingStoreTest
    extends DhisSpringTest
{
    @Autowired
    private SystemSettingStore systemSettingStore;

    private SystemSetting settingA;
    private SystemSetting settingB;
    private SystemSetting settingC;

    @Override
    public void setUpTest()
        throws Exception
    {
        settingA = new SystemSetting();
        settingA.setName( "Setting1" );
        settingA.setDisplayValue( "Value1" );

        settingB = new SystemSetting();
        settingB.setName( "Setting2" );
        settingB.setDisplayValue( "Value2" );

        settingC = new SystemSetting();
        settingC.setName( "Setting3" );
        settingC.setDisplayValue( "Value3" );
    }

    @Test
    public void testAddSystemSetting()
    {
        systemSettingStore.save( settingA );
        long idA = settingA.getId();
        systemSettingStore.save( settingB );
        systemSettingStore.save( settingC );

        settingA = systemSettingStore.get( idA );
        assertNotNull( settingA );
        assertEquals( "Setting1", settingA.getName() );
        assertEquals( "Value1", settingA.getDisplayValue() );

        settingA.setDisplayValue( "Value1.1" );
        systemSettingStore.update( settingA );

        settingA = systemSettingStore.get( idA );
        assertNotNull( settingA );
        assertEquals( "Setting1", settingA.getName() );
        assertEquals( "Value1.1", settingA.getDisplayValue() );
    }

    @Test
    public void testUpdateSystemSetting()
    {
        systemSettingStore.save( settingA );
        long id = settingA.getId();

        settingA = systemSettingStore.get( id );
        
        assertEquals( "Value1", settingA.getDisplayValue() );
        
        settingA.setDisplayValue( "Value2" );
        
        systemSettingStore.update( settingA );

        settingA = systemSettingStore.get( id );
        
        assertEquals( "Value2", settingA.getDisplayValue() );
    }

    @Test
    public void testDeleteSystemSetting()
    {
        systemSettingStore.save( settingA );
        long idA = settingA.getId();
        systemSettingStore.save( settingB );
        long idB = settingB.getId();
        systemSettingStore.save( settingC );

        systemSettingStore.delete( settingA );

        assertNull( systemSettingStore.get( idA ) );
        assertNotNull( systemSettingStore.get( idB ) );
    }

    @Test
    public void testGetSystemSetting()
    {
        systemSettingStore.save( settingA );
        systemSettingStore.save( settingB );

        SystemSetting s = systemSettingStore.getByName( "Setting1" );
        assertNotNull( s );
        assertEquals( "Setting1", s.getName() );
        assertEquals( "Value1", s.getDisplayValue() );

        s = systemSettingStore.getByName( "Setting3" );
        assertNull( s );
    }

    @Test
    public void testGetAllSystemSettings()
    {
        List<SystemSetting> settings = systemSettingStore.getAll();
        assertNotNull( settings );
        assertEquals( 0, settings.size() );

        systemSettingStore.save( settingA );
        systemSettingStore.save( settingB );

        settings = systemSettingStore.getAll();
        assertNotNull( settings );
        assertEquals( 2, settings.size() );
    }
}
