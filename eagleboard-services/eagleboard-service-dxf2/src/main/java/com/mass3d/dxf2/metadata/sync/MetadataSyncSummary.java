package com.mass3d.dxf2.metadata.sync;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.common.ImportSummary;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.webmessage.WebMessageResponse;
import com.mass3d.metadata.version.MetadataVersion;

/**
 * Defines the structure of metadata sync summary
 *
 */
@JacksonXmlRootElement( localName = "metadataSyncSummary", namespace = DxfNamespaces.DXF_2_0 )
public class MetadataSyncSummary implements WebMessageResponse
{
    private ImportReport importReport;

    private ImportSummary importSummary;

    private MetadataVersion metadataVersion;

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public MetadataVersion getMetadataVersion()
    {
        return metadataVersion;
    }

    public void setMetadataVersion( MetadataVersion metadataVersion )
    {
        this.metadataVersion = metadataVersion;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ImportSummary getImportSummary()
    {
        return importSummary;
    }

    public void setImportSummary( ImportSummary importSummary )
    {
        this.importSummary = importSummary;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ImportReport getImportReport()
    {
        return importReport;
    }

    public void setImportReport( ImportReport importReport )
    {
        this.importReport = importReport;
    }

    @Override
    public String toString()
    {
        return "MetadataSyncSummary{" +
            "importReport=" + importReport +
            ", importSummary=" + importSummary +
            ", metadataVersion=" + metadataVersion +
            '}';
    }
}
