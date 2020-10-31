package com.mass3d.program.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.trackedentity.TrackedEntityInstance;

@JacksonXmlRootElement( localName = "programMessageRecipients", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramMessageRecipients
    implements Serializable
{
    private static final long serialVersionUID = 1141462154959329242L;

    private TrackedEntityInstance trackedEntityInstance;

    private OrganisationUnit organisationUnit;

    private Set<String> phoneNumbers = new HashSet<>();

    private Set<String> emailAddresses = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramMessageRecipients()
    {
    }

    public ProgramMessageRecipients( Set<String> phoneNumbers, Set<String> emailAddresses )
    {
        this.phoneNumbers = phoneNumbers;
        this.emailAddresses = emailAddresses;
    }

    public ProgramMessageRecipients( TrackedEntityInstance trackedEntityInstance,
        OrganisationUnit organisationUnit, Set<String> phoneNumbers, Set<String> emailAddresses )
    {
        this.trackedEntityInstance = trackedEntityInstance;
        this.organisationUnit = organisationUnit;
        this.phoneNumbers = phoneNumbers;
        this.emailAddresses = emailAddresses;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasTrackedEntityInstance()
    {
        return trackedEntityInstance != null;
    }

    public boolean hasOrganisationUnit()
    {
        return organisationUnit != null;
    }

    // -------------------------------------------------------------------------
    // Setters and getters
    // -------------------------------------------------------------------------

    @JsonProperty( value = "trackedEntityInstance" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "trackedEntityInstance" )
    public TrackedEntityInstance getTrackedEntityInstance()
    {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance( TrackedEntityInstance trackedEntityInstance )
    {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    @JsonProperty( value = "organisationUnit" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "organisationUnit" )
    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( OrganisationUnit organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    @JsonProperty( value = "phoneNumbers" )
    @JacksonXmlProperty( localName = "phoneNumbers" )
    public Set<String> getPhoneNumbers()
    {
        return phoneNumbers;
    }

    public void setPhoneNumbers( Set<String> phoneNumbers )
    {
        this.phoneNumbers = phoneNumbers;
    }

    @JsonProperty( value = "emailAddresses" )
    @JacksonXmlProperty( localName = "emailAddresses" )
    public Set<String> getEmailAddresses()
    {
        return emailAddresses;
    }

    public void setEmailAddresses( Set<String> emailAddress )
    {
        this.emailAddresses = emailAddress;
    }

    @Override
    public String toString()
    {
        return "ProgramMessageRecipients[ " + (phoneNumbers != null ? " " + phoneNumbers + " "
            : " " + (emailAddresses != null ? " " + emailAddresses + " " : "")) + " ]";
    }
}
