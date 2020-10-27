package com.mass3d.schema;

import static org.junit.Assert.assertFalse;

import com.mass3d.DhisSpringTest;
import org.junit.Test;

public class SchemaServiceTest
    extends DhisSpringTest
{
    private SchemaService schemaService;

    @Override
    protected void setUpTest() throws Exception
    {
        schemaService = (SchemaService) getBean( SchemaService.class.getName() );
    }

    @Test
    public void testHaveSchemas()
    {
        assertFalse( schemaService.getSchemas().isEmpty() );
    }
}
