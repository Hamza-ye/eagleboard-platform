package com.mass3d.preheat;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.user.User;

public class PreheatParams
{
    /**
     * User to use for database queries.
     */
    private User user;

    /**
     * Mode to use for preheating.
     */
    private PreheatMode preheatMode = PreheatMode.REFERENCE;

    /**
     * Identifiers to match on.
     */
    private PreheatIdentifier preheatIdentifier = PreheatIdentifier.UID;

    /**
     * If preheat mode is ALL, only do full preheating on these classes.
     */
    private Set<Class<? extends IdentifiableObject>> classes = new HashSet<>();

    /**
     * Objects to scan (if preheat mode is REFERENCE).
     */
    private Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> objects = new HashMap<>();

    public PreheatParams()
    {
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public PreheatMode getPreheatMode()
    {
        return preheatMode;
    }

    public PreheatParams setPreheatMode( PreheatMode preheatMode )
    {
        this.preheatMode = preheatMode;
        return this;
    }

    public PreheatIdentifier getPreheatIdentifier()
    {
        return preheatIdentifier;
    }

    public PreheatParams setPreheatIdentifier( PreheatIdentifier preheatIdentifier )
    {
        this.preheatIdentifier = preheatIdentifier;
        return this;
    }

    public Set<Class<? extends IdentifiableObject>> getClasses()
    {
        return classes;
    }

    public PreheatParams setClasses( Set<Class<? extends IdentifiableObject>> classes )
    {
        this.classes = classes;
        return this;
    }

    public Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> getObjects()
    {
        return objects;
    }

    public void setObjects( Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> objects )
    {
        this.objects = objects;
    }

    public PreheatParams addObject( IdentifiableObject object )
    {
        if ( object == null )
        {
            return this;
        }

        if ( !objects.containsKey( object.getClass() ) )
        {
            objects.put( object.getClass(), new ArrayList<>() );
        }

        objects.get( object.getClass() ).add( object );

        return this;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "user", user )
            .add( "preheatMode", preheatMode )
            .add( "preheatIdentifier", preheatIdentifier )
            .add( "classes", classes )
            .add( "objects", objects )
            .toString();
    }
}
