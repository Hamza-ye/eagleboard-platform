package com.mass3d.dxf2.metadata.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.Stats;
import com.mass3d.feedback.Status;
import com.mass3d.feedback.TypeReport;

@JacksonXmlRootElement( localName = "importReport", namespace = DxfNamespaces.DXF_2_0 )
public class ImportReport
{
    private MetadataImportParams importParams;

    private Status status = Status.OK;

    private Map<Class<?>, TypeReport> typeReportMap = new HashMap<>();

    public ImportReport()
    {
    }

    //-----------------------------------------------------------------------------------
    // Utility Methods
    //-----------------------------------------------------------------------------------

    public TypeReport addTypeReport( TypeReport typeReport )
    {
        if ( !typeReportMap.containsKey( typeReport.getKlass() ) ) typeReportMap.put( typeReport.getKlass(), new TypeReport( typeReport.getKlass() ) );
        typeReportMap.get( typeReport.getKlass() ).merge( typeReport );

        return typeReport;
    }

    public void addTypeReports( List<TypeReport> typeReports )
    {
        typeReports.forEach( this::addTypeReport );
    }

    public void addTypeReports( Map<Class<?>, TypeReport> typeReportMap )
    {
        typeReportMap.values().forEach( this::addTypeReport );
    }

    public List<ErrorReport> getErrorReports()
    {
        List<ErrorReport> errorReports = new ArrayList<>();
        typeReportMap.values().forEach( typeReport -> errorReports.addAll( typeReport.getErrorReports() ) );

        return errorReports;
    }

    //-----------------------------------------------------------------------------------
    // Getters and Setters
    //-----------------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public MetadataImportParams getImportParams()
    {
        return importParams;
    }

    public void setImportParams( MetadataImportParams importParams )
    {
        this.importParams = importParams;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Status getStatus()
    {
        return status;
    }

    public void setStatus( Status status )
    {
        this.status = status;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Stats getStats()
    {
        Stats stats = new Stats();
        typeReportMap.values().forEach( typeReport -> stats.merge( typeReport.getStats() ) );

        return stats;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "typeReports", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "typeReport", namespace = DxfNamespaces.DXF_2_0 )
    public List<TypeReport> getTypeReports()
    {
        return new ArrayList<>( typeReportMap.values() );
    }
    
    @JsonProperty
    @JacksonXmlElementWrapper( localName = "typeReports", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "typeReport", namespace = DxfNamespaces.DXF_2_0 )
    public void setTypeReports( List<TypeReport> typeReports )
    {
        if ( typeReports != null )
        {
            typeReports.forEach( tr -> typeReportMap.put( tr.getKlass(), tr ) );
        }
    }

    public Map<Class<?>, TypeReport> getTypeReportMap()
    {
        return typeReportMap;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "stats", getStats() )
            .add( "typeReports", getTypeReports() )
            .toString();
    }
}
