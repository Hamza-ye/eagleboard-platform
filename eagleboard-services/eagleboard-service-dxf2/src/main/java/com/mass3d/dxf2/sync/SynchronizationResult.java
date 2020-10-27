package com.mass3d.dxf2.sync;

public class SynchronizationResult
{
    public final SynchronizationStatus status;
    public final String message;

    private SynchronizationResult( SynchronizationStatus status, String message )
    {
        this.status = status;
        this.message = message;
    }

    public static SynchronizationResult newSuccessResultWithMessage( String message )
    {
        return new SynchronizationResult( SynchronizationStatus.SUCCESS, message );
    }

    public static SynchronizationResult newFailureResultWithMessage( String message )
    {
        return new SynchronizationResult( SynchronizationStatus.FAILURE, message );
    }
}
