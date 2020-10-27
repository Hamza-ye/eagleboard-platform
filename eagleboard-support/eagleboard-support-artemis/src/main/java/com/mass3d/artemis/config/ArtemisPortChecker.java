package com.mass3d.artemis.config;

import java.net.InetAddress;
import java.net.ServerSocket;
import javax.annotation.PostConstruct;
import javax.net.ServerSocketFactory;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.springframework.stereotype.Component;

@Component
public class ArtemisPortChecker
{
    private final DhisConfigurationProvider dhisConfig;

    public ArtemisPortChecker( DhisConfigurationProvider dhisConfig )
    {
        this.dhisConfig = dhisConfig;
    }

    @PostConstruct
    public void init()
    {
        final int artemisPort = Integer.parseInt( dhisConfig.getProperty( ConfigurationKey.ARTEMIS_PORT ) );
        final String artemisHost = dhisConfig.getProperty( ConfigurationKey.ARTEMIS_HOST );

        if ( isEmbedded() && !isPortAvailable( artemisHost, artemisPort ) )
        {
            String message = "\n\n";
            message += "############################################################################################\n";
            message += "#\n";
            message += String.format( "# Current selected Apache Artemis port '%s' on host '%s' is already in use.\n", artemisPort, artemisHost );
            message += "#\n";
            message += "# Please change this in your 'dhis.conf' by using the 'artemis.port = X' key.\n";
            message += "#\n";
            message += "############################################################################################\n";
            message += "\n\n";

            System.err.println( message );

            System.exit( -1 );
        }
    }

    private boolean isEmbedded()
    {
        return ArtemisMode.valueOf( (dhisConfig.getProperty( ConfigurationKey.ARTEMIS_MODE )).toUpperCase() ) == ArtemisMode.EMBEDDED;
    }

    private boolean isPortAvailable( String host, int port )
    {
        try
        {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(
                port, 1, InetAddress.getByName( host ) );
            serverSocket.close();
            return true;
        }
        catch ( Exception ex )
        {
            return false;
        }
    }
}
