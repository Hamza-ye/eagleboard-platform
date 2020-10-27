package com.mass3d.startup;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.system.startup.AbstractStartupRoutine;

public class SettingUpgrader
    extends AbstractStartupRoutine
{
    private final SystemSettingManager manager;

    public SettingUpgrader( SystemSettingManager manager )
    {
        checkNotNull( manager );
        this.manager = manager;
    }

    @Override
    public void execute()
        throws Exception
    {
        String startModule = (String) manager.getSystemSetting( SettingKey.START_MODULE );

        if ( "dhis-web-dashboard-integration".equals( startModule ) )
        {
            manager.saveSystemSetting( SettingKey.START_MODULE, SettingKey.START_MODULE.getDefaultValue() );
        }
    }
}
