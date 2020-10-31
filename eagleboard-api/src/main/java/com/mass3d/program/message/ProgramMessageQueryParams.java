package com.mass3d.program.message;

import java.util.Date;
import java.util.Set;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;

public class ProgramMessageQueryParams
{
    private Set<String> organisationUnit;

    private ProgramMessageStatus messageStatus;

    private ProgramInstance programInstance;

    private ProgramStageInstance programStageInstance;

    private Date afterDate;

    private Date beforeDate;

    private Integer page;

    private Integer pageSize;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramMessageQueryParams()
    {
        super();
    }

    public ProgramMessageQueryParams( Set<String> organisationUnit, ProgramMessageStatus messageStatus,
        ProgramInstance programInstance, ProgramStageInstance programStageInstance, Date afterDate, Date beforeDate,
        Integer page, Integer pageSize )
    {
        super();
        this.organisationUnit = organisationUnit;
        this.messageStatus = messageStatus;
        this.programInstance = programInstance;
        this.programStageInstance = programStageInstance;
        this.afterDate = afterDate;
        this.beforeDate = beforeDate;
        this.page = page;
        this.pageSize = pageSize;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasOrignisationUnit()
    {
        return organisationUnit != null;
    }

    public boolean hasProgramInstance()
    {
        return programInstance != null;
    }

    public boolean hasProgramStageInstance()
    {
        return programStageInstance != null;
    }

    public boolean hasPaging()
    {
        return page != null && pageSize != null;
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------

    public ProgramInstance getProgramInstance()
    {
        return programInstance;
    }

    public void setProgramInstance( ProgramInstance programInstance )
    {
        this.programInstance = programInstance;
    }

    public ProgramStageInstance getProgramStageInstance()
    {
        return programStageInstance;
    }

    public void setProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        this.programStageInstance = programStageInstance;
    }

    public Set<String> getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( Set<String> organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }
    
    public Integer getPage()
    {
        return page;
    }

    public void setPage( Integer page )
    {
        this.page = page;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize( Integer pageSize )
    {
        this.pageSize = pageSize;
    }

    public ProgramMessageStatus getMessageStatus()
    {
        return messageStatus;
    }

    public void setMessageStatus( ProgramMessageStatus messageStatus )
    {
        this.messageStatus = messageStatus;
    }

    public Date getAfterDate()
    {
        return afterDate;
    }

    public void setAfterDate( Date afterDate )
    {
        this.afterDate = afterDate;
    }

    public Date getBeforeDate()
    {
        return beforeDate;
    }

    public void setBeforeDate( Date beforeDate )
    {
        this.beforeDate = beforeDate;
    }
}
