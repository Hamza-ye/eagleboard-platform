package com.mass3d.system.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnull;
import org.apache.commons.beanutils.BeanUtils;
import com.mass3d.common.DxfNamespaces;

public class DatabaseInfo
{
    private String name;

    private String user;

    private String password;

    private String url;

    private String databaseVersion;

    private boolean spatialSupport;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DatabaseInfo()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void clearSensitiveInfo()
    {
        this.name = null;
        this.user = null;
        this.password = null;
        this.url = null;
        this.databaseVersion = null;
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
    public String getUser()
    {
        return user;
    }

    public void setUser( String user )
    {
        this.user = user;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDatabaseVersion()
    {
        return databaseVersion;
    }

    public void setDatabaseVersion( String databaseVersion )
    {
        this.databaseVersion = databaseVersion;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isSpatialSupport()
    {
        return spatialSupport;
    }

    public void setSpatialSupport( boolean spatialSupport )
    {
        this.spatialSupport = spatialSupport;
    }

    /**
     * @return a cloned instance of this object.
     */
    @Nonnull
    public DatabaseInfo instance()
    {
        final DatabaseInfo cloned = new DatabaseInfo();
        try
        {
            BeanUtils.copyProperties( cloned, this );
        }
        catch ( IllegalAccessException | InvocationTargetException e )
        {
            throw new IllegalStateException( e );
        }

        return cloned;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "[Name: " + name + ", User: " + user + ", Password: " + password +
            ", URL: " + url + "]";
    }
}
