package com.mass3d.activity;

import com.mass3d.user.User;
import java.util.Collection;
import java.util.List;

public interface ActivityService {

  String ID = ActivityService.class.getName();

  // -------------------------------------------------------------------------
  // activity
  // -------------------------------------------------------------------------

  /**
   * Adds an activity.
   *
   * @param activity The activity to add.
   * @return The generated unique identifier for this activity.
   */
  long addActivity(Activity activity);

  /**
   * Updates an activity.
   *
   * @param activity The activity to update.
   */
  void updateActivity(Activity activity);

  /**
   * Deletes an activity.
   *
   * @param activity The activity to delete.
   */
  void deleteActivity(Activity activity);

  /**
   * Get an activity
   *
   * @param id The unique identifier for the activity to get.
   * @return The activity with the given id or null if it does not exist.
   */
  Activity getActivity(Long id);

  /**
   * Returns the activity with the given UID.
   *
   * @param uid the UID.
   * @return the activity with the given UID, or null if no match.
   */
  Activity getActivity(String uid);

  /**
   * Returns the activity with the given code.
   *
   * @param code the code.
   * @return the activity with the given code, or null if no match.
   */
  Activity getActivityByCode(String code);

  /**
   * Returns the activity with the given UID. Bypasses the ACL system.
   *
   * @param uid the UID.
   * @return the activity with the given UID, or null if no match.
   */
  Activity getActivityNoAcl(String uid);

//  /**
//   * Returns all Activitys associated with the given DataEntryForm.
//   *
//   * @param dataEntryForm the DataEntryForm.
//   * @return a list of Activitys.
//   */
//  List<activity> getActivitysByDataEntryForm( DataEntryForm dataEntryForm );

  /**
   * Get all Activitys.
   *
   * @return A list containing all Activitys.
   */
  List<Activity> getAllActivitys();
//
//  /**
//   * Gets all Activitys associated with the given PeriodType.
//   *
//   * @param periodType the PeriodType.
//   * @return a list of Activitys.
//   */
//  List<activity> getActivitysByPeriodType( PeriodType periodType );

  /**
   * Returns a list of data sets with the given uids.
   *
   * @param uids the collection of uids.
   * @return a list of data sets.
   */
  List<Activity> getActivitysByUid(Collection<String> uids);
//
//  /**
//   * Returns all Activitys that can be collected through mobile (one organisation unit).
//   */
//  List<activity> getActivitysForMobile(activity source);

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

  /**
   * Returns the data sets associated with the current user. If the current user has the ALL
   * authority then all data sets are returned.
   */
//  List<Activity> getCurrentUserActivitys();

  /**
   * Returns the Activities which current user have READ access. If the current user has the ALL
   * authority then all Activities are returned.
   */
  List<Activity> getAllDataRead();

  /**
   * Returns the Activities which given user have READ access. If the current user has the ALL
   * authority then all Activities are returned.
   *
   * @param user the user to query for field set list.
   * @return a list of Activities which the given user has data read access to.
   */
  List<Activity> getUserDataRead(User user);

  /**
   * Returns the Activities which current user have WRITE access. If the current user has the ALL
   * authority then all Activities are returned.
   */
  List<Activity> getAllDataWrite();

  /**
   * Returns the Activities which current user have WRITE access. If the current user has the ALL
   * authority then all Activities are returned.
   *
   * @param user the user to query for field set list.
   * @return a list of Activities which given user has data write access to.
   */
  List<Activity> getUserDataWrite(User user);

}
