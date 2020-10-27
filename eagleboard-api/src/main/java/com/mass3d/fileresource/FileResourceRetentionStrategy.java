package com.mass3d.fileresource;

import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.Years;

public enum FileResourceRetentionStrategy
{
    NONE ( Seconds.ZERO.toPeriod() ),
    THREE_MONTHS ( Months.THREE.toPeriod() ),
    ONE_YEAR ( Years.ONE.toPeriod() ),
    FOREVER ( null );

    private Period retentionTime;

    FileResourceRetentionStrategy( Period retentionTime )
    {
        this.retentionTime = retentionTime;
    }

    public Period getRetentionTime()
    {
        return retentionTime;
    }
}
