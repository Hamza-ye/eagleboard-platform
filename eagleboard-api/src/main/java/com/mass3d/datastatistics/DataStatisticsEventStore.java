package com.mass3d.datastatistics;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.mass3d.analytics.SortOrder;
import com.mass3d.common.GenericStore;

public interface DataStatisticsEventStore
    extends GenericStore<DataStatisticsEvent>
{
    /**
     * Method for retrieving aggregated event count data.
     *
     * @param startDate the start date.
     * @param endDate the end date.
     * @return a map between DataStatisticsEventTypes and counts.
     */
    Map<DataStatisticsEventType, Double> getDataStatisticsEventCount(Date startDate, Date endDate);

    /**
     * Returns top favorites by views
     *
     * @param eventType that should be counted
     * @param pageSize number of favorites
     * @param sortOrder sort order of the favorites
     * @param username of user
     * @return list of FavoriteStatistics
     */
    List<FavoriteStatistics> getFavoritesData(DataStatisticsEventType eventType, int pageSize,
        SortOrder sortOrder, String username);

    /**
     * Returns data statistics for the favorite with the given identifier.
     * 
     * @param uid the favorite identifier.
     * @return data statistics for the favorite with the given identifier.
     */
    FavoriteStatistics getFavoriteStatistics(String uid);
}