package com.mass3d.common;

import java.util.Date;

/**
 * Simple class to store start and end dates.
 *
 */
public class DateRange
{
    private Date startDate;

    private Date endDate;

    public DateRange( Date startDate, Date endDate )
    {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }
}
