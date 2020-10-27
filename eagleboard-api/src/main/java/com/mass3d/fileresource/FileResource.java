package com.mass3d.fileresource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import org.springframework.util.MimeTypeUtils;

public class FileResource
    extends BaseIdentifiableObject
{
    public static final String DEFAULT_FILENAME = "untitled";

    public static final String DEFAULT_CONTENT_TYPE = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;

    public static final Set<String> IMAGE_CONTENT_TYPES = ImmutableSet
        .of( "image/jpg", "image/png", "image/jpeg" );

    /**
     * MIME type.
     */
    private String contentType;

    /**
     * Byte size of content, non negative.
     */
    private long contentLength;

    /**
     * MD5 digest of content.
     */
    private String contentMd5;

    /**
     * Key used for content storage at external location.
     */
    private String storageKey;

    /**
     * Flag indicating whether the resource is assigned (e.g. to a DataValue) or
     * not. Unassigned FileResources are generally safe to delete when reaching
     * a certain age (unassigned objects might be in staging).
     */
    private boolean assigned = false;

    /**
     * The domain which this FileResource belongs to.
     */
    private FileResourceDomain domain;

    /**
     *  To keep track of those files which are not pre-generated and need to be processed later.
     *  Flag will be set to true for FileResource having more than one file associated with it (e.g images)
     */
    private boolean hasMultipleStorageFiles;

    /**
     * Current storage status of content.
     */
    private transient FileResourceStorageStatus storageStatus = FileResourceStorageStatus.NONE;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public FileResource()
    {
    }

    public FileResource( String name, String contentType, long contentLength, String contentMd5, FileResourceDomain domain )
    {
        this.name = name;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentMd5 = contentMd5;
        this.domain = domain;
        this.storageKey = FileResourceKeyUtil.makeKey( domain, Optional.empty() );
    }

    public FileResource( String key, String name, String contentType, long contentLength, String contentMd5,
        FileResourceDomain domain )
    {
        this.name = name;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentMd5 = contentMd5;
        this.domain = domain;
        this.storageKey = FileResourceKeyUtil.makeKey( domain, Optional.of( key ) );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getContentType()
    {
        return contentType;
    }

    public void setContentType( String contentType )
    {
        this.contentType = contentType;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public long getContentLength()
    {
        return contentLength;
    }

    public void setContentLength( long contentLength )
    {
        this.contentLength = contentLength;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getContentMd5()
    {
        return contentMd5;
    }

    public void setContentMd5( String contentMd5 )
    {
        this.contentMd5 = contentMd5;
    }

    public String getStorageKey()
    {
        return storageKey;
    }

    public void setStorageKey( String storageKey )
    {
        this.storageKey = storageKey;
    }

    public boolean isAssigned()
    {
        return assigned;
    }

    public void setAssigned( boolean assigned )
    {
        this.assigned = assigned;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public FileResourceStorageStatus getStorageStatus()
    {
        return storageStatus;
    }

    public void setStorageStatus( FileResourceStorageStatus storageStatus )
    {
        this.storageStatus = storageStatus;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public FileResourceDomain getDomain()
    {
        return domain;
    }

    public void setDomain( FileResourceDomain domain )
    {
        this.domain = domain;
    }

    public String getFormat()
    {
        return this.contentType.split("[/;]" )[1];
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isHasMultipleStorageFiles()
    {
        return hasMultipleStorageFiles;
    }

    public void setHasMultipleStorageFiles( boolean hasMultipleStorageFiles )
    {
        this.hasMultipleStorageFiles = hasMultipleStorageFiles;
    }
}
