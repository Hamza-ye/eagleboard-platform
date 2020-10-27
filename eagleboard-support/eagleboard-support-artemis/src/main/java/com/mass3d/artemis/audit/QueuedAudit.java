package com.mass3d.artemis.audit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class QueuedAudit implements Delayed
{
    private final long origin;

    private final long delay;

    private final Audit audit;

    public QueuedAudit( Audit audit, long delay )
    {
        checkNotNull( audit );

        this.origin = System.currentTimeMillis();
        this.audit = audit;
        this.delay = delay;
    }

    public Audit getAuditItem()
    {
        return audit;
    }

    @Override
    public long getDelay( TimeUnit unit )
    {
        return unit.convert( delay - (System.currentTimeMillis() - origin), TimeUnit.MILLISECONDS );
    }

    @Override
    public int compareTo( Delayed delayed )
    {
        if ( delayed == this )
        {
            return 0;
        }

        if ( delayed instanceof QueuedAudit )
        {
            long diff = delay - ((QueuedAudit) delayed).delay;
            return ((diff == 0) ? 0 : ((diff < 0) ? -1 : 1));
        }

        long d = (getDelay( TimeUnit.MILLISECONDS ) - delayed.getDelay( TimeUnit.MILLISECONDS ));

        return ((d == 0) ? 0 : ((d < 0) ? -1 : 1));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;

        int result = 1;
        result = prime * result + ((audit == null) ? 0 : audit.hashCode());

        return result;
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

        if ( !(obj instanceof QueuedAudit) )
        {
            return false;
        }

        final QueuedAudit other = (QueuedAudit) obj;

        if ( audit == null )
        {
            return other.audit == null;
        }
        else
        {
            return audit.equals( other.audit );
        }
    }
}
