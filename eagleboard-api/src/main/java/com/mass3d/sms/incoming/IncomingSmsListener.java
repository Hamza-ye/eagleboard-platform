package com.mass3d.sms.incoming;

public interface IncomingSmsListener
{
    boolean accept(IncomingSms sms);

    void receive(IncomingSms sms);
}
