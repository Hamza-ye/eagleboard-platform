package com.mass3d.artemis.audit.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mass3d.audit.AuditAttribute;
import com.mass3d.audit.Auditable;
import com.mass3d.system.util.AnnotationUtils;
import org.junit.Test;

public class AnnotationUtilsTest
{
    @Test
    public void testIsAnnotationPresent()
    {
        TestImplementation testImplementation = new TestImplementation();
        NestedTestImplementation nestedTestImplementation = new NestedTestImplementation();

        assertTrue( AnnotationUtils.isAnnotationPresent( testImplementation.getClass(), Auditable.class ) );
        assertTrue( AnnotationUtils.isAnnotationPresent( nestedTestImplementation.getClass(), Auditable.class ) );
    }

    @Test
    public void testGetAnnotatedFields()
    {
        NestedTestImplementation testImplementation = new NestedTestImplementation();
        assertEquals( 3, AnnotationUtils.getAnnotatedFields( testImplementation.getClass(), AuditAttribute.class ).size() );
    }

}