package com.mass3d.program.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;

@JacksonXmlRootElement( namespace = DxfNamespaces.DXF_2_0 )
public class ProgramNotificationInstance extends BaseIdentifiableObject
{
    private ProgramNotificationTemplate programNotificationTemplate;

    private ProgramInstance programInstance;

    private ProgramStageInstance programStageInstance;

    private Date sentAt;

    private Date scheduledAt;

    public ProgramNotificationInstance()
    {
    }

    public ProgramNotificationInstance( ProgramInstance programInstance, ProgramNotificationTemplate programNotificationTemplate,
        ProgramStageInstance programStageInstance, Date scheduledAt, Date sentAt )
    {
        this.programInstance = programInstance;
        this.programNotificationTemplate = programNotificationTemplate;
        this.programStageInstance = programStageInstance;
        this.scheduledAt = scheduledAt;
        this.sentAt = sentAt;
    }

    public boolean hasProgramInstance()
    {
        return programInstance != null;
    }

    public boolean hasProgramStageInstance()
    {
        return programStageInstance != null;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramInstance getProgramInstance()
    {
        return programInstance;
    }

    public void setProgramInstance( ProgramInstance programInstance )
    {
        this.programInstance = programInstance;
    }

    @JsonProperty()
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramNotificationTemplate getProgramNotificationTemplate()
    {
        return programNotificationTemplate;
    }

    public void setProgramNotificationTemplate( ProgramNotificationTemplate programNotificationTemplate )
    {
        this.programNotificationTemplate = programNotificationTemplate;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStageInstance getProgramStageInstance()
    {
        return programStageInstance;
    }

    public void setProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        this.programStageInstance = programStageInstance;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getScheduledAt()
    {
        return scheduledAt;
    }

    public void setScheduledAt( Date scheduledAt )
    {
        this.scheduledAt = scheduledAt;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getSentAt()
    {
        return sentAt;
    }

    public void setSentAt( Date sentAt )
    {
        this.sentAt = sentAt;
    }
}
