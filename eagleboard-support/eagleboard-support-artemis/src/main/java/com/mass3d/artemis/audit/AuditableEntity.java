package com.mass3d.artemis.audit;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AuditableEntity
{
    private Object entity;
}
