package com.mass3d.audit.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.AuditType;
import com.mass3d.common.DxfNamespaces;

/**
 * @deprecated Will be removed soon.
 */
@JacksonXmlRootElement( localName = "trackedEntityInstanceAudit", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityInstanceAudit
    implements Serializable
{
    private static final long serialVersionUID = 4260110537887403524L;

    private long id;

    private String trackedEntityInstance;

    private String comment;

    private Date created;

    private String accessedBy;

    private AuditType auditType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityInstanceAudit()
    {
    }

    public TrackedEntityInstanceAudit( String trackedEntityInstance, String accessedBy, AuditType auditType )
    {
        this.trackedEntityInstance = trackedEntityInstance;
        this.accessedBy = accessedBy;
        this.created = new Date();
        this.auditType = auditType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( trackedEntityInstance, comment, created, accessedBy, auditType );
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

        final TrackedEntityInstanceAudit other = (TrackedEntityInstanceAudit) obj;

        return Objects.equals( this.trackedEntityInstance, other.trackedEntityInstance )
            && Objects.equals( this.comment, other.comment )
            && Objects.equals( this.created, other.created )
            && Objects.equals( this.accessedBy, other.accessedBy )
            && Objects.equals( this.auditType, other.auditType );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getTrackedEntityInstance()
    {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance( String trackedEntityInstance )
    {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getAccessedBy()
    {
        return accessedBy;
    }

    public void setAccessedBy( String accessedBy )
    {
        this.accessedBy = accessedBy;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AuditType getAuditType()
    {
        return auditType;
    }

    public void setAuditType( AuditType auditType )
    {
        this.auditType = auditType;
    }
}
