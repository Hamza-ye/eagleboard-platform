package com.mass3d.dxf2.metadata.objectbundle.feedback;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.ObjectReport;
import com.mass3d.feedback.TypeReport;

public class ObjectBundleValidationReport
{
    private Map<Class<?>, TypeReport> typeReportMap = new HashMap<>();

    public ObjectBundleValidationReport()
    {
    }

    //-----------------------------------------------------------------------------------
    // Utility Methods
    //-----------------------------------------------------------------------------------

    public List<ErrorReport> getErrorReportsByCode( Class<?> klass, ErrorCode errorCode )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( !typeReportMap.containsKey( klass ) )
        {
            return errorReports;
        }

        List<ObjectReport> objectReports = typeReportMap.get( klass ).getObjectReports();

        for ( ObjectReport objectReport : objectReports )
        {
            List<ErrorReport> byCode = objectReport.getErrorReportsByCode().get( errorCode );

            if ( byCode != null )
            {
                errorReports.addAll( byCode );
            }
        }

        return errorReports;
    }

    public void addTypeReport( TypeReport report )
    {
        if ( report == null ) return;

        if ( !typeReportMap.containsKey( report.getKlass() ) )
        {
            typeReportMap.put( report.getKlass(), report );
            return;
        }

        TypeReport typeReport = typeReportMap.get( report.getKlass() );
        typeReport.merge( typeReport );
    }

    //-----------------------------------------------------------------------------------
    // Getters and Setters
    //-----------------------------------------------------------------------------------

    public boolean isEmpty()
    {
        return typeReportMap.isEmpty();
    }

    public boolean isEmpty( Class<?> klass )
    {
        return typeReportMap.containsKey( klass ) && typeReportMap.get( klass ).getObjectReportMap().isEmpty();
    }

    public Map<Class<?>, TypeReport> getTypeReportMap()
    {
        return typeReportMap;
    }

    public TypeReport getTypeReportMap( Class<?> klass )
    {
        return typeReportMap.get( klass );
    }

    public List<ObjectReport> getObjectReports( Class<?> klass )
    {
        if ( !typeReportMap.containsKey( klass ) )
        {
            return new ArrayList<>();
        }

        return typeReportMap.get( klass ).getObjectReports();
    }

    public List<ErrorReport> getErrorReports( Class<?> klass )
    {
        if ( !typeReportMap.containsKey( klass ) )
        {
            return new ArrayList<>();
        }

        return new ArrayList<>( typeReportMap.get( klass ).getErrorReports() );
    }

    public List<ErrorReport> getErrorReports()
    {
        List<ErrorReport> errorReports = new ArrayList<>();
        typeReportMap.values().forEach( typeReport -> errorReports.addAll( typeReport.getErrorReports() ) );

        return errorReports;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "typeReportMap", typeReportMap )
            .toString();
    }
}
