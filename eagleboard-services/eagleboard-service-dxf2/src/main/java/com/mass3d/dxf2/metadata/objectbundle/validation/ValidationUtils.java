package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectUtils;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.ObjectReport;
import com.mass3d.feedback.TypeReport;

public class ValidationUtils
{

    public static List<IdentifiableObject> joinObjects( List<IdentifiableObject> persistedObjects,
        List<IdentifiableObject> nonPersistedObjects )
    {
        return Stream
            .concat( persistedObjects.stream(), nonPersistedObjects.stream() ).collect( Collectors.toList() );
    }

    public static void addObjectReports( List<ErrorReport> reports, TypeReport typeReport, IdentifiableObject object,
        ObjectBundle bundle )
    {
        ObjectReport objectReport = new ObjectReport( object, bundle );
        objectReport.setDisplayName( IdentifiableObjectUtils.getDisplayName( object ) );
        objectReport.addErrorReports( reports );

        typeReport.addObjectReport( objectReport );
        typeReport.getStats().incIgnored();
    }

    public static void addObjectReport( ErrorReport report, TypeReport typeReport, IdentifiableObject object,
        ObjectBundle bundle )
    {
        ObjectReport objectReport = new ObjectReport( object, bundle );
        objectReport.setDisplayName( IdentifiableObjectUtils.getDisplayName( object ) );
        objectReport.addErrorReport( report );

        typeReport.addObjectReport( objectReport );
        typeReport.getStats().incIgnored();
    }
}
