package com.mass3d.analytics;

public enum AnalyticsTableType
{
    DATA_VALUE( "analytics", true, true ),
    COMPLETENESS( "analytics_completeness", true, true ),
    COMPLETENESS_TARGET( "analytics_completenesstarget", false, false ),
    ORG_UNIT_TARGET( "analytics_orgunittarget", false, false ),
    EVENT( "analytics_event", false, true ),
    ENROLLMENT( "analytics_enrollment", false, false ),
    VALIDATION_RESULT( "analytics_validationresult", true, false );

    private String tableName;

    private boolean periodDimension;

    private boolean latestPartition;

    AnalyticsTableType( String tableName, boolean periodDimension, boolean latestPartition )
    {
        this.tableName = tableName;
        this.periodDimension = periodDimension;
        this.latestPartition = latestPartition;
    }

    public String getTableName()
    {
        return tableName;
    }

    public boolean hasPeriodDimension()
    {
        return periodDimension;
    }

    public boolean hasLatestPartition()
    {
        return latestPartition;
    }
}
