package com.mass3d.notification.logging;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service( "com.mass3d.notification.logging.NotificationLoggingService" )
public class DefaultNotificationLoggingService
    implements NotificationLoggingService
{
    private final NotificationLoggingStore notificationLoggingStore;

    public DefaultNotificationLoggingService( NotificationLoggingStore notificationLoggingStore )
    {
        checkNotNull( notificationLoggingStore );

        this.notificationLoggingStore = notificationLoggingStore;
    }

    @Override
    public ExternalNotificationLogEntry get(String uid )
    {
        return notificationLoggingStore.getByUid( uid );
    }

    @Override
    public ExternalNotificationLogEntry get( int id )
    {
        return notificationLoggingStore.get( id );
    }

    @Override
    public ExternalNotificationLogEntry getByKey( String key )
    {
        return notificationLoggingStore.getByKey( key );
    }

    @Override
    public List<ExternalNotificationLogEntry> getAllLogEntries()
    {
        return notificationLoggingStore.getAll();
    }

    @Override
    public boolean isValidForSending( String key )
    {
        ExternalNotificationLogEntry logEntry = getByKey( key );

        return logEntry == null || logEntry.isAllowMultiple();
    }

    @Override
    public ExternalNotificationLogEntry getByTemplateUid( String templateUid )
    {
        return notificationLoggingStore.getByTemplateUid( templateUid );
    }

    @Override
    public void save( ExternalNotificationLogEntry entry )
    {
        notificationLoggingStore.save( entry );
    }

    @Override
    public void update( ExternalNotificationLogEntry entry )
    {
        notificationLoggingStore.update( entry );
    }
}
