package com.mass3d.webapi.controller;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.render.RenderService;
import com.mass3d.schema.SchemaService;
import com.mass3d.security.acl.AclService;
import com.mass3d.todotask.TodoTask;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserAccessService;
import com.mass3d.user.UserGroupAccessService;
import com.mass3d.user.UserGroupService;
import com.mass3d.user.UserService;
import com.mass3d.webapi.service.WebMessageService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import static org.hamcrest.core.StringContains.containsString;

/**
 * Unit tests for {@link SharingController}.
 *
 */
public class SharingControllerTest
{
    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private IdentifiableObjectManager manager;

    @Mock
    private UserGroupService userGroupService;

    @Mock
    private UserService userService;

    @Mock
    private UserGroupAccessService userGroupAccessService;

    @Mock
    private UserAccessService userAccessService;

    @Mock
    private AclService aclService;

    @Mock
    private WebMessageService webMessageService;

    @Mock
    private RenderService renderService;

    @Mock
    private SchemaService schemaService;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private MockHttpServletResponse response = new MockHttpServletResponse();

    @InjectMocks
    private SharingController sharingController;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test( expected = AccessDeniedException.class )
    public void notSystemDefaultMetadataNoAccess() throws Exception
    {
        final TodoTask todoTask = new TodoTask();

        Mockito.doReturn( TodoTask.class ).when( aclService ).classForType( Mockito.eq( "todoTask" ) );
        Mockito.when( aclService.isShareable( Mockito.eq( TodoTask.class ) ) ).thenReturn( true );
        Mockito.doReturn( todoTask ).when( manager ).get( Mockito.eq( TodoTask.class ), Mockito.eq( "kkSjhdhks" ) );

        sharingController.setSharing( "todoTask", "kkSjhdhks", response, request );
    }

//    @Test( expected = AccessDeniedException.class )
//    public void systemDefaultMetadataNoAccess() throws Exception
//    {
//        final Category category = new Category();
//        category.setName( Category.DEFAULT_NAME + "x" );
//
//        Mockito.doReturn( Category.class ).when( aclService ).classForType( Mockito.eq( "category" ) );
//        Mockito.when( aclService.isShareable( Mockito.eq( Category.class ) ) ).thenReturn( true );
//        Mockito.when( manager.get( Mockito.eq( Category.class ), Mockito.eq( "kkSjhdhks" ) ) ).thenReturn( category );
//
//        sharingController.setSharing( "category", "kkSjhdhks", response, request );
//    }

//    @Test( expected = WebMessageException.class )
//    public void systemDefaultMetadata() throws Exception
//    {
//        final Category category = new Category();
//        category.setName( Category.DEFAULT_NAME );
//
//        Mockito.doReturn( Category.class ).when( aclService ).classForType( Mockito.eq( "category" ) );
//        Mockito.when( aclService.isShareable( Mockito.eq( Category.class ) ) ).thenReturn( true );
//        Mockito.when( manager.get( Mockito.eq( Category.class ), Mockito.eq( "kkSjhdhks" ) ) ).thenReturn( category );
//
//        try
//        {
//            sharingController.setSharing( "category", "kkSjhdhks", response, request );
//        }
//        catch ( WebMessageException e )
//        {
//            Assert.assertThat( e.getWebMessage().getMessage(), containsString( "Sharing settings of system default metadata object" ) );
//            throw e;
//        }
//    }
}
