package com.mass3d.fileresource.events;

import java.io.File;
import java.util.Map;
import com.mass3d.fileresource.ImageFileDimension;

public class ImageFileSavedEvent
{
    private String fileResource;

    private Map<ImageFileDimension, File> imageFiles;

    public ImageFileSavedEvent( String fileResource, Map<ImageFileDimension, File> imageFiles )
    {
        this.fileResource = fileResource;
        this.imageFiles = imageFiles;
    }

    public String getFileResource()
    {
        return fileResource;
    }

    public Map<ImageFileDimension, File> getImageFiles()
    {
        return imageFiles;
    }
}
