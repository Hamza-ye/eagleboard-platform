package com.mass3d.todotask;

import com.mass3d.common.IdentifiableObjectStore;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TodoTaskStore
    extends IdentifiableObjectStore<TodoTask> {

  String ID = TodoTaskStore.class.getName();

//  List<TodoTask> getTodoTasksForMobile(TodoTask source);

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
   * Creates a mapping between organisation unit UID and set of data set UIDs being assigned to the
   * organisation unit.
   *
   * @return a map of sets.
   */
  Map<String, Set<String>> getTodoTaskDataSetAssociationMap();

}
