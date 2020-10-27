package com.mass3d.system.filter;

import com.mass3d.commons.filter.Filter;
import com.mass3d.dataelement.DataElement;

public class AggregatableDataElementFilter
    implements Filter<DataElement>
{
    public static final AggregatableDataElementFilter INSTANCE = new AggregatableDataElementFilter();

    @Override
    public boolean retain( DataElement object )
    {
        return object != null && object.getValueType().isAggregateable() && object.getAggregationType().isAggregateable();
    }
}
