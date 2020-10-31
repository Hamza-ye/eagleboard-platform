package com.mass3d.event;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "eventStatus", namespace = DxfNamespaces.DXF_2_0 )
public enum EventStatus
{
    ACTIVE( 0 ),
    COMPLETED( 1 ),
    VISITED( 2 ),
    SCHEDULE( 3 ),
    OVERDUE( 4 ),
    SKIPPED( 5 );

    private final int value;

    EventStatus( int value )
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static EventStatus fromInt( int status )
    {
        for ( EventStatus eventStatus : EventStatus.values() )
        {
            if ( eventStatus.getValue() == status )
            {
                return eventStatus;
            }
        }

        throw new IllegalArgumentException();
    }

    public static boolean isExistingEvent( EventStatus status )
    {
        return status != null && (COMPLETED.equals( status ) || VISITED.equals( status ));
    }
}

