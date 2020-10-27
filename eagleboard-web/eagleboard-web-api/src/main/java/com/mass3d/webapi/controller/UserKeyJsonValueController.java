package com.mass3d.webapi.controller;

import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.render.RenderService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.userkeyjsonvalue.UserKeyJsonValue;
import com.mass3d.userkeyjsonvalue.UserKeyJsonValueService;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.service.WebMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static com.mass3d.webapi.utils.ContextUtils.setNoStore;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping( "/userDataStore" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class UserKeyJsonValueController
{
    @Autowired
    private UserKeyJsonValueService userKeyJsonValueService;

    @Autowired
    private RenderService renderService;

    @Autowired
    private WebMessageService messageService;

    @Autowired
    private CurrentUserService currentUserService;

    /**
     * Returns a JSON array of strings representing the different namespaces used.
     * If no namespaces exist, an empty array is returned.
     */
    @RequestMapping( value = "", method = RequestMethod.GET, produces = "application/json" )
    public @ResponseBody
    List<String> getNamespaces( HttpServletResponse response )
        throws IOException
    {
        setNoStore( response );

        return userKeyJsonValueService.getNamespacesByUser( currentUserService.getCurrentUser() );
    }

    /**
     * Returns a JSON array of strings representing the different keys used in a given namespace.
     * If no namespaces exist, an empty array is returned.
     */
    @RequestMapping( value = "/{namespace}", method = RequestMethod.GET, produces = "application/json" )
    public @ResponseBody
    List<String> getKeys( @PathVariable String namespace, HttpServletResponse response )
        throws IOException, WebMessageException
    {
        if ( !userKeyJsonValueService.getNamespacesByUser( currentUserService.getCurrentUser() ).contains( namespace ) )
        {
            throw new WebMessageException(
                WebMessageUtils.notFound( "The namespace '" + namespace + "' was not found." ) );
        }

        setNoStore( response );

        return userKeyJsonValueService.getKeysByUserAndNamespace( currentUserService.getCurrentUser(), namespace );
    }

    /**
     * Deletes all keys with the given user and namespace.
     */
    @RequestMapping( value = "/{namespace}", method = RequestMethod.DELETE )
    public void deleteKeys(
        @PathVariable String namespace,
        HttpServletResponse response )
        throws WebMessageException
    {
        userKeyJsonValueService.deleteNamespaceFromUser( currentUserService.getCurrentUser(), namespace );

        messageService
            .sendJson( WebMessageUtils.ok( "All keys from namespace '" + namespace + "' deleted." ), response );
    }

    /**
     * Retrieves the value of the KeyJsonValue represented by the given key and namespace from
     * the current user.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.GET, produces = "application/json" )
    public @ResponseBody
    String getUserKeyJsonValue(
        @PathVariable String namespace,
        @PathVariable String key, HttpServletResponse response )
        throws IOException, WebMessageException
    {
        UserKeyJsonValue userKeyJsonValue = userKeyJsonValueService.getUserKeyJsonValue(
            currentUserService.getCurrentUser(), namespace, key );

        if ( userKeyJsonValue == null )
        {
            throw new WebMessageException( WebMessageUtils
                .notFound( "The key '" + key + "' was not found in the namespace '" + namespace + "'." ) );
        }

        setNoStore( response );

        return userKeyJsonValue.getValue();
    }

    /**
     * Creates a new KeyJsonValue Object on the current user with the key, namespace and value supplied.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json" )
    public void addUserKeyJsonValue(
        @PathVariable String namespace,
        @PathVariable String key,
        @RequestBody String body,
        @RequestParam( defaultValue = "false" ) boolean encrypt,
        HttpServletResponse response )
        throws IOException, WebMessageException
    {
        if ( userKeyJsonValueService.getUserKeyJsonValue( currentUserService.getCurrentUser(), namespace, key ) !=
            null )
        {
            throw new WebMessageException( WebMessageUtils
                .conflict( "The key '" + key + "' already exists in the namespace '" + namespace + "'." ) );
        }

        if ( !renderService.isValidJson( body ) )
        {
            throw new WebMessageException( WebMessageUtils.badRequest( "The data is not valid JSON." ) );
        }

        UserKeyJsonValue userKeyJsonValue = new UserKeyJsonValue();

        userKeyJsonValue.setKey( key );
        userKeyJsonValue.setUser( currentUserService.getCurrentUser() );
        userKeyJsonValue.setNamespace( namespace );
        userKeyJsonValue.setValue( body );
        userKeyJsonValue.setEncrypted( encrypt );

        userKeyJsonValueService.addUserKeyJsonValue( userKeyJsonValue );

        response.setStatus( HttpServletResponse.SC_CREATED );
        messageService
            .sendJson( WebMessageUtils.created( "Key '" + key + "' in namespace '" + namespace + "' created." ),
                response );
    }

    /**
     * Update a key.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json" )
    public void updateUserKeyJsonValue(
        @PathVariable String namespace,
        @PathVariable String key,
        @RequestBody String body,
        HttpServletResponse response )
        throws WebMessageException, IOException
    {
        UserKeyJsonValue userKeyJsonValue = userKeyJsonValueService.getUserKeyJsonValue(
            currentUserService.getCurrentUser(), namespace, key );

        if ( userKeyJsonValue == null )
        {
            throw new WebMessageException( WebMessageUtils
                .notFound( "The key '" + key + "' was not found in the namespace '" + namespace + "'." ) );
        }

        if ( !renderService.isValidJson( body ) )
        {
            throw new WebMessageException( WebMessageUtils.badRequest( "The data is not valid JSON." ) );
        }

        userKeyJsonValue.setValue( body );

        userKeyJsonValueService.updateUserKeyJsonValue( userKeyJsonValue );

        response.setStatus( HttpServletResponse.SC_OK );
        messageService
            .sendJson( WebMessageUtils.created( "Key '" + key + "' in namespace '" + namespace + "' updated." ),
                response );
    }

    /**
     * Delete a key.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.DELETE, produces = "application/json" )
    public void deleteUserKeyJsonValue(
        @PathVariable String namespace,
        @PathVariable String key,
        HttpServletResponse response )
        throws WebMessageException
    {
        UserKeyJsonValue userKeyJsonValue = userKeyJsonValueService.getUserKeyJsonValue(
            currentUserService.getCurrentUser(), namespace, key );

        if ( userKeyJsonValue == null )
        {
            throw new WebMessageException( WebMessageUtils
                .notFound( "The key '" + key + "' was not found in the namespace '" + namespace + "'." ) );
        }

        userKeyJsonValueService.deleteUserKeyJsonValue( userKeyJsonValue );

        messageService
            .sendJson( WebMessageUtils.ok( "Key '" + key + "' deleted from the namespace '" + namespace + "'." ),
                response );
    }
}
