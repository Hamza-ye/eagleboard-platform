package com.mass3d.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.PropertyRange;

@JacksonXmlRootElement( localName = "oAuth2Client", namespace = DxfNamespaces.DXF_2_0 )
public class OAuth2Client
    extends BaseIdentifiableObject implements MetadataObject
{
    /**
     * client_id
     */
    private String cid;

    /**
     * client_secret
     */
    private String secret = UUID.randomUUID().toString();

    /**
     * List of allowed redirect URI targets for this client.
     */
    private List<String> redirectUris = new ArrayList<>();

    /**
     * List of allowed grant types for this client.
     */
    private List<String> grantTypes = new ArrayList<>();

    public OAuth2Client()
    {
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( PropertyType.IDENTIFIER )
    public String getCid()
    {
        return cid;
    }

    public void setCid( String cid )
    {
        this.cid = cid;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 36, max = 36 )
    public String getSecret()
    {
        return secret;
    }

    public void setSecret( String secret )
    {
        this.secret = secret;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "redirectUris", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "redirectUri", namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getRedirectUris()
    {
        return redirectUris;
    }

    public void setRedirectUris( List<String> redirectUris )
    {
        this.redirectUris = redirectUris;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "grantTypes", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "grantType", namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getGrantTypes()
    {
        return grantTypes;
    }

    public void setGrantTypes( List<String> grantTypes )
    {
        this.grantTypes = grantTypes;
    }

    @Override
    public int hashCode()
    {
        return 31 * super.hashCode() + Objects.hash( cid, secret, redirectUris, grantTypes );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        if ( !super.equals( obj ) )
        {
            return false;
        }

        final OAuth2Client other = (OAuth2Client) obj;

        return Objects.equals( this.cid, other.cid )
            && Objects.equals( this.secret, other.secret )
            && Objects.equals( this.redirectUris, other.redirectUris )
            && Objects.equals( this.grantTypes, other.grantTypes );
    }
}
