package com.mass3d.activity;

import com.mass3d.common.IdentifiableObjectStore;
import java.util.List;

public interface ActivityStore
    extends IdentifiableObjectStore<Activity> {

  String ID = ActivityStore.class.getName();

//  List<activity> getTodoTasksForMobile(TodoTask source);

  /**
   * Returns all Activities which are not assigned to any project.
   *
   * @return all Activities which are not assigned to any project.
   */
  List<Activity> getActivitysWithoutProjects();

  /**
   * Returns all Activities which are assigned to at least one project.
   *
   * @return all Activities which are assigned to at least one project.
   */
  List<Activity> getActivitysWithProjects();
}
