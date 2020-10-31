package com.mass3d.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.adapter.DeviceRenderTypeMapSerializer;
import com.mass3d.dataelement.DataElement;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.type.ValueTypeRenderingObject;

@JacksonXmlRootElement( localName = "programStageDataElement", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramStageDataElement
    extends BaseIdentifiableObject implements EmbeddedObject
{
    private ProgramStage programStage;

    private DataElement dataElement;

    /**
     * True if this dataElement is mandatory in the dataEntryForm for this
     * programStage
     */
    private boolean compulsory = false;

    private Boolean allowProvidedElsewhere = false;

    private Integer sortOrder;

    private Boolean displayInReports = false;

    private Boolean allowFutureDate = false;

    // Remove this in the future, will be replaced by renderType
    private Boolean renderOptionsAsRadio = false;

    /**
     * The renderType defines how the ProgramStageSection should be rendered on the client
     */
    private DeviceRenderTypeMap<ValueTypeRenderingObject> renderType;

    private Boolean skipSynchronization = false;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramStageDataElement()
    {
    }

    public ProgramStageDataElement( ProgramStage programStage, DataElement dataElement )
    {
        this.programStage = programStage;
        this.dataElement = dataElement;
    }

    public ProgramStageDataElement( ProgramStage programStage, DataElement dataElement, boolean compulsory )
    {
        this.programStage = programStage;
        this.dataElement = dataElement;
        this.compulsory = compulsory;
    }

    public ProgramStageDataElement( ProgramStage programStage, DataElement dataElement, boolean compulsory,
        Integer sortOrder )
    {
        this.programStage = programStage;
        this.dataElement = dataElement;
        this.compulsory = compulsory;
        this.sortOrder = sortOrder;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( ProgramStage programStage )
    {
        this.programStage = programStage;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getAllowProvidedElsewhere()
    {
        return allowProvidedElsewhere;
    }

    public void setAllowProvidedElsewhere( Boolean allowProvidedElsewhere )
    {
        this.allowProvidedElsewhere = allowProvidedElsewhere;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isCompulsory()
    {
        return compulsory;
    }

    public void setCompulsory( boolean compulsory )
    {
        this.compulsory = compulsory;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getDisplayInReports()
    {
        return displayInReports;
    }

    public void setDisplayInReports( Boolean displayInReports )
    {
        this.displayInReports = displayInReports;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getAllowFutureDate()
    {
        return allowFutureDate;
    }

    public void setAllowFutureDate( Boolean allowFutureDate )
    {
        this.allowFutureDate = allowFutureDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getRenderOptionsAsRadio()
    {
        return renderOptionsAsRadio;
    }

    public void setRenderOptionsAsRadio( Boolean renderOptionsAsRadio )
    {
        this.renderOptionsAsRadio = renderOptionsAsRadio;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @JsonSerialize( using = DeviceRenderTypeMapSerializer.class )
    public DeviceRenderTypeMap<ValueTypeRenderingObject> getRenderType()
    {
        return renderType;
    }

    public void setRenderType( DeviceRenderTypeMap<ValueTypeRenderingObject> renderType )
    {
        this.renderType = renderType;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getSkipSynchronization()
    {
        return skipSynchronization;
    }

    public void setSkipSynchronization( Boolean skipSynchronization )
    {
        this.skipSynchronization = skipSynchronization;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        ProgramStageDataElement that = (ProgramStageDataElement) o;

        if ( dataElement != null ? !dataElement.equals( that.dataElement ) : that.dataElement != null ) return false;
        if ( programStage != null ? !programStage.equals( that.programStage ) : that.programStage != null ) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = programStage != null ? programStage.hashCode() : 0;
        result = 31 * result + (dataElement != null ? dataElement.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "ProgramStageDataElement{" +
            "programStage=" + programStage +
            ", dataElement=" + dataElement +
            ", compulsory=" + compulsory +
            ", allowProvidedElsewhere=" + allowProvidedElsewhere +
            ", sortOrder=" + sortOrder +
            ", displayInReports=" + displayInReports +
            ", allowFutureDate=" + allowFutureDate +
            ", renderOptionsAsRadio=" + renderOptionsAsRadio +
            '}';
    }
}
