package com.mass3d.sms.config;

import java.util.Optional;

public enum ContentType
{
    APPLICATION_JSON( "application/json" ),
    APPLICATION_XML( "application/xml" ),
    TEXT_PLAIN( "text/plain" ),
    FORM_URL_ENCODED( "application/x-www-form-urlencoded" );

    private String value;

    ContentType( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static Optional<ContentType> from( String value )
    {
        for ( ContentType type: ContentType.values() )
        {
            if ( type.getValue().equals( value ) )
            {
                return Optional.of( type );
            }
        }

        return Optional.empty();
    }
}