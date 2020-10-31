package com.mass3d.trackedentityfilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramStatus;

@JacksonXmlRootElement( localName = "trackedEntityInstanceFilter", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityInstanceFilter
    extends BaseIdentifiableObject implements MetadataObject
{

    /**
     * Property indicating program's of trackedEntityInstanceFilter
     */
    private Program program;

    /**
     * Property indicating description of trackedEntityInstanceFilter
     */
    private String description;

    /**
     * Property indicating the filter's order in tracked entity instance search
     * UI
     */
    private int sortOrder;

//    /**
//     * Property indicating the filter's rendering style
//     */
//    private ObjectStyle style;

    /**
     * Property indicating which enrollment status types to filter
     */
    private ProgramStatus enrollmentStatus;

    /**
     * Property indicating whether to filter tracked entity instances whose
     * enrollments are marked for followup or not
     */
    private Boolean followup = false;

    /**
     * Property to filter tracked entity instances based on enrollment dates
     */
    private FilterPeriod enrollmentCreatedPeriod;

    /**
     * Property to filter tracked entity instances based on event dates and
     * statues
     */
    private List<EventFilter> eventFilters = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityInstanceFilter()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
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
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

//    @JsonProperty
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public ObjectStyle getStyle()
//    {
//        return style;
//    }
//
//    public void setStyle( ObjectStyle style )
//    {
//        this.style = style;
//    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( int sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStatus getEnrollmentStatus()
    {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus( ProgramStatus enrollmentStatus )
    {
        this.enrollmentStatus = enrollmentStatus;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean isFollowup()
    {
        return followup;
    }

    public void setFollowup( Boolean followup )
    {
        this.followup = followup;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public FilterPeriod getEnrollmentCreatedPeriod()
    {
        return enrollmentCreatedPeriod;
    }

    public void setEnrollmentCreatedPeriod( FilterPeriod enrollmentCreatedPeriod )
    {
        this.enrollmentCreatedPeriod = enrollmentCreatedPeriod;
    }

    @JsonProperty( "eventFilters" )
    @JacksonXmlElementWrapper( localName = "eventFilters", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "eventFilters", namespace = DxfNamespaces.DXF_2_0 )
    public List<EventFilter> getEventFilters()
    {
        return eventFilters;
    }

    public void setEventFilters( List<EventFilter> eventFilters )
    {
        this.eventFilters = eventFilters;
    }
}
