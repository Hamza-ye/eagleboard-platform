package com.mass3d.dxf2.datavalueset;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import com.mass3d.common.IdSchemes;
import com.mass3d.datavalue.DataExportParams;

public interface DataValueSetStore
{
    void writeDataValueSetXml(DataExportParams params, Date completeDate, OutputStream out);

    void writeDataValueSetJson(DataExportParams params, Date completeDate, OutputStream out);

    void writeDataValueSetCsv(DataExportParams params, Date completeDate, Writer writer);

    /**
     * Query for {@link DataValueSet DataValueSets} and write result as JSON.
     *
     * @param lastUpdated specifies the date to filter complete data sets last updated after
     * @param outputStream the stream to write to
     * @param idSchemes idSchemes
     */
    void writeDataValueSetJson(Date lastUpdated, OutputStream outputStream, IdSchemes idSchemes);

    /**
     * Query for {@link DataValueSet DataValueSets} and write result as JSON.
     *
     * @param lastUpdated specifies the date to filter complete data sets last updated after
     * @param outputStream the stream to write to
     * @param idSchemes idSchemes
     * @param pageSize pageSize
     * @param page page
     */
    void writeDataValueSetJson(Date lastUpdated, OutputStream outputStream, IdSchemes idSchemes,
        int pageSize,
        int page);
}
