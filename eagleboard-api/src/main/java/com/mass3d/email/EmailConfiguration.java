package com.mass3d.email;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;

public class EmailConfiguration
{
    private String hostName;

    private String username;

    private String password;

    private String from;

    private int port;

    private boolean tls;

    public EmailConfiguration( String hostName, String username, String password, String from, int port, boolean tls )
    {
        this.hostName = StringUtils.trimToNull( hostName );
        this.username = StringUtils.trimToNull( username );
        this.password = StringUtils.trimToNull( password );
        this.from = from;
        this.port = port;
        this.tls = tls;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "Host name", hostName )
            .add( "Username", username )
            .add( "From", from )
            .add( "Port", port )
            .add( "TLS", tls ).toString();
    }
    
    public boolean isOk()
    {
        return hostName != null && username != null && password != null;
    }

    public String getHostName()
    {
        return hostName;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFrom()
    {
        return from;
    }

    public int getPort()
    {
        return port;
    }

    public boolean isTls()
    {
        return tls;
    }
}
