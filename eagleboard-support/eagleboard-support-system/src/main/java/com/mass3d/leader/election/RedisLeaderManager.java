package com.mass3d.leader.election;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import com.mass3d.scheduling.SchedulingManager;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

/**
 * Takes care of the leader election implementation backed by redis.
 *
 */
@Slf4j
public class RedisLeaderManager implements LeaderManager
{
    private static final String key = "dhis2:leader";

    private static final String CLUSTER_LEADER_RENEWAL = "Cluster leader renewal";

    private String nodeId;

    private Long timeToLiveSeconds;

    private SchedulingManager schedulingManager;

    private RedisTemplate<String, ?> redisTemplate;

    public RedisLeaderManager( Long timeToLiveMinutes, RedisTemplate<String, ?> redisTemplate )
    {
        this.nodeId = UUID.randomUUID().toString();
        log.info( "Setting up redis based leader manager on NodeId:" + this.nodeId );
        this.timeToLiveSeconds = timeToLiveMinutes * 60;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void renewLeader()
    {
        if ( isLeader() )
        {
            log.debug( "Renewing leader with nodeId:" + this.nodeId );
            redisTemplate.getConnectionFactory().getConnection().expire( key.getBytes(), timeToLiveSeconds );
        }
    }

    @Override
    public void electLeader()
    {
        log.debug( "Election attempt by nodeId:" + this.nodeId );
        redisTemplate.getConnectionFactory().getConnection().set( key.getBytes(), nodeId.getBytes(),
            Expiration.from( timeToLiveSeconds, TimeUnit.SECONDS ), SetOption.SET_IF_ABSENT );
        if ( isLeader() )
        {
            renewLeader();
            Calendar calendar = Calendar.getInstance();
            calendar.add( Calendar.SECOND, (int) (this.timeToLiveSeconds / 2) );
            log.debug( "Next leader renewal job nodeId:" + this.nodeId + " set at " + calendar.getTime().toString() );
            JobConfiguration leaderRenewalJobConfiguration = new JobConfiguration( CLUSTER_LEADER_RENEWAL,
                JobType.LEADER_RENEWAL, null, true );
            leaderRenewalJobConfiguration.setLeaderOnlyJob( true );
            schedulingManager.scheduleJobWithStartTime( leaderRenewalJobConfiguration, calendar.getTime() );
        }
    }

    @Override
    public boolean isLeader()
    {
        byte[] leaderIdBytes = redisTemplate.getConnectionFactory().getConnection().get( key.getBytes() );
        String leaderId = null;
        if ( leaderIdBytes != null )
        {
            leaderId = new String( leaderIdBytes );
        }
        return nodeId.equals( leaderId );
    }

    @Override
    public void setSchedulingManager( SchedulingManager schedulingManager )
    {
        this.schedulingManager = schedulingManager;
    }

}