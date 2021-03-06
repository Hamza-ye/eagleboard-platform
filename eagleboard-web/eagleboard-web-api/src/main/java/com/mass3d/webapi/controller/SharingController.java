package com.mass3d.webapi.controller;

import com.mass3d.common.SystemDefaultMetadataObject;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramType;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.program.Program;
import com.mass3d.render.RenderService;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.security.acl.AccessStringHelper;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserAccessService;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserGroupAccess;
import com.mass3d.user.UserGroupAccessService;
import com.mass3d.user.UserGroupService;
import com.mass3d.user.UserService;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.WebMessageService;
import com.mass3d.webapi.utils.ContextUtils;
import com.mass3d.webapi.webdomain.sharing.Sharing;
import com.mass3d.webapi.webdomain.sharing.SharingUserAccess;
import com.mass3d.webapi.webdomain.sharing.SharingUserGroupAccess;
import com.mass3d.webapi.webdomain.sharing.comparator.SharingUserGroupAccessNameComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping( value = SharingController.RESOURCE_PATH, method = RequestMethod.GET )
@Slf4j
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class SharingController
{
    public static final String RESOURCE_PATH = "/sharing";

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupAccessService userGroupAccessService;

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private AclService aclService;

    @Autowired
    private WebMessageService webMessageService;

    @Autowired
    private RenderService renderService;

    @Autowired
    private SchemaService schemaService;

    // -------------------------------------------------------------------------
    // Resources
    // -------------------------------------------------------------------------

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    public void getSharing( @RequestParam String type, @RequestParam String id, HttpServletResponse response ) throws IOException, WebMessageException
    {
        type = getSharingType( type );

        if ( !aclService.isShareable( type ) )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Type " + type + " is not supported." ) );
        }

        Class<? extends IdentifiableObject> klass = aclService.classForType( type );
        IdentifiableObject object = manager.get( klass, id );

        if ( object == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "Object of type " + type + " with ID " + id + " was not found." ) );
        }

        User user = currentUserService.getCurrentUser();

        if ( !aclService.canRead( user, object ) )
        {
            throw new AccessDeniedException( "You do not have manage access to this object." );
        }

        Sharing sharing = new Sharing();

        sharing.getMeta().setAllowPublicAccess( aclService.canMakePublic( user, object.getClass() ) );
        sharing.getMeta().setAllowExternalAccess( aclService.canMakeExternal( user, object.getClass() ) );

        sharing.getObject().setId( object.getUid() );
        sharing.getObject().setName( object.getDisplayName() );
        sharing.getObject().setDisplayName( object.getDisplayName() );
        sharing.getObject().setExternalAccess( object.getExternalAccess() );

        if ( object.getPublicAccess() == null )
        {
            String access;

            if ( aclService.canMakePublic( user, klass ) )
            {
                access = AccessStringHelper.newInstance().enable( AccessStringHelper.Permission.READ ).enable( AccessStringHelper.Permission.WRITE ).build();
            }
            else
            {
                access = AccessStringHelper.newInstance().build();
            }

            sharing.getObject().setPublicAccess( access );
        }
        else
        {
            sharing.getObject().setPublicAccess( object.getPublicAccess() );
        }

        if ( object.getUser() != null )
        {
            sharing.getObject().getUser().setId( object.getUser().getUid() );
            sharing.getObject().getUser().setName( object.getUser().getDisplayName() );
        }

        for ( UserGroupAccess userGroupAccess : object.getUserGroupAccesses() )
        {
            SharingUserGroupAccess sharingUserGroupAccess = new SharingUserGroupAccess();
            sharingUserGroupAccess.setId( userGroupAccess.getUserGroup().getUid() );
            sharingUserGroupAccess.setName( userGroupAccess.getUserGroup().getDisplayName() );
            sharingUserGroupAccess.setDisplayName( userGroupAccess.getUserGroup().getDisplayName() );
            sharingUserGroupAccess.setAccess( userGroupAccess.getAccess() );

            sharing.getObject().getUserGroupAccesses().add( sharingUserGroupAccess );
        }

        for ( UserAccess userAccess : object.getUserAccesses() )
        {
            SharingUserAccess sharingUserAccess = new SharingUserAccess();

            sharingUserAccess.setId( userAccess.getUser().getUid() );
            sharingUserAccess.setName( userAccess.getUser().getDisplayName() );
            sharingUserAccess.setDisplayName( userAccess.getUser().getDisplayName() );
            sharingUserAccess.setAccess( userAccess.getAccess() );

            sharing.getObject().getUserAccesses().add( sharingUserAccess );
        }

        sharing.getObject().getUserGroupAccesses().sort( SharingUserGroupAccessNameComparator.INSTANCE );

        response.setContentType( MediaType.APPLICATION_JSON_VALUE );
        response.setHeader( ContextUtils.HEADER_CACHE_CONTROL, CacheControl.noCache().cachePrivate().getHeaderValue() );
        renderService.toJson( response.getOutputStream(), sharing );
    }

    @RequestMapping( method = { RequestMethod.POST, RequestMethod.PUT }, consumes = MediaType.APPLICATION_JSON_VALUE )
    public void setSharing( @RequestParam String type, @RequestParam String id, HttpServletResponse response, HttpServletRequest request ) throws IOException, WebMessageException
    {
        type = getSharingType( type );

        Class<? extends IdentifiableObject> sharingClass = aclService.classForType( type );

        if ( sharingClass == null || !aclService.isShareable( sharingClass ) )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Type " + type + " is not supported." ) );
        }

        BaseIdentifiableObject object = (BaseIdentifiableObject) manager.get( sharingClass, id );

        if ( object == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "Object of type " + type + " with ID " + id + " was not found." ) );
        }

        if ( ( object instanceof SystemDefaultMetadataObject) && ( (SystemDefaultMetadataObject) object ).isDefault() )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Sharing settings of system default metadata object of type " + type + " cannot be modified." ) );
        }

        User user = currentUserService.getCurrentUser();

        if ( !aclService.canManage( user, object ) )
        {
            throw new AccessDeniedException( "You do not have manage access to this object." );
        }

        Sharing sharing = renderService.fromJson( request.getInputStream(), Sharing.class );

        if ( !AccessStringHelper.isValid( sharing.getObject().getPublicAccess() ) )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Invalid public access string: " + sharing.getObject().getPublicAccess() ) );
        }

        // ---------------------------------------------------------------------
        // Ignore externalAccess if user is not allowed to make objects external
        // ---------------------------------------------------------------------

        if ( aclService.canMakeExternal( user, object.getClass() ) )
        {
            object.setExternalAccess( sharing.getObject().hasExternalAccess() );
        }

        // ---------------------------------------------------------------------
        // Ignore publicAccess if user is not allowed to make objects public
        // ---------------------------------------------------------------------

        Schema schema = schemaService.getDynamicSchema( sharingClass );

        if ( aclService.canMakePublic( user, object.getClass() ) )
        {
            object.setPublicAccess( sharing.getObject().getPublicAccess() );
        }

        if ( !schema.isDataShareable() )
        {
            if ( AccessStringHelper.hasDataSharing( object.getPublicAccess() ) )
            {
                throw new WebMessageException( WebMessageUtils.conflict( "Data sharing is not enabled for this object." ) );
            }
        }

        if ( object.getUser() == null )
        {
            object.setUser( user );
        }

        Iterator<UserGroupAccess> userGroupAccessIterator = object.getUserGroupAccesses().iterator();

        while ( userGroupAccessIterator.hasNext() )
        {
            UserGroupAccess userGroupAccess = userGroupAccessIterator.next();
            userGroupAccessIterator.remove();

            userGroupAccessService.deleteUserGroupAccess( userGroupAccess );
        }

        for ( SharingUserGroupAccess sharingUserGroupAccess : sharing.getObject().getUserGroupAccesses() )
        {
            UserGroupAccess userGroupAccess = new UserGroupAccess();

            if ( !AccessStringHelper.isValid( sharingUserGroupAccess.getAccess() ) )
            {
                throw new WebMessageException( WebMessageUtils.conflict( "Invalid user group access string: " + sharingUserGroupAccess.getAccess() ) );
            }

            if ( !schema.isDataShareable() )
            {
                if ( AccessStringHelper.hasDataSharing( sharingUserGroupAccess.getAccess() ) )
                {
                    throw new WebMessageException( WebMessageUtils.conflict( "Data sharing is not enabled for this object." ) );
                }
            }

            userGroupAccess.setAccess( sharingUserGroupAccess.getAccess() );

            UserGroup userGroup = manager.get( UserGroup.class, sharingUserGroupAccess.getId() );

            if ( userGroup != null )
            {
                userGroupAccess.setUserGroup( userGroup );
                userGroupAccessService.addUserGroupAccess( userGroupAccess );

                object.getUserGroupAccesses().add( userGroupAccess );
            }
        }

        Iterator<UserAccess> userAccessIterator = object.getUserAccesses().iterator();

        while ( userAccessIterator.hasNext() )
        {
            UserAccess userAccess = userAccessIterator.next();
            userAccessIterator.remove();

            userAccessService.deleteUserAccess( userAccess );
        }

        for ( SharingUserAccess sharingUserAccess : sharing.getObject().getUserAccesses() )
        {
            UserAccess userAccess = new UserAccess();

            if ( !AccessStringHelper.isValid( sharingUserAccess.getAccess() ) )
            {
                throw new WebMessageException( WebMessageUtils.conflict( "Invalid user access string: " + sharingUserAccess.getAccess() ) );
            }

            if ( !schema.isDataShareable() )
            {
                if ( AccessStringHelper.hasDataSharing( sharingUserAccess.getAccess() ) )
                {
                    throw new WebMessageException( WebMessageUtils.conflict( "Data sharing is not enabled for this object." ) );
                }
            }

            userAccess.setAccess( sharingUserAccess.getAccess() );

            User sharingUser = manager.get( User.class, sharingUserAccess.getId() );

            if ( sharingUser != null )
            {
                userAccess.setUser( sharingUser );
                userAccessService.addUserAccess( userAccess );

                object.getUserAccesses().add( userAccess );
            }
        }

        manager.updateNoAcl( object );

        if ( Program.class.isInstance( object ) )
        {
            syncSharingForEventProgram( (Program) object );
        }

        log.info( sharingToString( object ) );

        webMessageService.send( WebMessageUtils.ok( "Access control set" ), response, request );
    }

    @RequestMapping( value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    public void searchUserGroups( @RequestParam String key, @RequestParam( required = false ) Integer pageSize,
        HttpServletResponse response ) throws IOException, WebMessageException
    {
        if ( key == null )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Search key not specified" ) );
        }

        int max = pageSize != null ? pageSize : Integer.MAX_VALUE;

        List<SharingUserGroupAccess> userGroupAccesses = getSharingUserGroups( key, max );
        List<SharingUserAccess> userAccesses = getSharingUser( key, max );

        Map<String, Object> output = new HashMap<>();
        output.put( "userGroups", userGroupAccesses );
        output.put( "users", userAccesses );

        response.setContentType( MediaType.APPLICATION_JSON_VALUE );
        response.setHeader( ContextUtils.HEADER_CACHE_CONTROL, CacheControl.noCache().cachePrivate().getHeaderValue() );
        renderService.toJson( response.getOutputStream(), output );
    }

    private List<SharingUserAccess> getSharingUser( String key, int max )
    {
        List<SharingUserAccess> sharingUsers = new ArrayList<>();
        List<User> users = userService.getAllUsersBetweenByName( key, 0, max );

        for ( User user : users )
        {
            SharingUserAccess sharingUserAccess = new SharingUserAccess();
            sharingUserAccess.setId( user.getUid() );
            sharingUserAccess.setName( user.getDisplayName() );
            sharingUserAccess.setDisplayName( user.getDisplayName() );

            sharingUsers.add( sharingUserAccess );
        }

        return sharingUsers;
    }

    private List<SharingUserGroupAccess> getSharingUserGroups( @RequestParam String key, int max )
    {
        List<SharingUserGroupAccess> sharingUserGroupAccesses = new ArrayList<>();
        List<UserGroup> userGroups = userGroupService.getUserGroupsBetweenByName( key, 0, max );

        for ( UserGroup userGroup : userGroups )
        {
            SharingUserGroupAccess sharingUserGroupAccess = new SharingUserGroupAccess();

            sharingUserGroupAccess.setId( userGroup.getUid() );
            sharingUserGroupAccess.setName( userGroup.getDisplayName() );
            sharingUserGroupAccess.setDisplayName( userGroup.getDisplayName() );

            sharingUserGroupAccesses.add( sharingUserGroupAccess );
        }

        return sharingUserGroupAccesses;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String sharingToString( BaseIdentifiableObject object )
    {
        StringBuilder builder = new StringBuilder()
            .append( "'" ).append( currentUserService.getCurrentUsername() ).append( "'" )
            .append( " update sharing on " ).append( object.getClass().getName() )
            .append( ", uid: " ).append( object.getUid() )
            .append( ", name: " ).append( object.getName() )
            .append( ", publicAccess: " ).append( object.getPublicAccess() )
            .append( ", externalAccess: " ).append( object.getExternalAccess() );

        if ( !object.getUserGroupAccesses().isEmpty() )
        {
            builder.append( ", userGroupAccesses: " );

            for ( UserGroupAccess userGroupAccess : object.getUserGroupAccesses() )
            {
                builder.append( "{uid: " ).append( userGroupAccess.getUserGroup().getUid() )
                    .append( ", name: " ).append( userGroupAccess.getUserGroup().getName() )
                    .append( ", access: " ).append( userGroupAccess.getAccess() )
                    .append( "} " );
            }
        }

        if ( !object.getUserAccesses().isEmpty() )
        {
            builder.append( ", userAccesses: " );

            for ( UserAccess userAccess : object.getUserAccesses() )
            {
                builder.append( "{uid: " ).append( userAccess.getUser().getUid() )
                    .append( ", name: " ).append( userAccess.getUser().getName() )
                    .append( ", access: " ).append( userAccess.getAccess() )
                    .append( "} " );
            }
        }

        return builder.toString();
    }

    private void syncSharingForEventProgram( Program program )
    {
        if ( ProgramType.WITH_REGISTRATION == program.getProgramType()
            || program.getProgramStages().isEmpty() )
        {
            return;
        }

        ProgramStage programStage = program.getProgramStages().iterator().next();
        AccessStringHelper.copySharing( program, programStage );

        programStage.setUser( program.getUser() );
        manager.update( programStage );
    }

    /**
     * This method is being used only during the deprecation process of the
     * Pivot/ReportTable API. It must be removed once the process is complete.
     * 
     * @return "visualization" if the given type is equals to "reportTable" or
     *         "chart", otherwise it returns the given type itself.
     */
    @Deprecated
    private String getSharingType( final String type )
    {
        if ( "reportTable".equalsIgnoreCase( type ) || "chart".equalsIgnoreCase( type ) )
        {
            return "visualization";
        }
        return type;
    }
}
