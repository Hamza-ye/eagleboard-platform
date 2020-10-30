package com.mass3d.i18n.ui.resourcebundle;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mass3d.common.comparator.LocaleNameComparator;
import com.mass3d.commons.util.PathUtils;
import com.mass3d.i18n.locale.LocaleManager;

public class DefaultResourceBundleManager
    implements ResourceBundleManager
{
    private static final String EXT_RESOURCE_BUNDLE = ".properties";

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private final String globalResourceBundleName;

    private final String specificResourceBundleName;

    public DefaultResourceBundleManager( String globalResourceBundleName, String specificResourceBundleName )
    {
        checkNotNull( globalResourceBundleName );
        checkNotNull( specificResourceBundleName );

        this.globalResourceBundleName = globalResourceBundleName;
        this.specificResourceBundleName = specificResourceBundleName;
    }

    // -------------------------------------------------------------------------
    // ResourceBundleManager implementation
    // -------------------------------------------------------------------------

    @Override
    public ResourceBundle getSpecificResourceBundle( Class<?> clazz, Locale locale )
    {
        return getSpecificResourceBundle( clazz.getName(), locale );
    }

    @Override
    public ResourceBundle getSpecificResourceBundle( String clazzName, Locale locale )
    {
        String path = PathUtils.getClassPath( clazzName );

        for ( String dir = path; dir != null; dir = PathUtils.getParent( dir ) )
        {
            String baseName = PathUtils.addChild( dir, specificResourceBundleName );

            try
            {
                return ResourceBundle.getBundle( baseName, locale );
            }
            catch ( MissingResourceException ignored )
            {
            }
        }

        return null;
    }

    @Override
    public ResourceBundle getGlobalResourceBundle( Locale locale )
        throws ResourceBundleManagerException
    {
        try
        {
            return ResourceBundle.getBundle( globalResourceBundleName, locale );
        }
        catch ( MissingResourceException e )
        {
            throw new ResourceBundleManagerException( "Failed to get global resource bundle" );
        }
    }

    @Override
    public List<Locale> getAvailableLocales()
        throws ResourceBundleManagerException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL url = classLoader.getResource( globalResourceBundleName + EXT_RESOURCE_BUNDLE );
//        URL url = classLoader.getResource( "i18n_global.properties" );

        if ( url == null )
        {
            throw new ResourceBundleManagerException( "Failed to find global resource bundle" );
        }

        List<Locale> locales;

        if ( url.toExternalForm().startsWith( "jar:" ) )
        {
            locales = new ArrayList<>( getAvailableLocalesFromJar( url ) );
        }
        else
        {
            String dirPath = new File( url.getFile() ).getParent();

            locales = new ArrayList<>( getAvailableLocalesFromDir( dirPath ) );
        }

        locales.sort(LocaleNameComparator.INSTANCE);

        return locales;
    }

    private Collection<Locale> getAvailableLocalesFromJar( URL url )
        throws ResourceBundleManagerException
    {
        JarFile jar;

        Set<Locale> availableLocales = new HashSet<>();

        try
        {
            JarURLConnection connection = (JarURLConnection) url.openConnection();

            jar = connection.getJarFile();

            Enumeration<JarEntry> e = jar.entries();

            while ( e.hasMoreElements() )
            {
                JarEntry entry = e.nextElement();

                String name = entry.getName();

                if ( name.startsWith( globalResourceBundleName ) && name.endsWith( EXT_RESOURCE_BUNDLE ) )
                {
                    availableLocales.add( getLocaleFromName( name ) );
                }
            }
        }
        catch ( IOException e )
        {
            throw new ResourceBundleManagerException( "Failed to get jar file: " + url, e );
        }

        return availableLocales;
    }

    private Collection<Locale> getAvailableLocalesFromDir( String dirPath )
    {
        dirPath = convertURLToFilePath( dirPath );

        File dir = new File( dirPath );
        Set<Locale> availableLocales = new HashSet<>();

        File[] files = dir.listFiles(
            ( dir1, name ) -> name.startsWith( globalResourceBundleName ) && name.endsWith( EXT_RESOURCE_BUNDLE ) );

        if ( files != null )
        {
            for ( File file : files )
            {
                availableLocales.add( getLocaleFromName( file.getName() ) );
            }
        }

        return availableLocales;
    }

    private Locale getLocaleFromName( String name )
    {
        Pattern pattern = Pattern.compile( "^" + globalResourceBundleName
            + "(?:_([a-z]{2,3})(?:_([A-Z]{2})(?:_(.+))?)?)?" + EXT_RESOURCE_BUNDLE + "$" );

        Matcher matcher = pattern.matcher( name );

        if ( matcher.matches() )
        {
            if ( matcher.group( 1 ) != null )
            {
                if ( matcher.group( 2 ) != null )
                {
                    if ( matcher.group( 3 ) != null )
                    {
                        return new Locale( matcher.group( 1 ), matcher.group( 2 ), matcher.group( 3 ) );
                    }

                    return new Locale( matcher.group( 1 ), matcher.group( 2 ) );
                }

                return new Locale( matcher.group( 1 ) );
            }
        }

        return LocaleManager.DEFAULT_LOCALE;
    }

    // -------------------------------------------------------------------------
    // Support method
    // -------------------------------------------------------------------------

    private String convertURLToFilePath( String url )
    {
        try
        {
            url = URLDecoder.decode( url, "iso-8859-1" );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        return url;
    }
}
