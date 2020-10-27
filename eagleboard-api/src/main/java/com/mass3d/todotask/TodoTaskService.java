package com.mass3d.todotask;

import com.mass3d.user.User;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TodoTaskService {

  String ID = TodoTaskService.class.getName();

  // -------------------------------------------------------------------------
  // TodoTask
  // -------------------------------------------------------------------------

  /**
   * Adds a TodoTask.
   *
   * @param todoTask The TodoTask to add.
   * @return The generated unique identifier for this TodoTask.
   */
  Long addTodoTask(TodoTask todoTask);

  /**
   * Updates a TodoTask.
   *
   * @param todoTask The TodoTask to update.
   */
  void updateTodoTask(TodoTask todoTask);

  /**
   * Deletes a TodoTask.
   *
   * @param todoTask The TodoTask to delete.
   */
  void deleteTodoTask(TodoTask todoTask);

  /**
   * Get a TodoTask
   *
   * @param id The unique identifier for the TodoTask to get.
   * @return The TodoTask with the given id or null if it does not exist.
   */
  TodoTask getTodoTask(Long id);

  /**
   * Returns the TodoTask with the given UID.
   *
   * @param uid the UID.
   * @return the TodoTask with the given UID, or null if no match.
   */
  TodoTask getTodoTask(String uid);

  /**
   * Returns the TodoTask with the given UID. Bypasses the ACL system.
   *
   * @param uid the UID.
   * @return the TodoTask with the given UID, or null if no match.
   */
  TodoTask getTodoTaskNoAcl(String uid);

//  /**
//   * Returns all TodoTasks associated with the given DataEntryForm.
//   *
//   * @param dataEntryForm the DataEntryForm.
//   * @return a list of TodoTasks.
//   */
//  List<TodoTask> getTodoTasksByDataEntryForm( DataEntryForm dataEntryForm );

  /**
   * Get all TodoTasks.
   *
   * @return A list containing all TodoTasks.
   */
  List<TodoTask> getAllTodoTasks();
//
//  /**
//   * Gets all TodoTasks associated with the given PeriodType.
//   *
//   * @param periodType the PeriodType.
//   * @return a list of TodoTasks.
//   */
//  List<TodoTask> getTodoTasksByPeriodType( PeriodType periodType );

  /**
   * Returns a list of todoTasks with the given uids.
   *
   * @param uids the collection of uids.
   * @return a list of todoTasks.
   */
  List<TodoTask> getTodoTasksByUid(Collection<String> uids);


  /**
   * Returns all TodoTasks which are not assigned to any activity.
   *
   * @return all TodoTasks which are not assigned to any activity.
   */
  List<TodoTask> getTodoTasksWithoutActivities();

  /**
   * Returns all TodoTasks which are assigned to at least one activity.
   *
   * @return all TodoTasks which are assigned to at least one activity.
   */
  List<TodoTask> getTodoTasksWithActivities();

//  /**
//   * Returns the todoTasks associated with the current user. If the current user has the ALL
//   * authority then all todoTasks are returned.
//   */
//  List<TodoTask> getCurrentUserTodoTasks();

  /**
   * Returns all TodoTasks by lastUpdated.
   *
   * @param lastUpdated TodoTasks from this date
   * @return a list of all TodoTasks, or an empty list if there are no TodoTasks.
   */
  List<TodoTask> getAllTodoTasksByLastUpdated(Date lastUpdated);

//  /**
//   * Returns a list of TodoTasks based on the given params.
//   *
//   * @param params the params.
//   * @return a list of TodoTasks.
//   */
//  List<TodoTask> getTodoTasks( TodoTaskQueryParams params );

  /**
   * Creates a mapping between todoTask UID and set of field set UIDs being assigned to the
   * todoTask.
   *
   * @return a map of sets.
   */
  Map<String, Set<String>> getTodoTaskDataSetAssociationMap();

  /**
   * Returns the todoTasks which current user have READ access. If the current user has the ALL
   * authority then all todoTasks are returned.
   */
  List<TodoTask> getAllDataRead();

  /**
   * Returns the todoTasks which given user have READ access. If the current user has the ALL
   * authority then all todoTasks are returned.
   *
   * @param user the user to query for field set list.
   * @return a list of todoTasks which the given user has data read access to.
   */
  List<TodoTask> getUserDataRead(User user);

  /**
   * Returns the todoTasks which current user have WRITE access. If the current user has the ALL
   * authority then all todoTasks are returned.
   */
  List<TodoTask> getAllDataWrite();

  /**
   * Returns the todoTasks which current user have WRITE access. If the current user has the ALL
   * authority then all todoTasks are returned.
   *
   * @param user the user to query for field set list.
   * @return a list of todoTasks which given user has data write access to.
   */
  List<TodoTask> getUserDataWrite(User user);
  // -------------------------------------------------------------------------
  // TodoTask LockExceptions
  // -------------------------------------------------------------------------

//  /**
//   * Add new lock exception
//   *
//   * @param lockException LockException instance to add
//   * @return Database ID of LockException
//   */
//  int addLockException( LockException lockException );
//
//  /**
//   * Update lock exception
//   *
//   * @param lockException LockException instance to update
//   */
//  void updateLockException( LockException lockException );
//
//  /**
//   * Delete lock exception
//   *
//   * @param lockException LockException instance to delete
//   */
//  void deleteLockException( LockException lockException );
//
//  /**
//   * Get LockException by ID
//   *
//   * @param id ID of LockException to get
//   * @return LockException with given ID, or null if not found
//   */
//  LockException getLockException( int id );
//
//  /**
//   * Get number of LockExceptions in total
//   *
//   * @return Total count of LockExceptions
//   */
//  int getLockExceptionCount();
//
//  /**
//   * Returns all lock exceptions
//   *
//   * @return List of all the lock exceptions
//   */
//  List<LockException> getAllLockExceptions();
//
//  /**
//   * Get all LockExceptions within a specific range
//   *
//   * @param first Index to start at
//   * @param max   Number of results wanted
//   * @return List of LockExceptions withing the range specified
//   */
//  List<LockException> getLockExceptionsBetween( int first, int max );
//
//  /**
//   * Find all unique todoTask + period combinations
//   * (mainly used for batch removal)
//   *
//   * @return A list of all unique combinations (only todoTask and period is set)
//   */
//  List<LockException> getLockExceptionCombinations();

//  /**
//   * Delete a todoTask + period combination, used for batch removal, e.g. when you
//   * have a lock exception set on 100 OUs with the same todoTask + period combination.
//   *
//   * @param todoTask TodoTask part of the combination
//   * @param period  Period part of the combination
//   */
//  void deleteLockExceptionCombination( TodoTask todoTask, Period period );
//
//  /**
//   * Delete a todoTask + period + organisationUnit combination
//   *
//   * @param todoTask          TodoTask part of the combination
//   * @param period           Period part of the combination
//   * @param organisationUnit OrganisationUnit part of the combination
//   */
//  void deleteLockExceptionCombination( TodoTask todoTask, Period period, OrganisationUnit organisationUnit );
//
//  /**
//   * Checks whether the period is locked for data entry for the given input,
//   * checking the dataset's expiryDays and lockExceptions.
//   *
//   * @param todoTask          the data set
//   * @param period           the period.
//   * @param organisationUnit the organisation unit.
//   * @param now              the base date for deciding locked date, current date if null.
//   * @return true or false indicating whether the system is locked or not.
//   */
//  boolean isLocked( TodoTask todoTask, Period period, OrganisationUnit organisationUnit, Date now );
//
//  /**
//   * Checks whether the system is locked for data entry for the given input,
//   * checking expiryDays, lockExceptions and approvals.
//   *
//   * @param todoTask              the data set
//   * @param period               the period.
//   * @param organisationUnit     the organisation unit.
//   * @param attributeOptionCombo the attribute option combo.
//   * @param now                  the base date for deciding locked date, current date if null.
//   * @return true or false indicating whether the system is locked or not.
//   */
//  boolean isLocked( TodoTask todoTask, Period period, OrganisationUnit organisationUnit, DataElementCategoryOptionCombo attributeOptionCombo, Date now );
//
//  /**
//   * Checks whether the system is locked for data entry for the given input,
//   * checking expiryDays, lockExceptions and approvals.
//   *
//   * @param todoTask              the data set
//   * @param period               the period.
//   * @param organisationUnit     the organisation unit.
//   * @param attributeOptionCombo the attribute option combo.
//   * @param now                  the base date for deciding locked date, current date if null.
//   * @param useOrgUnitChildren   whether to check children of the given org unit or the org unit only.
//   * @return true or false indicating whether the system is locked or not.
//   */
//  boolean isLocked( TodoTask todoTask, Period period, OrganisationUnit organisationUnit, DataElementCategoryOptionCombo attributeOptionCombo, Date now, boolean useOrgUnitChildren );
//
//  /**
//   * Checks whether the system is locked for data entry for the given input,
//   * checking expiryDays, lockExceptions and approvals.
//   *
//   * @param dataElement          the data element.
//   * @param period               the period.
//   * @param organisationUnit     the organisation unit.
//   * @param attributeOptionCombo the attribute option combo.
//   * @param now                  the base date for deciding locked date, current date if null.
//   * @return true or false indicating whether the system is locked or not.
//   */
//  boolean isLocked( DataElement dataElement, Period period, OrganisationUnit organisationUnit, DataElementCategoryOptionCombo attributeOptionCombo, Date now );
//
//  /**
//   * Return a list of LockException with given filter list
//   *
//   * @param filters
//   * @return a list of LockException with given filter list
//   */
//  List<LockException> filterLockExceptions( List<String> filters );
}
