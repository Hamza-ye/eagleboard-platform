package com.mass3d.program.notification;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "notificationTrigger", namespace = DxfNamespaces.DXF_2_0 )
public enum NotificationTrigger
{
    /**
     * Program enrollment.
     */
    ENROLLMENT,

    /**
     * Program or ProgramStage completion.
     */
    COMPLETION,

    /**
     * Triggered by ProgramRule.
     */
    PROGRAM_RULE,

    /**
     * Scheduled days relative to the dueDate of the ProgramStageInstance (event) and DataSet completion.
     */
    SCHEDULED_DAYS_DUE_DATE,

    /**
     * Scheduled days relative to the incidentDate of the ProgramInstance (enrollment).
     */
    SCHEDULED_DAYS_INCIDENT_DATE,

    /**
     * Scheduled days relative to the enrollmentDate of the ProgramInstance (enrollment).
     */
    SCHEDULED_DAYS_ENROLLMENT_DATE;

    private static final Set<NotificationTrigger> IMMEDIATE_TRIGGERS =
        new ImmutableSet.Builder<NotificationTrigger>()
            .add( ENROLLMENT, COMPLETION, PROGRAM_RULE ).build();

    private static final Set<NotificationTrigger> SCHEDULED_TRIGGERS =
        new ImmutableSet.Builder<NotificationTrigger>()
            .add( SCHEDULED_DAYS_DUE_DATE, SCHEDULED_DAYS_INCIDENT_DATE, SCHEDULED_DAYS_ENROLLMENT_DATE ).build();

    private static final Set<NotificationTrigger> APPLICABLE_TO_PROGRAM_INSTANCE =
        new ImmutableSet.Builder<NotificationTrigger>()
            .add( ENROLLMENT, COMPLETION, SCHEDULED_DAYS_INCIDENT_DATE, SCHEDULED_DAYS_ENROLLMENT_DATE ).build();

    private static final Set<NotificationTrigger> APPLICABLE_TO_PROGRAM_STAGE_INSTANCE =
        new ImmutableSet.Builder<NotificationTrigger>()
            .add( COMPLETION, SCHEDULED_DAYS_DUE_DATE ).build();
    
    public boolean isImmediate()
    {
        return IMMEDIATE_TRIGGERS.contains( this );
    }

    public boolean isScheduled()
    {
        return SCHEDULED_TRIGGERS.contains( this );
    }

    public static Set<NotificationTrigger> getAllScheduledTriggers()
    {
        return SCHEDULED_TRIGGERS;
    }

    public static Set<NotificationTrigger> getAllImmediateTriggers()
    {
        return IMMEDIATE_TRIGGERS;
    }

    public static Set<NotificationTrigger> getAllApplicableToProgramInstance()
    {
        return APPLICABLE_TO_PROGRAM_INSTANCE;
    }

    public static Set<NotificationTrigger> getAllApplicableToProgramStageInstance()
    {
        return APPLICABLE_TO_PROGRAM_STAGE_INSTANCE;
    }

    public boolean isApplicableToProgramStageInstance()
    {
        return APPLICABLE_TO_PROGRAM_STAGE_INSTANCE.contains( this );
    }

    public boolean isApplicableToProgramInstance()
    {
        return APPLICABLE_TO_PROGRAM_INSTANCE.contains( this );
    }
}
