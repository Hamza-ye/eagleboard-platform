package com.mass3d.dxf2.importsummary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ImportSummariesTest
{
    @Test
    public void testAddImportSummary()
    {
        ImportSummaries summaries = new ImportSummaries();
        
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.SUCCESS, "Great success",
                new ImportCount( 4, 2, 1, 2 ) ) );
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.WARNING, "Ouch warning",
                new ImportCount( 1, 2, 3, 0 ) ) );
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.SUCCESS, "Great failure",
                new ImportCount( 0, 0, 4, 3 ) ) );
        
        assertEquals( 5, summaries.getImported() );
        assertEquals( 4, summaries.getUpdated() );
        assertEquals( 8, summaries.getIgnored() );
        assertEquals( 5, summaries.getDeleted() );        
    }
    
    @Test
    public void testGetImportStatus()
    {
        ImportSummaries summaries = new ImportSummaries();
        
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.SUCCESS, "Great success",
                new ImportCount( 4, 2, 1, 2 ) ) );
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.WARNING, "Ouch warning",
                new ImportCount( 1, 2, 3, 0 ) ) );
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.ERROR, "Great failure",
                new ImportCount( 0, 0, 4, 3 ) ) );
        
        assertEquals( ImportStatus.ERROR, summaries.getStatus() );
        
        summaries = new ImportSummaries();
        
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.SUCCESS, "Great success",
                new ImportCount( 4, 2, 1, 2 ) ) );
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.WARNING, "Ouch warning",
                new ImportCount( 1, 2, 3, 0 ) ) );
        
        assertEquals( ImportStatus.WARNING, summaries.getStatus() );

        summaries = new ImportSummaries();
        
        summaries.addImportSummary( 
            new ImportSummary( ImportStatus.SUCCESS, "Great success",
                new ImportCount( 4, 2, 1, 2 ) ) );

        assertEquals( ImportStatus.SUCCESS, summaries.getStatus() );

        summaries = new ImportSummaries();

        assertEquals( ImportStatus.SUCCESS, summaries.getStatus() );
    }
}
