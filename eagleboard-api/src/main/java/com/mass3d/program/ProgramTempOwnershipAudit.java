package com.mass3d.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.trackedentity.TrackedEntityInstance;

@JacksonXmlRootElement( localName = "programTempOwnershipAudit", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramTempOwnershipAudit implements Serializable
{
    private static final long serialVersionUID = 6713155272099925278L;

    private int id;

    private Program program;

    private String reason;

    private Date created;

    private String accessedBy;

    private TrackedEntityInstance entityInstance;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramTempOwnershipAudit()
    {
    }

    public ProgramTempOwnershipAudit( Program program, TrackedEntityInstance entityInstance, String reason,
        String accessedBy )
    {
        this.program = program;
        this.reason = reason;
        this.accessedBy = accessedBy;
        this.created = new Date();
        this.entityInstance = entityInstance;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( program, entityInstance, reason, created, accessedBy );
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

        final ProgramTempOwnershipAudit other = (ProgramTempOwnershipAudit) obj;

        return Objects.equals( this.program, other.program )
            && Objects.equals( this.reason, other.reason ) && Objects.equals( this.created, other.created )
            && Objects.equals( this.accessedBy, other.accessedBy ) && Objects
            .equals( this.entityInstance, other.entityInstance );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    public TrackedEntityInstance getEntityInstance()
    {
        return entityInstance;
    }

    public void setEntityInstance( TrackedEntityInstance entityInstance )
    {
        this.entityInstance = entityInstance;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getReason()
    {
        return reason;
    }

    public void setReason( String reason )
    {
        this.reason = reason;
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
    public String getAccessedBy()
    {
        return accessedBy;
    }

    public void setAccessedBy( String accessedBy )
    {
        this.accessedBy = accessedBy;
    }
}
