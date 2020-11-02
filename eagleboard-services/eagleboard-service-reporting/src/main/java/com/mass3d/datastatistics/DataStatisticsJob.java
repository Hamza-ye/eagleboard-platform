package com.mass3d.datastatistics;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.scheduling.AbstractJob;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import org.springframework.stereotype.Component;

@Slf4j
@Component( "dataStatisticsJob" )
public class DataStatisticsJob
    extends AbstractJob
{
    private final DataStatisticsService dataStatisticsService;

    public DataStatisticsJob( DataStatisticsService dataStatisticsService )
    {
        checkNotNull( dataStatisticsService );
        this.dataStatisticsService = dataStatisticsService;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.DATA_STATISTICS;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        long id = dataStatisticsService.saveDataStatisticsSnapshot();

        if ( id > 0 )
        {
            log.info( "Saved data statistics snapshot" );
        }
    }

}
