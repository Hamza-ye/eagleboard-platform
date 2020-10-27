package com.mass3d.preheat;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import org.springframework.util.StringUtils;

public enum PreheatIdentifier
{
    /**
     * Preheat using UID identifiers.
     */
    UID,

    /**
     * Preheat using CODE identifiers.
     */
    CODE,

    /**
     * Find first non-null identifier in order: UID, CODE
     */
    AUTO;

    public <T extends IdentifiableObject> String getIdentifier( T object )
    {
        switch ( this )
        {
            case UID:
                return object.getUid();
            case CODE:
                return object.getCode();
        }

        throw new RuntimeException( "Unhandled identifier type." );
    }

    public <T extends IdentifiableObject> List<String> getIdentifiers( T object )
    {
        switch ( this )
        {
            case UID:
            {
                return Lists.newArrayList( object.getUid() );
            }
            case CODE:
            {
                return Lists.newArrayList( object.getCode() );
            }
            case AUTO:
            {
                return Lists.newArrayList( object.getUid(), object.getCode() );
            }
        }

        return new ArrayList<>();
    }

    public <T extends IdentifiableObject> String getIdentifiersWithName( T object )
    {
        List<String> identifiers = getIdentifiers( object );
        String name = StringUtils.isEmpty( object.getDisplayName() ) ? null : object.getDisplayName();

        if ( name == null )
        {
            return identifiers.toString() + " (" + object.getClass().getSimpleName() + ")";
        }

        return name + " " + identifiers.toString() + " (" + object.getClass().getSimpleName() + ")";
    }
}
