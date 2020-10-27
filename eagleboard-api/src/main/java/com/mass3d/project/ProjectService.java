package com.mass3d.project;

//import com.eagleboard.project.project;

import com.mass3d.user.User;
import java.util.Collection;
import java.util.List;

public interface ProjectService {

  String ID = ProjectService.class.getName();

  // -------------------------------------------------------------------------
  // project
  // -------------------------------------------------------------------------

  /**
   * Adds a project.
   *
   * @param project The project to add.
   * @return The generated unique identifier for this project.
   */
  Long addProject(Project project);

  /**
   * Updates a project.
   *
   * @param project The project to update.
   */
  void updateProject(Project project);

  /**
   * Deletes a project.
   *
   * @param project The project to delete.
   */
  void deleteProject(Project project);

  /**
   * Get a project
   *
   * @param id The unique identifier for the project to get.
   * @return The project with the given id or null if it does not exist.
   */
  Project getProject(Long id);

  /**
   * Returns the project with the given UID.
   *
   * @param uid the UID.
   * @return the project with the given UID, or null if no match.
   */
  Project getProject(String uid);

  /**
   * Returns the project with the given UID. Bypasses the ACL system.
   *
   * @param uid the UID.
   * @return the project with the given UID, or null if no match.
   */
  Project getProjectNoAcl(String uid);

  /**
   * Get all Projects.
   *
   * @return A list containing all Projects.
   */
  List<Project> getAllProjects();

  /**
   * Returns a list of data sets with the given uids.
   *
   * @param uids the collection of uids.
   * @return a list of data sets.
   */
  List<Project> getProjectsByUid(Collection<String> uids);


  /**
   * Returns the projects which current user have READ access. If the current user has the ALL
   * authority then all projects are returned.
   */
  List<Project> getAllDataRead();

  /**
   * Returns the projects which given user have READ access. If the current user has the ALL
   * authority then all projects are returned.
   *
   * @param user the user to query for field set list.
   * @return a list of projects which the given user has data read access to.
   */
  List<Project> getUserDataRead(User user);

  /**
   * Returns the projects which current user have WRITE access. If the current user has the ALL
   * authority then all projects are returned.
   */
  List<Project> getAllDataWrite();

  /**
   * Returns the projects which current user have WRITE access. If the current user has the ALL
   * authority then all projects are returned.
   *
   * @param user the user to query for field set list.
   * @return a list of projects which given user has data write access to.
   */
  List<Project> getUserDataWrite(User user);
//  /**
//   * Returns all Projects that can be collected through mobile (one organisation unit).
//   */
//  List<project> getProjectsForMobile(project source);

//  /**
//   * Returns the data sets associated with the current user. If the current user has the ALL
//   * authority then all data sets are returned.
//   */
//  List<project> getCurrentUserProjects();

}
