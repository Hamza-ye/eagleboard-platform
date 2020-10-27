package com.mass3d.fileresource.events;

public class BinaryFileSavedEvent
{
    private String fileResource;

    private byte[] bytes;

    public BinaryFileSavedEvent( String fileResource, byte[] bytes )
    {
        this.fileResource = fileResource;
        this.bytes = bytes;
    }

    public String getFileResource()
    {
        return fileResource;
    }

    public byte[] getBytes()
    {
        return bytes;
    }
}
