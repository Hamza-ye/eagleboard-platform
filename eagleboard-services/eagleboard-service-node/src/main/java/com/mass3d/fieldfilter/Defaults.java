package com.mass3d.fieldfilter;

public enum Defaults
{
    /**
     * Include all defaults, both for roots and for references.
     */
    INCLUDE,

    /**
     * Remove defaults from nodes. Roots will not be included, collections
     * which contains default will have them removed, 1-to-1 mappings will have
     * them set to null.
     */
    EXCLUDE
}
