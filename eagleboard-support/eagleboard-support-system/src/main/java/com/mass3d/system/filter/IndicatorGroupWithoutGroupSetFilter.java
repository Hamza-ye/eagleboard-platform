package com.mass3d.system.filter;

import com.mass3d.commons.filter.Filter;
import com.mass3d.indicator.IndicatorGroup;

public class IndicatorGroupWithoutGroupSetFilter
    implements Filter<IndicatorGroup>
{
    @Override
    public boolean retain( IndicatorGroup object )
    {
        return object == null || object.getGroupSet() == null;
    }
}
