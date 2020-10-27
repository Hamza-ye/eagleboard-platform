package com.mass3d.dxf2.metadata.sync;

import static org.junit.Assert.assertEquals;

import com.mass3d.DhisSpringTest;
import com.mass3d.dxf2.metadata.systemsettings.DefaultMetadataSystemSettingService;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class MetadataSystemSettingServiceTest
    extends DhisSpringTest
{
    @Autowired
    SystemSettingManager systemSettingManager;

    @Autowired
    DefaultMetadataSystemSettingService metadataSystemSettingService;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks( this );

        systemSettingManager.saveSystemSetting( SettingKey.REMOTE_INSTANCE_URL, "http://localhost:9080" );
        systemSettingManager.saveSystemSetting( SettingKey.REMOTE_INSTANCE_USERNAME, "username" );
        systemSettingManager.saveSystemSetting( SettingKey.REMOTE_INSTANCE_PASSWORD, "password" );
        systemSettingManager.saveSystemSetting( SettingKey.STOP_METADATA_SYNC, true );
    }

    @Test
    public void testShouldGetRemoteUserName()
    {
        String remoteInstanceUserName = metadataSystemSettingService.getRemoteInstanceUserName();

        assertEquals( "username", remoteInstanceUserName );
    }

    @Test
    public void testShouldGetRemotePassword()
    {
        String remoteInstancePassword = metadataSystemSettingService.getRemoteInstancePassword();

        assertEquals( "password", remoteInstancePassword );
    }

    @Test
    public void testShouldDownloadMetadataVersionForGivenVersionName()
    {
        String downloadVersionUrl = metadataSystemSettingService.getVersionDetailsUrl( "Version_Name" );

        assertEquals( "http://localhost:9080/api/metadata/version?versionName=Version_Name", downloadVersionUrl );
    }

    @Test
    public void testShouldDownloadMetadataVersionSnapshotForGivenVersionName()
    {
        String downloadVersionUrl = metadataSystemSettingService.getDownloadVersionSnapshotURL( "Version_Name" );

        assertEquals( "http://localhost:9080/api/metadata/version/Version_Name/data.gz", downloadVersionUrl );
    }

    @Test
    public void testShouldGetAllVersionsCreatedAfterTheGivenVersionName()
    {
        String metadataDifferenceUrl = metadataSystemSettingService.getMetaDataDifferenceURL("Version_Name");

        assertEquals("http://localhost:9080/api/metadata/version/history?baseline=Version_Name", metadataDifferenceUrl);
    }

    @Test
    public void testShouldGetEntireVersionHistoryWhenNoVersionNameIsGiven()
    {
        String versionHistoryUrl = metadataSystemSettingService.getEntireVersionHistory();

        assertEquals("http://localhost:9080/api/metadata/version/history", versionHistoryUrl );
    }

    @Test
    public void testShouldGetStopMetadataSyncSettingValue()
    {
        Boolean stopMetadataSync = metadataSystemSettingService.getStopMetadataSyncSetting(  );

        assertEquals( true, stopMetadataSync );
    }

    @Test
    public void testShouldReturnFalseIfStopMetadataSyncSettingValueIsNull()
    {
        systemSettingManager.saveSystemSetting( SettingKey.STOP_METADATA_SYNC, null );
        Boolean stopMetadataSync = metadataSystemSettingService.getStopMetadataSyncSetting(  );

        assertEquals( false, stopMetadataSync );
    }
}