package com.mass3d.trackedentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.vividsolutions.jts.geom.Geometry;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.audit.AuditAttribute;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.Auditable;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.SoftDeletableObject;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.ProgramInstance;
import com.mass3d.relationship.RelationshipItem;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;

@JacksonXmlRootElement( localName = "trackedEntityInstance", namespace = DxfNamespaces.DXF_2_0 )
@Auditable( scope = AuditScope.TRACKER )
public class TrackedEntityInstance
    extends SoftDeletableObject
{
    public static String PREFIX_TRACKED_ENTITY_ATTRIBUTE = "attr";

    private Date createdAtClient;

    private Date lastUpdatedAtClient;

    private Set<TrackedEntityAttributeValue> trackedEntityAttributeValues = new HashSet<>();

    private Set<RelationshipItem> relationshipItems = new HashSet<>();

    private Set<ProgramInstance> programInstances = new HashSet<>();

    private Set<TrackedEntityProgramOwner> programOwners = new HashSet<>();

    @AuditAttribute
    private OrganisationUnit organisationUnit;

    @AuditAttribute
    private TrackedEntityType trackedEntityType;

    @AuditAttribute
    private Boolean inactive = false;

    private Geometry geometry;

    private Date lastSynchronized = new Date( 0 );

    private String storedBy;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityInstance()
    {
    }

    @Override
    public void setAutoFields()
    {
        super.setAutoFields();

        if ( createdAtClient == null )
        {
            createdAtClient = created;
        }

        if ( lastUpdatedAtClient == null )
        {
            lastUpdatedAtClient = lastUpdated;
        }
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        trackedEntityAttributeValues.add( attributeValue );
        attributeValue.setEntityInstance( this );
    }

    public void removeAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        trackedEntityAttributeValues.remove( attributeValue );
        attributeValue.setEntityInstance( null );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getCreatedAtClient()
    {
        return createdAtClient;
    }

    public void setCreatedAtClient( Date createdAtClient )
    {
        this.createdAtClient = createdAtClient;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getLastUpdatedAtClient()
    {
        return lastUpdatedAtClient;
    }

    public void setLastUpdatedAtClient( Date lastUpdatedAtClient )
    {
        this.lastUpdatedAtClient = lastUpdatedAtClient;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getStoredBy()
    {
        return storedBy;
    }

    public void setStoredBy( String storedBy )
    {
        this.storedBy = storedBy;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( OrganisationUnit organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    @JsonProperty( "trackedEntityAttributeValues" )
    @JacksonXmlElementWrapper( localName = "trackedEntityAttributeValues", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "trackedEntityAttributeValue", namespace = DxfNamespaces.DXF_2_0 )
    public Set<TrackedEntityAttributeValue> getTrackedEntityAttributeValues()
    {
        return trackedEntityAttributeValues;
    }

    public void setTrackedEntityAttributeValues( Set<TrackedEntityAttributeValue> trackedEntityAttributeValues )
    {
        this.trackedEntityAttributeValues = trackedEntityAttributeValues;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "programInstances", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "programInstance", namespace = DxfNamespaces.DXF_2_0 )
    public Set<ProgramInstance> getProgramInstances()
    {
        return programInstances;
    }

    public void setProgramInstances( Set<ProgramInstance> programInstances )
    {
        this.programInstances = programInstances;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "programOwners", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "programOwners", namespace = DxfNamespaces.DXF_2_0 )
    public Set<TrackedEntityProgramOwner> getProgramOwners()
    {
        return programOwners;
    }

    public void setProgramOwners( Set<TrackedEntityProgramOwner> programOwners )
    {
        this.programOwners = programOwners;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "trackedEntityType", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "trackedEntityType", namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityType getTrackedEntityType()
    {
        return trackedEntityType;
    }

    public void setTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        this.trackedEntityType = trackedEntityType;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "inactive", namespace = DxfNamespaces.DXF_2_0 )
    public Boolean isInactive()
    {
        return inactive;
    }

    public void setInactive( Boolean inactive )
    {
        this.inactive = inactive;
    }

    @JsonIgnore
    public Date getLastSynchronized()
    {
        return lastSynchronized;
    }

    public void setLastSynchronized( Date lastSynchronized )
    {
        this.lastSynchronized = lastSynchronized;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "relationshipItems", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "relationshipItem", namespace = DxfNamespaces.DXF_2_0 )
    public Set<RelationshipItem> getRelationshipItems()
    {
        return relationshipItems;
    }

    public void setRelationshipItems( Set<RelationshipItem> relationshipItems )
    {
        this.relationshipItems = relationshipItems;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Geometry getGeometry()
    {
        return geometry;
    }

    public void setGeometry( Geometry geometry )
    {
        this.geometry = geometry;
    }

    @Override
    public String toString()
    {
        return "TrackedEntityInstance{" +
            "id=" + id +
            ", uid='" + uid + '\'' +
            ", name='" + name + '\'' +
            ", organisationUnit=" + organisationUnit +
            ", trackedEntityType=" + trackedEntityType +
            ", inactive=" + inactive +
            ", deleted=" + isDeleted() +
            ", lastSynchronized=" + lastSynchronized +
            '}';
    }
}
