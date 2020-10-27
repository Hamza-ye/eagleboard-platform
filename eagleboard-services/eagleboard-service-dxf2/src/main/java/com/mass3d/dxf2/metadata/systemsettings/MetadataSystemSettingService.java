package com.mass3d.dxf2.metadata.systemsettings;

/**
 * MetadataSystemSettingService is the helper class for getting the Versioning
 * endpoint specific constructs.
 *
 */
public interface MetadataSystemSettingService
{
    String getRemoteInstanceUserName();

    String getRemoteInstancePassword();

    String getVersionDetailsUrl(String versionName);

    String getDownloadVersionSnapshotURL(String versionName);

    String getMetaDataDifferenceURL(String versionName);

    String getEntireVersionHistory();

    void setSystemMetadataVersion(String versionName);

    String getSystemMetadataVersion();

    Boolean getStopMetadataSyncSetting();
}
