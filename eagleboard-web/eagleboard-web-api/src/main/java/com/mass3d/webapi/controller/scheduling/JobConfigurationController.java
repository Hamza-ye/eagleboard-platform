package com.mass3d.webapi.controller.scheduling;

import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorMessage;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.ObjectReport;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobConfigurationService;
import com.mass3d.scheduling.SchedulingManager;
import com.mass3d.schema.Property;
import com.mass3d.schema.descriptors.JobConfigurationSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import com.mass3d.webapi.webdomain.JobTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Simple controller for API endpoints
 *
 */
@RestController
@RequestMapping( value = JobConfigurationSchemaDescriptor.API_ENDPOINT )
public class JobConfigurationController
    extends AbstractCrudController<JobConfiguration>
{
    private final JobConfigurationService jobConfigurationService;

    private final SchedulingManager schedulingManager;

    public JobConfigurationController( JobConfigurationService jobConfigurationService,
        SchedulingManager schedulingManager )
    {
        this.jobConfigurationService = jobConfigurationService;
        this.schedulingManager = schedulingManager;
    }

    @RequestMapping( value = "/jobTypesExtended", method = RequestMethod.GET, produces = { "application/json", "application/javascript" } )
    public @ResponseBody Map<String, Map<String, Property>> getJobTypesExtended()
    {
        return jobConfigurationService.getJobParametersSchema();
    }

    @GetMapping( value = "/jobTypes", produces = "application/json" )
    public JobTypes getJobTypeInfo()
    {
        return new JobTypes( jobConfigurationService.getJobTypeInfo() );
    }

    @RequestMapping( value = "{uid}/execute", method = RequestMethod.GET, produces = { "application/json", "application/javascript" } )
    public ObjectReport executeJobConfiguration( @PathVariable( "uid" ) String uid )
    {
        JobConfiguration jobConfiguration = jobConfigurationService.getJobConfigurationByUid( uid );

        ObjectReport objectReport = new ObjectReport( JobConfiguration.class, 0 );

        boolean success = schedulingManager.executeJob( jobConfiguration );

        if ( !success )
        {
            objectReport.addErrorReport( new ErrorReport( JobConfiguration.class, new ErrorMessage( ErrorCode.E7006, jobConfiguration.getName() ) ) );
        }

        return objectReport;
    }

    @Override
    protected void postPatchEntity( JobConfiguration jobConfiguration )
    {
        jobConfigurationService.refreshScheduling( jobConfiguration );
    }
}
