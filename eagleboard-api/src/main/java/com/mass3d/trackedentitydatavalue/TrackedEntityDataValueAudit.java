package com.mass3d.trackedentitydatavalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.AuditType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.ProgramStageInstance;

@JacksonXmlRootElement( localName = "trackedEntityDataValueAudit", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityDataValueAudit
    implements Serializable
{
    private long id;

    private DataElement dataElement;

    private ProgramStageInstance programStageInstance;

    private Date created;

    private String value;

    private Boolean providedElsewhere;

    private String modifiedBy;

    private AuditType auditType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityDataValueAudit()
    {
    }

    public TrackedEntityDataValueAudit( DataElement dataElement, ProgramStageInstance programStageInstance, String value, String modifiedBy, boolean providedElsewhere, AuditType auditType )
    {
        this.dataElement = dataElement;
        this.programStageInstance = programStageInstance;
        this.providedElsewhere = providedElsewhere;

        this.created = new Date();
        this.value = value;
        this.modifiedBy = modifiedBy;
        this.auditType = auditType;
    }

    @Override
    public int hashCode()
    {
        return Objects
            .hash( dataElement, programStageInstance, created, value, providedElsewhere, modifiedBy, auditType );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }

        final TrackedEntityDataValueAudit other = (TrackedEntityDataValueAudit) obj;

        return Objects.equals( this.dataElement, other.dataElement )
            && Objects.equals( this.programStageInstance, other.programStageInstance )
            && Objects.equals( this.created, other.created )
            && Objects.equals( this.value, other.value )
            && Objects.equals( this.providedElsewhere, other.providedElsewhere )
            && Objects.equals( this.modifiedBy, other.modifiedBy )
            && Objects.equals( this.auditType, other.auditType );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStageInstance getProgramStageInstance()
    {
        return programStageInstance;
    }

    public void setProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        this.programStageInstance = programStageInstance;
    }

    @JsonProperty
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
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean getProvidedElsewhere()
    {
        return providedElsewhere;
    }

    public void setProvidedElsewhere( boolean providedElsewhere )
    {
        this.providedElsewhere = providedElsewhere;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy( String modifiedBy )
    {
        this.modifiedBy = modifiedBy;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AuditType getAuditType()
    {
        return auditType;
    }

    public void setAuditType( AuditType auditType )
    {
        this.auditType = auditType;
    }
}
