package com.mass3d.setting;

import static com.mass3d.setting.SettingKey.*;
import static com.mass3d.setting.SettingKey.APPLICATION_INTRO;
import static com.mass3d.setting.SettingKey.APPLICATION_NOTIFICATION;
import static com.mass3d.setting.SettingKey.APPLICATION_TITLE;
import static com.mass3d.setting.SettingKey.EMAIL_PORT;
import static com.mass3d.setting.SettingKey.HELP_PAGE_LINK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemSettingManagerTest
    extends DhisSpringTest
{
    @Autowired
    private SystemSettingManager systemSettingManager;

    @Override
    public void setUpTest()
    {
        systemSettingManager.invalidateCache();
    }

    @Test
    public void testSaveGetSetting()
    {
        systemSettingManager.saveSystemSetting( APPLICATION_INTRO, "valueA" );
        systemSettingManager.saveSystemSetting( APPLICATION_NOTIFICATION, "valueB" );

        assertEquals( "valueA", systemSettingManager.getSystemSetting( APPLICATION_INTRO ) );
        assertEquals( "valueB", systemSettingManager.getSystemSetting( APPLICATION_NOTIFICATION ) );
    }

    @Test
    public void testSaveGetSettingWithDefault()
    {
        assertEquals( EMAIL_PORT.getDefaultValue(), systemSettingManager.getSystemSetting( EMAIL_PORT ) );
    }

    @Test
    public void testSaveGetDeleteSetting()
    {
        assertNull( systemSettingManager.getSystemSetting( APPLICATION_INTRO ) );
        assertEquals( HELP_PAGE_LINK.getDefaultValue(), systemSettingManager.getSystemSetting( HELP_PAGE_LINK ) );

        systemSettingManager.saveSystemSetting( APPLICATION_INTRO, "valueA" );
        systemSettingManager.saveSystemSetting( HELP_PAGE_LINK, "valueB" );

        assertEquals( "valueA", systemSettingManager.getSystemSetting( APPLICATION_INTRO ) );
        assertEquals( "valueB", systemSettingManager.getSystemSetting( HELP_PAGE_LINK ) );

        systemSettingManager.deleteSystemSetting( APPLICATION_INTRO );

        assertNull( systemSettingManager.getSystemSetting( APPLICATION_INTRO ) );
        assertEquals( "valueB", systemSettingManager.getSystemSetting( HELP_PAGE_LINK ) );

        systemSettingManager.deleteSystemSetting( HELP_PAGE_LINK );

        assertNull( systemSettingManager.getSystemSetting( APPLICATION_INTRO ) );
        assertEquals( HELP_PAGE_LINK.getDefaultValue(), systemSettingManager.getSystemSetting( HELP_PAGE_LINK ) );
    }

    @Test
    public void testGetAllSystemSettings()
    {
        systemSettingManager.saveSystemSetting( APPLICATION_INTRO, "valueA" );
        systemSettingManager.saveSystemSetting( APPLICATION_NOTIFICATION, "valueB" );

        List<SystemSetting> settings = systemSettingManager.getAllSystemSettings();

        assertNotNull( settings );
        assertEquals( 2, settings.size() );
    }

    @Test
    public void testGetSystemSettingsAsMap()
    {
        systemSettingManager.saveSystemSetting( SettingKey.APPLICATION_TITLE, "valueA" );
        systemSettingManager.saveSystemSetting( SettingKey.APPLICATION_NOTIFICATION, "valueB" );

        Map<String, Serializable> settingsMap = systemSettingManager.getSystemSettingsAsMap();

        assertTrue( settingsMap.containsKey( SettingKey.APPLICATION_TITLE.getName() ) );
        assertTrue( settingsMap.containsKey( SettingKey.APPLICATION_NOTIFICATION.getName() ) );

        assertEquals( "valueA", settingsMap.get( SettingKey.APPLICATION_TITLE.getName() ) );
        assertEquals( "valueB", settingsMap.get( SettingKey.APPLICATION_NOTIFICATION.getName() ) );
        assertEquals( SettingKey.CACHE_STRATEGY.getDefaultValue(), settingsMap.get( SettingKey.CACHE_STRATEGY.getName() ) );
        assertEquals( SettingKey.CREDENTIALS_EXPIRES.getDefaultValue(), settingsMap.get( SettingKey.CREDENTIALS_EXPIRES.getName() ) );
    }

    @Test
    public void testGetSystemSettingsByCollection()
    {
        Collection<SettingKey> keys = ImmutableSet
            .of( SettingKey.APPLICATION_TITLE, SettingKey.APPLICATION_INTRO );

        systemSettingManager.saveSystemSetting( APPLICATION_TITLE, "valueA" );
        systemSettingManager.saveSystemSetting( APPLICATION_INTRO, "valueB" );

        assertEquals( systemSettingManager.getSystemSettings( keys ).size(), 2 );
    }

    @Test
    public void testIsConfidential()
    {
        assertEquals( SettingKey.EMAIL_PASSWORD.isConfidential(), true );
        assertEquals( systemSettingManager.isConfidential( SettingKey.EMAIL_PASSWORD.getName() ), true );

        assertEquals( SettingKey.EMAIL_HOST_NAME.isConfidential(), false );
        assertEquals( systemSettingManager.isConfidential( SettingKey.EMAIL_HOST_NAME.getName() ), false);
    }
}
