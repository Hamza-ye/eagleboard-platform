package com.mass3d.webportal.module;

import java.util.Arrays;
import java.util.List;

import com.mass3d.commons.filter.Filter;

public class StartableModuleFilter
    implements Filter<Module>
{
    private List<String> NOT_VIABLE = Arrays.asList( "dhis-web-mapping", "dhis-web-visualizer" );
    
    @Override
    public boolean retain( Module module )
    {
        return module != null && !NOT_VIABLE.contains( module.getName() );
    }
}
