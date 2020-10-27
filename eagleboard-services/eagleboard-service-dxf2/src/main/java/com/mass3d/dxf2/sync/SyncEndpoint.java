package com.mass3d.dxf2.sync;

import com.mass3d.dxf2.importsummary.ImportSummaries;
import com.mass3d.dxf2.importsummary.ImportSummary;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;

public enum SyncEndpoint
{
    TRACKED_ENTITY_INSTANCES( "/api/trackedEntityInstances", ImportSummaries.class ),
    ENROLLMENTS( "/api/enrollments", ImportSummaries.class ),
    EVENTS( "/api/events", ImportSummaries.class ),
    COMPLETE_DATA_SET_REGISTRATIONS( "/api/completeDataSetRegistrations", ImportSummary.class ),
    DATA_VALUE_SETS( "/api/dataValueSets", ImportSummary.class );

    private String path;
    private Class<? extends AbstractWebMessageResponse> klass;

    SyncEndpoint( String path, Class<? extends AbstractWebMessageResponse> klass )
    {
        this.path = path;
        this.klass = klass;
    }

    public String getPath()
    {
        return path;
    }

    public Class<? extends AbstractWebMessageResponse> getKlass()
    {
        return klass;
    }
}
