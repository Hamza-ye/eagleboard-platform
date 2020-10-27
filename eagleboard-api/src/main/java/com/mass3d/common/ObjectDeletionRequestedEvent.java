package com.mass3d.common;

import org.springframework.context.ApplicationEvent;

public class ObjectDeletionRequestedEvent
    extends ApplicationEvent
{
    /**
     * Should rollback the transaction if DeleteNotAllowedException is thrown
     */
    private boolean shouldRollBack = true;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ObjectDeletionRequestedEvent( Object source )
    {
        super( source );
    }

    // -------------------------------------------------------------------------
    // Getter && Setter
    // -------------------------------------------------------------------------

    public boolean isShouldRollBack()
    {
        return shouldRollBack;
    }

    public void setShouldRollBack( boolean shouldRollBack )
    {
        this.shouldRollBack = shouldRollBack;
    }
}
