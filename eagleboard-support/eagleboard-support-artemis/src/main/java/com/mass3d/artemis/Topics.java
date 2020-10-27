package com.mass3d.artemis;

/**
 * Constants representing actual queue names.
 * <P>All members of this class are immutable.
 *
 */
public final class Topics
{
    public static final String METADATA_TOPIC_NAME = "dhis2.topic.metadata";
    public static final String AGGREGATE_TOPIC_NAME = "dhis2.topic.aggregate";
    public static final String TRACKER_TOPIC_NAME = "dhis2.topic.tracker";
    public static final String TRACKER_IMPORT_JOB_TOPIC_NAME = "dhis2.jobs.tracker";
    public static final String TRACKER_IMPORT_NOTIFICATION_TOPIC_NAME = "dhis2.jobs.tracker.notifications";
    public static final String TRACKER_IMPORT_RULE_ENGINE_TOPIC_NAME = "dhis2.jobs.tracker.rule.engine";
}
