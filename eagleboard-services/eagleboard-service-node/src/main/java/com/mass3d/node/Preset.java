package com.mass3d.node;

import com.google.common.collect.Lists;
import java.util.List;

public enum Preset
{
    ID( "id", Lists.newArrayList( "id" ) ),
    CODE( "code", Lists.newArrayList( "code" ) ),
    ID_NAME( "idName", Lists.newArrayList( "id", "displayName" ) ),
    ALL( "all", Lists.newArrayList( "*" ) ),
    IDENTIFIABLE( "identifiable", Lists
        .newArrayList( "id", "name", "code", "created", "lastUpdated", "href" ) ),
    NAMEABLE( "nameable", Lists
        .newArrayList( "id", "name", "shortName", "description", "code", "created", "lastUpdated", "href" ) );

    private String name;

    private List<String> fields;

    Preset( String name, List<String> fields )
    {
        this.name = name;
        this.fields = fields;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getFields()
    {
        return fields;
    }

    public static Preset defaultPreset()
    {
        return Preset.ID_NAME;
    }

    public static Preset defaultAssociationPreset()
    {
        return Preset.ID;
    }
}
