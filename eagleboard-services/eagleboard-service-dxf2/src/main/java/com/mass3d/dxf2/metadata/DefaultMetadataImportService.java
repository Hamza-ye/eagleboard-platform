package com.mass3d.dxf2.metadata;

import com.google.common.base.Enums;
import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.MergeMode;
import com.mass3d.commons.timer.SystemTimer;
import com.mass3d.commons.timer.Timer;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.feedback.ImportReportMode;
import com.mass3d.dxf2.metadata.objectbundle.*;
import com.mass3d.dxf2.metadata.objectbundle.feedback.ObjectBundleCommitReport;
import com.mass3d.dxf2.metadata.objectbundle.feedback.ObjectBundleValidationReport;
import com.mass3d.feedback.Status;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.preheat.PreheatMode;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import com.mass3d.security.acl.AclService;
import com.mass3d.system.notification.NotificationLevel;
import com.mass3d.system.notification.Notifier;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service( "com.mass3d.dxf2.metadata.MetadataImportService" )
@Transactional
public class DefaultMetadataImportService implements MetadataImportService
{
    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private ObjectBundleService objectBundleService;

    @Autowired
    private ObjectBundleValidationService objectBundleValidationService;

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private AclService aclService;

    @Autowired
    private Notifier notifier;

    @Override
    public ImportReport importMetadata( MetadataImportParams params )
    {
        Timer timer = new SystemTimer().start();

        ImportReport importReport = new ImportReport();
        importReport.setImportParams( params );
        importReport.setStatus( Status.OK );

        if ( params.getUser() == null )
        {
            params.setUser( currentUserService.getCurrentUser() );
        }

        if ( params.getUserOverrideMode() == UserOverrideMode.CURRENT )
        {
            params.setOverrideUser( currentUserService.getCurrentUser() );
        }

        String message = "(" + params.getUsername() + ") Import:Start";
        log.info( message );

        if ( params.hasJobId() )
        {
            notifier.notify( params.getId(), message );
        }

        ObjectBundleParams bundleParams = params.toObjectBundleParams();
        ObjectBundle bundle = objectBundleService.create( bundleParams );

        prepareBundle( bundle, bundleParams );

        ObjectBundleValidationReport validationReport = objectBundleValidationService.validate( bundle );
        importReport.addTypeReports( validationReport.getTypeReportMap() );

        if ( !(!validationReport.getErrorReports().isEmpty() && AtomicMode.ALL == bundle.getAtomicMode()) )
        {
            Timer commitTimer = new SystemTimer().start();

            ObjectBundleCommitReport commitReport = objectBundleService.commit( bundle );
            importReport.addTypeReports( commitReport.getTypeReportMap() );

            if ( !importReport.getErrorReports().isEmpty() )
            {
                importReport.setStatus( Status.WARNING );
            }

            log.info( "(" + bundle.getUsername() + ") Import:Commit took " + commitTimer.toString() );
        }
        else
        {
            importReport.getStats().ignored();
            importReport.getTypeReports().forEach( tr -> tr.getStats().ignored() );

            importReport.setStatus( Status.ERROR );
        }

        message = "(" + bundle.getUsername() + ") Import:Done took " + timer.toString();

        log.info( message );

        if ( bundle.hasJobId() )
        {
            notifier.notify( bundle.getJobId(), NotificationLevel.INFO, message, true )
                .addJobSummary( bundle.getJobId(), importReport, ImportReport.class );
        }

        if ( ObjectBundleMode.VALIDATE == params.getImportMode() )
        {
            return importReport;
        }

        Lists.newArrayList( importReport.getTypeReportMap().keySet() ).forEach( typeReportKey ->
        {
            if ( importReport.getTypeReportMap().get( typeReportKey ).getStats().getTotal() == 0 )
            {
                importReport.getTypeReportMap().remove( typeReportKey );
                return;
            }

            TypeReport typeReport = importReport.getTypeReportMap().get( typeReportKey );

            if ( ImportReportMode.ERRORS == params.getImportReportMode() )
            {
                Lists.newArrayList( typeReport.getObjectReportMap().keySet() ).forEach( objectReportKey ->
                {
                    if ( typeReport.getObjectReportMap().get( objectReportKey ).getErrorReportsByCode().isEmpty() )
                    {
                        typeReport.getObjectReportMap().remove( objectReportKey );
                    }
                } );
            }

            if ( ImportReportMode.DEBUG != params.getImportReportMode() )
            {
                typeReport.getObjectReports().forEach( objectReport -> objectReport.setDisplayName( null ) );
            }
        } );

        return importReport;
    }

