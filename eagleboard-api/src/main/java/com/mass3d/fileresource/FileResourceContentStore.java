package com.mass3d.fileresource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;

public interface FileResourceContentStore
{
    /**
     * Get the content bytes of a FileResource from the file store.
     * @param key the key.
     * @return a ByteSource which provides a stream to the content or null if the content cannot be found or read.
     */
    InputStream getFileResourceContent(String key);

    /**
     * Get the content length of a FileResource from the file store.
     * @param key the key.
     * @return the content length
     */
    long getFileResourceContentLength(String key);

    /**
     * Save the contents of the byte array to the file store.
     *
     * @param fileResource the FileResource object. Must be complete and include the storageKey,
     *                     contentLength, contentMd5 and name.
     * @param bytes the byte array.
     * @return the key on success or null if saving failed.
     */
    String saveFileResourceContent(FileResource fileResource, byte[] bytes);

    /**
     * Save the contents of the File to the file store.
     *
     * @param fileResource the FileResource object.
     * @param file the File. Will be consumed upon deletion.
     * @return the key on success or null if saving failed.
     */
    String saveFileResourceContent(FileResource fileResource, File file);

    /**
     * Save the content of image files.
     * @param fileResource  the FileResource object.
     * @param imageFile will map image dimension to its associated file.
     * @return the key on success or null if saving failed.
     */
    String saveFileResourceContent(FileResource fileResource,
        Map<ImageFileDimension, File> imageFile);

    /**
     * Delete the content bytes of a file resource.
     * @param key the key.
     */
    void deleteFileResourceContent(String key);

    /**
     * Check existence of a file.
     * @param key key of the file.
     * @return true if the file exists in the file store, false otherwise.
     */
    boolean fileResourceContentExists(String key);

    /**
     * Create a signed GET request which gives access to the content.
     * @param key the key.
     * @return a URI containing the signed GET request or null if signed requests are not supported.
     */
    URI getSignedGetContentUri(String key);

    /**
     * Copies the content of a stream to the resource stored under key to the output stream.
     * @param key the key used to store a resource
     * @param output the output stream to copy the stream into
     */
    void copyContent(String key, OutputStream output) throws IOException, NoSuchElementException;
}
