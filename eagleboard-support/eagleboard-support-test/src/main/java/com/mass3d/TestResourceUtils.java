
package com.mass3d;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.shaded.com.google.common.io.Files;

public class TestResourceUtils
{
    public static File getFile( String path )
        throws IOException
    {
        return new ClassPathResource( path ).getFile();
    }

    public static String getFileContent( String path )
        throws IOException
    {
        return Files.toString( getFile( path ), Charsets.UTF_8 );
    }
}
