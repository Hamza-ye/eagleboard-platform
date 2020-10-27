package com.mass3d.dxf2.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;
import com.mass3d.feedback.ObjectReport;
import com.mass3d.feedback.Stats;
import com.mass3d.feedback.Status;
import com.mass3d.feedback.TypeReport;
import org.springframework.util.Assert;

public class TypeReportWebMessageResponse
    extends AbstractWebMessageResponse
{
    private final TypeReport typeReport;

    public TypeReportWebMessageResponse( TypeReport typeReport )
    {
        Assert.notNull( typeReport, "ImportReport is require to be non-null." );
        this.typeReport = typeReport;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Status getStatus()
    {
        return typeReport.getErrorReports().isEmpty() ? Status.OK : Status.ERROR;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Stats getStats()
    {
        return typeReport.getStats();
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "objectReports", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "objectReport", namespace = DxfNamespaces.DXF_2_0 )
    public List<ObjectReport> getObjectReports()
    {
        return typeReport.getObjectReports();
    }
}
