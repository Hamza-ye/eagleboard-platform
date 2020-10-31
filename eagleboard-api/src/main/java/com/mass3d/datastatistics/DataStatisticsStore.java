package com.mass3d.datastatistics;

import java.util.Date;
import java.util.List;
import com.mass3d.common.GenericStore;

public interface DataStatisticsStore 
    extends GenericStore<DataStatistics>
{
    /**
     * Retrieves data from database and maps aggregated data to 
     * AggregatedStatistic object.
     *
     * @param eventInterval interval of DAY, MONTH, WEEK, YEAR.
     * @param startDate the start date.
     * @param endDate the end date.
     * 
     * @return a list of AggregatedStatistics instances.
     */
    List<AggregatedStatistics> getSnapshotsInInterval(EventInterval eventInterval, Date startDate,
        Date endDate);
}
