package com.mass3d.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitLevel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.adapter.JacksonPeriodTypeSerializer;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.period.PeriodType;
import com.mass3d.period.YearlyPeriodType;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserGroup;

@JacksonXmlRootElement( localName = "configuration", namespace = DxfNamespaces.DXF_2_0 )
public class Configuration
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 936186436040704261L;
    
    private static final PeriodType DEFAULT_INFRASTRUCTURAL_PERIODTYPE = new YearlyPeriodType();
    
    private int id;

    // -------------------------------------------------------------------------
    // Various
    // -------------------------------------------------------------------------

    private String systemId;
    
    private UserGroup feedbackRecipients;
    
    private OrganisationUnitLevel offlineOrganisationUnitLevel;

    private IndicatorGroup infrastructuralIndicators;

    private DataElementGroup infrastructuralDataElements;
    
    private PeriodType infrastructuralPeriodType;
    
    private UserAuthorityGroup selfRegistrationRole;
    
    private OrganisationUnit selfRegistrationOrgUnit;
    
    private Set<String> corsWhitelist = new HashSet<>();
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public Configuration()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public PeriodType getInfrastructuralPeriodTypeDefaultIfNull()
    {
        return infrastructuralPeriodType != null ? infrastructuralPeriodType : DEFAULT_INFRASTRUCTURAL_PERIODTYPE;
    }
    
    public boolean selfRegistrationAllowed()
    {
        return selfRegistrationRole != null; // && selfRegistrationOrgUnit != null;
    }
    
    // -------------------------------------------------------------------------
    // Set and get methods
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }
    
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getSystemId()
    {
        return systemId;
    }

    public void setSystemId( String systemId )
    {
        this.systemId = systemId;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public UserGroup getFeedbackRecipients()
    {
        return feedbackRecipients;
    }

    public void setFeedbackRecipients( UserGroup feedbackRecipients )
    {
        this.feedbackRecipients = feedbackRecipients;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public OrganisationUnitLevel getOfflineOrganisationUnitLevel()
    {
        return offlineOrganisationUnitLevel;
    }
    
    public void setOfflineOrganisationUnitLevel( OrganisationUnitLevel offlineOrganisationUnitLevel )
    {
        this.offlineOrganisationUnitLevel = offlineOrganisationUnitLevel;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public IndicatorGroup getInfrastructuralIndicators()
    {
        return infrastructuralIndicators;
    }

    public void setInfrastructuralIndicators( IndicatorGroup infrastructuralIndicators )
    {
        this.infrastructuralIndicators = infrastructuralIndicators;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataElementGroup getInfrastructuralDataElements()
    {
        return infrastructuralDataElements;
    }

    public void setInfrastructuralDataElements( DataElementGroup infrastructuralDataElements )
    {
        this.infrastructuralDataElements = infrastructuralDataElements;
    }

    @JsonProperty
    @JsonSerialize( using = JacksonPeriodTypeSerializer.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( PropertyType.TEXT )
    public PeriodType getInfrastructuralPeriodType()
    {
        return infrastructuralPeriodType;
    }

    public void setInfrastructuralPeriodType( PeriodType infrastructuralPeriodType )
    {
        this.infrastructuralPeriodType = infrastructuralPeriodType;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public UserAuthorityGroup getSelfRegistrationRole()
    {
        return selfRegistrationRole;
    }

    public void setSelfRegistrationRole( UserAuthorityGroup selfRegistrationRole )
    {
        this.selfRegistrationRole = selfRegistrationRole;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public OrganisationUnit getSelfRegistrationOrgUnit()
    {
        return selfRegistrationOrgUnit;
    }

    public void setSelfRegistrationOrgUnit( OrganisationUnit selfRegistrationOrgUnit )
    {
        this.selfRegistrationOrgUnit = selfRegistrationOrgUnit;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<String> getCorsWhitelist()
    {
        return corsWhitelist;
    }

    public void setCorsWhitelist( Set<String> corsWhitelist )
    {
        this.corsWhitelist = corsWhitelist;
    }
}
