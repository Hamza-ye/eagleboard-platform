package com.mass3d.datastatistics;

/**
 * Types of DataStatisticsEvents.
 *
 */
public enum DataStatisticsEventType
{
    REPORT_TABLE_VIEW( "reporttable" ),
    VISUALIZATION_VIEW( "visualization" ),
    CHART_VIEW( "chart" ),
    MAP_VIEW( "map" ),
    EVENT_REPORT_VIEW( "eventreport" ),
    EVENT_CHART_VIEW( "eventchart" ),
    DASHBOARD_VIEW( "dashboard" ),
    DATA_SET_REPORT_VIEW( "dataset" ),
    TOTAL_VIEW( null );

    private String table;

    DataStatisticsEventType( String table )
    {
        this.table = table;
    }

    public String getTable()
    {
        return table;
    }
}
