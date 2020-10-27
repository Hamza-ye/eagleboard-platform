package com.mass3d.dxf2.importsummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.common.ImportOptions;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;

@JacksonXmlRootElement( localName = "importSummaries", namespace = DxfNamespaces.DXF_2_0 )
public class ImportSummaries extends AbstractWebMessageResponse
{
    private ImportStatus status = ImportStatus.SUCCESS;

    private int imported;

    private int updated;

    private int deleted;

    private int ignored;

    private ImportOptions importOptions;

    private List<ImportSummary> importSummaries = new ArrayList<>();

    public ImportSummaries()
    {
    }

    public void addImportSummaries( ImportSummaries importSummaries )
    {
        importSummaries.getImportSummaries().forEach( this::addImportSummary );
    }

    public void addImportSummaries( List<ImportSummary> importSummaries )
    {
        for ( ImportSummary importSummary : importSummaries )
        {
            this.addImportSummary( importSummary );
        }
    }

    public ImportSummaries addImportSummary( ImportSummary importSummary )
    {
        if ( importSummary == null )
        {
            return this;
        }

        if ( importSummary.getImportCount() != null )
        {
            imported += importSummary.getImportCount().getImported();
            updated += importSummary.getImportCount().getUpdated();
            deleted += importSummary.getImportCount().getDeleted();
            ignored += importSummary.getImportCount().getIgnored();
        }

        importSummaries.add( importSummary );

        status = getHighestOrderImportStatus();

        return this;
    }

    public String toCountString()
    {
        return String
            .format( "Imported %d, updated %d, deleted %d, ignored %d", imported, updated, deleted, ignored );
    }

    public boolean isStatus( ImportStatus status )
    {
        ImportStatus st = getStatus();

        return st != null && st.equals( status );
    }

    public boolean hasConflicts()
    {
        for ( ImportSummary importSummary : importSummaries )
        {
            if ( importSummary.getConflicts().size() > 0 )
            {
                return true;
            }
        }
        return false;
    }
    
    public Optional<ImportSummary> getByReference( String reference )
    {
        for ( ImportSummary importSummary : importSummaries )
        {
            if ( importSummary.getReference() != null && importSummary.getReference().equals( reference ) )
            {
                return Optional.of( importSummary );
            }
        }
        return Optional.empty();
    }
    
    /**
     * Returns the {@link ImportStatus} with the highest order from the list
     * of import summaries, where {@link ImportStatus#ERROR} is the highest.
     * If no import summaries are present, {@link ImportStatus#SUCCESS} is
     * returned.
     *
     * @return import status with highest order.
     */
    public ImportStatus getHighestOrderImportStatus()
    {
        return importSummaries.stream()
            .map( ImportSummary::getStatus )
            .max( Comparator.comparingInt( ImportStatus::getOrder ) )
            .orElse( ImportStatus.SUCCESS );
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ImportStatus getStatus()
    {
        return status;
    }

    public void setStatus( ImportStatus status )
    {
        this.status = status;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getImported()
    {
        return imported;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getUpdated()
    {
        return updated;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getDeleted()
    {
        return deleted;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getIgnored()
    {
        return ignored;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getTotal()
    {
        return imported + updated + deleted + ignored;
    }

    public void setTotal( int total )
    {
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ImportOptions getImportOptions()
    {
        return importOptions;
    }

    public void setImportOptions( ImportOptions importOptions )
    {
        this.importOptions = importOptions;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "importSummaryList", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "importSummary", namespace = DxfNamespaces.DXF_2_0 )
    public List<ImportSummary> getImportSummaries()
    {
        return importSummaries;
    }

    public void setImportSummaries( List<ImportSummary> importSummaries )
    {
        this.importSummaries = importSummaries;
    }

    public String toMinimalString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "imported", imported )
            .add( "updated", updated )
            .add( "deleted", deleted )
            .add( "ignored", ignored ).toString();
    }

    @Override
    public String toString()
    {
        return "ImportSummaries{" +
            "importSummaries=" + importSummaries +
            '}';
    }
}
