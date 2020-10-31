package com.mass3d.notification;

public enum SendStrategy
{
    COLLECTIVE_SUMMARY( "Summary" ),
    SINGLE_NOTIFICATION( "Single" );

    private String description;

    SendStrategy()
    {
    }

    SendStrategy( String description )
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}