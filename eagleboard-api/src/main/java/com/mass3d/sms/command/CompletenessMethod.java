package com.mass3d.sms.command;

public enum  CompletenessMethod
{
    ALL_DATAVALUE( "Receive all data values" ),
    AT_LEAST_ONE_DATAVALUE( "Receive at least one data value" ),
    DO_NOT_MARK_COMPLETE( "Do not mark the form as complete" );

    private String method;

    CompletenessMethod( String method )
    {
        this.method = method;
    }

    public String getMethod()
    {
        return method;
    }
}
