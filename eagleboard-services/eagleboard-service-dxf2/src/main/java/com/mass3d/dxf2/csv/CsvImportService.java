package com.mass3d.dxf2.csv;

import java.io.IOException;
import java.io.InputStream;
import com.mass3d.dxf2.metadata.Metadata;

public interface CsvImportService
{
    Metadata fromCsv(InputStream input, CsvImportOptions options)
        throws IOException;
}
