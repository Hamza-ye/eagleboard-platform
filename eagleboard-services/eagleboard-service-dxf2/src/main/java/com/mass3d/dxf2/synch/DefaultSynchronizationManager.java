package com.mass3d.dxf2.synch;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.IdSchemes;
import com.mass3d.datavalue.DataValueService;
import com.mass3d.dxf2.datavalueset.DataValueSetService;
import com.mass3d.dxf2.importsummary.ImportCount;
import com.mass3d.dxf2.importsummary.ImportStatus;
import com.mass3d.dxf2.importsummary.ImportSummary;
import com.mass3d.dxf2.metadata.AtomicMode;
import com.mass3d.dxf2.metadata.Metadata;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.MetadataImportService;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.sync.SyncEndpoint;
import com.mass3d.dxf2.sync.SyncUtils;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;
import com.mass3d.dxf2.webmessage.WebMessageParseException;
import com.mass3d.schema.SchemaService;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.system.util.CodecUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component( "com.mass3d.dxf2.synch.SynchronizationManager" )
public class DefaultSynchronizationManager
    implements SynchronizationManager
{
    private static final String HEADER_AUTHORIZATION = "Authorization";

//    private final DataValueSetService dataValueSetService;
//    private final DataValueService dataValueService;
    private final MetadataImportService importService;
    private final SchemaService schemaService;
    private final CurrentUserService currentUserService;
    private final SystemSettingManager systemSettingManager;
    private final RestTemplate restTemplate;
    private final ObjectMapper jsonMapper;

    public DefaultSynchronizationManager(
//        DataValueSetService dataValueSetService,
//        DataValueService dataValueService,
        MetadataImportService importService,
        SchemaService schemaService,
        CurrentUserService currentUserService,
        SystemSettingManager systemSettingManager,
        RestTemplate restTemplate,
        ObjectMapper jsonMapper )
    {
//        checkNotNull( dataValueSetService );
//        checkNotNull( dataValueService );
        checkNotNull( importService );
        checkNotNull( schemaService );
        checkNotNull( currentUserService );
        checkNotNull( systemSettingManager );
        checkNotNull( restTemplate );
        checkNotNull( jsonMapper );

//        this.dataValueSetService = dataValueSetService;
//        this.dataValueService = dataValueService;
        this.importService = importService;
        this.schemaService = schemaService;
        this.currentUserService = currentUserService;
        this.systemSettingManager = systemSettingManager;
        this.restTemplate = restTemplate;
        this.jsonMapper = jsonMapper;
    }

    // -------------------------------------------------------------------------
    // SynchronizationManager implementation
    // -------------------------------------------------------------------------

    @Override
    public AvailabilityStatus isRemoteServerAvailable()
    {
        return SyncUtils.isRemoteServerAvailable( systemSettingManager, restTemplate );
    }

    @Override
    public ImportSummary executeDataValuePush()
        throws WebMessageParseException
    {
        AvailabilityStatus availability = isRemoteServerAvailable();

        if ( !availability.isAvailable() )
        {
            log.info( "Aborting data values push, server not available" );
            return null;
        }

        String url = systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_URL )
            + SyncEndpoint.DATA_VALUE_SETS.getPath();
        String username = (String) systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_USERNAME );
        String password = (String) systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_PASSWORD );

        SystemInstance instance = new SystemInstance( url, username, password );

        return executeDataValuePush( instance );
    }

    /**
     * Executes a push of data values to the given remote instance.
     *
     * @param instance the remote system instance.
     * @return an ImportSummary.
     */
    private ImportSummary executeDataValuePush( SystemInstance instance )
        throws WebMessageParseException
    {
        // ---------------------------------------------------------------------
        // Set time for last success to start of process to make data saved
        // subsequently part of next synch process without being ignored
        // ---------------------------------------------------------------------
        final Date startTime = new Date();
        final Date lastSuccessTime = SyncUtils.getLastSyncSuccess( systemSettingManager,
            SettingKey.LAST_SUCCESSFUL_DATA_VALUE_SYNC );
        final Date skipChangedBefore = (Date) systemSettingManager
            .getSystemSetting( SettingKey.SKIP_SYNCHRONIZATION_FOR_DATA_CHANGED_BEFORE );
        final Date lastUpdatedAfter = lastSuccessTime.after( skipChangedBefore ) ? lastSuccessTime : skipChangedBefore;
//        final int objectsToSynchronize = dataValueService.getDataValueCountLastUpdatedAfter( lastUpdatedAfter, true );

        log.info( "DataValues last changed before " + skipChangedBefore + " will not be synchronized." );

//        if ( objectsToSynchronize == 0 )
//        {
//            SyncUtils.setLastSyncSuccess( systemSettingManager, SettingKey.LAST_SUCCESSFUL_DATA_VALUE_SYNC,
//                startTime );
//            log.debug( "Skipping data values push, no new or updated data values" );
//
//            ImportCount importCount = new ImportCount( 0, 0, 0, 0 );
//            return new ImportSummary( ImportStatus.SUCCESS, "No new or updated data values to push.", importCount );
//        }

//        log.info( "Data Values: " + objectsToSynchronize + " to push since last synchronization success: "
//            + lastSuccessTime );
        log.info( "Remote server POST URL: " + instance.getUrl() );

        final RequestCallback requestCallback = request -> {
            request.getHeaders().setContentType( MediaType.APPLICATION_JSON );
            request.getHeaders().add( HEADER_AUTHORIZATION,
                CodecUtils.getBasicAuthString( instance.getUsername(), instance.getPassword() ) );

//            dataValueSetService
//                .writeDataValueSetJson( lastUpdatedAfter, request.getBody(), new IdSchemes() );
        };

        final int maxSyncAttempts = (int) systemSettingManager.getSystemSetting( SettingKey.MAX_SYNC_ATTEMPTS );

        Optional<AbstractWebMessageResponse> responseSummary =
            SyncUtils.runSyncRequest(
                restTemplate,
                requestCallback,
                SyncEndpoint.DATA_VALUE_SETS.getKlass(),
                instance.getUrl(),
                maxSyncAttempts );

        ImportSummary summary = null;
        if ( responseSummary.isPresent() )
        {
            summary = (ImportSummary) responseSummary.get();

            if ( ImportStatus.SUCCESS.equals( summary.getStatus() ) )
            {
                log.info( "Push successful: " + summary );
            }
            else
            {
                log.warn( "Push failed: " + summary );
            }
        }

        return summary;
    }

    @Override
    public ImportReport executeMetadataPull( String url )
    {
        User user = currentUserService.getCurrentUser();

        String userUid = user != null ? user.getUid() : null;

        log.info( String.format( "Metadata pull, url: %s, user: %s", url, userUid ) );

        String json = restTemplate.getForObject( url, String.class );

        Metadata metadata = null;

        try
        {
            metadata = jsonMapper.readValue( json, Metadata.class );
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to parse remote JSON document", ex );
        }

        MetadataImportParams importParams = new MetadataImportParams();
        importParams.setSkipSharing( true );
        importParams.setAtomicMode( AtomicMode.NONE );
        importParams.addMetadata( schemaService.getMetadataSchemas(), metadata );

        return importService.importMetadata( importParams );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
}