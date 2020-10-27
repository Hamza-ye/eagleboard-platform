package com.mass3d.dxf2.sync;

import com.mass3d.dxf2.synch.SystemInstance;
import com.mass3d.system.util.Clock;

public abstract class DataSynchronizationWithoutPaging
{
    protected Clock clock;
    protected int objectsToSynchronize;
    protected SystemInstance instance;

    public abstract SynchronizationResult synchronizeData();
}
