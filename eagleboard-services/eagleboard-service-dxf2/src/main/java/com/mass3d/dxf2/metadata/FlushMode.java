package com.mass3d.dxf2.metadata;

public enum FlushMode
{
    /**
     * Flush for every db write.
     */
    OBJECT,

    /**
     * Let the importer decide the flushing.
     */
    AUTO,
}
