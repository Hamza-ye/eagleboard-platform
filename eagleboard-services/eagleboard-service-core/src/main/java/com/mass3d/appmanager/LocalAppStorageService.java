package com.mass3d.appmanager;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import com.mass3d.cache.Cache;
import com.mass3d.external.location.LocationManager;
import com.mass3d.external.location.LocationManagerException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 *
 * NB! This class is mostly code from pre 2.28's DefaultAppManager. This is to support apps
 * installed before 2.28. post 2.28, all installations using DHIS2 will use JCloudsAppStorageService.
 */
@Slf4j
@Service( "com.mass3d.appmanager.LocalAppStorageService" )
public class LocalAppStorageService
    implements AppStorageService
{
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    private Map<String, App> apps = new HashMap<>();

    private Map<String, App> reservedNamespaces = new HashMap<>();

    private final LocationManager locationManager;

    public LocalAppStorageService( LocationManager locationManager )
    {
        checkNotNull( locationManager );

        this.locationManager = locationManager;
    }

    @Override
    public Map<String, App> discoverInstalledApps()
    {
        Map<String, App> appMap = new HashMap<>();
        List<App> appList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        String path = getAppFolderPath();

        apps.clear();

        // Make sure external directory is set
        if ( path == null )
        {
            log.error( "Failed to discover installed apps: Could not get app folder path, external directory not set" );
            return appMap;
        }

        File appFolderPath = new File( path );

        // If no apps folder exists, there is nothing to discover
        if ( !appFolderPath.exists() )
        {
            log.info( "Old apps folder does not exist, stopping discovery" );
            return appMap;
        }

        if ( !appFolderPath.isDirectory() )
        {
            log.error( "Failed to discover installed apps: Path is not a directory '" + path + "'" );
        }
        else
        {
            File[] listFiles = appFolderPath.listFiles();

            // Should only happen is IO error occurs
            if ( listFiles == null )
            {
                log.error( "Failed to discover installed apps: Could not list contents of directory '" + path + "'" );
            }
            else
            {
                for ( File folder : listFiles )
                {

                    // Found a file that is not an app in the app directory.
                    if ( !folder.isDirectory() )
                    {
                        log.warn( "Failed to discover app '" + folder.getName() + "': Path is not a directory '" +
                            folder.getPath() + "'" );
                    }
                    else
                    {
                        File appManifest = new File( folder, "manifest.webapp" );

                        if ( !appManifest.exists() )
                        {
                            log.warn( "Failed to discover app '" + folder.getName() +
                                "': Missing 'manifest.webapp' in app directory" );
                        }
                        else
                        {
                            try
                            {
                                App app = mapper.readValue( appManifest, App.class );
                                app.setFolderName( folder.getName() );
                                app.setAppStorageSource( AppStorageSource.LOCAL );
                                appList.add( app );
                            }
                            catch ( IOException ex )
                            {
                                log.error( ex.getLocalizedMessage(), ex );
                            }
                        }
                    }
                }
            }
        }

        appList.forEach(
            app -> {
                String namespace = app.getActivities().getDhis().getNamespace();

                if ( namespace != null && !namespace.isEmpty() )
                {
                    reservedNamespaces.put( namespace, app );
                }

                appMap.put( app.getUrlFriendlyName(), app );
                apps.put( app.getUrlFriendlyName(), app );

                log.info( "Discovered app '" + app.getName() + "' from local storage " );
            }
        );

        if ( appList.isEmpty() )
        {
            log.info( "No apps found during local discovery.");
        }

        return appMap;
    }

    @Override
    public Map<String, App> getReservedNamespaces()
    {
        return reservedNamespaces;
    }

    @Override
    public App installApp( File file, String fileName, Cache<App> appCache )
    {
        throw new UnsupportedOperationException( "LocalAppStorageService.installApp is deprecated and should no longer be used." );
    }

    @Override
    public boolean deleteApp( App app )
    {
        boolean deleted = false;

        if ( !apps.containsKey( app.getUrlFriendlyName() ) )
        {
            log.warn( String.format( "Failed to delete app '%s': App not found", app.getName() ) );
        }

        try
        {
            String folderPath = getAppFolderPath() + File.separator + app.getFolderName();
            FileUtils.forceDelete( new File( folderPath ) );

            deleted = true;
        }
        catch ( FileNotFoundException ex )
        {
            log.error( String.format( "Failed to delete app '%s': Files not found", app.getName() ), ex );
        }
        catch ( IOException ex )
        {
            log.error( String.format( "Failed to delete app '%s'", app.getName() ), ex );
        }
        finally
        {
            discoverInstalledApps();
        }

        return deleted;
    }

    private String getAppFolderPath()
    {
        try
        {
            return locationManager.getExternalDirectoryPath() + "/" + APPS_DIR;
        }
        catch ( LocationManagerException ex )
        {
            return null;
        }
    }

    @Override
    public Resource getAppResource( App app, String pageName )
        throws IOException
    {
        List<Resource> locations = Lists.newArrayList(
            resourceLoader.getResource( "file:" + getAppFolderPath() + "/" + app.getFolderName() + "/" ),
            resourceLoader.getResource( "classpath*:/apps/" + app.getFolderName() + "/" )
        );

        for ( Resource location : locations )
        {
            Resource resource = location.createRelative( pageName );

            if ( resource.exists() && resource.isReadable() )
            {
                File file = resource.getFile();

                // Make sure that file resolves into path app folder
                if ( file != null && file.toPath().startsWith( getAppFolderPath() ) )
                {
                    return resource;
                }
            }
        }

        return null;
    }
}
