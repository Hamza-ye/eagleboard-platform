package com.mass3d.dxf2.datavalueset;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Set;
import com.mass3d.common.IdSchemes;
import com.mass3d.dataset.DataSet;
import com.mass3d.datavalue.DataExportParams;
import com.mass3d.dxf2.common.ImportOptions;
import com.mass3d.dxf2.importsummary.ImportSummary;
import com.mass3d.node.types.RootNode;
import com.mass3d.period.Period;
import com.mass3d.scheduling.JobConfiguration;

public interface DataValueSetService
{
    /**
     * Returns a data export object for the given parameters.
     *
     * @param dataSets data sets.
     * @param dataElementGroups the data element groups.
     * @param periods the periods.
     * @param startDate the start date.
     * @param endDate the end date.
     * @param organisationUnits the organisation units.
     * @param includeChildren whether to include org unit children.
     * @param organisationUnitGroups the organisation unit groupps.
     * @param attributeOptionCombos the attribute option combos.
     * @param includeDeleted whether to include deleted data values.
     * @param lastUpdated filter data values updated after a time stamp.
     * @param lastUpdatedDuration the last updated duration filter.
     * @param limit max number of data values to return.
     * @param idSchemes the identifier schemes.
     * @return
     */
    DataExportParams getFromUrl(Set<String> dataSets, Set<String> dataElementGroups,
        Set<String> periods,
        Date startDate, Date endDate, Set<String> organisationUnits, boolean includeChildren,
        Set<String> organisationUnitGroups, Set<String> attributeOptionCombos,
        boolean includeDeleted, Date lastUpdated,
        String lastUpdatedDuration, Integer limit, IdSchemes idSchemes);

    void validate(DataExportParams params);

    void decideAccess(DataExportParams params);

    void writeDataValueSetXml(DataExportParams params, OutputStream out);

    void writeDataValueSetJson(DataExportParams params, OutputStream out);

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

    void writeDataValueSetCsv(DataExportParams params, Writer writer);

    RootNode getDataValueSetTemplate(DataSet dataSet, Period period, List<String> orgUnits,
        boolean writeComments,
        String ouScheme, String deScheme);

    ImportSummary saveDataValueSet(InputStream in);

    ImportSummary saveDataValueSetJson(InputStream in);

    ImportSummary saveDataValueSet(InputStream in, ImportOptions importOptions);

    ImportSummary saveDataValueSetJson(InputStream in, ImportOptions importOptions);

    ImportSummary saveDataValueSetCsv(InputStream in, ImportOptions importOptions);

    ImportSummary saveDataValueSetPdf(InputStream in, ImportOptions importOptions);

    ImportSummary saveDataValueSet(InputStream in, ImportOptions importOptions,
        JobConfiguration jobId);

    ImportSummary saveDataValueSetJson(InputStream in, ImportOptions importOptions,
        JobConfiguration jobId);

    ImportSummary saveDataValueSetCsv(InputStream in, ImportOptions importOptions,
        JobConfiguration id);

    ImportSummary saveDataValueSetPdf(InputStream in, ImportOptions importOptions,
        JobConfiguration id);
}
