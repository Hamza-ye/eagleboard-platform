package com.mass3d.dataset;

import com.mass3d.period.PeriodType;
import com.mass3d.todotask.TodoTask;
import com.mass3d.user.User;
import java.util.Collection;
import java.util.List;

public interface DataSetService {

  String ID = DataSetService.class.getName();

  // -------------------------------------------------------------------------
  // DataSet
  // -------------------------------------------------------------------------

  /**
   * Adds a DataSet.
   *
   * @param dataSet The DataSet to add.
   * @return The generated unique identifier for this DataSet.
   */
  long addDataSet(DataSet dataSet);

  /**
   * Updates a DataSet.
   *
   * @param dataSet The DataSet to update.
   */
  void updateDataSet(DataSet dataSet);

  /**
   * Deletes a DataSet.
   *
   * @param dataSet The DataSet to delete.
   */
  void deleteDataSet(DataSet dataSet);

  /**
   * Get a DataSet
   *
   * @param id The unique identifier for the DataSet to get.
   * @return The DataSet with the given id or null if it does not exist.
   */
  DataSet getDataSet(long id);

  /**
   * Returns the DataSet with the given UID.
   *
   * @param uid the UID.
   * @return the DataSet with the given UID, or null if no match.
   */
  DataSet getDataSet(String uid);

  /**
   * Returns the DataSet with the given UID. Bypasses the ACL system.
   *
   * @param uid the UID.
   * @return the DataSet with the given UID, or null if no match.
   */
  DataSet getDataSetNoAcl(String uid);

  /**
   * Get all DataSets.
   *
   * @return A list containing all DataSets.
   */
  List<DataSet> getAllDataSets();

  /**
   * Gets all DataSets associated with the given PeriodType.
   *
   * @param periodType the PeriodType.
   * @return a list of DataSets.
   */
  List<DataSet> getDataSetsByPeriodType( PeriodType periodType );

  /**
   * Returns a list of data sets with the given uids.
   *
   * @param uids the collection of uids.
   * @return a list of data sets.
   */
  List<DataSet> getDataSetsByUid(Collection<String> uids);

  /**
   * Returns all DataSets that can be collected through mobile (one organisation unit).
   */
  List<DataSet> getDataSetsForMobile(TodoTask source);
  /**
   * Returns all DataSets which are not assigned to any DataSets.
   *
   * @return all DataSets which are not assigned to any DataSets.
   */
  List<DataSet> getDataSetsWithoutTodoTasks();

  /**
   * Returns all DataSets which are assigned to at least one DataSet.
   *
   * @return all DataSets which are assigned to at least one DataSet.
   */
  List<DataSet> getDataSetsWithTodoTasks();
  /**
   * Returns the data sets which given user have READ access. If the current user has the ALL
   * authority then all data sets are returned.
   *
   * @param user the user to query for data set list.
   * @return a list of data sets which the given user has data read access to.
   */
  List<DataSet> getUserDataRead(User user);

  /**
   * Returns the data sets which current user have READ access. If the current user has the ALL
   * authority then all data sets are returned.
   */
  List<DataSet> getAllDataRead();

  /**
   * Returns the data sets which current user have WRITE access. If the current user has the ALL
   * authority then all data sets are returned.
   */
  List<DataSet> getAllDataWrite();

  /**
   * Returns the data sets which current user have WRITE access. If the current user has the ALL
   * authority then all data sets are returned.
   *
   * @param user the user to query for data set list.
   * @return a list of data sets which given user has data write access to.
   */
  List<DataSet> getUserDataWrite(User user);
}
