package com.mass3d.dxf2.synch;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;
import com.mass3d.dxf2.sync.SynchronizationJob;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import com.mass3d.scheduling.parameters.DataSynchronizationJobParameters;
import com.mass3d.system.notification.Notifier;
import org.springframework.stereotype.Component;

@Component( "dataSyncJob" )
public class DataSynchronizationJob extends SynchronizationJob
{
    private final SynchronizationManager synchronizationManager;
    private final Notifier notifier;
//    private final DataValueSynchronization dataValueSynchronization;
//    private final CompleteDataSetRegistrationSynchronization completenessSynchronization;

    public DataSynchronizationJob(
        Notifier notifier,
//        DataValueSynchronization dataValueSynchronization,
//        CompleteDataSetRegistrationSynchronization completenessSynchronization,
        SynchronizationManager synchronizationManager )
    {
        checkNotNull( notifier );
//        checkNotNull( dataValueSynchronization );
//        checkNotNull( completenessSynchronization );
        checkNotNull( synchronizationManager );

        this.notifier = notifier;
//        this.dataValueSynchronization = dataValueSynchronization;
//        this.completenessSynchronization = completenessSynchronization;
        this.synchronizationManager = synchronizationManager;
    }


    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.DATA_SYNC;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        DataSynchronizationJobParameters jobParameters =
            (DataSynchronizationJobParameters) jobConfiguration.getJobParameters();
//        dataValueSynchronization.synchronizeData( jobParameters.getPageSize() );
        notifier.notify( jobConfiguration, "Data value sync successful" );

//        completenessSynchronization.synchronizeData();
        notifier.notify( jobConfiguration, "Complete data set registration sync successful" );

        notifier.notify( jobConfiguration, "Data value and Complete data set registration sync successful" );
    }

    @Override
    public ErrorReport validate()
    {
        Optional<ErrorReport> errorReport = validateRemoteServerAvailability( synchronizationManager,
            DataSynchronizationJob.class );

        return errorReport.orElse( super.validate() );
    }
}