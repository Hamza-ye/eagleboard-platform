package com.mass3d.datastatistics;

import java.util.Date;
import java.util.List;
import com.mass3d.analytics.SortOrder;
import com.mass3d.datasummary.DataSummary;

public interface DataStatisticsService
{
    /**
     * Adds an DataStatistics event.
     *
     * @param event object to be saved
     * @return id of the object in the database
     */
    int addEvent(DataStatisticsEvent event);

    /**
     * Gets number of saved events from a start date to an end date.
     *
     * @param startDate     start date
     * @param endDate       end date
     * @param eventInterval event interval
     * @return list of reports
     */
    List<AggregatedStatistics> getReports(Date startDate, Date endDate, EventInterval eventInterval);

    /**
     * Returns a DataStatistics instance for the given day.
     *
     * @param day the day to generate the DataStatistics instance for.
     * @return a DataStatistics instance for the given day.
     */
    DataStatistics getDataStatisticsSnapshot(Date day);

    /**
     * Saves a DataStatistics instance.
     *
     * @param dataStatistics the DataStatistics instance.
     * @return identifier of the persisted DataStatistics object.
     */
    long saveDataStatistics(DataStatistics dataStatistics);

    /**
     * Gets all information and creates a DataStatistics object and persists it.
     *
     * @return identifier of the persisted DataStatistics object.
     */
    long saveDataStatisticsSnapshot();

    /**
     * Returns top favorites by views
     *
     * @param eventType that should be counted
     * @param pageSize  number of favorites
     * @param sortOrder sort order of the favorites
     * @param username  name of user, makes the query specified to this user
     * @return list of FavoriteStatistics
     */

    List<FavoriteStatistics> getTopFavorites(DataStatisticsEventType eventType, int pageSize,
        SortOrder sortOrder,
        String username);

    /**
     * Returns data statistics for the favorite with the given identifier.
     *
     * @param uid the favorite identifier.
     * @return data statistics for the favorite with the given identifier.
     */
    FavoriteStatistics getFavoriteStatistics(String uid);

    /**
     * Returns a DataSummary instance with Data Statistics about System.
     *
     * @return a DataSummary instance with Data Statistics about System.
     */
    DataSummary getSystemStatisticsSummary();
}
