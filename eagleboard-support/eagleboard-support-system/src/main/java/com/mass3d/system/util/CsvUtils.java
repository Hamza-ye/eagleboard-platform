package com.mass3d.system.util;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;

public class CsvUtils
{
    private static final char DELIMITER = ',';

    /**
     * Returns a {@link CsvReader} using the UTF-8 char set.
     *
     * @param in the {@link InputStream}.
     * @return a {@link CsvReader}.
     */
    public static CsvReader getReader( InputStream in )
    {
        return new CsvReader( in, StandardCharsets.UTF_8 );
    }

    /**
     * Returns the CSV file represented by the given file path as a
     * list of string arrays. The file must exist on the class path.
     *
     * @param filePath the file path on the class path.
     * @param ignoreFirstRow whether to ignore the first row.
     * @return a list of string arrays.
     * @throws IOException
     */
    public static List<String[]> readCsvAsListFromClasspath( String filePath, boolean ignoreFirstRow )
        throws IOException
    {
        InputStream in = new ClassPathResource( filePath ).getInputStream();
        return readCsvAsList( in, ignoreFirstRow );
    }

    /**
     * Returns the CSV file represented by the given input stream as a
     * list of string arrays.
     *
     * @param in the {@link InputStream} representing the CSV file.
     * @param ignoreFirstRow whether to ignore the first row.
     * @return a list of string arrays.
     * @throws IOException
     */
    public static List<String[]> readCsvAsList( InputStream in, boolean ignoreFirstRow )
        throws IOException
    {
        CsvReader reader = getReader( in );

        if ( ignoreFirstRow )
        {
            reader.readRecord();
        }

        List<String[]> lines = new ArrayList<>();

        while ( reader.readRecord() )
        {
            lines.add( reader.getValues() );
        }

        return lines;
    }

    /**
     * Returns a {@link CsvWriter} using the UTF-8 char set.
     *
     * @param writer the {@link Writer}.
     * @return a {@link CsvWriter}.
     */
    public static CsvWriter getWriter( Writer writer )
    {
        return new CsvWriter( writer, DELIMITER );
    }
}
