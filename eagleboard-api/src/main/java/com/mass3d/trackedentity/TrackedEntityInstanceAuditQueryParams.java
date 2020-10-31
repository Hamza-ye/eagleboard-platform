package com.mass3d.trackedentity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.AuditType;

public class TrackedEntityInstanceAuditQueryParams
{
    /**
     * Tracked entity instances to fetch audits for
     */
    private Set<String> trackedEntityInstances = new HashSet<>();
    
    /**
     * Users to fetch audits for
     */
    private Set<String> users = new HashSet<>();
    
    /**
     * AuditType to fetch for
     */
    private AuditType auditType;

    /**
     * Starting date.
     */
    private Date startDate = null;

    /**
     * Ending date.
     */
    private Date endDate = null;
    
    /**
     * Tracked entity instance audit count start
     */
    private int first;
    
    /**
     * Tracked entity instance audit count end
     */
    private int max;    

    /**
     * Traked entity instance audit skip paging or not
     */
    private boolean skipPaging;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityInstanceAuditQueryParams()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasTrackedEntityInstances()
    {
        return trackedEntityInstances != null && !trackedEntityInstances.isEmpty();
    }
    
    public boolean hasUsers()
    {
        return users != null && !users.isEmpty();
    }
    
    public boolean hasAuditType()
    {
        return auditType != null;
    }

    public boolean hasStartDate()
    {
        return startDate != null;
    }

    public boolean hasEndDate()
    {
        return endDate != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public Set<String> getTrackedEntityInstances()
    {
        return trackedEntityInstances;
    }

    public void setTrackedEntityInstances( Set<String> trackedEntityInstances )
    {
        this.trackedEntityInstances = trackedEntityInstances;
    }    

    public Set<String> getUsers()
    {
        return users;
    }

    public void setUsers( Set<String> users )
    {
        this.users = users;
    }    

    public AuditType getAuditType()
    {
        return auditType;
    }

    public void setAuditType( AuditType auditType )
    {
        this.auditType = auditType;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    public int getFirst()
    {
        return first;
    }

    public void setFirst( int first )
    {
        this.first = first;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax( int max )
    {
        this.max = max;
    }    

    public boolean isSkipPaging()
    {
        return skipPaging;
    }

    public void setSkipPaging( boolean skipPaging )
    {
        this.skipPaging = skipPaging;
    }
}
