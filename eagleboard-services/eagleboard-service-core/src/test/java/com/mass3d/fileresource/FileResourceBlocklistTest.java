
package com.mass3d.fileresource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FileResourceBlocklistTest
{
    @Test
    public void testValid()
    {
        FileResource frA = new FileResource( "My_Checklist.pdf", "application/pdf", 324, "", FileResourceDomain.DATA_VALUE );
        FileResource frB = new FileResource( "Evaluation.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 541, "", FileResourceDomain.MESSAGE_ATTACHMENT );
        FileResource frC = new FileResource( "FinancialReport.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 143, "", FileResourceDomain.DATA_VALUE );

        assertTrue( FileResourceBlocklist.isValid( frA ) );
        assertTrue( FileResourceBlocklist.isValid( frB ) );
        assertTrue( FileResourceBlocklist.isValid( frC ) );
    }

    @Test
    public void testInvalid()
    {
        FileResource frA = new FileResource( "Click_Me.exe", "application/x-ms-dos-executable", 451, "", FileResourceDomain.DATA_VALUE );
        FileResource frB = new FileResource( "evil_script.sh", "application/pdf", 125, "", FileResourceDomain.MESSAGE_ATTACHMENT ); // Fake content type
        FileResource frC = new FileResource( "cookie_stealer", "text/javascript", 631, "", FileResourceDomain.USER_AVATAR ); // No file extension
        FileResource frD = new FileResource( "malicious_software.msi", null, 235, "", FileResourceDomain.USER_AVATAR ); // No content type

        assertFalse( FileResourceBlocklist.isValid( frA ) );
        assertFalse( FileResourceBlocklist.isValid( frB ) );
        assertFalse( FileResourceBlocklist.isValid( frC ) );
        assertFalse( FileResourceBlocklist.isValid( frD ) );
        assertFalse( FileResourceBlocklist.isValid( null ) );
    }
}
