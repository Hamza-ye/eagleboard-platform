package com.mass3d.dxf2.sync;

import com.mass3d.dxf2.synch.SystemInstance;
import com.mass3d.system.util.Clock;

public abstract class DataSynchronizationWithPaging
{
    protected boolean syncResult = false;
    protected Clock clock;
    protected int objectsToSynchronize;
    protected SystemInstance instance;
    protected int pages;

    public abstract SynchronizationResult synchronizeData( final int pageSize );

    protected void runSyncWithPaging( int pageSize )
    {
        syncResult = true;

        for ( int page = 1; page <= pages; page++ )
        {
            synchronizePage( page, pageSize );
        }
    }

    protected abstract void synchronizePage( int page, int pageSize );
}
