package com.mass3d.fileresource.events;

import com.mass3d.fileresource.FileResourceDomain;

public class FileDeletedEvent
{
    private String storageKey;

    private String contentType;

    private FileResourceDomain domain;

    public FileDeletedEvent( String storageKey, String contentType, FileResourceDomain domain )
    {
        this.storageKey = storageKey;
        this.contentType = contentType;
        this.domain = domain;
    }

    public String getStorageKey()
    {
        return storageKey;
    }

    public String getContentType()
    {
        return contentType;
    }

    public FileResourceDomain getDomain()
    {
        return domain;
    }
}
