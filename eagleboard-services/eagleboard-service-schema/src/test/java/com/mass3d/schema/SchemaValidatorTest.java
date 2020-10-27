package com.mass3d.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mass3d.DhisSpringTest;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.schema.validation.SchemaValidator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SchemaValidatorTest
    extends DhisSpringTest
{
    @Autowired
    private SchemaValidator schemaValidator;

    @Test
    public void testCollectionOutOfMinRange()
    {
        TestCollectionSize objectUnderTest = new TestCollectionSize();
        List<ErrorReport> errorReports = schemaValidator.validate( objectUnderTest, false );

        assertEquals( 1, errorReports.size() );
        assertEquals( ErrorCode.E4007, errorReports.get( 0 ).getErrorCode() );
    }

    @Test
    public void testCollectionOutOfMaxRange()
    {
        TestCollectionSize objectUnderTest = new TestCollectionSize();
        objectUnderTest.getItems().add( "Item #1" );
        objectUnderTest.getItems().add( "Item #2" );
        objectUnderTest.getItems().add( "Item #3" );

        List<ErrorReport> errorReports = schemaValidator.validate( objectUnderTest, false );

        assertEquals( 1, errorReports.size() );
        assertEquals( ErrorCode.E4007, errorReports.get( 0 ).getErrorCode() );
    }

    @Test
    public void testCollectionInRange()
    {
        TestCollectionSize objectUnderTest = new TestCollectionSize();
        objectUnderTest.getItems().add( "Item #1" );
        objectUnderTest.getItems().add( "Item #2" );

        List<ErrorReport> errorReports = schemaValidator.validate( objectUnderTest, false );

        assertTrue( errorReports.isEmpty() );
    }

    public static class TestCollectionSize
    {
        private List<String> items = new ArrayList<>();

        public TestCollectionSize()
        {
        }

        @JsonProperty
        @PropertyRange( min = 1, max = 2 )
        public List<String> getItems()
        {
            return items;
        }

        public void setItems( List<String> items )
        {
            this.items = items;
        }
    }
}
