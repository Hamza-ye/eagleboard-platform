package com.mass3d.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramStage;
import com.mass3d.trackedentity.TrackedEntityType;

@JacksonXmlRootElement( localName = "relationshipConstraint", namespace = DxfNamespaces.DXF_2_0 )
public class RelationshipConstraint
    implements EmbeddedObject
{
    private int id;

    private RelationshipEntity relationshipEntity;

    private TrackedEntityType trackedEntityType;

    private Program program;

    private ProgramStage programStage;

    public RelationshipConstraint()
    {
    }

    @JsonIgnore
    public void setId( int id )
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public RelationshipEntity getRelationshipEntity()
    {
        return relationshipEntity;
    }

    public void setRelationshipEntity( RelationshipEntity relationshipEntity )
    {
        this.relationshipEntity = relationshipEntity;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityType getTrackedEntityType()
    {
        return trackedEntityType;
    }

    public void setTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        this.trackedEntityType = trackedEntityType;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    @JsonProperty( )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( ProgramStage programStage )
    {
        this.programStage = programStage;
    }

    @Override
    public String toString()
    {
        return "RelationshipConstraint{" +
            "id=" + id +
            ", relationshipEntity=" + relationshipEntity +
            ", trackedEntityType=" + trackedEntityType +
            ", program=" + program +
            ", programStage=" + programStage +
            '}';
    }
}
