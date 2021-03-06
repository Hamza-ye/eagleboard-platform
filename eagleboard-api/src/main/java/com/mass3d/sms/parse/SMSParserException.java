package com.mass3d.sms.parse;

public class SMSParserException
    extends RuntimeException
{
    private String reason;

    public static final String NO_VALUE = "no_value";

    public static final String WRONG_FORMAT = "wrong_format";

    public static final String MORE_THAN_ONE_ORGUNIT = "more_than_one_orgunit";

    public SMSParserException( String message )
    {
        super( message );
    }

    public SMSParserException( String message, String reason )
    {
        super( message );
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason( String reason )
    {
        this.reason = reason;
    }
}
