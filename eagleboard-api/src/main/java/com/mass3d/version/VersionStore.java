package com.mass3d.version;

import com.mass3d.common.GenericStore;

public interface VersionStore
    extends GenericStore<Version>
{
    String ID = VersionStore.class.getName();

    /**
     * @param key Key to lookup.
     * @return Value that matched key, or null if there was no match.
     */
    Version getVersionByKey(String key);
}
