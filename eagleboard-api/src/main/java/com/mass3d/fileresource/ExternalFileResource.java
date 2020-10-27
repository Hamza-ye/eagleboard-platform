package com.mass3d.fileresource;

import java.util.Date;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.MetadataObject;

public class ExternalFileResource
    extends BaseIdentifiableObject implements MetadataObject
{
    /**
     * FileResource containing the file we are exposing
     */
    private FileResource fileResource;

    /**
     * The accessToken required to identify ExternalFileResources trough the api
     */
    private String accessToken;

    /**
     * Date when the resource expires. Null means it never expires
     */
    private Date expires;

    public Date getExpires()
    {
        return expires;
    }

    public void setExpires( Date expires )
    {
        this.expires = expires;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken( String accessToken )
    {
        this.accessToken = accessToken;
    }

    public FileResource getFileResource()
    {
        return fileResource;
    }

    public void setFileResource( FileResource fileResource )
    {
        this.fileResource = fileResource;
    }
}
