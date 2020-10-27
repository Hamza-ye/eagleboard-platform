package com.mass3d.common;

/**
 * Marker interface marking the class as a proper metadata object
 * (not data, not embedded object, etc) and specifies that the system itself
 * creates a predefined set of metadata objects of this type.
 *
 */
public interface SystemDefaultMetadataObject extends MetadataObject
{
    /**
     * Checks if this metadata object is a system default metadata object.
     *
     * @return <code>true</code> if this metadata object is a system default
     * metadata object, <code>false</code> if it is user generated.
     */
    boolean isDefault();
}
