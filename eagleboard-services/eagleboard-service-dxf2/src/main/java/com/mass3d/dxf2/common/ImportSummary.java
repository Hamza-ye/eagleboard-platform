package com.mass3d.dxf2.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.importsummary.ImportCount;

@JacksonXmlRootElement( localName = "importSummary", namespace = DxfNamespaces.DXF_2_0 )
public class ImportSummary
{
    private ImportCount importCount = new ImportCount();

    private List<ImportTypeSummary> importTypeSummaries = new ArrayList<>();

    public ImportSummary()
    {

    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ImportCount getImportCount()
    {
        return importCount;
    }

    public void setImportCount( ImportCount importCount )
    {
        this.importCount = importCount;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "typeSummaries", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "typeSummary", namespace = DxfNamespaces.DXF_2_0 )
    public List<ImportTypeSummary> getImportTypeSummaries()
    {
        return importTypeSummaries;
    }

    public void setImportTypeSummaries( List<ImportTypeSummary> importTypeSummaries )
    {
        this.importTypeSummaries = importTypeSummaries;
    }

    //-------------------------------------------------------------------------
    // Helpers
    //-------------------------------------------------------------------------

    public void incrementImportCount( ImportCount importCount )
    {
        this.importCount.incrementImported( importCount.getImported() );
        this.importCount.incrementUpdated( importCount.getUpdated() );
        this.importCount.incrementIgnored( importCount.getIgnored() );
        this.importCount.incrementDeleted( importCount.getDeleted() );
    }

    @Override
    public String toString()
    {
        return "ImportSummary{" +
            "importCount=" + importCount +
            ", importTypeSummaries=" + importTypeSummaries +
            '}';
    }
}
