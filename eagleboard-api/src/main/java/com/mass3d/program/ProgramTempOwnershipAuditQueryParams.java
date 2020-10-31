package com.mass3d.program;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ProgramTempOwnershipAuditQueryParams
{
    
    /**
     * Programs to fetch audits for
     */
    private Set<Program> programs = new HashSet<>();
    
    /**
     * Users to fetch audits for
     */
    private Set<String> users = new HashSet<>();
    
    /**
     * Starting date.
     */
    private Date startDate = null;

    /**
     * Ending date.
     */
    private Date endDate = null;
    
    /**
     * Program temp ownership audit count start
     */
    private int first;
    
    /**
     * Program temp ownership audit count end
     */
    private int max;    

    /**
     * Program temp ownership audit skip paging or not
     */
    private boolean skipPaging;

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasPrograms()
    {
        return programs != null && !programs.isEmpty();
    }
    
    public boolean hasUsers()
    {
        return users != null && !users.isEmpty();
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

    public Set<Program> getPrograms()
    {
        return programs;
    }
    
    public void setPrograms( Set<Program> programs )
    {
        this.programs = programs;
    }

    public Set<String> getUsers()
    {
        return users;
    }

    public void setUsers( Set<String> users )
    {
        this.users = users;
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
