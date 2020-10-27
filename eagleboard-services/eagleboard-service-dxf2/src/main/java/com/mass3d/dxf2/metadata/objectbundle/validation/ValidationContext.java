package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundleHook;
import com.mass3d.schema.SchemaService;
import com.mass3d.schema.validation.SchemaValidator;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.UserService;

public class ValidationContext
{

    private List<ObjectBundleHook> objectBundleHooks;

    private SchemaValidator schemaValidator;

    private AclService aclService;

    private UserService userService;

    private SchemaService schemaService;

    private List<IdentifiableObject> markedForRemoval = new ArrayList<>();

    public ValidationContext( List<ObjectBundleHook> objectBundleHooks, SchemaValidator schemaValidator,
        AclService aclService, UserService userService, SchemaService schemaService )
    {
        this.objectBundleHooks = objectBundleHooks;
        this.schemaValidator = schemaValidator;
        this.aclService = aclService;
        this.userService = userService;
        this.schemaService = schemaService;
    }

    public List<ObjectBundleHook> getObjectBundleHooks()
    {
        return objectBundleHooks;
    }

    public SchemaValidator getSchemaValidator()
    {
        return schemaValidator;
    }

    public AclService getAclService()
    {
        return aclService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public SchemaService getSchemaService()
    {
        return schemaService;
    }

    public void markForRemoval( IdentifiableObject object )
    {
        this.markedForRemoval.add( object );
    }

    public List<IdentifiableObject> getMarkedForRemoval()
    {
        return markedForRemoval;
    }
}
