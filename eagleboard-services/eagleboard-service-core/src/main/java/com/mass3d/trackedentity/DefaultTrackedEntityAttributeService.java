package com.mass3d.trackedentity;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.common.QueryItem;
import com.mass3d.common.QueryOperator;
import com.mass3d.common.ValueType;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceService;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramService;
import com.mass3d.program.ProgramTrackedEntityAttributeStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.system.util.MathUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import com.mass3d.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service( "com.mass3d.trackedentity.TrackedEntityAttributeService" )
public class DefaultTrackedEntityAttributeService
    implements TrackedEntityAttributeService
{
    private static final int VALUE_MAX_LENGTH = 50000;

    private static final Set<String> VALID_IMAGE_FORMATS = ImmutableSet.<String>builder().add(
        ImageIO.getReaderFormatNames() ).build();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final TrackedEntityAttributeStore attributeStore;
    private final ProgramService programService;
    private final TrackedEntityTypeService trackedEntityTypeService;
    private final FileResourceService fileResourceService;
    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final AclService aclService;
    private final TrackedEntityAttributeStore trackedEntityAttributeStore;
    private final TrackedEntityTypeAttributeStore entityTypeAttributeStore;
    private final ProgramTrackedEntityAttributeStore programAttributeStore;

    public DefaultTrackedEntityAttributeService ( TrackedEntityAttributeStore attributeStore,
        ProgramService programService, TrackedEntityTypeService trackedEntityTypeService,
        FileResourceService fileResourceService, UserService userService, CurrentUserService currentUserService,
        AclService aclService, TrackedEntityAttributeStore trackedEntityAttributeStore,
        TrackedEntityTypeAttributeStore entityTypeAttributeStore,
        ProgramTrackedEntityAttributeStore programAttributeStore )
    {
        checkNotNull( attributeStore );
        checkNotNull( programService );
        checkNotNull( trackedEntityTypeService );
        checkNotNull( fileResourceService );
        checkNotNull( userService );
        checkNotNull( currentUserService );
        checkNotNull( aclService );
        checkNotNull( trackedEntityAttributeStore );
        checkNotNull( entityTypeAttributeStore );
        checkNotNull( programAttributeStore );

        this.attributeStore = attributeStore;
        this.programService = programService;
        this.trackedEntityTypeService = trackedEntityTypeService;
        this.fileResourceService = fileResourceService;
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.aclService = aclService;
        this.trackedEntityAttributeStore = trackedEntityAttributeStore;
        this.entityTypeAttributeStore = entityTypeAttributeStore;
        this.programAttributeStore = programAttributeStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void deleteTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        attributeStore.delete( attribute );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityAttribute> getAllTrackedEntityAttributes()
    {
        return attributeStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityAttribute getTrackedEntityAttribute( long id )
    {
        return attributeStore.get( id );
    }

    @Override
    @Transactional
    public long addTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        attributeStore.save( attribute );
        return attribute.getId();
    }

    @Override
    @Transactional
    public void updateTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        attributeStore.update( attribute );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityAttribute getTrackedEntityAttributeByName( String name )
    {
        return attributeStore.getByName( name );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityAttribute getTrackedEntityAttribute( String uid )
    {
        return attributeStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityAttribute> getTrackedEntityAttributesByDisplayOnVisitSchedule(
        boolean displayOnVisitSchedule )
    {
        return attributeStore.getByDisplayOnVisitSchedule( displayOnVisitSchedule );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityAttribute> getTrackedEntityAttributesDisplayInListNoProgram()
    {
        return attributeStore.getDisplayInListNoProgram();
    }

    @Override
    @Transactional(readOnly = true)
    public String validateAttributeUniquenessWithinScope( TrackedEntityAttribute trackedEntityAttribute,
        String value, TrackedEntityInstance trackedEntityInstance, OrganisationUnit organisationUnit )
    {
        Assert.notNull( trackedEntityAttribute, "tracked entity attribute is required." );
        Assert.notNull( value, "tracked entity attribute value is required." );

        TrackedEntityInstanceQueryParams params = new TrackedEntityInstanceQueryParams();
        params.addAttribute( new QueryItem( trackedEntityAttribute, QueryOperator.EQ, value, trackedEntityAttribute.getValueType(),
            trackedEntityAttribute.getAggregationType(), trackedEntityAttribute.getOptionSet() ) );

        if ( trackedEntityAttribute.getOrgUnitScopeNullSafe() )
        {
            Assert.notNull( organisationUnit, "organisation unit is required for org unit scope" );
            params.addOrganisationUnit( organisationUnit );
        }

        Optional<String> fetchedTeiUid = trackedEntityAttributeStore.getTrackedEntityInstanceUidWithUniqueAttributeValue( params );

        if ( fetchedTeiUid.isPresent() && (trackedEntityInstance == null || !fetchedTeiUid.get().equals( trackedEntityInstance.getUid() )) )
        {
            return "Non-unique attribute value '" + value + "' for attribute " + trackedEntityAttribute.getUid();
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public String validateValueType( TrackedEntityAttribute trackedEntityAttribute, String value )
    {
        Assert.notNull( trackedEntityAttribute, "tracked entity attribute is required" );
        ValueType valueType = trackedEntityAttribute.getValueType();

        String errorValue = StringUtils.substring( value, 0, 30 );

        if ( value.length() > VALUE_MAX_LENGTH )
        {
            return "Value length is greater than 50000 chars for attribute " + trackedEntityAttribute.getUid();
        }

        if ( ValueType.NUMBER == valueType && !MathUtils.isNumeric( value ) )
        {
            return "Value '" + errorValue + "' is not a valid numeric type for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.BOOLEAN == valueType && !MathUtils.isBool( value ) )
        {
            return "Value '" + errorValue + "' is not a valid boolean type for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.DATE == valueType && DateUtils.parseDate( value ) == null )
        {
            return "Value '" + errorValue + "' is not a valid date type for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.TRUE_ONLY == valueType && !"true".equals( value ) )
        {
            return "Value '" + errorValue + "' is not true (true-only type) for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.USERNAME == valueType )
        {
            if ( userService.getUserCredentialsByUsername( value ) == null )
            {
                return "Value '" + errorValue + "' is not a valid username for attribute " + trackedEntityAttribute.getUid();
            }
        }
        else if ( ValueType.DATE == valueType && !DateUtils.dateIsValid( value ) )
        {
            return "Value '" + errorValue + "' is not a valid date for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.DATETIME == valueType && !DateUtils.dateTimeIsValid( value ) )
        {
            return "Value '" + errorValue + "' is not a valid datetime for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.IMAGE == valueType )
        {
            return validateImage( value );
        }
        else if ( trackedEntityAttribute.hasOptionSet() && !trackedEntityAttribute.isValidOptionValue( value ) )
        {
            return "Value '" + errorValue + "' is not a valid option for attribute " +
                trackedEntityAttribute.getUid() + " and option set " + trackedEntityAttribute.getOptionSet().getUid();
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TrackedEntityAttribute> getAllUserReadableTrackedEntityAttributes()
    {
        return getAllUserReadableTrackedEntityAttributes( currentUserService.getCurrentUser() );
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TrackedEntityAttribute> getAllUserReadableTrackedEntityAttributes( User user )
    {
        List<Program> programs = programService.getAllPrograms();
        List<TrackedEntityType> trackedEntityTypes = trackedEntityTypeService.getAllTrackedEntityType();

        return getAllUserReadableTrackedEntityAttributes( user, programs, trackedEntityTypes );
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TrackedEntityAttribute> getAllUserReadableTrackedEntityAttributes( User user, List<Program> programs, List<TrackedEntityType> trackedEntityTypes )
    {
        Set<TrackedEntityAttribute> attributes = new HashSet<>();

        if ( programs != null && !programs.isEmpty() )
        {
            attributes.addAll( programAttributeStore.getAttributes(
                programs.stream().filter( program -> aclService.canDataRead( user, program ) ).collect( Collectors
                    .toList()) ) );
        }

        if ( trackedEntityTypes != null && !trackedEntityTypes.isEmpty() )
        {
            attributes.addAll( entityTypeAttributeStore.getAttributes(
                trackedEntityTypes.stream().filter( trackedEntityType -> aclService.canDataRead( user, trackedEntityType ) ).collect(
                    Collectors.toList()) ));
        }

        return attributes;
    }

    // -------------------------------------------------------------------------
    // ProgramTrackedEntityAttribute
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityAttribute> getAllSystemWideUniqueTrackedEntityAttributes()
    {
        return getAllTrackedEntityAttributes().stream().filter(TrackedEntityAttribute::isSystemWideUnique)
            .collect( Collectors.toList() );
    }

    @Override
    @Transactional( readOnly = true )
    public Set<TrackedEntityAttribute> getTrackedEntityAttributesByTrackedEntityTypes()
    {
        return this.trackedEntityAttributeStore.getTrackedEntityAttributesByTrackedEntityTypes();
    }

    @Override
    @Transactional( readOnly = true )
    public Map<Program, Set<TrackedEntityAttribute>> getTrackedEntityAttributesByProgram()
    {
        return this.trackedEntityAttributeStore.getTrackedEntityAttributesByProgram();
    }

    private String validateImage(String uid )
    {
        FileResource fileResource = fileResourceService.getFileResource( uid );

        if ( fileResource == null )
        {
            return "Value '" + uid + "' is not the uid of a file";
        }
        else if ( !VALID_IMAGE_FORMATS.contains( fileResource.getFormat() ) )
        {
            return "File resource with uid '" + uid + "' is not a valid image";
        }

        return null;
    }
}
