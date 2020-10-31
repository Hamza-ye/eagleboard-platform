package com.mass3d.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.schema.annotation.PropertyRange;

@JacksonXmlRootElement( localName = "validationRuleGroup", namespace = DxfNamespaces.DXF_2_0 )
public class ValidationRuleGroup
    extends BaseIdentifiableObject implements MetadataObject
{
    private String description;

    private Set<ValidationRule> members = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ValidationRuleGroup()
    {

    }

    public ValidationRuleGroup( String name, String description, Set<ValidationRule> members )
    {
        this.name = name;
        this.description = description;
        this.members = members;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addValidationRule( ValidationRule validationRule )
    {
        members.add( validationRule );
        validationRule.getGroups().add( this );
    }

    public void removeValidationRule( ValidationRule validationRule )
    {
        members.remove( validationRule );
        validationRule.getGroups().remove( this );
    }

    public void removeAllValidationRules()
    {
        for ( ValidationRule validationRule : members )
        {
            validationRule.getGroups().remove( this );
        }

        members.clear();
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

    @JsonProperty( "validationRules" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "validationRules", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "validationRule", namespace = DxfNamespaces.DXF_2_0 )
    public Set<ValidationRule> getMembers()
    {
        return members;
    }

    public void setMembers( Set<ValidationRule> members )
    {
        this.members = members;
    }
}
