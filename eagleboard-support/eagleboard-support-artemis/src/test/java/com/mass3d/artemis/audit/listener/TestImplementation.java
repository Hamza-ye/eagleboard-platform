package com.mass3d.artemis.audit.listener;

import com.mass3d.audit.AuditAttribute;

public class TestImplementation
    implements com.mass3d.artemis.audit.listener.TestInterface
{
    @AuditAttribute
    private String testAttribute1;

    private String testAttribute2;

    public String getTestAttribute1()
    {
        return testAttribute1;
    }

    @AuditAttribute
    public String getTestAttribute2()
    {
        return testAttribute2;
    }
}
