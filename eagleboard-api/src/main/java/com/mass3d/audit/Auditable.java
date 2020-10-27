package com.mass3d.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation signal the system that the object annotated with it can be
 * processed by the auditing sub-system.
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( value = ElementType.TYPE )
public @interface Auditable
{
    /**
     * NOT IMPLEMENTED
     *
     */
    String[] eventType() default "all";

    /**
     * The scope of the annotated class. The scope is used to group logically
     * adjacent objects (e.g. METADATA)
     * 
     * @return an {@see AuditScope}
     */
    AuditScope scope();
}