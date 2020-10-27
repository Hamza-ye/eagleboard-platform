package com.mass3d.version;

import java.util.List;

public interface VersionService
{
    String ORGANISATIONUNIT_VERSION = "organisationUnit";

    /**
     * @param version Version object to add.
     * @return ID of the saved version object.
     */
    long addVersion(Version version);

    /**
     * @param version Version object to update.
     */
    void updateVersion(Version version);

    /**
     * @param key
     */
    void updateVersion(String key);

    /**
     * @param key
     * @param value
     */
    void updateVersion(String key, String value);

    /**
     * @param version Version object to delete.
     */
    void deleteVersion(Version version);

    /**
     * @param id Get Version with this ID.
     * @return Version that matched ID, or null if there was no match.
     */
    Version getVersion(long id);

    /**
     * @param key Key to lookup the value with.
     * @return Version that matched key, or null if there was no match.
     */
    Version getVersionByKey(String key);

    /**
     * @return List of all version objects.
     */
    List<Version> getAllVersions();
}
