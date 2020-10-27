package com.mass3d.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.schema.annotation.PropertyRange;

/**
 * An IndicatorGroupSet is a set of IndicatorGroups. It is by default exclusive,
 * in the sense that an Indicator can only be a member of one or zero of the
 * IndicatorGroups in a IndicatorGroupSet.
 *
 */
@JacksonXmlRootElement( localName = "indicatorGroupSet", namespace = DxfNamespaces.DXF_2_0 )
public class IndicatorGroupSet
    extends BaseIdentifiableObject implements MetadataObject
{
    private String description;

    private Boolean compulsory = false;

    private List<IndicatorGroup> members = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public IndicatorGroupSet()
    {

    }

    public IndicatorGroupSet( String name )
    {
        this.name = name;
        this.compulsory = false;
    }

    public IndicatorGroupSet( String name, Boolean compulsory )
    {
        this( name );
        this.compulsory = compulsory;
    }

    public IndicatorGroupSet( String name, String description, Boolean compulsory )
    {
        this( name, compulsory );
        this.description = description;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public Collection<Indicator> getIndicators()
    {
        List<Indicator> indicators = new ArrayList<>();

        for ( IndicatorGroup group : members )
        {
            indicators.addAll( group.getMembers() );
        }

        return indicators;
    }

    public IndicatorGroup getGroup( Indicator indicator )
    {
        for ( IndicatorGroup group : members )
        {
            if ( group.getMembers().contains( indicator ) )
            {
                return group;
            }
        }

        return null;
    }

    public Boolean isMemberOfIndicatorGroups( Indicator indicator )
    {
        for ( IndicatorGroup group : members )
        {
            if ( group.getMembers().contains( indicator ) )
            {
                return true;
            }
        }

        return false;
    }

    public Boolean hasIndicatorGroups()
    {
        return members != null && members.size() > 0;
    }

    public List<IndicatorGroup> getSortedGroups()
    {
        List<IndicatorGroup> sortedGroups = new ArrayList<>( members );

        Collections.sort( sortedGroups );

        return sortedGroups;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void removeAllIndicatorGroups()
    {
        members.clear();
    }

    public void addIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        members.add( indicatorGroup );
        indicatorGroup.setGroupSet( this );
    }

    public void removeIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        members.remove( indicatorGroup );
        indicatorGroup.setGroupSet( null );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 2 )
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
    public Boolean isCompulsory()
    {
        if ( compulsory == null )
        {
            return false;
        }

        return compulsory;
    }

    public void setCompulsory( Boolean compulsory )
    {
        this.compulsory = compulsory;
    }

    @JsonProperty( "indicatorGroups" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "indicatorGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "indicatorGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<IndicatorGroup> getMembers()
    {
        return members;
    }

    public void setMembers( List<IndicatorGroup> members )
    {
        this.members = members;
    }
}
