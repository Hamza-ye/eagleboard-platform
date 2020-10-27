package com.mass3d.scheduling;

import org.springframework.stereotype.Component;

/**
 * @author Henning Håkonsen
 */
@Component( "mockJob" )
public class MockJob
    extends AbstractJob
{
    @Override
    public JobType getJobType()
    {
        return JobType.MOCK;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        try
        {
            Thread.sleep( 10000 );
        }
        catch ( InterruptedException ex )
        {
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }

}
