package com.mass3d.common.event;

import org.springframework.context.ApplicationEvent;

public class ApplicationCacheClearedEvent
    extends ApplicationEvent
{
    public ApplicationCacheClearedEvent()
    {
        super( "ApplicationCacheCleared" );
    }

    public ApplicationCacheClearedEvent( Object source )
    {
        super( source );
    }
}
