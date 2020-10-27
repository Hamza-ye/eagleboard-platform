package com.mass3d.fileresource.events;

import java.io.File;

public class FileSavedEvent
{
    private String fileResource;

    private File file;

    public FileSavedEvent( String fileResource, File file )
    {
        this.fileResource = fileResource;
        this.file = file;
    }

    public String getFileResource()
    {
        return fileResource;
    }

    public File getFile()
    {
        return file;
    }
}
