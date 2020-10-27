package com.mass3d.node.config;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Config
{
    /**
     * Inclusion strategy to use. There are a few already defined inclusions in the
     * Inclusions enum.
     *
     * @see com.mass3d.node.config.InclusionStrategy.Include
     */
    private InclusionStrategy inclusionStrategy = InclusionStrategy.Include.NON_NULL;

    /**
     * Property map that can hold any key=value pair, can be used to set custom
     * properties that only certain serializers know about.
     */
    private final Map<String, Object> properties = Maps.newHashMap();

    public Config()
    {
    }

    public InclusionStrategy getInclusionStrategy()
    {
        return inclusionStrategy;
    }

    public void setInclusionStrategy( InclusionStrategy inclusionStrategy )
    {
        this.inclusionStrategy = inclusionStrategy;
    }

    public Map<String, Object> getProperties()
    {
        return properties;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        Config config = (Config) o;

        final boolean propertiesAreEqual = Arrays
            .deepEquals( properties.entrySet().toArray(), ((Config) o).getProperties().entrySet().toArray() );

        return Objects.equals( inclusionStrategy, config.inclusionStrategy )
            && propertiesAreEqual;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( inclusionStrategy, properties );
    }
}
