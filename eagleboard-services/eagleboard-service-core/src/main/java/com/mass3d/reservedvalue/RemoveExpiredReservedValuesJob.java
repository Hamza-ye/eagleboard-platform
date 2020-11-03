package com.mass3d.reservedvalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.scheduling.AbstractJob;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import org.springframework.stereotype.Component;

@Component( "removeExpiredReservedValuesJob" )
public class RemoveExpiredReservedValuesJob
    extends AbstractJob
{
    private final ReservedValueService reservedValueService;

    public RemoveExpiredReservedValuesJob( ReservedValueService reservedValueService )
    {
        checkNotNull( reservedValueService );

        this.reservedValueService = reservedValueService;
    }

    @Override
    public JobType getJobType()
    {
        return JobType.REMOVE_EXPIRED_RESERVED_VALUES;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        reservedValueService.removeExpiredReservations();
    }
}