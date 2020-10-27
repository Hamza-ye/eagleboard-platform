package com.mass3d.scheduling;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.mass3d.scheduling.parameters.*;

/**
 * Enum describing the different jobs in the system. Each job has a key, class, configurable
 * status and possibly a map containing relative endpoints for possible parameters.
 * <p>
 * The key must match the jobs bean name so that the {@link SchedulingManager} can fetch
 * the correct job
 *
 */
public enum JobType
{
    DATA_STATISTICS( "dataStatisticsJob", false ),
    DATA_INTEGRITY( "dataIntegrityJob", true ),
    RESOURCE_TABLE( "resourceTableJob", true ),
    ANALYTICS_TABLE( "analyticsTableJob", true, SchedulingType.CRON, AnalyticsJobParameters.class, ImmutableMap
        .of(
        "skipTableTypes", "/api/analytics/tableTypes" ) ),
    CONTINUOUS_ANALYTICS_TABLE( "continuousAnalyticsTableJob", true, SchedulingType.FIXED_DELAY, ContinuousAnalyticsJobParameters.class, ImmutableMap
        .of(
        "skipTableTypes", "/api/analytics/tableTypes" ) ),
    DATA_SYNC( "dataSyncJob", true, SchedulingType.CRON, DataSynchronizationJobParameters.class, null ),
    TRACKER_PROGRAMS_DATA_SYNC( "trackerProgramsDataSyncJob", true, SchedulingType.CRON, TrackerProgramsDataSynchronizationJobParameters.class, null ),
    EVENT_PROGRAMS_DATA_SYNC( "eventProgramsDataSyncJob", true, SchedulingType.CRON, EventProgramsDataSynchronizationJobParameters.class, null ),
    FILE_RESOURCE_CLEANUP( "fileResourceCleanUpJob", false ),
    IMAGE_PROCESSING( "imageProcessingJob", false ),
    META_DATA_SYNC( "metadataSyncJob", true, SchedulingType.CRON, MetadataSyncJobParameters.class, null ),
    SMS_SEND( "sendSmsJob", false, SchedulingType.CRON, SmsJobParameters.class, null ),
    SEND_SCHEDULED_MESSAGE( "sendScheduledMessageJob", true ),
    PROGRAM_NOTIFICATIONS( "programNotificationsJob", true ),
    VALIDATION_RESULTS_NOTIFICATION( "validationResultNotificationJob", false ),
    CREDENTIALS_EXPIRY_ALERT( "credentialsExpiryAlertJob", false ),
    MONITORING( "monitoringJob", true, SchedulingType.CRON, MonitoringJobParameters.class, ImmutableMap
        .of(
        "relativePeriods", "/api/periodTypes/relativePeriodTypes", "validationRuleGroups", "/api/validationRuleGroups" ) ),
    PUSH_ANALYSIS( "pushAnalysisJob", true, SchedulingType.CRON, PushAnalysisJobParameters.class, ImmutableMap
        .of(
        "pushAnalysis", "/api/pushAnalysis" ) ),
    PREDICTOR( "predictorJob", true, SchedulingType.CRON, PredictorJobParameters.class, ImmutableMap
        .of(
        "predictors", "/api/predictors", "predictorGroups", "/api/predictorGroups" ) ),
    DATA_SET_NOTIFICATION( "dataSetNotificationJob", false ),
    REMOVE_EXPIRED_RESERVED_VALUES( "removeExpiredReservedValuesJob", false ),
    TRACKER_IMPORT_JOB( "trackerImportJob", false ),
    TRACKER_IMPORT_NOTIFICATION_JOB( "trackerImportNotificationJob", false ),
    TRACKER_IMPORT_RULE_ENGINE_JOB( "trackerImportRuleEngineJob", false ),

    // Internal jobs
    LEADER_ELECTION( "leaderElectionJob", false ),
    LEADER_RENEWAL( "leaderRenewalJob", false ),
    COMPLETE_DATA_SET_REGISTRATION_IMPORT( null, false ),
    DATAVALUE_IMPORT_INTERNAL( null, false ),
    METADATA_IMPORT( null, false ),
    DATAVALUE_IMPORT( null, false ),
    EVENT_IMPORT( null, false ),
    ENROLLMENT_IMPORT( null, false ),
    TEI_IMPORT( null, false ),

    // Testing purposes
    MOCK( "mockJob", false, SchedulingType.CRON, MockJobParameters.class, null ),

    // Deprecated, present to satisfy code using the old enumeration TaskCategory
    @Deprecated GML_IMPORT( null, false ),
    @Deprecated ANALYTICSTABLE_UPDATE( null, false ),
    @Deprecated PROGRAM_DATA_SYNC( null, false );

    private final String key;

    private final boolean configurable;

    private final SchedulingType schedulingType;

    private final Class<? extends JobParameters> jobParameters;

    private final Map<String, String> relativeApiElements;

    JobType( String key, boolean configurable )
    {
        this( key, configurable, SchedulingType.CRON, null, null );
    }

    JobType( String key, boolean configurable, SchedulingType schedulingType, Class<? extends JobParameters> jobParameters,
        Map<String, String> relativeApiElements )
    {
        this.key = key;
        this.configurable = configurable;
        this.schedulingType = schedulingType;
        this.jobParameters = jobParameters;
        this.relativeApiElements = relativeApiElements;
    }

    public boolean isCronSchedulingType()
    {
        return getSchedulingType() == SchedulingType.CRON;
    }

    public boolean isFixedDelaySchedulingType()
    {
        return getSchedulingType() == SchedulingType.FIXED_DELAY;
    }

    public boolean hasJobParameters()
    {
        return jobParameters != null;
    }

    public String getKey()
    {
        return key;
    }

    public Class<? extends JobParameters> getJobParameters()
    {
        return jobParameters;
    }

    public boolean isConfigurable()
    {
        return configurable;
    }

    public SchedulingType getSchedulingType()
    {
        return schedulingType;
    }

    public Map<String, String> getRelativeApiElements()
    {
        return relativeApiElements;
    }
}
