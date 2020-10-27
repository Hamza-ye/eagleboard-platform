package com.mass3d.query.planner;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import java.util.Arrays;
import com.mass3d.schema.Property;

public class QueryPath
{
    private final Property property;

    private final boolean persisted;

    private String[] alias = new String[]{};

    private static final Joiner PATH_JOINER = Joiner.on( "." );

    public QueryPath( Property property, boolean persisted )
    {
        this.property = property;
        this.persisted = persisted;
    }

    public QueryPath( Property property, boolean persisted, String[] alias )
    {
        this( property, persisted );
        this.alias = alias;
    }

    public Property getProperty()
    {
        return property;
    }

    public String getPath()
    {
        String fieldName = property.getFieldName();

        if ( fieldName == null )
        {
            fieldName = property.getName();
        }

        return haveAlias() ? PATH_JOINER.join( alias ) + "." + fieldName : fieldName;
    }

    public boolean isPersisted()
    {
        return persisted;
    }

    public String[] getAlias()
    {
        return alias;
    }

    public boolean haveAlias()
    {
        return alias != null && alias.length > 0;
    }

    public boolean haveAlias( int n )
    {
        return alias != null && alias.length > n;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "name", property.getName() )
            .add( "path", getPath() )
            .add( "persisted", persisted )
            .add( "alias", Arrays.toString( alias ) )
            .toString();
    }
}
