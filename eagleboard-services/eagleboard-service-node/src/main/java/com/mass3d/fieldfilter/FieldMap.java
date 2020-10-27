package com.mass3d.fieldfilter;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;
import java.util.Map;
import com.mass3d.node.LinearNodePipeline;

public class FieldMap extends ForwardingMap<String, FieldMap>
{
    private final Map<String, FieldMap> delegate = Maps.newHashMap();

    private final LinearNodePipeline pipeline = new LinearNodePipeline();

    @Override
    protected Map<String, FieldMap> delegate()
    {
        return delegate;
    }

    public LinearNodePipeline getPipeline()
    {
        return pipeline;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "map", standardToString() )
            .toString();
    }
}
