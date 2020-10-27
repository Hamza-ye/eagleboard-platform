package com.mass3d.webapi.controller.user;

import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.hibernate.exception.DeleteAccessDeniedException;
import com.mass3d.hibernate.exception.UpdateAccessDeniedException;
import com.mass3d.query.Order;
import com.mass3d.query.QueryParserException;
import com.mass3d.schema.descriptors.UserRoleSchemaDescriptor;
import com.mass3d.user.User;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserService;
import com.mass3d.webapi.controller.AbstractCrudController;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.webapi.webdomain.WebMetadata;
import com.mass3d.webapi.webdomain.WebOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping( value = UserRoleSchemaDescriptor.API_ENDPOINT )
public class UserRoleController
    extends AbstractCrudController<UserAuthorityGroup>
{
    @Autowired
    private UserService userService;

    @Override
    protected List<UserAuthorityGroup> getEntityList( WebMetadata metadata, WebOptions options, List<String> filters, List<Order> orders )
        throws QueryParserException
    {
        List<UserAuthorityGroup> entityList = super.getEntityList( metadata, options, filters, orders );

        if ( options.getOptions().containsKey( "canIssue" ) && Boolean.parseBoolean( options.getOptions().get( "canIssue" ) ) )
        {
            userService.canIssueFilter( entityList );
        }

        return entityList;
    }

    @RequestMapping( value = "/{id}/users/{userId}", method = { RequestMethod.POST, RequestMethod.PUT } )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void addUserToRole( @PathVariable( value = "id" ) String pvId, @PathVariable( "userId" ) String pvUserId, HttpServletResponse response ) throws WebMessageException
    {
        UserAuthorityGroup userAuthorityGroup = userService.getUserAuthorityGroup( pvId );

        if ( userAuthorityGroup == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "UserRole does not exist: " + pvId ) );
        }

        User user = userService.getUser( pvUserId );

        if ( user == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "User does not exist: " + pvId ) );
        }

        if ( !aclService.canUpdate( currentUserService.getCurrentUser(), userAuthorityGroup ) )
        {
            throw new UpdateAccessDeniedException( "You don't have the proper permissions to update this object." );
        }

        if ( !user.getUserCredentials().getUserAuthorityGroups().contains( userAuthorityGroup ) )
        {
            user.getUserCredentials().getUserAuthorityGroups().add( userAuthorityGroup );
            userService.updateUserCredentials( user.getUserCredentials() );
        }
    }

    @RequestMapping( value = "/{id}/users/{userId}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void removeUserFromRole( @PathVariable( value = "id" ) String pvId, @PathVariable( "userId" ) String pvUserId, HttpServletResponse response ) throws WebMessageException
    {
        UserAuthorityGroup userAuthorityGroup = userService.getUserAuthorityGroup( pvId );

        if ( userAuthorityGroup == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "UserRole does not exist: " + pvId ) );
        }

        User user = userService.getUser( pvUserId );

        if ( user == null || user.getUserCredentials() == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "User does not exist: " + pvId ) );
        }

        if ( !aclService.canUpdate( currentUserService.getCurrentUser(), userAuthorityGroup ) )
        {
            throw new DeleteAccessDeniedException( "You don't have the proper permissions to delete this object." );
        }

        if ( user.getUserCredentials().getUserAuthorityGroups().contains( userAuthorityGroup ) )
        {
            user.getUserCredentials().getUserAuthorityGroups().remove( userAuthorityGroup );
            userService.updateUserCredentials( user.getUserCredentials() );
        }
    }
}
