package com.mass3d.dxf2.config;

import static com.google.common.collect.Lists.newArrayList;
import static com.mass3d.importexport.ImportStrategy.CREATE;
import static com.mass3d.importexport.ImportStrategy.DELETE;
import static com.mass3d.importexport.ImportStrategy.UPDATE;

import com.mass3d.dxf2.metadata.objectbundle.validation.MandatoryAttributesCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.UniqueAttributesCheck;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mass3d.dxf2.metadata.objectbundle.validation.CreationCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.DeletionCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.DuplicateIdsCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.ReferencesCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.SchemaCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.SecurityCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.UniquenessCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.UpdateCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.ValidationCheck;
import com.mass3d.dxf2.metadata.objectbundle.validation.ValidationHooksCheck;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncServiceException;
import com.mass3d.external.conf.ConfigurationPropertyFactoryBean;
import com.mass3d.importexport.ImportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.google.common.collect.ImmutableMap;

/**
 * @author Luciano Fiandesio
 */
@Configuration( "dxf2ServiceConfig" )
public class ServiceConfig
{
    @Autowired
    @Qualifier( "initialInterval" )
    private ConfigurationPropertyFactoryBean initialInterval;

    @Autowired
    @Qualifier( "maxAttempts" )
    private ConfigurationPropertyFactoryBean maxAttempts;

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        return new NamedParameterJdbcTemplate( jdbcTemplate );
    }

    @Bean( "retryTemplate" )
    public RetryTemplate retryTemplate()
    {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();

        backOffPolicy.setInitialInterval( Long.parseLong( (String) initialInterval.getObject() ) );

        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
        exceptionMap.put( MetadataSyncServiceException.class, true );
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(
            Integer.parseInt( (String) maxAttempts.getObject() ), exceptionMap );

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy( backOffPolicy );
        retryTemplate.setRetryPolicy( simpleRetryPolicy );

        return retryTemplate;
    }

    private final static List<Class<? extends ValidationCheck>> CREATE_UPDATE_CHECKS = newArrayList(
        DuplicateIdsCheck.class,
        ValidationHooksCheck.class,
        SecurityCheck.class,
        SchemaCheck.class,
        UniquenessCheck.class,
        MandatoryAttributesCheck.class,
        UniqueAttributesCheck.class,
        ReferencesCheck.class );

    private final static List<Class<? extends ValidationCheck>> CREATE_CHECKS = newArrayList(
        DuplicateIdsCheck.class,
        ValidationHooksCheck.class,
        SecurityCheck.class,
        CreationCheck.class,
        SchemaCheck.class,
        UniquenessCheck.class,
        MandatoryAttributesCheck.class,
        UniqueAttributesCheck.class,
        ReferencesCheck.class );

    private final static List<Class<? extends ValidationCheck>> UPDATE_CHECKS = newArrayList(
        DuplicateIdsCheck.class,
        ValidationHooksCheck.class,
        SecurityCheck.class,
        UpdateCheck.class,
        SchemaCheck.class,
        UniquenessCheck.class,
        MandatoryAttributesCheck.class,
        UniqueAttributesCheck.class,
        ReferencesCheck.class );

    private final static List<Class<? extends ValidationCheck>> DELETE_CHECKS = newArrayList(
        SecurityCheck.class,
        DeletionCheck.class );

    @Bean( "validatorMap" )
    public Map<ImportStrategy, List<Class<? extends ValidationCheck>>> validatorMap()
    {
        return ImmutableMap.of(
            ImportStrategy.CREATE_AND_UPDATE, CREATE_UPDATE_CHECKS,
            CREATE, CREATE_CHECKS,
            ImportStrategy.UPDATE, UPDATE_CHECKS,
            ImportStrategy.DELETE, DELETE_CHECKS );
    }

//    /*
//     * TRACKER EVENT IMPORT VALIDATION
//     */
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Checker>>> eventInsertValidatorMap()
//    {
//        return ImmutableMap.of( CREATE, newArrayList(
//            EventDateCheck.class,
//            OrgUnitCheck.class,
//            ProgramCheck.class,
//            ProgramStageCheck.class,
//            TrackedEntityInstanceCheck.class,
//            ProgramInstanceCheck.class,
//            ProgramInstanceRepeatableStageCheck.class,
//            ProgramOrgUnitCheck.class,
//            EventGeometryCheck.class,
//            EventCreationAclCheck.class,
//            EventBaseCheck.class,
//            AttributeOptionComboCheck.class,
//            AttributeOptionComboDateCheck.class,
//            AttributeOptionComboAclCheck.class,
//            DataValueCheck.class,
//            DataValueAclCheck.class,
//            ExpirationDaysCheck.class ) );
//    }

//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Checker>>> eventUpdateValidatorMap()
//    {
//        return ImmutableMap.of( UPDATE, newArrayList(
//            EventSimpleCheck.class,
//            EventBaseCheck.class,
//            ProgramStageInstanceBasicCheck.class,
//            ProgramStageInstanceAclCheck.class,
//            ProgramCheck.class,
//            ProgramInstanceCheck.class,
//            ProgramStageInstanceAuthCheck.class,
//            AttributeOptionComboCheck.class,
//            AttributeOptionComboDateCheck.class,
//            EventGeometryCheck.class,
//            DataValueCheck.class,
//            ExpirationDaysCheck.class ) );
//    }
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Checker>>> eventDeleteValidatorMap()
//    {
//        return ImmutableMap.of( DELETE, newArrayList(
//            com.mass3d.dxf2.events.importer.delete.validation.ProgramStageInstanceAclCheck.class ) );
//    }
//
//    /*
//     * TRACKER EVENT PRE/POST PROCESSING
//     */
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Processor>>> eventInsertPreProcessorMap()
//    {
//        return ImmutableMap.of( CREATE, newArrayList(
//            ImportOptionsPreProcessor.class,
//            EventStoredByPreProcessor.class,
//            ProgramInstancePreProcessor.class,
//            ProgramStagePreProcessor.class,
//            EventGeometryPreProcessor.class ) );
//    }
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Processor>>> eventInsertPostProcessorMap()
//    {
//        return ImmutableMap.of( CREATE, newArrayList(
//            ProgramNotificationPostProcessor.class,
//            EventInsertAuditPostProcessor.class ) );
//    }
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Processor>>> eventUpdatePreProcessorMap()
//    {
//        return ImmutableMap.of( UPDATE, newArrayList(
//            ImportOptionsPreProcessor.class,
//            EventStoredByPreProcessor.class,
//            ProgramStageInstanceUpdatePreProcessor.class,
//            ProgramInstanceGeometryPreProcessor.class ) );
//    }
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Processor>>> eventUpdatePostProcessorMap()
//    {
//        return ImmutableMap.of( UPDATE, newArrayList(
//            PublishEventPostProcessor.class,
//            ProgramNotificationPostProcessor.class,
//            EventUpdateAuditPostProcessor.class ) );
//    }
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Processor>>> eventDeletePreProcessorMap()
//    {
//        return ImmutableMap.of( DELETE, newArrayList(
//        /*
//         * Intentionally left empty since we don't have pre-delete processors at the
//         * moment, so at the moment this is a placeholder where to add pre-delete
//         * processors when we will need it (if ever). Remove this comment if you add a
//         * pre-delete processor.
//         */
//        ) );
//    }
//
//    @Bean
//    public Map<ImportStrategy, List<Class<? extends Processor>>> eventDeletePostProcessorMap()
//    {
//        return ImmutableMap.of( DELETE, newArrayList(
//            EventDeleteAuditPostProcessor.class ) );
//    }
}
