package com.mass3d.dxf2.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.importsummary.ImportConflict;
import com.mass3d.dxf2.importsummary.ImportCount;
import com.mass3d.dxf2.importsummary.ImportSummary;

@JacksonXmlRootElement( localName = "typeSummary", namespace = DxfNamespaces.DXF_2_0 )
public class ImportTypeSummary extends ImportSummary
{
    private String type;

    private ImportCount importCount = new ImportCount();

    private List<ImportConflict> importConflicts = new ArrayList<>();

    /**
     * This will always have the UID of the latest imported object. This is used for cases where you are only importing a single
     * object, and want to return the Location header etc to the user. We might extend this in the future, so that we can get all
     * UIDs of imported objects.
     */
    private String lastImported;

    public ImportTypeSummary( String type )
    {
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ImportCount getImportCount()
    {
        return importCount;
    }

    @Override
    public ImportSummary setImportCount( ImportCount importCount )
    {
        this.importCount = importCount;
        return null;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "conflicts", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "conflict", namespace = DxfNamespaces.DXF_2_0 )
    public List<ImportConflict> getImportConflicts()
    {
        return importConflicts;
    }

    public void setImportConflicts( List<ImportConflict> importConflicts )
    {
        this.importConflicts = importConflicts;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getLastImported()
    {
        return lastImported;
    }

    public void setLastImported( String lastImported )
    {
        this.lastImported = lastImported;
    }

    //-------------------------------------------------------------------------
    // Helpers
    //-------------------------------------------------------------------------

    @Override
    public ImportTypeSummary incrementImported()
    {
        importCount.incrementImported();
        return this;
    }

    @Override
    public ImportTypeSummary incrementUpdated()
    {
        importCount.incrementUpdated();
        return this;
    }

    @Override
    public ImportTypeSummary incrementIgnored()
    {
        importCount.incrementIgnored();
        return this;
    }

    @Override
    public ImportTypeSummary incrementDeleted()
    {
        importCount.incrementDeleted();
        return this;
    }

    @Override
    public String toString()
    {
        return "ImportTypeSummary{" +
            "type='" + type + '\'' +
            ", importCount=" + importCount +
            ", importConflicts=" + importConflicts +
            '}';
    }
}
