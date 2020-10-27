package com.mass3d.artemis.audit.listener;

import com.mass3d.audit.AuditAttribute;

public class NestedTestImplementation
    extends
        com.mass3d.artemis.audit.listener.TestImplementation
{
    private String testAttribute3;

    @AuditAttribute
    public String getTestAttribute3()
    {
        return testAttribute3;
    }
}
