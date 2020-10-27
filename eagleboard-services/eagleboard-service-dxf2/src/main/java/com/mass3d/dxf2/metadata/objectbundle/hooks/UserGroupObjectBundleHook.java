package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.user.UserGroup;
import org.springframework.stereotype.Component;

@Component
public class UserGroupObjectBundleHook extends AbstractObjectBundleHook
{
    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        if ( !UserGroup.class.isInstance( persistedObject ) ) return;
        handleCreatedUserProperty( object, persistedObject, bundle );
    }

    /**
     * As User property of UserGroup is marked with @JsonIgnore ( see {@link UserGroup} ), the new object will always has User = NULL.
     * So we need to get this from persisted UserGroup, otherwise it will always be set to current User when updating.
     * @param object
     * @param persistedObject
     * @param <T>
     */
    private <T extends IdentifiableObject> void handleCreatedUserProperty( T object, T persistedObject, ObjectBundle bundle )
    {
        UserGroup userGroup = (UserGroup) object;
        UserGroup persistedUserGroup = (UserGroup) persistedObject;

        userGroup.setUser( persistedUserGroup.getUser() );
        bundle.getPreheat().put( bundle.getPreheatIdentifier(), persistedUserGroup.getUser() );
    }
}