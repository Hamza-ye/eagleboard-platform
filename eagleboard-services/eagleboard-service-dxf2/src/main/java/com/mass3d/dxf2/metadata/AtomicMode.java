package com.mass3d.dxf2.metadata;

public enum AtomicMode
{
    /**
     * Import object if it passes all validation tests (including references)
     * (not supported at the moment)
     */
    OBJECT,

    /**
     * Import objects only if they all pass the validation phase (including references)
     */
    ALL,

    /**
     * Legacy mode. Allow non-valid references when importing.
     */
    NONE
}
