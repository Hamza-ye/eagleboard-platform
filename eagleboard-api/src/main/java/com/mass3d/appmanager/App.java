package com.mass3d.appmanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "app", namespace = DxfNamespaces.DXF_2_0 )
public class App
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -6638197841892194228L;

    public static final String SEE_APP_AUTHORITY_PREFIX = "M_";

    public static final String INSTALLED_APP_PATH = "api/apps/";

    /**
     * Required.
     */
    private String version;

    private String name;

    private AppType appType = AppType.APP;

    private String launchPath;

    private String[] installsAllowedFrom;

    private String defaultLocale;

    private AppStorageSource appStorageSource;

    private String folderName;

    /**
     * Optional.
     */
    private String shortName;

    private String description;

    private AppIcons icons;

    private AppDeveloper developer;

    private String locales;

    private AppActivities activities;

    private String launchUrl;

    private String baseUrl;

    private Set<String> authorities = new HashSet<>();

    /**
     * Generated.
     */
    private AppStatus appState = AppStatus.OK;

    private boolean isBundledApp = false;

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Initializes the app. Sets the launchUrl property.
     *
     * @param contextPath the context path of this instance.
     */
    public void init( String contextPath )
    {
        isBundledApp = AppManager.BUNDLED_APPS.contains( getShortName() );

        String appPathPrefix = isBundledApp ? AppManager.BUNDLED_APP_PREFIX : INSTALLED_APP_PATH;

        this.baseUrl = String.join( "/", contextPath, appPathPrefix ) + getUrlFriendlyName();

        if ( contextPath != null && name != null && launchPath != null )
        {
            launchUrl = String.join( "/", baseUrl, launchPath.replaceFirst( "^/+", "" ) );
        }
    }

    /**
     * Unique identifier for the app. Is based on app-name
     */
    @JsonProperty
    public String getKey()
    {
        return getUrlFriendlyName();
    }

    @JsonProperty
    public boolean getIsBundledApp()
    {
        return isBundledApp;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    @JsonProperty( "short_name" )
    @JacksonXmlProperty( localName = "short_name", namespace = DxfNamespaces.DXF_2_0 )
    public String getShortName()
    {
        if ( shortName == null )
        {
            return name;
        }
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

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
    public AppType getAppType()
    {
        return appType;
    }

    public void setAppType( AppType appType )
    {
        this.appType = appType;
    }

    @JsonProperty( "launch_path" )
    @JacksonXmlProperty( localName = "launch_path", namespace = DxfNamespaces.DXF_2_0 )
    public String getLaunchPath()
    {
        return launchPath;
    }

    public void setLaunchPath( String launchPath )
    {
        this.launchPath = launchPath;
    }

    @JsonProperty( "installs_allowed_from" )
    @JacksonXmlProperty( localName = "installs_allowed_from", namespace = DxfNamespaces.DXF_2_0 )
    public String[] getInstallsAllowedFrom()
    {
        return installsAllowedFrom;
    }

    public void setInstallsAllowedFrom( String[] installsAllowedFrom )
    {
        this.installsAllowedFrom = installsAllowedFrom;
    }

    @JsonProperty( "default_locale" )
    @JacksonXmlProperty( localName = "default_locale", namespace = DxfNamespaces.DXF_2_0 )
    public String getDefaultLocale()
    {
        return defaultLocale;
    }

    public void setDefaultLocale( String defaultLocale )
    {
        this.defaultLocale = defaultLocale;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AppDeveloper getDeveloper()
    {
        return developer;
    }

    public void setDeveloper( AppDeveloper developer )
    {
        this.developer = developer;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AppIcons getIcons()
    {
        return icons;
    }

    public void setIcons( AppIcons icons )
    {
        this.icons = icons;
    }

    @JsonIgnore
    public String getLocales()
    {
        return locales;
    }

    public void setLocales( String locales )
    {
        this.locales = locales;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AppActivities getActivities()
    {
        return activities;
    }

    public void setActivities( AppActivities activities )
    {
        this.activities = activities;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getFolderName()
    {
        return folderName;
    }

    public void setFolderName( String folderName )
    {
        this.folderName = folderName;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getLaunchUrl()
    {
        return launchUrl;
    }

    public void setLaunchUrl( String launchUrl )
    {
        this.launchUrl = launchUrl;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl( String baseUrl )
    {
        this.baseUrl = baseUrl;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AppStorageSource getAppStorageSource()
    {
        return appStorageSource;
    }

    public void setAppStorageSource( AppStorageSource appStorageSource )
    {
        this.appStorageSource = appStorageSource;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<String> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities( Set<String> authorities )
    {
        this.authorities = authorities;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AppStatus getAppState()
    {
        return appState;
    }

    public void setAppState( AppStatus appState )
    {
        this.appState = appState;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + (this.getShortName() != null ? this.getShortName().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null )
        {
            return false;
        }

        if ( getClass() != obj.getClass() )
        {
            return false;
        }

        final App other = (App) obj;

        return this.getShortName() == null ? other.getShortName() == null
            : this.getShortName().equals( other.getShortName() );
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"version:\"" + version + "\", " +
            "\"name:\"" + name + "\", " +
            "\"shortName:\"" + getShortName() + "\", " +
            "\"appType:\"" + appType + "\", " +
            "\"baseUrl:\"" + baseUrl + "\", " +
            "\"launchPath:\"" + launchPath + "\" " +
            "}";
    }

    public String getUrlFriendlyName()
    {
        if ( getShortName() != null )
        {
            return getShortName()
                .trim()
                .replaceAll( "[^A-Za-z0-9\\s-]", "" )
                .replaceAll( "\\s+", "-" );
        }

        return null;
    }

    public String getSeeAppAuthority()
    {
        return SEE_APP_AUTHORITY_PREFIX
            + getShortName().trim().replaceAll( "[^a-zA-Z0-9\\s]", "" ).replaceAll( "\\s+", "_" );
    }
}
