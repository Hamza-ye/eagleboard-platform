package com.mass3d.scheduling;

import java.util.List;
import java.util.Map;
import com.mass3d.schema.Property;

/**
 * Simple service for {@link JobConfiguration} objects.
 *
 */
public interface JobConfigurationService
{
    String ID = JobConfiguration.class.getName();

    /**
     * Add a job configuration
     *
     * @param jobConfiguration the job configuration to be added
     * @return id
     */
    long addJobConfiguration(JobConfiguration jobConfiguration);

    /**
     * Add a collection of job configurations
     *
     * @param jobConfigurations the job configurations to add
     */
    void addJobConfigurations(List<JobConfiguration> jobConfigurations);

    /**
     * Update an existing job configuration
     *
     * @param jobConfiguration the job configuration to be added
     * @return id
     */
    long updateJobConfiguration(JobConfiguration jobConfiguration);

    /**
     * Delete a job configuration
     *
     * @param jobConfiguration the id of the job configuration to be deleted
     */
    void deleteJobConfiguration(JobConfiguration jobConfiguration);

    /**
     * Get job configuration for given id
     *
     * @param jobId id for job configuration
     * @return Job configuration
     */
    JobConfiguration getJobConfiguration(long jobId);

    /**
     * Get a job configuration for given uid
     *
     * @param uid uid to search for
     * @return job configuration
     */
    JobConfiguration getJobConfigurationByUid(String uid);

    /**
     * Get all job configurations
     *
     * @return list of all job configurations in the system
     */
    List<JobConfiguration> getAllJobConfigurations();

    /**
     * Get a map of parameter classes with appropriate properties
     * This can be used for a frontend app or for other appropriate applications which needs information about the jobs
     * in the system.
     * <p>
     * It uses {@link JobType}.
     *
     * @return map with parameters classes
     */
    Map<String, Map<String, Property>> getJobParametersSchema();

    /**
     * Returns a list of all configurable and available job types.
     *
     * @return a list of {@link JobTypeInfo}.
     */
    List<JobTypeInfo> getJobTypeInfo();

    /**
     * Update the state of the jobConfiguration.
     * @param jobConfiguration
     */
    void refreshScheduling(JobConfiguration jobConfiguration);
}
