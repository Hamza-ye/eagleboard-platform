package com.mass3d.relationship;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.Stream;

public enum RelationshipEntity
{
    TRACKED_ENTITY_INSTANCE( "tracked_entity" ),
    PROGRAM_INSTANCE( "enrollment" ),
    PROGRAM_STAGE_INSTANCE( "event" );

    private String name;

    RelationshipEntity( String name )
    {
        this.name = name;
    }

    private static final Map<String, RelationshipEntity> LOOKUP = Stream.of( values() )
        .collect( toMap( RelationshipEntity::getName, x -> x ) );

    public String getName()
    {
        return name;
    }

    public static RelationshipEntity get( String name )
    {
        return LOOKUP.get( name );
    }
}
