package com.mass3d.datastatistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mass3d.common.BaseIdentifiableObject;

/**
 * DataStatistics object to be saved as snapshot.
 *
 */
public class DataStatistics
    extends BaseIdentifiableObject
{
    private Double mapViews;
    private Double chartViews;
    private Double reportTableViews;
    private Double visualizationViews;
    private Double eventReportViews;
    private Double eventChartViews;
    private Double dashboardViews;
    private Double dataSetReportViews;
    private Double totalViews;
    private Double savedMaps;
    private Double savedCharts;
    private Double savedReportTables;
    private Double savedVisualizations;
    private Double savedEventReports;
    private Double savedEventCharts;
    private Double savedDashboards;
    private Double savedIndicators;
    private Double savedDataValues;
    private Integer activeUsers;
    private Integer users;

    public DataStatistics()
    {
    }

    public DataStatistics( Double mapViews, Double chartViews, Double reportTableViews, Double visualizationViews,
        Double eventReportViews, Double eventChartViews, Double dashboardViews, Double dataSetReportViews,
        Double totalViews, Double savedMaps, Double savedCharts, Double savedReportTables, Double savedVisualizations,
        Double savedEventReports, Double savedEventCharts, Double savedDashboards, Double savedIndicators,
        Double savedDataValues, Integer activeUsers, Integer users )
    {
        this.mapViews = mapViews;
        this.chartViews = chartViews;
        this.reportTableViews = reportTableViews;
        this.visualizationViews = visualizationViews;
        this.eventReportViews = eventReportViews;
        this.eventChartViews = eventChartViews;
        this.dashboardViews = dashboardViews;
        this.dataSetReportViews = dataSetReportViews;
        this.totalViews = totalViews;
        this.savedMaps = savedMaps;
        this.savedCharts = savedCharts;
        this.savedReportTables = savedReportTables;
        this.savedVisualizations = savedVisualizations;
        this.savedEventReports = savedEventReports;
        this.savedEventCharts = savedEventCharts;
        this.savedDashboards = savedDashboards;
        this.savedIndicators = savedIndicators;
        this.savedDataValues = savedDataValues;
        this.activeUsers = activeUsers;
        this.users = users;
    }

    @JsonProperty
    public Integer getActiveUsers()
    {
        return activeUsers;
    }

    public void setActiveUsers( Integer activeUsers )
    {
        this.activeUsers = activeUsers;
    }

    @JsonProperty
    public Double getMapViews()
    {
        return mapViews;
    }

    public void setMapViews( Double mapViews )
    {
        this.mapViews = mapViews;
    }

    @JsonProperty
    public Double getChartViews()
    {
        return chartViews;
    }

    public void setChartViews( Double chartViews )
    {
        this.chartViews = chartViews;
    }

    @JsonProperty
    public Double getReportTableViews()
    {
        return reportTableViews;
    }

    public void setReportTableViews( Double reportTableViews )
    {
        this.reportTableViews = reportTableViews;
    }

    @JsonProperty
    public Double getVisualizationViews()
    {
        return visualizationViews;
    }

    public void setVisualizationViews( Double visualizationViews )
    {
        this.visualizationViews = visualizationViews;
    }

    @JsonProperty
    public Double getEventReportViews()
    {
        return eventReportViews;
    }

    public void setEventReportViews( Double eventReportViews )
    {
        this.eventReportViews = eventReportViews;
    }

    @JsonProperty
    public Double getEventChartViews()
    {
        return eventChartViews;
    }

    public void setEventChartViews( Double eventChartViews )
    {
        this.eventChartViews = eventChartViews;
    }

    @JsonProperty
    public Double getDashboardViews()
    {
        return dashboardViews;
    }

    public void setDashboardViews( Double dashboardViews )
    {
        this.dashboardViews = dashboardViews;
    }

    @JsonProperty
    public Double getDataSetReportViews()
    {
        return dataSetReportViews;
    }

    public void setDataSetReportViews( Double dataSetReportViews )
    {
        this.dataSetReportViews = dataSetReportViews;
    }

    @JsonProperty
    public Double getTotalViews()
    {
        return totalViews;
    }

    public void setTotalViews( Double totalViews )
    {
        this.totalViews = totalViews;
    }

    @JsonProperty
    public Double getSavedMaps()
    {
        return savedMaps;
    }

    public void setSavedMaps( Double savedMaps )
    {
        this.savedMaps = savedMaps;
    }

    @JsonProperty
    public Double getSavedCharts()
    {
        return savedCharts;
    }

    public void setSavedCharts( Double savedCharts )
    {
        this.savedCharts = savedCharts;
    }

    @JsonProperty
    public Double getSavedReportTables()
    {
        return savedReportTables;
    }

    public void setSavedReportTables( Double savedReportTables )
    {
        this.savedReportTables = savedReportTables;
    }

    @JsonProperty
    public Double getSavedVisualizations()
    {
        return savedVisualizations;
    }

    public void setSavedVisualizations( Double savedVisualizations )
    {
        this.savedVisualizations = savedVisualizations;
    }

    @JsonProperty
    public Double getSavedEventReports()
    {
        return savedEventReports;
    }

    public void setSavedEventReports( Double savedEventReports )
    {
        this.savedEventReports = savedEventReports;
    }

    @JsonProperty
    public Double getSavedEventCharts()
    {
        return savedEventCharts;
    }

    public void setSavedEventCharts( Double savedEventCharts )
    {
        this.savedEventCharts = savedEventCharts;
    }

    @JsonProperty
    public Double getSavedDashboards()
    {
        return savedDashboards;
    }

    public void setSavedDashboards( Double savedDashboards )
    {
        this.savedDashboards = savedDashboards;
    }

    @JsonProperty
    public Double getSavedIndicators()
    {
        return savedIndicators;
    }

    public void setSavedIndicators( Double savedIndicators )
    {
        this.savedIndicators = savedIndicators;
    }

    @JsonProperty
    public Double getSavedDataValues()
    {
        return savedDataValues;
    }

    public void setSavedDataValues( Double savedDataValues )
    {
        this.savedDataValues = savedDataValues;
    }

    @JsonProperty
    public Integer getUsers()
    {
        return users;
    }

    @JsonProperty
    public void setUsers( Integer users )
    {
        this.users = users;
    }

    @Override
    public String toString()
    {
        return super.toString() + "DataStatistics{" +
            "mapViews=" + mapViews +
            ", chartViews=" + chartViews +
            ", reportTableViews=" + reportTableViews +
            ", visualizationViews=" + visualizationViews +
            ", eventReportViews=" + eventReportViews +
            ", eventChartViews=" + eventChartViews +
            ", dashboardViews=" + dashboardViews +
            ", totalViews=" + totalViews +
            ", savedMaps=" + savedMaps +
            ", savedCharts=" + savedCharts +
            ", savedReportTables=" + savedReportTables +
            ", savedVisualizations=" + savedVisualizations +
            ", savedEventReports=" + savedEventReports +
            ", savedEventCharts=" + savedEventCharts +
            ", savedDashboards=" + savedDashboards +
            ", savedIndicators=" + savedIndicators +
            ", savedDataValues=" + savedDataValues +
            ", activeUsers=" + activeUsers +
            ", users=" + users +
            '}';
    }
}
