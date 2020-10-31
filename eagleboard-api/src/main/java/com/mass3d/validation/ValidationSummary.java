package com.mass3d.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dataelement.DataElementOperand;

@JacksonXmlRootElement( localName = "validationSummary", namespace = DxfNamespaces.DXF_2_0 )
public class ValidationSummary
{
    private List<ValidationResult> validationRuleViolations = new ArrayList<>();
    
    private List<DataElementOperand> commentRequiredViolations = new ArrayList<>();
    
    public ValidationSummary()
    {
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "validationRuleViolations", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "validationRuleViolation", namespace = DxfNamespaces.DXF_2_0 )
    public List<ValidationResult> getValidationRuleViolations()
    {
        return validationRuleViolations;
    }

    public void setValidationRuleViolations( List<ValidationResult> validationRuleViolations )
    {
        this.validationRuleViolations = validationRuleViolations;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "commentRequiredViolations", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "commentRequiredViolation", namespace = DxfNamespaces.DXF_2_0 )
    public List<DataElementOperand> getCommentRequiredViolations()
    {
        return commentRequiredViolations;
    }

    public void setCommentRequiredViolations( List<DataElementOperand> commentRequiredViolations )
    {
        this.commentRequiredViolations = commentRequiredViolations;
    }
}
