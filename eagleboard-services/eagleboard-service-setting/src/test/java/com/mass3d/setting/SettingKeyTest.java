package com.mass3d.setting;

import com.mass3d.common.DisplayProperty;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link SettingKey}.
 *
 */
public class SettingKeyTest
{
    @Test
    public void getAsRealClassEnum()
    {
        Assert.assertSame( DisplayProperty.SHORTNAME, SettingKey.getAsRealClass( SettingKey.ANALYSIS_DISPLAY_PROPERTY.getName(), "shortName" ) );
    }

    @Test
    public void getAsRealClassOther()
    {
        Assert.assertSame( "Test Layout", SettingKey.getAsRealClass( SettingKey.TRACKER_DASHBOARD_LAYOUT.getName(), "Test Layout" ) );
    }
}