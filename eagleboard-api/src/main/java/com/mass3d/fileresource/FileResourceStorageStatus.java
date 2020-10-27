package com.mass3d.fileresource;

public enum FileResourceStorageStatus
{
    NONE,       // No content stored
    PENDING,    // In transit to store, not available
    FAILED,     // Storing the resource failed
    STORED      // Is available from store
}
