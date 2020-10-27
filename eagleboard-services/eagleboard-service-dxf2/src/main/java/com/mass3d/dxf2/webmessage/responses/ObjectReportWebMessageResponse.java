package com.mass3d.dxf2.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.ObjectReport;
import org.springframework.util.Assert;

public class ObjectReportWebMessageResponse
    extends AbstractWebMessageResponse
{
    private final ObjectReport objectReport;

    public ObjectReportWebMessageResponse( ObjectReport objectReport )
    {
        Assert.notNull( objectReport, "ObjectReport is required to be non-null." );
        this.objectReport = objectReport;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Class<?> getKlass()
    {
        return objectReport.getKlass();
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getUid()
    {
        return objectReport.getUid();
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "errorReports", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "errorReport", namespace = DxfNamespaces.DXF_2_0 )
    public List<ErrorReport> getErrorReports()
    {
        return objectReport.getErrorReports();
    }
}
