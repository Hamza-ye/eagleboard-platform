package com.mass3d.dxf2.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;
import com.mass3d.feedback.Stats;
import com.mass3d.feedback.Status;
import com.mass3d.feedback.TypeReport;
import org.springframework.util.Assert;

public class ImportReportWebMessageResponse
    extends AbstractWebMessageResponse
{
    private final ImportReport importReport;

    public ImportReportWebMessageResponse( ImportReport importReport )
    {
        Assert.notNull( importReport, "ImportReport is require to be non-null." );
        this.importReport = importReport;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Status getStatus()
    {
        return importReport.getStatus();
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Stats getStats()
    {
        return importReport.getStats();
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "typeReports", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "typeReport", namespace = DxfNamespaces.DXF_2_0 )
    public List<TypeReport> getTypeReports()
    {
        return importReport.getTypeReports();
    }
}
