package com.mass3d.datastatistics;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.mass3d.analytics.SortOrder;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.datasummary.DataSummary;
import com.mass3d.datavalue.DataValueService;
import com.mass3d.indicator.Indicator;
import com.mass3d.statistics.StatisticsProvider;
import com.mass3d.user.User;
import com.mass3d.user.UserInvitationStatus;
import com.mass3d.user.UserQueryParams;
import com.mass3d.user.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.datastatistics.DataStatisticsService" )
@Transactional
public class DefaultDataStatisticsService
    implements DataStatisticsService
{
    @Autowired
    private DataStatisticsStore dataStatisticsStore;

    @Autowired
    private DataStatisticsEventStore dataStatisticsEventStore;

    @Autowired
    private UserService userService;

    @Autowired
    private IdentifiableObjectManager idObjectManager;

    @Autowired
    private DataValueService dataValueService;

    @Autowired
    private StatisticsProvider statisticsProvider;

//    @Autowired
//    private ProgramStageInstanceService programStageInstanceService;
//
//    @Autowired
//    private VisualizationStore visualizationStore;

    // -------------------------------------------------------------------------
    // DataStatisticsService implementation
    // -------------------------------------------------------------------------

    @Override
    public int addEvent( DataStatisticsEvent event )
    {
        dataStatisticsEventStore.save( event );

        return event.getId();
    }

    @Override
    public List<AggregatedStatistics> getReports( Date startDate, Date endDate, EventInterval eventInterval )
    {
        return dataStatisticsStore.getSnapshotsInInterval( eventInterval, startDate, endDate );
    }

    @Override
    public DataStatistics getDataStatisticsSnapshot( Date day )
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime( day );
        cal.add( Calendar.DATE, -1 );
        Date startDate = cal.getTime();
        Date now = new Date();
        long diff = now.getTime() - startDate.getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

//        double savedCharts = visualizationStore.countChartsCreated( startDate );
//        double savedReportTables = visualizationStore.countPivotTablesCreated( startDate );
//        double savedMaps = idObjectManager.getCountByCreated( com.mass3d.mapping.Map.class, startDate );
//        double savedVisualizations = idObjectManager.getCountByCreated( Visualization.class, startDate );
//        double savedEventReports = idObjectManager.getCountByCreated( EventReport.class, startDate );
//        double savedEventCharts = idObjectManager.getCountByCreated( EventChart.class, startDate );
//        double savedDashboards = idObjectManager.getCountByCreated( Dashboard.class, startDate );
        double savedIndicators = idObjectManager.getCountByCreated( Indicator.class, startDate );
        double savedDataValues = dataValueService.getDataValueCount( days );
        int activeUsers = userService.getActiveUsersCount( 1 );
        int users = idObjectManager.getCount( User.class );

        Map<DataStatisticsEventType, Double> eventCountMap = dataStatisticsEventStore.getDataStatisticsEventCount( startDate, day );

        DataStatistics dataStatistics = new DataStatistics(
            eventCountMap.get( DataStatisticsEventType.MAP_VIEW ),
            eventCountMap.get( DataStatisticsEventType.CHART_VIEW ),
            eventCountMap.get( DataStatisticsEventType.REPORT_TABLE_VIEW ),
            eventCountMap.get( DataStatisticsEventType.VISUALIZATION_VIEW ),
            eventCountMap.get( DataStatisticsEventType.EVENT_REPORT_VIEW ),
            eventCountMap.get( DataStatisticsEventType.EVENT_CHART_VIEW ),
            eventCountMap.get( DataStatisticsEventType.DASHBOARD_VIEW ),
            eventCountMap.get( DataStatisticsEventType.DATA_SET_REPORT_VIEW ),
            eventCountMap.get( DataStatisticsEventType.TOTAL_VIEW ),
//            savedMaps, savedCharts, savedReportTables, savedVisualizations, savedEventReports,
//            savedEventCharts, savedDashboards,
            null, null, null, null, null,
            null, null,
            savedIndicators, savedDataValues, activeUsers, users );

        return dataStatistics;
    }

    @Override
    public long saveDataStatistics( DataStatistics dataStatistics )
    {
        dataStatisticsStore.save( dataStatistics );

        return dataStatistics.getId();
    }

    @Override
    public long saveDataStatisticsSnapshot()
    {
        return saveDataStatistics( getDataStatisticsSnapshot( new Date() ) );
    }

    @Override
    public List<FavoriteStatistics> getTopFavorites( DataStatisticsEventType eventType, int pageSize, SortOrder sortOrder, String username )
    {
        return dataStatisticsEventStore.getFavoritesData( eventType, pageSize, sortOrder, username );
    }

    @Override
    public FavoriteStatistics getFavoriteStatistics( String uid )
    {
        return dataStatisticsEventStore.getFavoriteStatistics( uid );
    }

    @Override
    public DataSummary getSystemStatisticsSummary()
    {
        DataSummary statistics = new DataSummary();

        /* database object counts */
        Map<String, Integer> objectCounts = new HashMap<>(  );
        statisticsProvider.getObjectCounts().forEach( (object, count) -> objectCounts.put( object.getValue(), count ));
        statistics.setObjectCounts( objectCounts );

        /* active users count */
        Date lastHour = new DateTime().minusHours( 1 ).toDate();

        Map<Integer, Integer> activeUsers = new HashMap<>(  );

        activeUsers.put( 0,  userService.getActiveUsersCount( lastHour ));
        activeUsers.put( 1,  userService.getActiveUsersCount( 0 ));
        activeUsers.put( 2,  userService.getActiveUsersCount( 1 ));
        activeUsers.put( 7,  userService.getActiveUsersCount( 7 ));
        activeUsers.put( 30,  userService.getActiveUsersCount( 30 ));

        statistics.setActiveUsers( activeUsers );

        /* user invitations count */
        Map<String, Integer> userInvitations = new HashMap<>(  );

        UserQueryParams inviteAll = new UserQueryParams();
        inviteAll.setInvitationStatus( UserInvitationStatus.ALL );
        userInvitations.put( UserInvitationStatus.ALL.getValue(),  userService.getUserCount( inviteAll ) );

        UserQueryParams inviteExpired = new UserQueryParams();
        inviteExpired.setInvitationStatus( UserInvitationStatus.EXPIRED );
        userInvitations.put( UserInvitationStatus.EXPIRED.getValue(),  userService.getUserCount( inviteExpired ) );

        statistics.setUserInvitations( userInvitations );

        /* data value count */
        Map<Integer, Integer> dataValueCount = new HashMap<>(  );

        dataValueCount.put( 0, dataValueService.getDataValueCount( 0 ));
        dataValueCount.put( 1, dataValueService.getDataValueCount( 1 ));
        dataValueCount.put( 7, dataValueService.getDataValueCount( 7 ));
        dataValueCount.put( 30, dataValueService.getDataValueCount( 30 ));

        statistics.setDataValueCount( dataValueCount );

        /* event count */
        Map<Integer, Long> eventCount = new HashMap<>(  );

//        eventCount.put( 0, programStageInstanceService.getProgramStageInstanceCount( 0 ) );
//        eventCount.put( 1, programStageInstanceService.getProgramStageInstanceCount( 1 ) );
//        eventCount.put( 7, programStageInstanceService.getProgramStageInstanceCount( 7 ) );
//        eventCount.put( 30, programStageInstanceService.getProgramStageInstanceCount( 30 ) );

        statistics.setEventCount( eventCount );

        return statistics;
    }
}
