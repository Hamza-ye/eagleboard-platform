package com.mass3d.datavalue;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.mass3d.dataelement.DataElement;
import com.mass3d.todotask.TodoTask;
import com.mass3d.period.Period;

/**
 * Defines the functionality for persisting DataValues.
 *
 * @version $Id: DataValueStore.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public interface DataValueStore {

  String ID = DataValueStore.class.getName();

  // -------------------------------------------------------------------------
  // Basic DataValue
  // -------------------------------------------------------------------------

  /**
   * Adds a DataValue.
   *
   * @param dataValue the DataValue to add.
   */
  void addDataValue(DataValue dataValue);

  /**
   * Updates a DataValue.
   *
   * @param dataValue the DataValue to update.
   */
  void updateDataValue(DataValue dataValue);

  /**
   * Deletes all data values for the given organisation unit.
   *
   * @param todoTask the organisation unit.
   */
  void deleteDataValues(TodoTask todoTask);

  /**
   * Deletes all data values for the given data element.
   *
   * @param dataElement the data element.
   */
  void deleteDataValues(DataElement dataElement);

  /**
   * Returns a DataValue.
   *
   * @param dataElement the DataElement of the DataValue.
   * @param period the Period of the DataValue.
   * @param source the Source of the DataValue.
   * @return the DataValue which corresponds to the given parameters, or null if no match.
   */
  DataValue getDataValue(DataElement dataElement, Period period, TodoTask source);

  /**
   * Returns a soft deleted DataValue.
   *
   * @param dataValue the DataValue to use as parameters.
   * @return the DataValue which corresponds to the given parameters, or null if no match.
   */
  DataValue getSoftDeletedDataValue(DataValue dataValue);

  // -------------------------------------------------------------------------
  // Collections of DataValues
  // -------------------------------------------------------------------------

  /**
   * Returns data values for the given data export parameters.
   *
   * @param params the data export parameters.
   * @return a list of data values.
   */
  List<DataValue> getDataValues(DataExportParams params);

  /**
   * Returns all DataValues.
   *
   * @return a list of all DataValues.
   */
  List<DataValue> getAllDataValues();

  /**
   * Returns all DataValues for a given Source, Period, collection of DataElements and
   * CategoryOptionCombo.
   *
   * @param source the Source of the DataValues.
   * @param period the Period of the DataValues.
   * @param dataElements the DataElements of the DataValues.
   * @return a list of all DataValues which match the given Source, Period, and any of the
   * DataElements, or an empty collection if no values match.
   */
  List<DataValue> getDataValues(TodoTask source, Period period,
      Collection<DataElement> dataElements);

  /**
   * Returns deflated data values for the given data export parameters.
   *
   * @param params the data export parameters.
   * @return a list of deflated data values.
   */
  List<DeflatedDataValue> getDeflatedDataValues(DataExportParams params);

  /**
   * Gets the number of DataValues which have been updated between the given start and end date. The
   * <pre>startDate</pre> and <pre>endDate</pre> parameters can both be null but one must be
   * defined.
   *
   * @param startDate the start date to compare against data value last updated.
   * @param endDate the end date to compare against data value last updated.
   * @param includeDeleted whether to include deleted data values.
   * @return the number of DataValues.
   */
  int getDataValueCountLastUpdatedBetween(Date startDate, Date endDate, boolean includeDeleted);
}
