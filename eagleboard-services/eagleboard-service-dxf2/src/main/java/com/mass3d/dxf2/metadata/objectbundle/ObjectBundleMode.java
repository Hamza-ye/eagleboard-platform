package com.mass3d.dxf2.metadata.objectbundle;

public enum ObjectBundleMode
{
    /**
     * If bundle is valid, commit the bundle to the database.
     * Commits at intervals.
     */
    COMMIT,

    /**
     * Validate bundle only (dry run)
     */
    VALIDATE;
}
