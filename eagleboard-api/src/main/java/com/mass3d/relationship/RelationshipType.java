package com.mass3d.relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.translation.TranslationProperty;

@JacksonXmlRootElement( localName = "relationshipType", namespace = DxfNamespaces.DXF_2_0 )
public class RelationshipType
    extends BaseIdentifiableObject
    implements MetadataObject
{
    private RelationshipConstraint fromConstraint;

    private RelationshipConstraint toConstraint;

    private String description;

    private boolean bidirectional = false;

    private String fromToName;

    private String toFromName;

    private transient String displayFromToName;

    private transient String displayToFromName;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public RelationshipType()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( required = Property.Value.TRUE, value = PropertyType.COMPLEX )
    public RelationshipConstraint getFromConstraint()
    {
        return fromConstraint;
    }

    public void setFromConstraint( RelationshipConstraint fromConstraint )
    {
        this.fromConstraint = fromConstraint;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( required = Property.Value.TRUE, value = PropertyType.COMPLEX )
    public RelationshipConstraint getToConstraint()
    {
        return toConstraint;
    }

    public void setToConstraint( RelationshipConstraint toConstraint )
    {
        this.toConstraint = toConstraint;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isBidirectional()
    {
        return bidirectional;
    }

    public void setBidirectional( boolean bidirectional )
    {
        this.bidirectional = bidirectional;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getFromToName()
    {
        return fromToName;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDisplayFromToName()
    {
        displayFromToName = getTranslation( TranslationProperty.RELATIONSHIP_FROM_TO_NAME, displayFromToName );
        return  displayFromToName != null ? displayFromToName : getFromToName();
    }

    public void setFromToName( String fromToName )
    {
        this.fromToName = fromToName;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getToFromName()
    {
        return toFromName;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDisplayToFromName()
    {
        displayToFromName = getTranslation( TranslationProperty.RELATIONSHIP_TO_FROM_NAME, displayToFromName );
        return displayToFromName != null ? displayToFromName : getToFromName();
    }

    public void setToFromName( String toFromName )
    {
        this.toFromName = toFromName;
    }
}
