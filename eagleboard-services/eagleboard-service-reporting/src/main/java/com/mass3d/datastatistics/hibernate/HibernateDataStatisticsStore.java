package com.mass3d.datastatistics.hibernate;

import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.datastatistics.AggregatedStatistics;
import com.mass3d.datastatistics.DataStatistics;
import com.mass3d.datastatistics.DataStatisticsStore;
import com.mass3d.datastatistics.EventInterval;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.util.DateUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository( "com.mass3d.datastatistics.DataStatisticsStore" )
public class HibernateDataStatisticsStore
    extends HibernateIdentifiableObjectStore<DataStatistics>
    implements DataStatisticsStore
{
    public HibernateDataStatisticsStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataStatistics.class, currentUserService, aclService, false );
    }

    // -------------------------------------------------------------------------
    // DataStatisticsStore implementation
    // -------------------------------------------------------------------------

    @Override
    public List<AggregatedStatistics> getSnapshotsInInterval( EventInterval eventInterval, Date startDate, Date endDate )
    {
        final String sql = getQuery( eventInterval, startDate, endDate );

        log.debug( "Get snapshots SQL: " + sql );

        return jdbcTemplate.query( sql, ( resultSet, i ) -> {

            AggregatedStatistics ads = new AggregatedStatistics();

            ads.setYear( resultSet.getInt( "yr" ) );

            if ( eventInterval == EventInterval.DAY )
            {
                ads.setDay( resultSet.getInt( "day" ) );
                ads.setMonth( resultSet.getInt( "mnt" ) );
            }
            else if ( eventInterval == EventInterval.WEEK )
            {
                ads.setWeek( resultSet.getInt( "week" ) );
            }
            else if ( eventInterval == EventInterval.MONTH )
            {
                ads.setMonth( resultSet.getInt( "mnt" ) );
            }

            ads.setMapViews( resultSet.getInt( "mapViews" ) );
            ads.setChartViews( resultSet.getInt( "chartViews" ) );
            ads.setPivotTableViews( resultSet.getInt( "reportTableViews" ) );
            ads.setEventReportViews( resultSet.getInt( "eventReportViews" ) );
            ads.setEventChartViews( resultSet.getInt( "eventChartViews" ) );
            ads.setDashboardViews( resultSet.getInt( "dashboardViews" ) );
            ads.setDataSetReportViews( resultSet.getInt( "dataSetReportViews" ) );
            ads.setTotalViews( resultSet.getInt( "totalViews" ) );
            ads.setAverageViews( resultSet.getInt( "averageViews" ) );
            ads.setAverageMapViews( resultSet.getInt( "averageMapViews" ) );
            ads.setAverageChartViews( resultSet.getInt( "averageChartViews" ) );
            ads.setAveragePivotTableViews( resultSet.getInt( "averageReportTableViews" ) );
            ads.setAverageEventReportViews( resultSet.getInt( "averageEventReportViews" ) );
            ads.setAverageEventChartViews( resultSet.getInt( "averageEventChartViews" ) );
            ads.setAverageDashboardViews( resultSet.getInt( "averageDashboardViews" ) );
            ads.setSavedMaps( resultSet.getInt( "savedMaps" ) );
            ads.setSavedCharts( resultSet.getInt( "savedCharts" ) );
            ads.setSavedPivotTables( resultSet.getInt( "savedReportTables" ) );
            ads.setSavedEventReports( resultSet.getInt( "savedEventReports" ) );
            ads.setSavedEventCharts( resultSet.getInt( "savedEventCharts" ) );
            ads.setSavedDashboards( resultSet.getInt( "savedDashboards" ) );
            ads.setSavedIndicators( resultSet.getInt( "savedIndicators" ) );
            ads.setSavedDataValues( resultSet.getInt( "savedDataValues" ) );
            ads.setActiveUsers( resultSet.getInt( "activeUsers" ) );
            ads.setUsers( resultSet.getInt( "users" ) );

            return ads;
        } );
    }

    private String getQuery( EventInterval eventInterval, Date startDate, Date endDate )
    {
        String sql;

        if ( eventInterval == EventInterval.DAY )
        {
            sql = getDaySql( startDate, endDate );
        }
        else if ( eventInterval == EventInterval.WEEK )
        {
            sql = getWeekSql( startDate, endDate );
        }
        else if ( eventInterval == EventInterval.MONTH )
        {
            sql = getMonthSql( startDate, endDate );
        }
        else if ( eventInterval == EventInterval.YEAR )
        {
            sql = getYearSql( startDate, endDate );
        }
        else
        {
            sql = getDaySql( startDate, endDate );
        }

        return sql;
    }

    /**
     * Creating a SQL for retrieving aggregated data with group by YEAR.
     *
     * @param start start date
     * @param end   end date
     * @return SQL string
     */
    private String getYearSql( Date start, Date end )
    {
        return "select extract(year from created) as yr, " +
            getCommonSql( start, end ) +
            "group by yr order by yr;";
    }

    /**
     * Creating a SQL for retrieving aggregated data with group by YEAR, MONTH.
     *
     * @param start start date
     * @param end   end date
     * @return SQL string
     */
    private String getMonthSql( Date start, Date end )
    {
        return "select extract(year from created) as yr, " +
            "extract(month from created) as mnt, " +
            getCommonSql( start, end ) +
            "group by yr, mnt order by yr, mnt;";
    }

    /**
     * Creating a SQL for retrieving aggregated data with group by YEAR, WEEK.
     * Ignoring week 53.
     *
     * @param start start date
     * @param end   end date
     * @return SQL string
     */
    private String getWeekSql( Date start, Date end )
    {
        return "select extract(year from created) as yr, " +
            "extract(week from created) as week, " +
            getCommonSql( start, end ) +
            "and extract(week from created) < 53 " +
            "group by yr, week order by yr, week;";
    }

    /**
     * Creating a SQL for retrieving aggregated data with group by YEAR, DAY.
     *
     * @param start start date
     * @param end   end date
     * @return SQL string
     */
    private String getDaySql( Date start, Date end )
    {
        return "select extract(year from created) as yr, " +
            "extract(month from created) as mnt," +
            "extract(day from created) as day, " +
            getCommonSql( start, end ) +
            "group by yr, mnt, day order by yr, mnt, day;";
    }

    /**
     * Part of SQL witch is always the same in the different intervals YEAR,
     * MONTH, WEEK and DAY.
     *
     * @param start start date
     * @param end   end date
     * @return SQL string
     */
    private String getCommonSql( Date start, Date end )
    {
        return
            "cast(round(cast(sum(mapviews) as numeric),0) as int) as mapViews," +
            "cast(round(cast(sum(chartviews) as numeric),0) as int) as chartViews," +
            "cast(round(cast(sum(reporttableviews) as numeric),0) as int) as reportTableViews, " +
            "cast(round(cast(sum(eventreportviews) as numeric),0) as int) as eventReportViews, " +
            "cast(round(cast(sum(eventchartviews) as numeric),0) as int) as eventChartViews," +
            "cast(round(cast(sum(dashboardviews) as numeric),0) as int) as dashboardViews, " +
            "cast(round(cast(sum(datasetreportviews) as numeric),0) as int) as dataSetReportViews, " +
            "max(active_users) as activeUsers," +
            "coalesce(sum(totalviews)/nullif(max(active_users), 0), 0) as averageViews," +
            "coalesce(sum(mapviews)/nullif(max(active_users), 0), 0) as averageMapViews, " +
            "coalesce(sum(chartviews)/nullif(max(active_users), 0), 0) as averageChartViews, " +
            "coalesce(sum(reporttableviews)/nullif(max(active_users), 0), 0) as averageReportTableViews, " +
            "coalesce(sum(eventreportviews)/nullif(max(active_users), 0), 0) as averageEventReportViews, " +
            "coalesce(sum(eventchartviews)/nullif(max(active_users), 0), 0) as averageEventChartViews, " +
            "coalesce(sum(dashboardviews)/nullif(max(active_users), 0), 0) as averageDashboardViews, " +
            "cast(round(cast(sum(totalviews) as numeric),0) as int) as totalViews," +
            "cast(round(cast(sum(maps) as numeric),0) as int) as savedMaps," +
            "cast(round(cast(sum(charts) as numeric),0) as int) as savedCharts," +
            "cast(round(cast(sum(reporttables) as numeric),0) as int) as savedReportTables," +
            "cast(round(cast(sum(eventreports) as numeric),0) as int) as savedEventReports," +
            "cast(round(cast(sum(eventcharts) as numeric),0) as int) as savedEventCharts," +
            "cast(round(cast(sum(dashboards) as numeric),0) as int) as savedDashboards, " +
            "cast(round(cast(sum(indicators) as numeric),0) as int) as savedIndicators," +
            "cast(round(cast(sum(datavalues) as numeric),0) as int) as savedDataValues," +
            "max(users) as users from datastatistics " +
            "where created >= '" + DateUtils.getLongDateString( start ) + "' " +
            "and created <= '" + DateUtils.getLongDateString( end ) + "' ";
    }
}
