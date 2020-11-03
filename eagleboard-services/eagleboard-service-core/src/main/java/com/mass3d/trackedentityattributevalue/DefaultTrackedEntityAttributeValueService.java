package com.mass3d.trackedentityattributevalue;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.external.conf.ConfigurationKey.CHANGELOG_TRACKER;
import static com.mass3d.system.util.ValidationUtils.dataValueIsValid;

import java.util.Collection;
import java.util.List;
import com.mass3d.common.AuditType;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceService;
import com.mass3d.reservedvalue.ReservedValueService;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service( "com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueService" )
public class DefaultTrackedEntityAttributeValueService
    implements TrackedEntityAttributeValueService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final TrackedEntityAttributeValueStore attributeValueStore;

    private final FileResourceService fileResourceService;

    private final TrackedEntityAttributeValueAuditService trackedEntityAttributeValueAuditService;

    private final ReservedValueService reservedValueService;

    private final CurrentUserService currentUserService;

    private final DhisConfigurationProvider config;

    public DefaultTrackedEntityAttributeValueService( TrackedEntityAttributeValueStore attributeValueStore,
        FileResourceService fileResourceService,
        TrackedEntityAttributeValueAuditService trackedEntityAttributeValueAuditService,
        ReservedValueService reservedValueService, CurrentUserService currentUserService,
        DhisConfigurationProvider dhisConfigurationProvider )
    {
        checkNotNull( attributeValueStore );
        checkNotNull( fileResourceService );
        checkNotNull( trackedEntityAttributeValueAuditService );
        checkNotNull( reservedValueService );
        checkNotNull( currentUserService );
        checkNotNull( dhisConfigurationProvider );

        this.attributeValueStore = attributeValueStore;
        this.fileResourceService = fileResourceService;
        this.trackedEntityAttributeValueAuditService = trackedEntityAttributeValueAuditService;
        this.reservedValueService = reservedValueService;
        this.currentUserService = currentUserService;
        this.config = dhisConfigurationProvider;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void deleteTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        TrackedEntityAttributeValueAudit trackedEntityAttributeValueAudit = new TrackedEntityAttributeValueAudit(
            attributeValue,
            attributeValue.getAuditValue(), currentUserService.getCurrentUsername(), AuditType.DELETE );

        if ( config.isEnabled( CHANGELOG_TRACKER ) )
        {
            trackedEntityAttributeValueAuditService.addTrackedEntityAttributeValueAudit( trackedEntityAttributeValueAudit );
        }

        deleteFileValue( attributeValue );
        attributeValueStore.delete( attributeValue );
    }

    @Override
    @Transactional( readOnly = true )
    public TrackedEntityAttributeValue getTrackedEntityAttributeValue( TrackedEntityInstance instance,
        TrackedEntityAttribute attribute )
    {
        return attributeValueStore.get( instance, attribute );
    }

    @Override
    @Transactional( readOnly = true )
    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues( TrackedEntityInstance instance )
    {
        return attributeValueStore.get( instance );
    }

    @Override
    @Transactional( readOnly = true )
    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues( TrackedEntityAttribute attribute )
    {
        return attributeValueStore.get( attribute );
    }

    @Override
    @Transactional( readOnly = true )
    public int getCountOfAssignedTrackedEntityAttributeValues( TrackedEntityAttribute attribute )
    {
        return attributeValueStore.getCountOfAssignedTEAValues( attribute );
    }

    @Override
    @Transactional( readOnly = true )
    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues(
        Collection<TrackedEntityInstance> instances )
    {
        if ( instances != null && instances.size() > 0 )
        {
            return attributeValueStore.get( instances );
        }

        return null;
    }

    @Override
    @Transactional
    public void addTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        if ( attributeValue == null || attributeValue.getAttribute() == null ||
            attributeValue.getAttribute().getValueType() == null )
        {
            throw new IllegalQueryException( "Attribute or type is null or empty" );
        }

        if ( attributeValue.getAttribute().isConfidentialBool() &&
            !config.getEncryptionStatus().isOk() )
        {
            throw new IllegalStateException( "Unable to encrypt data, encryption is not correctly configured" );
        }

        String result = dataValueIsValid( attributeValue.getValue(), attributeValue.getAttribute().getValueType() );

        if ( result != null )
        {
            throw new IllegalQueryException( "Value is not valid:  " + result );
        }

        attributeValue.setAutoFields();

        if ( attributeValue.getAttribute().getValueType().isFile() && !addFileValue( attributeValue ) )
        {
            throw new IllegalQueryException(
                String.format( "FileResource with id '%s' not found", attributeValue.getValue() ) );
        }

        if ( attributeValue.getValue() != null )
        {
            attributeValueStore.saveVoid( attributeValue );

            if ( attributeValue.getAttribute().isGenerated() && attributeValue.getAttribute().getTextPattern() != null )
            {
                reservedValueService
                    .useReservedValue( attributeValue.getAttribute().getTextPattern(), attributeValue.getValue() );
            }
        }
    }

    @Override
    @Transactional
    public void updateTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        updateTrackedEntityAttributeValue( attributeValue, currentUserService.getCurrentUser() );
    }

    @Override
    @Transactional
    public void updateTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue, User user )
    {
        if ( attributeValue != null && StringUtils.isEmpty( attributeValue.getValue() ) )
        {
            deleteFileValue( attributeValue );
            attributeValueStore.delete( attributeValue );
        }
        else
        {
            if ( attributeValue == null || attributeValue.getAttribute() == null ||
                attributeValue.getAttribute().getValueType() == null )
            {
                throw new IllegalQueryException( "Attribute or type is null or empty" );
            }

            attributeValue.setAutoFields();

            String result = dataValueIsValid( attributeValue.getValue(), attributeValue.getAttribute().getValueType() );

            if ( result != null )
            {
                throw new IllegalQueryException( "Value is not valid:  " + result );
            }

            TrackedEntityAttributeValueAudit trackedEntityAttributeValueAudit = new TrackedEntityAttributeValueAudit(
                attributeValue, attributeValue.getAuditValue(), User.username( user ), AuditType.UPDATE );

            if ( config.isEnabled( CHANGELOG_TRACKER ) )
            {
                trackedEntityAttributeValueAuditService.addTrackedEntityAttributeValueAudit( trackedEntityAttributeValueAudit );
            }

            attributeValueStore.update( attributeValue );

            if ( attributeValue.getAttribute().isGenerated() && attributeValue.getAttribute().getTextPattern() != null )
            {
                reservedValueService
                    .useReservedValue( attributeValue.getAttribute().getTextPattern(), attributeValue.getValue() );
            }
        }
    }

    private void deleteFileValue( TrackedEntityAttributeValue value )
    {
        if ( !value.getAttribute().getValueType().isFile() ||
            fileResourceService.getFileResource( value.getValue() ) == null )
        {
            return;
        }

        FileResource fileResource = fileResourceService.getFileResource( value.getValue() );
        fileResourceService.updateFileResource( fileResource );
    }

    private boolean addFileValue( TrackedEntityAttributeValue value )
    {
        FileResource fileResource = fileResourceService.getFileResource( value.getValue() );

        if ( fileResource == null )
        {
            return false;
        }

        fileResource.setAssigned( true );
        fileResourceService.updateFileResource( fileResource );
        return true;
    }
}
