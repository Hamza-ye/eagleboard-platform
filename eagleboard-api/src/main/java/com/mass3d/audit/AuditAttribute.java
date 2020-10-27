package com.mass3d.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate an attribute of a entity should be saved for auditing.
 * If the marked attribute is an {@link com.mass3d.common.IdentifiableObject} then its UID will be extracted for saving,
 * otherwise the value object of the attribute will be saved.
 * All attributes's values will be put into {@link Audit}.attributes and saved to database as JSONB column.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface AuditAttribute
{
}
