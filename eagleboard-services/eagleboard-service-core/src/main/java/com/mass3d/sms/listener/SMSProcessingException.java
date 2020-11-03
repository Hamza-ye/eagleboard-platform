package com.mass3d.sms.listener;

import org.hisp.dhis.smscompression.SmsResponse;

public class SMSProcessingException
    extends
    RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 353425388316643481L;

    private SmsResponse resp;

    private Throwable err;

    public SMSProcessingException( SmsResponse resp )
    {
        this.resp = resp;
    }

    public SMSProcessingException( SmsResponse resp, Throwable err )
    {
        this.resp = resp;
        this.err = err;
    }

    @Override
    public String getMessage()
    {
        return resp.getDescription();
    }

    public SmsResponse getResp()
    {
        return resp;
    }

    public Throwable getErr()
    {
        return err;
    }
}