package com.mass3d.webapi.controller;

import org.apache.commons.beanutils.BeanUtils;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.keyjsonvalue.KeyJsonValue;
import com.mass3d.keyjsonvalue.KeyJsonValueService;
import com.mass3d.render.RenderService;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.WebMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mass3d.webapi.utils.ContextUtils.setNoStore;

@Controller
@RequestMapping( "/dataStore" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class KeyJsonValueController
{
    @Autowired
    private KeyJsonValueService keyJsonValueService;

    @Autowired
    private RenderService renderService;

    @Autowired
    private AclService aclService;

    @Autowired
    private CurrentUserService currentUserService;

//    @Autowired
//    private AppManager appManager;

    @Autowired
    private WebMessageService messageService;

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

        return keyJsonValueService.getNamespaces( currentUserIsAdmin() );
    }

    /**
     * Returns a list of strings representing keys in the given namespace.
     */
    @RequestMapping( value = "/{namespace}", method = RequestMethod.GET, produces = "application/json" )
    public @ResponseBody
    List<String> getKeysInNamespace( @RequestParam( required = false ) Date lastUpdated, @PathVariable String namespace,
        HttpServletResponse response )
        throws IOException, WebMessageException
    {
        validateAccess( namespace );

        getNameS( namespace );

        setNoStore( response );

        return keyJsonValueService.getKeysInNamespace( namespace, lastUpdated, currentUserIsAdmin() );
    }

    /**
     * Deletes all keys with the given namespace.
     */
    @RequestMapping( value = "/{namespace}", method = RequestMethod.DELETE )
    public void deleteNamespace( @PathVariable String namespace, HttpServletResponse response )
        throws WebMessageException
    {
        validateAccess( namespace );

        getNameS( namespace );

        List<KeyJsonValue> keys = keyJsonValueService.getKeyJsonValuesInNamespace( namespace, currentUserIsAdmin() )
            .stream()
            .filter( keyJsonValue -> !aclService.canWrite( currentUserService.getCurrentUser(), keyJsonValue ) )
            .collect( Collectors.toList() );

        if ( keys.size() > 0 )
        {
            throw new WebMessageException( WebMessageUtils
                .forbidden( "You do not have the authority to delete the namespace:'" + namespace + "'" ) );
        }

        keyJsonValueService.deleteNamespace( namespace );

        messageService.sendJson( WebMessageUtils.ok( "Namespace '" + namespace + "' deleted." ), response );
    }

    /**
     * Retrieves the value of the KeyJsonValue represented by the given key from
     * the given namespace.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.GET, produces = "application/json" )
    public @ResponseBody
    String getKeyJsonValue( @PathVariable String namespace, @PathVariable String key, HttpServletResponse response )
        throws IOException, WebMessageException
    {
        KeyJsonValue keyJsonValue = validateAndGetValue( namespace, key );

        if ( !aclService.canRead( currentUserService.getCurrentUser(), keyJsonValue ) )
        {
            throw new WebMessageException(
                WebMessageUtils.forbidden(
                    "You do not have the authority to access the key: '" + key + "' in the namespace:'" + namespace +
                        "'" ) );
        }

        return keyJsonValue.getValue();
    }

    /**
     * Retrieves the KeyJsonValue represented by the given key from the given namespace.
     */
    @RequestMapping( value = "/{namespace}/{key}/metaData", method = RequestMethod.GET, produces = "application/json" )
    public @ResponseBody
    KeyJsonValue getKeyJsonValueMetaData( @PathVariable String namespace, @PathVariable String key,
        HttpServletResponse response )
        throws Exception
    {
        KeyJsonValue keyJsonValue = validateAndGetValue( namespace, key );

        if ( !aclService.canRead( currentUserService.getCurrentUser(), keyJsonValue ) )
        {
            throw new WebMessageException(
                WebMessageUtils.forbidden(
                    "You do not have the authority to access the key: '" + key + "' in the namespace:'" + namespace +
                        "'" ) );
        }

        KeyJsonValue metaDataValue = new KeyJsonValue();
        BeanUtils.copyProperties( metaDataValue, keyJsonValue );
        metaDataValue.setValue( null );

        return metaDataValue;
    }

    /**
     * Creates a new KeyJsonValue Object on the given namespace with the key and value supplied.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json" )
    public void addKeyJsonValue( @PathVariable String namespace, @PathVariable String key, @RequestBody String body,
        @RequestParam( defaultValue = "false" ) boolean encrypt, HttpServletResponse response )
        throws IOException, WebMessageException
    {
        validateAccess( namespace );

        if ( keyJsonValueService.getKeyJsonValue( namespace, key, currentUserIsAdmin() ) != null )
        {
            throw new WebMessageException( WebMessageUtils
                .conflict( "The key '" + key + "' already exists on the namespace '" + namespace + "'." ) );
        }

        if ( !renderService.isValidJson( body ) )
        {
            throw new WebMessageException( WebMessageUtils.badRequest( "The data is not valid JSON." ) );
        }

        KeyJsonValue keyJsonValue = new KeyJsonValue();

        keyJsonValue.setKey( key );
        keyJsonValue.setNamespace( namespace );
        keyJsonValue.setValue( body );
        keyJsonValue.setEncrypted( encrypt );

        keyJsonValueService.addKeyJsonValue( keyJsonValue );

        response.setStatus( HttpServletResponse.SC_CREATED );
        messageService.sendJson( WebMessageUtils.created( "Key '" + key + "' created." ), response );
    }

    /**
     * Update a key in the given namespace.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json" )
    public void updateKeyJsonValue( @PathVariable String namespace, @PathVariable String key, @RequestBody String body,
        HttpServletRequest request, HttpServletResponse response )
        throws WebMessageException, IOException
    {
        KeyJsonValue keyJsonValue = validateAndGetValue( namespace, key );

        if ( !aclService.canWrite( currentUserService.getCurrentUser(), keyJsonValue ) )
        {
            throw new WebMessageException(
                WebMessageUtils.forbidden(
                    "You do not have the authority to update the key: '" + key + "' in the namespace: '" + namespace +
                        "'" ) );
        }

        if ( !renderService.isValidJson( body ) )
        {
            throw new WebMessageException( WebMessageUtils.badRequest( "The data is not valid JSON." ) );
        }

        keyJsonValue.setValue( body );

        keyJsonValueService.updateKeyJsonValue( keyJsonValue );

        response.setStatus( HttpServletResponse.SC_OK );
        messageService.sendJson( WebMessageUtils.ok( "Key '" + key + "' updated." ), response );
    }

    /**
     * Delete a key from the given namespace.
     */
    @RequestMapping( value = "/{namespace}/{key}", method = RequestMethod.DELETE, produces = "application/json" )
    public void deleteKeyJsonValue( @PathVariable String namespace, @PathVariable String key,
        HttpServletResponse response )
        throws WebMessageException
    {
        KeyJsonValue keyJsonValue = validateAndGetValue( namespace, key );

        if ( !aclService.canWrite( currentUserService.getCurrentUser(), keyJsonValue ) )
        {
            throw new WebMessageException(
                WebMessageUtils.forbidden(
                    "You do not have the authority to delete the key: '" + key + "' in the namespace: '" + namespace +
                        "'" ) );
        }

        keyJsonValueService.deleteKeyJsonValue( keyJsonValue );

        messageService.sendJson( WebMessageUtils.ok( "Key '" + key + "' deleted from namespace '" + namespace + "'." ),
            response );
    }

    /**
     * Validates access and looks up the value, thrown an exception if there is no such key/value pair in the given namespace
     *
     * @param namespace Namespace to use when looking up the value
     * @param key       Key of the key/value pair
     * @return
     * @throws WebMessageException
     */
    protected KeyJsonValue validateAndGetValue( @PathVariable String namespace, @PathVariable String key )
        throws WebMessageException
    {
        validateAccess( namespace );

        KeyJsonValue keyJsonValue = keyJsonValueService.getKeyJsonValue( namespace, key, currentUserIsAdmin() );

        if ( keyJsonValue == null )
        {
            throw new WebMessageException( WebMessageUtils
                .notFound( "The key '" + key + "' was not found in the namespace '" + namespace + "'." ) );
        }
        return keyJsonValue;
    }

    /**
     * Validates access and throws an exception if the current user does not have proper access
     *
     * @param namespace The namespace to validate
     * @throws WebMessageException
     */
    protected void validateAccess( String namespace )
        throws WebMessageException
    {
        if ( !hasAccess( namespace ) )
        {
            throw new WebMessageException( WebMessageUtils.forbidden( "The namespace '" + namespace +
                "' is protected, and you don't have the right authority to access it." ) );
        }
    }

    private boolean hasAccess( String namespace )
    {
//        App app = appManager.getAppByNamespace( namespace );
        return true; //app == null || appManager.isAccessible( app );
    }

    protected void getNameS( @PathVariable String namespace )
        throws WebMessageException
    {
        if ( !keyJsonValueService.getNamespaces( currentUserIsAdmin() ).contains( namespace ) )
        {
            throw new WebMessageException(
                WebMessageUtils.notFound( "The namespace '" + namespace + "' was not found." ) );
        }
    }

    private boolean currentUserIsAdmin()
    {
        User currentUser = currentUserService.getCurrentUser();
        return currentUser != null && currentUser.getUserCredentials().isSuper();
    }
}
