package com.mass3d.dxf2.metadata.objectbundle.hooks;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceService;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.schema.MergeParams;
import com.mass3d.security.acl.AclService;
import com.mass3d.system.util.ValidationUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserObjectBundleHook extends AbstractObjectBundleHook
{
    private final UserService userService;

    private final FileResourceService fileResourceService;

    private final CurrentUserService currentUserService;

    private final AclService aclService;

    public UserObjectBundleHook( UserService userService, FileResourceService fileResourceService, CurrentUserService currentUserService, AclService aclService )
    {
        checkNotNull( userService );
        checkNotNull( fileResourceService );
        checkNotNull( currentUserService );
        checkNotNull( aclService );
        this.userService = userService;
        this.fileResourceService = fileResourceService;
        this.currentUserService = currentUserService;
        this.aclService = aclService;
    }

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        if ( !(object instanceof User) )
        {
            return new ArrayList<>();
        }

        ArrayList<ErrorReport> errorReports = new ArrayList<>(  );
        User user = (User) object;

        if ( user.getWhatsApp() != null && !ValidationUtils.validateWhatsapp( user.getWhatsApp() ) )
        {
            errorReports.add( new ErrorReport( User.class, ErrorCode.E4027, user.getWhatsApp(), "Whatsapp" ) );
        }

        return errorReports;
    }

    @Override
    public void preCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !User.class.isInstance( object ) || ((User) object).getUserCredentials() == null ) return;

        User user = (User) object;

        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser != null )
        {
//            user.getUserCredentials().getCogsDimensionConstraints().addAll(
//                currentUser.getUserCredentials().getCogsDimensionConstraints() );
//
//            user.getUserCredentials().getCatDimensionConstraints().addAll(
//                currentUser.getUserCredentials().getCatDimensionConstraints() );
        }

        bundle.putExtras( user, "uc", user.getUserCredentials() );
        user.setUserCredentials( null );
    }

    @Override
    public void postCreate( IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        if ( !User.class.isInstance( persistedObject ) || !bundle.hasExtras( persistedObject, "uc" ) ) return;

        User user = (User) persistedObject;
        final UserCredentials userCredentials = (UserCredentials) bundle.getExtras( persistedObject, "uc" );

        if ( !StringUtils.isEmpty( userCredentials.getPassword() ) )
        {
            userService.encodeAndSetPassword( userCredentials, userCredentials.getPassword() );
        }

        if ( user.getAvatar() != null )
        {
            FileResource fileResource = fileResourceService.getFileResource( user.getAvatar().getUid() );
            fileResource.setAssigned( true );
            fileResourceService.updateFileResource( fileResource );
        }

        userCredentials.setUserInfo( user );
        preheatService.connectReferences( userCredentials, bundle.getPreheat(), bundle.getPreheatIdentifier() );
        sessionFactory.getCurrentSession().save( userCredentials );
        user.setUserCredentials( userCredentials );
        sessionFactory.getCurrentSession().update( user );
        bundle.removeExtras( persistedObject, "uc" );
    }

    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        if ( !User.class.isInstance( object ) || ((User) object).getUserCredentials() == null ) return;
        User user = (User) object;
        bundle.putExtras( user, "uc", user.getUserCredentials() );

        User persisted = (User) persistedObject;

        if ( persisted.getAvatar() != null && (user.getAvatar() == null || !persisted.getAvatar().getUid().equals( user.getAvatar().getUid() ) ) )
        {
            FileResource fileResource = fileResourceService.getFileResource( persisted.getAvatar().getUid() );
            fileResourceService.updateFileResource( fileResource );

            if ( user.getAvatar() != null )
            {
                fileResource = fileResourceService.getFileResource( user.getAvatar().getUid() );
                fileResource.setAssigned( true );
                fileResourceService.updateFileResource( fileResource );
            }
        }
    }

    @Override
    public void postUpdate( IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        if ( !User.class.isInstance( persistedObject ) || !bundle.hasExtras( persistedObject, "uc" ) ) return;

        User user = (User) persistedObject;
        final UserCredentials userCredentials = (UserCredentials) bundle.getExtras( persistedObject, "uc" );
        final UserCredentials persistedUserCredentials = bundle.getPreheat().get( bundle.getPreheatIdentifier(), UserCredentials.class, user );

        if ( !StringUtils.isEmpty( userCredentials.getPassword() ) )
        {
            userService.encodeAndSetPassword( persistedUserCredentials, userCredentials.getPassword() );
        }

        mergeService.merge( new MergeParams<>( userCredentials, persistedUserCredentials ).setMergeMode( bundle.getMergeMode() ) );
        preheatService.connectReferences( persistedUserCredentials, bundle.getPreheat(), bundle.getPreheatIdentifier() );

        persistedUserCredentials.setUserInfo( user );
        user.setUserCredentials( persistedUserCredentials );

        sessionFactory.getCurrentSession().update( user.getUserCredentials() );
        bundle.removeExtras( persistedObject, "uc" );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void postCommit( ObjectBundle bundle )
    {
        if ( !bundle.getObjectMap().containsKey( User.class ) ) return;

        List<IdentifiableObject> objects = bundle.getObjectMap().get( User.class );
        Map<String, Map<String, Object>> userReferences = bundle.getObjectReferences( User.class );
        Map<String, Map<String, Object>> userCredentialsReferences = bundle.getObjectReferences( UserCredentials.class );

        if ( userReferences == null || userReferences.isEmpty() || userCredentialsReferences == null || userCredentialsReferences.isEmpty() )
        {
            return;
        }

        for ( IdentifiableObject identifiableObject : objects )
        {
            User user = (User) identifiableObject;
            handleNoAccessRoles( user, bundle );

            user = bundle.getPreheat().get( bundle.getPreheatIdentifier(), user );
            Map<String, Object> userReferenceMap = userReferences.get( identifiableObject.getUid() );

            if ( userReferenceMap == null || userReferenceMap.isEmpty() )
            {
                continue;
            }

            UserCredentials userCredentials = user.getUserCredentials();

            if ( userCredentials == null )
            {
                continue;
            }

            Map<String, Object> userCredentialsReferenceMap = userCredentialsReferences.get( userCredentials.getUid() );

            if ( userCredentialsReferenceMap == null || userCredentialsReferenceMap.isEmpty() )
            {
                continue;
            }

//            user.setTodoTasks( (Set<OrganisationUnit>) userReferenceMap.get( "organisationUnits" ) );
//            user.setDataViewOrganisationUnits( (Set<OrganisationUnit>) userReferenceMap.get( "dataViewOrganisationUnits" ) );
            userCredentials.setUser( (User) userCredentialsReferenceMap.get( "user" ) );
            userCredentials.setUserInfo( user );

            preheatService.connectReferences( user, bundle.getPreheat(), bundle.getPreheatIdentifier() );
            preheatService.connectReferences( userCredentials, bundle.getPreheat(), bundle.getPreheatIdentifier() );

            user.setUserCredentials( userCredentials );
            sessionFactory.getCurrentSession().update( user );
        }
    }

    /**
     * If currentUser doesn't have read access to a UserRole  and it is included in the
     * payload, then that UserRole should not be removed from updating User.
     *
     * @param user the updating User.
     * @param bundle the ObjectBundle.
     */
    private void handleNoAccessRoles( User user, ObjectBundle bundle )
    {
        Set<String> preHeatedRoles = bundle.getPreheat().get( PreheatIdentifier.UID, user )
            .getUserCredentials().getUserAuthorityGroups().stream().map( role -> role.getUid() ).collect( Collectors
                .toSet() );

        user.getUserCredentials().getUserAuthorityGroups().stream()
            .filter( role -> !preHeatedRoles.contains( role.getUid() ) )
            .forEach( role -> {
                UserAuthorityGroup persistedRole = bundle.getPreheat().get( PreheatIdentifier.UID, role );

                if ( persistedRole == null )
                {
                    persistedRole = manager.getNoAcl( UserAuthorityGroup.class, role.getUid() );
                }

                if ( !aclService.canRead( bundle.getUser(), persistedRole ) )
                {
                    bundle.getPreheat().get( PreheatIdentifier.UID, user ).getUserCredentials().getUserAuthorityGroups().add( persistedRole );
                    bundle.getPreheat().put( PreheatIdentifier.UID, persistedRole );
                }
            } );
    }
}
