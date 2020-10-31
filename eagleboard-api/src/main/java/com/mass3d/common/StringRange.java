package com.mass3d.common;

/**
 * Simple class to store start and end dates.
 *
 */
public class StringRange
{
    private String start;

    private String end;

    public StringRange( String start, String end )
    {
        this.start = start;
        this.end = end;
    }

    public String getStart()
    {
        return start;
    }

    public String getEnd()
    {
        return end;
    }
}
