package com.mass3d.dxf2.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.mass3d.util.DateUtils;

public class Options
{
    //--------------------------------------------------------------------------
    // Internal State
    //--------------------------------------------------------------------------

    protected Map<String, String> options = new HashMap<>();

    protected boolean assumeTrue;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public Options( Map<String, String> options )
    {
        this.options = options;
        this.assumeTrue = options.get( "assumeTrue" ) == null || options.get( "assumeTrue" ).equalsIgnoreCase( "true" );
    }

    public Options()
    {
    }

    //--------------------------------------------------------------------------
    // Object helpers
    //--------------------------------------------------------------------------

    /**
     * Indicates whether the given object type is enabled. Takes the assumeTrue
     * parameter into account.
     */
    public boolean isEnabled( String type )
    {
        String enabled = options.get( type );

        return stringIsTrue( enabled ) || (enabled == null && assumeTrue);
    }

    /**
     * Indicates whether the given object type is disabled. Takes the assumeTrue
     * parameter into account.
     */
    public boolean isDisabled( String type )
    {
        return !isEnabled( type );
    }

    //--------------------------------------------------------------------------
    // Options helpers
    //--------------------------------------------------------------------------

    public Date getDate( String key )
    {
        return DateUtils.parseDate( options.get( key ) );
    }

    /**
     * Indicates whether the options contains the given parameter key.
     */
    public boolean contains( String key )
    {
        return options.containsKey( key );
    }

    /**
     * Indicates whether the options contains a non-null option value for the given
     * parameter key.
     */
    public boolean containsValue( String key )
    {
        return options.get( key ) != null;
    }

    /**
     * Returns the option value for the given parameter key.
     */
    public String get( String key )
    {
        return options.get( key );
    }

    /**
     * Returns the option value for the given parameter key.
     */
    public String get( String key, String defaultValue )
    {
        String value = options.get( key );
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the option value for the given parameter key as in Integer.
     */
    public Integer getInt( String key )
    {
        return options.containsKey( key ) ? Integer.parseInt( options.get( key ) ) : null;
    }

    /**
     * Indicates whether the option value for the parameter key is true.
     */
    public boolean isTrue( String key )
    {
        return options.containsKey( key ) && Boolean.parseBoolean( options.get( key ) );
    }

    //--------------------------------------------------------------------------
    // Getters and Setters
    //--------------------------------------------------------------------------

    public Map<String, String> getOptions()
    {
        return options;
    }

    public void setOptions( Map<String, String> options )
    {
        this.options = options;
    }

    public boolean isAssumeTrue()
    {
        return assumeTrue;
    }

    public void setAssumeTrue( boolean assumeTrue )
    {
        this.assumeTrue = assumeTrue;
    }

    //--------------------------------------------------------------------------
    // Getters for standard options
    //--------------------------------------------------------------------------

    public Date getLastUpdated()
    {
        return getDate( "lastUpdated" );
    }

    //--------------------------------------------------------------------------
    // Adding options
    //--------------------------------------------------------------------------

    public void addOption( String option, String value )
    {
        options.put( option, value );
    }

    public void addOptions( Map<String, String> newOptions )
    {
        options.putAll( options );
    }

    //--------------------------------------------------------------------------
    // Static helpers
    //--------------------------------------------------------------------------

    protected static String stringAsString( String str, String defaultValue )
    {
        if ( str == null )
        {
            return defaultValue;
        }

        return str;
    }

    protected static boolean stringAsBoolean( String str, boolean defaultValue )
    {
        return str != null ? Boolean.parseBoolean( str ) : defaultValue;
    }

    protected static boolean stringIsTrue( String str )
    {
        return stringAsBoolean( str, false );
    }

    protected static int stringAsInt( String str )
    {
        return stringAsInt( str, 0 );
    }

    protected static int stringAsInt( String str, int defaultValue )
    {
        if ( str != null )
        {
            try
            {
                return Integer.parseInt( str );
            }
            catch ( NumberFormatException ignored )
            {
            }
        }

        return defaultValue;
    }
}
