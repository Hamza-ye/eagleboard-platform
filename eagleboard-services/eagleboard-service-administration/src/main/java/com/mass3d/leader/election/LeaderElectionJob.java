package com.mass3d.leader.election;

import com.mass3d.scheduling.AbstractJob;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import org.springframework.stereotype.Component;

/**
 * Job that attempts to elect the current instance as the leader of the cluster.
 *
 */
@Component
public class LeaderElectionJob extends AbstractJob
{
    private LeaderManager leaderManager;

    public LeaderElectionJob( LeaderManager leaderManager )
    {
        this.leaderManager = leaderManager;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.LEADER_ELECTION;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        leaderManager.electLeader();
    }
}
