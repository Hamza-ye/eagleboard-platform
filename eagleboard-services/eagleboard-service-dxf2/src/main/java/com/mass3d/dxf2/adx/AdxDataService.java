package com.mass3d.dxf2.adx;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import com.mass3d.common.IdSchemes;
import com.mass3d.datavalue.DataExportParams;
import com.mass3d.dxf2.common.ImportOptions;
import com.mass3d.dxf2.importsummary.ImportSummary;
import com.mass3d.scheduling.JobConfiguration;

public interface AdxDataService
{
    //--------------------------------------------------------------------------
    // ADX standard constants
    //--------------------------------------------------------------------------

    String NAMESPACE = "urn:ihe:qrph:adx:2015";    
    String ROOT = "adx";
    String GROUP = "group";
    String DATASET = "dataSet";
    String PERIOD = "period";
    String ORGUNIT = "orgUnit";    
    String DATAELEMENT = "dataElement";
    String DATAVALUE = "dataValue";
    String VALUE = "value";
    String ANNOTATION = "annotation";
    String ERROR = "error";
    
    //--------------------------------------------------------------------------
    // DHIS 2 specific constants
    //--------------------------------------------------------------------------

    String CATOPTCOMBO = "categoryOptionCombo";    
    String ATTOPTCOMBO = "attributeOptionCombo";

    //--------------------------------------------------------------------------
    // Methods
    //--------------------------------------------------------------------------

    DataExportParams getFromUrl(Set<String> dataSets, Set<String> periods, Date startDate,
        Date endDate,
        Set<String> organisationUnits, boolean includeChildren, boolean includeDeleted,
        Date lastUpdated, Integer limit, IdSchemes outputIdSchemes);
    
    
    /**
     * Post data. Takes ADX Data from input stream and saves a series of DXF2 
     * DataValueSets.
     * 
     * @param in the InputStream.
     * @param importOptions the importOptions.
     * @param id the task id, can be null.
     * 
     * @return an ImportSummaries collection of ImportSummary for each DataValueSet.
     */
    ImportSummary saveDataValueSet(InputStream in, ImportOptions importOptions, JobConfiguration id);

    /**
     * Get data. Writes adx export data to output stream.
     * 
     * @param in the InputStream.
     * @param importOptions the importOptions.
     * @param id the task id, can be null.
     * 
     * @return an ImportSummaries collection of ImportSummary for each DataValueSet.
     * @throws AdxException for conflicts during export process.
     */
    void writeDataValueSet(DataExportParams params, OutputStream out)
        throws AdxException;

}
