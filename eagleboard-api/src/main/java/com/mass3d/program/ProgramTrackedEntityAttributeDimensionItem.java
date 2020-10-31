package com.mass3d.program;

import static com.mass3d.common.DimensionalObjectUtils.COMPOSITE_DIM_OBJECT_PLAIN_SEP;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.List;
import com.mass3d.analytics.AggregationType;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdScheme;
import com.mass3d.trackedentity.TrackedEntityAttribute;

@JacksonXmlRootElement( localName = "programAttributeDimension", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramTrackedEntityAttributeDimensionItem
    extends BaseDimensionalItemObject implements EmbeddedObject
{
    private Program program;

    private TrackedEntityAttribute attribute;
    
    public ProgramTrackedEntityAttributeDimensionItem()
    {
    }
    
    public ProgramTrackedEntityAttributeDimensionItem( Program program, TrackedEntityAttribute attribute )
    {
        this.program = program;
        this.attribute = attribute;
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public String getDimensionItem()
    {
        return program.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + attribute.getUid();
    }

    @Override
    public String getDimensionItem( IdScheme idScheme )
    {
        return program.getPropertyValue( idScheme ) + COMPOSITE_DIM_OBJECT_PLAIN_SEP + attribute.getPropertyValue( idScheme );
    }

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.PROGRAM_ATTRIBUTE;
    }

//    @Override
//    public List<LegendSet> getLegendSets()
//    {
//        return attribute.getLegendSets();
//    }

    @Override
    public AggregationType getAggregationType()
    {
        return attribute.getAggregationType();
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "program", program )
            .add( "attribute", attribute ).toString();
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode( program, attribute );
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( !getClass().isAssignableFrom( object.getClass() ) )
        {
            return false;
        }

        ProgramTrackedEntityAttributeDimensionItem other = (ProgramTrackedEntityAttributeDimensionItem) object;
        
        return Objects.equal( attribute, other.attribute ) && Objects.equal( program, other.program );
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getAttribute()
    {
        return attribute;
    }

    public void setAttribute( TrackedEntityAttribute attribute )
    {
        this.attribute = attribute;
    }
    
    @Override
    public String getName()
    {
        return program.getName() + " " + attribute.getName();
    }
    
    @Override
    public String getDisplayName()
    {
        return program.getDisplayName() + " " + attribute.getDisplayName();
    }
}
