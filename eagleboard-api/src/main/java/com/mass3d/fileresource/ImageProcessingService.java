package com.mass3d.fileresource;

import java.io.File;
import java.util.Map;

/**
 * creates images with pre-defined sizes
 *
 */
public interface ImageProcessingService
{
    /**
     *
     * Service creates images in pre-defined sizes given in {@link ImageFileDimension} and puts the collection in map
     *
     * @param fileResource file resource with image content type
     * @param file image file
     * @return map containing {@link ImageFileDimension} and its associated file.
     */
    Map<ImageFileDimension, File> createImages(FileResource fileResource, File file);
}
