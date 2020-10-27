package com.mass3d.dxf2.metadata.systemsettings;

import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Provide the endpoints for api calls in metadata versioning
 *
 */
@Service( "com.mass3d.dxf2.metadata.sync.MetadataSystemSettingService" )
@Scope("prototype")
public class DefaultMetadataSystemSettingService
    implements MetadataSystemSettingService
{
    @Autowired
    private SystemSettingManager systemSettingManager;

    private final String API_URL = "/api/metadata/version";
    private final String BASELINE_URL = API_URL + "/history?baseline=";

    public String getRemoteInstanceUserName()
    {
        return (String) systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_USERNAME );
    }

    public String getRemoteInstancePassword()
    {
        return (String) systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_PASSWORD );
    }

    public String getVersionDetailsUrl( String versionName )
    {
        return systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_URL ) + API_URL + "?versionName=" + versionName;
    }

    public String getDownloadVersionSnapshotURL( String versionName )
    {
        return systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_URL ) + API_URL + "/" + versionName + "/data.gz";
    }

    public String getMetaDataDifferenceURL( String versionName )
    {
        return systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_URL ) + BASELINE_URL + versionName;
    }

    public String getEntireVersionHistory()
    {
        return systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_URL ) + API_URL + "/history";
    }

    public void setSystemMetadataVersion( String versionName )
    {
        systemSettingManager.saveSystemSetting( SettingKey.SYSTEM_METADATA_VERSION, versionName );
    }

    public String getSystemMetadataVersion()
    {
        return (String) systemSettingManager.getSystemSetting( SettingKey.SYSTEM_METADATA_VERSION );
    }

    public Boolean getStopMetadataSyncSetting()
    {
        Boolean stopSyncSetting = (Boolean) systemSettingManager.getSystemSetting( SettingKey.STOP_METADATA_SYNC );
        return stopSyncSetting == null ? false : stopSyncSetting;
    }
}
