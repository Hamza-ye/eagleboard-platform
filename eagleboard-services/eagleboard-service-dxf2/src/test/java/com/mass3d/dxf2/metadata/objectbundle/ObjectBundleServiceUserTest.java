package com.mass3d.dxf2.metadata.objectbundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dxf2.metadata.AtomicMode;
import com.mass3d.dxf2.metadata.objectbundle.feedback.ObjectBundleValidationReport;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.render.RenderFormat;
import com.mass3d.render.RenderService;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;

public class ObjectBundleServiceUserTest
    extends DhisSpringTest
{
    @Autowired
    private ObjectBundleService objectBundleService;

    @Autowired
    private ObjectBundleValidationService objectBundleValidationService;

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private RenderService _renderService;

    @Autowired
    private UserService _userService;

    @Override
    protected void setUpTest() throws Exception
    {
        renderService = _renderService;
        userService = _userService;
    }

    @Test
    public void testCreateUsers() throws IOException
    {
        createUserAndInjectSecurityContext( true );

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/users.json" ).getInputStream(), RenderFormat.JSON );

        ObjectBundleParams params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.CREATE );
        params.setAtomicMode( AtomicMode.NONE );
        params.setObjects( metadata );

        ObjectBundle bundle = objectBundleService.create( params );
        ObjectBundleValidationReport validate = objectBundleValidationService.validate( bundle );
        assertEquals( 1, validate.getErrorReportsByCode( UserAuthorityGroup.class, ErrorCode.E5003 ).size() );
        objectBundleService.commit( bundle );

        List<User> users = manager.getAll( User.class );
        assertEquals( 4, users.size() );

        User userA = manager.get( User.class, "sPWjoHSY03y" );
        User userB = manager.get( User.class, "MwhEJUnTHkn" );

        assertNotNull( userA );
        assertNotNull( userB );

        assertNotNull( userA.getUserCredentials().getUserInfo() );
        assertNotNull( userB.getUserCredentials().getUserInfo() );
        assertNotNull( userA.getUserCredentials().getUserInfo().getUserCredentials() );
        assertNotNull( userB.getUserCredentials().getUserInfo().getUserCredentials() );
        assertEquals( "UserA", userA.getUserCredentials().getUserInfo().getUserCredentials().getUsername() );
        assertEquals( "UserB", userB.getUserCredentials().getUserInfo().getUserCredentials().getUsername() );

        assertNotNull( userA.getUserCredentials().getUser() );
        assertNotNull( userB.getUserCredentials().getUser() );
        assertNotNull( userA.getUserCredentials().getUser().getUserCredentials() );
        assertNotNull( userB.getUserCredentials().getUser().getUserCredentials() );
        assertEquals( "admin", userA.getUserCredentials().getUser().getUserCredentials().getUsername() );
        assertEquals( "admin", userB.getUserCredentials().getUser().getUserCredentials().getUsername() );

//        assertEquals( 1, userA.getOrganisationUnits().size() );
//        assertEquals( 1, userB.getOrganisationUnits().size() );
    }

    @Test
    public void testUpdateUsers() throws IOException
    {
        createUserAndInjectSecurityContext( true );

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/users.json" ).getInputStream(), RenderFormat.JSON );

        ObjectBundleParams params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.CREATE );
        params.setAtomicMode( AtomicMode.NONE );
        params.setObjects( metadata );

        ObjectBundle bundle = objectBundleService.create( params );
        ObjectBundleValidationReport validate = objectBundleValidationService.validate( bundle );
        assertEquals( 1, validate.getErrorReportsByCode( UserAuthorityGroup.class, ErrorCode.E5003 ).size() );
        objectBundleService.commit( bundle );

        metadata = renderService.fromMetadata( new ClassPathResource( "dxf2/users_update.json" ).getInputStream(), RenderFormat.JSON );

        params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.UPDATE );
        params.setAtomicMode( AtomicMode.NONE );
        params.setObjects( metadata );

        bundle = objectBundleService.create( params );
        validate = objectBundleValidationService.validate( bundle );
        assertEquals( 1, validate.getErrorReportsByCode( UserAuthorityGroup.class, ErrorCode.E5001 ).size() );
        objectBundleService.commit( bundle );

        List<User> users = manager.getAll( User.class );
        assertEquals( 4, users.size() );

        User userA = manager.get( User.class, "sPWjoHSY03y" );
        User userB = manager.get( User.class, "MwhEJUnTHkn" );

        assertNotNull( userA );
        assertNotNull( userB );

        assertNotNull( userA.getUserCredentials().getUserInfo() );
        assertNotNull( userB.getUserCredentials().getUserInfo() );
        assertNotNull( userA.getUserCredentials().getUserInfo().getUserCredentials() );
        assertNotNull( userB.getUserCredentials().getUserInfo().getUserCredentials() );
        assertEquals( "UserAA", userA.getUserCredentials().getUserInfo().getUserCredentials().getUsername() );
        assertEquals( "UserBB", userB.getUserCredentials().getUserInfo().getUserCredentials().getUsername() );

        assertNotNull( userA.getUserCredentials().getUser() );
        assertNotNull( userB.getUserCredentials().getUser() );
        assertNotNull( userA.getUserCredentials().getUser().getUserCredentials() );
        assertNotNull( userB.getUserCredentials().getUser().getUserCredentials() );
        assertEquals( "admin", userA.getUserCredentials().getUser().getUserCredentials().getUsername() );
        assertEquals( "admin", userB.getUserCredentials().getUser().getUserCredentials().getUsername() );
    }

    @Test
    public void testCreateMetadataWithDuplicateUsername() throws IOException
    {
        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/user_duplicate_username.json" ).getInputStream(), RenderFormat.JSON );

        ObjectBundleParams params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.CREATE_AND_UPDATE );
        params.setAtomicMode( AtomicMode.NONE );
        params.setObjects( metadata );

        ObjectBundle bundle = objectBundleService.create( params );
        objectBundleValidationService.validate( bundle );
        objectBundleService.commit( bundle );

        assertEquals( 1, manager.getAll( User.class ).size() );
    }

    @Test
    public void testCreateMetadataWithDuplicateUsernameAndInjectedUser() throws IOException
    {
        createUserAndInjectSecurityContext( true );

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/user_duplicate_username.json" ).getInputStream(), RenderFormat.JSON );

        ObjectBundleParams params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.CREATE_AND_UPDATE );
        params.setAtomicMode( AtomicMode.NONE );
        params.setObjects( metadata );

        ObjectBundle bundle = objectBundleService.create( params );
        objectBundleValidationService.validate( bundle );

        objectBundleService.commit( bundle );
        assertEquals( 2, manager.getAll( User.class ).size() );
    }

    @Test
    public void testUpdateAdminUser() throws IOException
    {
        createAndInjectAdminUser();

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/user_admin.json" ).getInputStream(), RenderFormat.JSON );

        ObjectBundleParams params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.UPDATE );
        params.setAtomicMode( AtomicMode.ALL );
        params.setObjects( metadata );

        ObjectBundle bundle = objectBundleService.create( params );
        assertEquals( 0, objectBundleValidationService.validate( bundle ).getErrorReports().size() );
    }

    @Test
    public void testCreateUsersWithInvalidPasswords() throws IOException
    {
        createUserAndInjectSecurityContext( true );

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/users_passwords.json" ).getInputStream(), RenderFormat.JSON );

        ObjectBundleParams params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.VALIDATE );
        params.setImportStrategy( ImportStrategy.CREATE );
        params.setObjects( metadata );

        ObjectBundle bundle = objectBundleService.create( params );
        ObjectBundleValidationReport validate = objectBundleValidationService.validate( bundle );
        assertEquals( 1, validate.getErrorReportsByCode( User.class, ErrorCode.E4005 ).size() );
    }

    @Test
    public void testUpdateUserWithNoAccessUserRole()
        throws IOException
    {
        createUserAndInjectSecurityContext( true );

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/user_userrole.json" ).getInputStream(), RenderFormat.JSON );

        ObjectBundleParams params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.CREATE_AND_UPDATE );
        params.setObjects( metadata );

        ObjectBundle bundle = objectBundleService.create( params );

        objectBundleService.commit( bundle );

        User userB = manager.get( User.class, "MwhEJUnTHkn" );
        User userA = manager.get( User.class, "sPWjoHSY03y" );

        assertEquals( 2, userA.getUserCredentials().getUserAuthorityGroups().size() );
        assertEquals( 2, userB.getUserCredentials().getUserAuthorityGroups().size() );

        UserAuthorityGroup userManagerRole = manager.get( UserAuthorityGroup.class, "xJZBzAHI88H" );
        assertNotNull(  userManagerRole );
        userManagerRole.getUserAccesses().clear();
        userManagerRole.getUserAccesses().add( new UserAccess( userB, "rw------" ) );
        userManagerRole.setPublicAccess( "--------" );
        userManagerRole.setUser( userB );
        manager.update( userManagerRole );

        SecurityContextHolder.clearContext();
        userA.getUserCredentials().setPassword( "passwordUserA" );
        manager.update( userA );
        injectSecurityContext( userA );

       metadata = renderService.fromMetadata(
            new ClassPathResource( "dxf2/user_userrole_update.json" ).getInputStream(), RenderFormat.JSON );

        params = new ObjectBundleParams();
        params.setObjectBundleMode( ObjectBundleMode.COMMIT );
        params.setImportStrategy( ImportStrategy.CREATE_AND_UPDATE );
        params.setObjects( metadata );

        bundle = objectBundleService.create( params );
        objectBundleService.commit( bundle );

        assertEquals( 2, userA.getUserCredentials().getUserAuthorityGroups().size() );
        assertEquals( 2, userB.getUserCredentials().getUserAuthorityGroups().size() );

    }
}
