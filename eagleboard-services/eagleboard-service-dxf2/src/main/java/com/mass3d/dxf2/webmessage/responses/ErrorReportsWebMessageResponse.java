package com.mass3d.dxf2.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;
import com.mass3d.feedback.ErrorReport;

public class ErrorReportsWebMessageResponse
    extends AbstractWebMessageResponse
{
    private List<ErrorReport> errorReports = new ArrayList<>();

    public ErrorReportsWebMessageResponse( List<ErrorReport> errorReports )
    {
        this.errorReports = errorReports;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "errorReports", namespace = DxfNamespaces.DXF_2_0, useWrapping = false )
    @JacksonXmlProperty( localName = "errorReport", namespace = DxfNamespaces.DXF_2_0 )
    public List<ErrorReport> getErrorReports()
    {
        return errorReports;
    }

    public void setErrorReports( List<ErrorReport> errorReports )
    {
        this.errorReports = errorReports;
    }
}
