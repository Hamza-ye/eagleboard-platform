package com.mass3d.system.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;

public class NotificationMap
{
    private final static int MAX_POOL_TYPE_SIZE = 100;

    private Map<JobType, LinkedHashMap<String, LinkedList<Notification>>> notificationsWithType;

    private Map<JobType, LinkedHashMap<String, Object>> summariesWithType;

    NotificationMap()
    {
        notificationsWithType = new HashMap<>();
        Arrays.stream( JobType.values() )
            .forEach( jobType -> notificationsWithType.put( jobType, new LinkedHashMap<>() ) );

        summariesWithType = new HashMap<>();
        Arrays.stream( JobType.values() )
            .forEach( jobType -> summariesWithType.put( jobType, new LinkedHashMap<>() ) );
    }

    public List<Notification> getLastNotificationsByJobType( JobType jobType )
    {
        LinkedHashMap<String, LinkedList<Notification>> jobTypeNotifications = notificationsWithType.get( jobType );

        if ( jobTypeNotifications.size() == 0 )
        {
            return new ArrayList<>();
        }

        String key = (String) jobTypeNotifications.keySet().toArray()[jobTypeNotifications.size() - 1];

        return jobTypeNotifications.get( key );
    }

    public Map<JobType, LinkedHashMap<String, LinkedList<Notification>>> getNotifications()
    {
        return notificationsWithType;
    }

    public LinkedList<Notification> getNotificationsByJobId( JobType jobType, String jobId )
    {
        if ( notificationsWithType.get( jobType ).containsKey( jobId ) )
        {
            return notificationsWithType.get( jobType ).get( jobId );
        }
        else
        {
            return new LinkedList<>();
        }
    }

    public Map<String, LinkedList<Notification>> getNotificationsWithType( JobType jobType )
    {
        return notificationsWithType.get( jobType );
    }

    public void add( JobConfiguration jobConfiguration, Notification notification )
    {
        String uid = jobConfiguration.getUid();

        LinkedHashMap<String, LinkedList<Notification>> uidNotifications = notificationsWithType
            .get( jobConfiguration.getJobType() );

        LinkedList<Notification> notifications;
        if ( uidNotifications.containsKey( uid ) )
        {
            notifications = uidNotifications.get( uid );
        }
        else
        {
            notifications = new LinkedList<>();
        }

        notifications.addFirst( notification );

        if ( uidNotifications.size() >= MAX_POOL_TYPE_SIZE )
        {
            String key = (String) uidNotifications.keySet().toArray()[0];
            uidNotifications.remove( key );
        }

        uidNotifications.put( uid, notifications );

        notificationsWithType.put( jobConfiguration.getJobType(), uidNotifications );
    }

    public void addSummary( JobConfiguration jobConfiguration, Object summary )
    {
        LinkedHashMap<String, Object> summaries = summariesWithType.get( jobConfiguration.getJobType() );

        if ( summaries.size() >= MAX_POOL_TYPE_SIZE )
        {
            String key = (String) summaries.keySet().toArray()[0];
            summaries.remove( key );
        }

        summaries.put( jobConfiguration.getUid(), summary );
    }

    public Object getSummary( JobType jobType )
    {
        LinkedHashMap<String, Object> summariesForJobType = summariesWithType.get( jobType );

        if ( summariesForJobType.size() == 0 )
        {
            return null;
        }
        else
        {
            return summariesForJobType.values().toArray()[summariesForJobType.size() - 1];
        }
    }

    public Object getSummary( JobType jobType, String jobId )
    {
        return summariesWithType.get( jobType ).get( jobId );
    }

    public Object getJobSummariesForJobType( JobType jobType )
    {
        return summariesWithType.get( jobType );
    }

    public void clear( JobConfiguration jobConfiguration )
    {
        notificationsWithType.get( jobConfiguration.getJobType() ).remove( jobConfiguration.getUid() );
        summariesWithType.get( jobConfiguration.getJobType() ).remove( jobConfiguration.getUid() );
    }
}
