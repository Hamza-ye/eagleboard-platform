package com.mass3d.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.trackedentity.TrackedEntityInstance;

@JacksonXmlRootElement( localName = "programOwnershipHistory", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramOwnershipHistory implements Serializable
{
    private static final long serialVersionUID = 6713155272099925278L;

    private int id;

    private Program program;

    private Date startDate;

    private Date endDate;

    private String createdBy;

    private TrackedEntityInstance entityInstance;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramOwnershipHistory()
    {
    }

    public ProgramOwnershipHistory( Program program, TrackedEntityInstance entityInstance, Date startDate,
        String createdBy )
    {
        this.program = program;
        this.startDate = startDate;
        this.createdBy = createdBy;
        this.endDate = new Date();
        this.entityInstance = entityInstance;
    }

    public ProgramOwnershipHistory( Program program, TrackedEntityInstance entityInstance, Date startDate, Date endDate,
        String createdBy )
    {
        this.program = program;
        this.startDate = startDate;
        this.createdBy = createdBy;
        this.endDate = endDate;
        this.entityInstance = entityInstance;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( program, entityInstance, startDate, createdBy, endDate );
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

        final ProgramOwnershipHistory other = (ProgramOwnershipHistory) obj;

        return Objects.equals( this.program, other.program ) && Objects
            .equals( this.startDate, other.startDate )
            && Objects.equals( this.createdBy, other.createdBy ) && Objects
            .equals( this.endDate, other.endDate )
            && Objects.equals( this.entityInstance, other.entityInstance );
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
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

}
