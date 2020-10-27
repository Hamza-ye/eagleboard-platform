package com.mass3d.scheduling;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Interface for scheduling jobs.
 * <p>
 * <p>
 * The main steps of the scheduling:
 * <p>
 * <ul>
 * <li>Create a job configuration {@link JobConfiguration}</li>
 * <li>This job configuration needs a job specific parameters object {@link JobParameters}, ie {@link com.mass3d.scheduling.parameters.AnalyticsJobParameters}.</li>
 * <li>Call scheduleJob with the job configuration.</li>
 * <li>The schedulingManager calls the spring scheduler with a runnable object {@link JobInstance}.</li>
 * <li>When the cron expression occurs the job will try to execute from the runnable object, job instance.</li>
 * </ul>
 *
 */
public interface SchedulingManager
{
    /**
     * Check if this job configuration is currently running
     *
     * @param jobConfiguration the job to check
     * @return true/false
     */
    boolean isJobConfigurationRunning(JobConfiguration jobConfiguration);

    /**
     * Set up default behavior for a started job.
     *
     * @param jobConfiguration the job which started
     */
    void jobConfigurationStarted(JobConfiguration jobConfiguration);

    /**
     * Set up default behavior for a finished job.
     * <p>
     * A special case is if a job is disabled when running, but the job does not stop. The job will run normally one last
     * time and try to set finished status. Since the job is disabled we manually set these parameters in this method so
     * that the job is not automatically rescheduled.
     * <p>
     * Also we don't want to update a job configuration if the job is deleted.
     *
     * @param jobConfiguration the job which started
     */
    void jobConfigurationFinished(JobConfiguration jobConfiguration);

    /**
     * Schedules a job with the given job configuration. The job will be scheduled
     * based on the {@link JobConfiguration#cronExpression} property.
     *
     * @param jobConfiguration the job to schedule.
     */
    void scheduleJob(JobConfiguration jobConfiguration);

    /**
     * Schedule a job with the given start time.
     *
     * @param jobConfiguration The jobConfiguration with job details to be scheduled
     * @param startTime The time at which the job should start
     */
    void scheduleJobWithStartTime(JobConfiguration jobConfiguration, Date startTime);

    /**
     * Stops one job.
     */
    void stopJob(JobConfiguration jobConfiguration);

    /**
     * Get a job based on the job type.
     *
     * @param jobType the job type for the job we want to collect
     * @return the job
     */
    Job getJob(JobType jobType);

    /**
     * Execute the job.
     *
     * @param jobConfiguration The configuration of the job to be executed
     */
    boolean executeJob(JobConfiguration jobConfiguration);

    /**
     * Execute an actual job without validation
     *
     * @param job The job to be executed
     */
    void executeJob(Runnable job);

    /**
     * Execute the given job immediately and return a ListenableFuture.
     *
     * @param callable the job to execute.
     * @param <T> return type of the supplied {@link Callable}.
     * @return a ListenableFuture representing the result of the job.
     */
    <T> ListenableFuture<T> executeJob(Callable<T> callable);

    /**
     * Returns a list of all scheduled jobs sorted based on cron expression and the current time.
     *
     * @return list of jobs
     */
    Map<String, ScheduledFuture<?>> getAllFutureJobs();
}
