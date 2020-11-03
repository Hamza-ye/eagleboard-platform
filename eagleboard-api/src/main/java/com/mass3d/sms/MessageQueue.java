package com.mass3d.sms;

import com.mass3d.sms.incoming.IncomingSms;

public interface MessageQueue
{
    void put(IncomingSms message);

    IncomingSms get();

    void remove(IncomingSms message);

    void initialize();
}