    @Override
    public MetadataImportParams getParamsFromMap( Map<String, List<String>> parameters )
    {
        MetadataImportParams params = new MetadataImportParams();

        if ( params.getUser() == null )
        {
            params.setUser( currentUserService.getCurrentUser() );
        }

        params.setSkipSharing( getBooleanWithDefault( parameters, "skipSharing", false ) );
        params.setSkipTranslation( getBooleanWithDefault( parameters, "skipTranslation", false ) );
        params.setSkipValidation( getBooleanWithDefault( parameters, "skipValidation", false ) );
        params.setUserOverrideMode( getEnumWithDefault( UserOverrideMode.class, parameters, "userOverrideMode", UserOverrideMode.NONE ) );
        params.setImportMode( getEnumWithDefault( ObjectBundleMode.class, parameters, "importMode", ObjectBundleMode.COMMIT ) );
        params.setPreheatMode( getEnumWithDefault( PreheatMode.class, parameters, "preheatMode", PreheatMode.REFERENCE ) );
        params.setIdentifier( getEnumWithDefault( PreheatIdentifier.class, parameters, "identifier", PreheatIdentifier.UID ) );
        params.setImportStrategy( getEnumWithDefault( ImportStrategy.class, parameters, "importStrategy", ImportStrategy.CREATE_AND_UPDATE ) );
        params.setAtomicMode( getEnumWithDefault( AtomicMode.class, parameters, "atomicMode", AtomicMode.ALL ) );
        params.setMergeMode( getEnumWithDefault( MergeMode.class, parameters, "mergeMode", MergeMode.REPLACE ) );
        params.setFlushMode( getEnumWithDefault( FlushMode.class, parameters, "flushMode", FlushMode.AUTO ) );
        params.setImportReportMode( getEnumWithDefault( ImportReportMode.class, parameters, "importReportMode", ImportReportMode.ERRORS ) );
        params.setFirstRowIsHeader( getBooleanWithDefault( parameters, "firstRowIsHeader", true ) );

        if ( getBooleanWithDefault( parameters, "async", false ) )
        {
            JobConfiguration jobId = new JobConfiguration( "metadataImport", JobType.METADATA_IMPORT, params.getUser().getUid(), true );
            notifier.clear( jobId );
            params.setId( jobId );
        }

        if ( params.getUserOverrideMode() == UserOverrideMode.SELECTED )
        {
            User overrideUser = null;

            if ( parameters.containsKey( "overrideUser" ) )
            {
                List<String> overrideUsers = parameters.get( "overrideUser" );
                overrideUser = manager.get( User.class, overrideUsers.get( 0 ) );
            }

            if ( overrideUser == null )
            {
                throw new MetadataImportException( "UserOverrideMode.SELECTED is enabled, but overrideUser parameter does not point to a valid user." );
            }
            else
            {
                params.setOverrideUser( overrideUser );
            }
        }

        return params;
    }

    //-----------------------------------------------------------------------------------
    // Utility Methods
    //-----------------------------------------------------------------------------------

    private boolean getBooleanWithDefault( Map<String, List<String>> parameters, String key, boolean defaultValue )
    {
        if ( parameters == null || parameters.get( key ) == null || parameters.get( key ).isEmpty() )
        {
            return defaultValue;
        }

        String value = String.valueOf( parameters.get( key ).get( 0 ) );

        return "true".equals( value.toLowerCase() );
    }

    private <T extends Enum<T>> T getEnumWithDefault( Class<T> enumKlass, Map<String, List<String>> parameters, String key, T defaultValue )
    {
        if ( parameters == null || parameters.get( key ) == null || parameters.get( key ).isEmpty() )
        {
            return defaultValue;
        }

        String value = String.valueOf( parameters.get( key ).get( 0 ) );

        return Enums.getIfPresent( enumKlass, value ).or( defaultValue );
    }

    private void prepareBundle( ObjectBundle bundle, ObjectBundleParams params )
    {
        if ( bundle.getUser() == null )
        {
            return;
        }

        for ( Class<? extends IdentifiableObject> klass : bundle.getObjectMap().keySet() )
        {
            bundle.getObjectMap().get( klass ).forEach( o -> prepareObject( (BaseIdentifiableObject) o, bundle, params ) );
        }
    }

    private void prepareObject( BaseIdentifiableObject object, ObjectBundle bundle, ObjectBundleParams params )
    {
        if ( StringUtils.isEmpty( object.getPublicAccess() ) )
        {
            aclService.resetSharing( object, bundle.getUser() );
        }

        if ( object.getUser() == null || bundle.getPreheat().get( params.getPreheatIdentifier(), User.class, object.getUser().getUid() ) == null )
        {
            object.setUser( bundle.getUser() );
        }

        if ( object.getUserAccesses() == null )
        {
            object.setUserAccesses( new HashSet<>() );
        }

        if ( object.getUserGroupAccesses() == null )
        {
            object.setUserGroupAccesses( new HashSet<>() );
        }

        object.setLastUpdatedBy( bundle.getUser() );
    }
}
