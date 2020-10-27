package com.mass3d.dxf2.sync;

import java.util.Optional;
import com.mass3d.dxf2.synch.AvailabilityStatus;
import com.mass3d.dxf2.synch.SynchronizationManager;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.AbstractJob;

public abstract class SynchronizationJob extends AbstractJob
{
    protected Optional<ErrorReport> validateRemoteServerAvailability( SynchronizationManager synchronizationManager,
        Class<?> klass )
    {
        AvailabilityStatus isRemoteServerAvailable = synchronizationManager.isRemoteServerAvailable();

        if ( !isRemoteServerAvailable.isAvailable() )
        {
            return Optional.of( new ErrorReport( klass, ErrorCode.E7010, isRemoteServerAvailable.getMessage() ) );
        }

        return Optional.empty();
    }
}
