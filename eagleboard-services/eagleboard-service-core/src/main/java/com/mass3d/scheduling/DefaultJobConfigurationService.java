package com.mass3d.scheduling;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.scheduling.JobType.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.commons.util.TextUtils;
import com.mass3d.schema.NodePropertyIntrospectorService;
import com.mass3d.schema.Property;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service( "jobConfigurationService" )
public class DefaultJobConfigurationService
    implements JobConfigurationService
{
    private final IdentifiableObjectStore<JobConfiguration> jobConfigurationStore;

    public DefaultJobConfigurationService( @Qualifier( "com.mass3d.scheduling.JobConfigurationStore" ) IdentifiableObjectStore<JobConfiguration> jobConfigurationStore )
    {
        checkNotNull( jobConfigurationStore );

        this.jobConfigurationStore = jobConfigurationStore;
    }

    @Override
    @Transactional
    public long addJobConfiguration( JobConfiguration jobConfiguration )
    {
        if ( !jobConfiguration.isInMemoryJob() )
        {
            jobConfigurationStore.save( jobConfiguration );
        }

        return jobConfiguration.getId();
    }

    @Override
    @Transactional
    public void addJobConfigurations( List<JobConfiguration> jobConfigurations )
    {
        jobConfigurations.forEach( jobConfiguration -> jobConfigurationStore.save( jobConfiguration ) );
    }

    @Override
    @Transactional
    public long updateJobConfiguration( JobConfiguration jobConfiguration )
    {
        if ( !jobConfiguration.isInMemoryJob() )
        {
            jobConfigurationStore.update( jobConfiguration );
        }

        return jobConfiguration.getId();
    }

    @Override
    @Transactional
    public void deleteJobConfiguration( JobConfiguration jobConfiguration )
    {
        if ( !jobConfiguration.isInMemoryJob() )
        {
            jobConfigurationStore.delete( jobConfigurationStore.getByUid( jobConfiguration.getUid() ) );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JobConfiguration getJobConfigurationByUid( String uid )
    {
        return jobConfigurationStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public JobConfiguration getJobConfiguration( long jobId )
    {
        return jobConfigurationStore.get( jobId );
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobConfiguration> getAllJobConfigurations()
    {
        return jobConfigurationStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Map<String, Property>> getJobParametersSchema()
    {
        Map<String, Map<String, Property>> propertyMap = Maps.newHashMap();

        for ( JobType jobType : values() )
        {
            if ( !jobType.isConfigurable() )
            {
                continue;
            }

            Map<String, Property> jobParameters = Maps.uniqueIndex( getJobParameters( jobType ), p -> p.getName() );

            propertyMap.put( jobType.name(), jobParameters );
        }

        return propertyMap;
    }

    @Override
    public List<JobTypeInfo> getJobTypeInfo()
    {
        List<JobTypeInfo> jobTypes = new ArrayList<>();

        for ( JobType jobType : values() )
        {
            if ( !jobType.isConfigurable() )
            {
                continue;
            }

            String name = TextUtils.getPrettyEnumName( jobType );

            List<Property> jobParameters = getJobParameters( jobType );

            JobTypeInfo info = new JobTypeInfo( name, jobType, jobParameters );

            jobTypes.add( info );
        }

        return jobTypes;
    }

    @Override
    public void refreshScheduling( JobConfiguration jobConfiguration )
    {
        if ( jobConfiguration.isEnabled() )
        {
            jobConfiguration.setJobStatus( JobStatus.SCHEDULED );
        }
        else
        {
            jobConfiguration.setJobStatus( JobStatus.DISABLED );
        }

        jobConfigurationStore.update( jobConfiguration );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Returns a list of job parameters for the given job type.
     *
     * @param jobType the {@link JobType}.
     * @return a list of {@link Property}.
     */
    private List<Property> getJobParameters( JobType jobType )
    {
        List<Property> jobParameters = new ArrayList<>();

        Class<?> clazz = jobType.getJobParameters();

        if ( clazz == null )
        {
            return jobParameters;
        }

        final Set<String> propertyNames = Stream.of( PropertyUtils.getPropertyDescriptors( clazz ) )
            .filter( pd -> pd.getReadMethod() != null && pd.getWriteMethod() != null && pd.getReadMethod().getAnnotation( JsonProperty.class ) != null )
            .map( PropertyDescriptor::getName )
            .collect( Collectors.toSet() );

        for ( Field field : Stream.of( clazz.getDeclaredFields() ).filter( f -> propertyNames.contains( f.getName() ) ).collect( Collectors
            .toList() ) )
        {
            Property property = new Property( Primitives.wrap( field.getType() ), null, null );
            property.setName( field.getName() );
            property.setFieldName( TextUtils.getPrettyPropertyName( field.getName() ) );

            try
            {
                field.setAccessible( true );
                property.setDefaultValue( field.get( jobType.getJobParameters().newInstance() ) );
            }
            catch ( IllegalAccessException | InstantiationException e )
            {
                log.error( "Fetching default value for JobParameters properties failed for property: " + field.getName(), e );
            }

            String relativeApiElements = jobType.getRelativeApiElements() != null ?
                jobType.getRelativeApiElements().get( field.getName() ) : "";

            if ( relativeApiElements != null && !relativeApiElements.equals( "" ) )
            {
                property.setRelativeApiEndpoint( relativeApiElements );
            }

            if ( Collection.class.isAssignableFrom( field.getType() ) )
            {
                property = new NodePropertyIntrospectorService()
                    .setPropertyIfCollection( property, field, clazz );
            }

            jobParameters.add( property );
        }

        return jobParameters;
    }
}
