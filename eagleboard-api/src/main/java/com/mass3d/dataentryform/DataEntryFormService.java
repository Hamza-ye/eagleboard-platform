package com.mass3d.dataentryform;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataset.DataSet;
import com.mass3d.i18n.I18n;

public interface DataEntryFormService
{
    String ID = DataEntryFormService.class.getName();

    Pattern INPUT_PATTERN = Pattern.compile( "(<input.*?/>)", Pattern.DOTALL );
    Pattern IDENTIFIER_PATTERN = Pattern.compile( "id=\"(\\w+)-(\\w+)-val\"" );
    Pattern DATAELEMENT_TOTAL_PATTERN = Pattern.compile( "dataelementid=\"(\\w+?)\"" );
    Pattern INDICATOR_PATTERN = Pattern.compile( "indicatorid=\"(\\w+)\"" );
    Pattern VALUE_TAG_PATTERN = Pattern.compile( "value=\"(.*?)\"", Pattern.DOTALL );
    Pattern TITLE_TAG_PATTERN = Pattern.compile( "title=\"(.*?)\"", Pattern.DOTALL );
    
    // -------------------------------------------------------------------------
    // DataEntryForm
    // -------------------------------------------------------------------------

    /**
     * Adds a DataEntryForm.
     * 
     * @param dataEntryForm The DataEntryForm to add.
     * @return The generated unique identifier for this DataEntryForm.
     */
    long addDataEntryForm(DataEntryForm dataEntryForm);

    /**
     * Updates a DataEntryForm.
     * 
     * @param dataEntryForm The DataEntryForm to update.
     */
    void updateDataEntryForm(DataEntryForm dataEntryForm);

    /**
     * Deletes a DataEntryForm.
     * 
     * @param dataEntryForm The DataEntryForm to delete.
     */
    void deleteDataEntryForm(DataEntryForm dataEntryForm);

    /**
     * Get a DataEntryForm
     * 
     * @param id The unique identifier for the DataEntryForm to get.
     * @return The DataEntryForm with the given id or null if it does not exist.
     */
    DataEntryForm getDataEntryForm(long id);

    /**
     * Returns a DataEntryForm with the given name.
     * 
     * @param name The name.
     * @return A DataEntryForm with the given name.
     */
    DataEntryForm getDataEntryFormByName(String name);
    
    /**
     * Get all DataEntryForms.
     * 
     * @return A collection containing all DataEntryForms.
     */
    List<DataEntryForm> getAllDataEntryForms();    
    
    /**
     * Prepare DataEntryForm code for save by reversing the effects of
     * prepareDataEntryFormForEdit().
     * 
     * @return htmlCode the HTML code of the data entry form.
     */
    String prepareDataEntryFormForSave(String htmlCode);

    /**
     * Prepares the data entry form code by injecting the data element operand
     * name as value and title for each entry field.
     * 
     * @param dataEntryForm the data entry form.
     * @param dataSet the data set associated with this form.
     * @param i18n the i18n object.
     * @return HTML code for the data entry form.
     */
    String prepareDataEntryFormForEdit(DataEntryForm dataEntryForm, DataSet dataSet, I18n i18n);
    
    /**
     * Prepares the data entry form for data entry by injecting required javascripts
     * and drop down lists.
     * 
     * @param dataEntryForm the data entry form.
     * @param dataSet the data set associated with this form.
     * @param i18n the i18n object.
     * @return HTML code for the form.
     */
    String prepareDataEntryFormForEntry(DataEntryForm dataEntryForm, DataSet dataSet, I18n i18n);
    
    Set<DataElement> getDataElementsInDataEntryForm(DataSet dataSet);
}
