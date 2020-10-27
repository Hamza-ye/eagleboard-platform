package com.mass3d.project;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.mass3d.project.Project;
import com.mass3d.project.ProjectService;
import com.mass3d.project.ProjectStore;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("com.mass3d.project.ProjectService")
@Transactional
public class DefaultProjectService
    implements ProjectService {

  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------
  private ProjectStore projectStore;
  private CurrentUserService currentUserService;

  public DefaultProjectService(ProjectStore projectStore,
      CurrentUserService currentUserService) {

    checkNotNull( projectStore  );
    checkNotNull( currentUserService  );

    this.projectStore = projectStore;
    this.currentUserService = currentUserService;
  }

//
//  @Autowired
//  public void setProjectStore(ProjectStore projectStore) {
//    this.projectStore = projectStore;
//  }
//
//  @Autowired
//  public void setCurrentUserService(CurrentUserService currentUserService) {
//    this.currentUserService = currentUserService;
//  }

  // -------------------------------------------------------------------------
  // project
  // -------------------------------------------------------------------------

  @Override
  public Long addProject(Project fieldSet) {
    projectStore.save(fieldSet);
    return fieldSet.getId();
  }

  @Override
  public void updateProject(Project fieldSet) {
    projectStore.update(fieldSet);
  }

  @Override
  public void deleteProject(Project fieldSet) {
    projectStore.delete(fieldSet);
  }

  @Override
  public Project getProject(Long id) {
    return projectStore.get(id);
  }

  @Override
  public Project getProject(String uid) {
    return projectStore.getByUid(uid);
  }

  @Override
  public Project getProjectNoAcl(String uid) {
    return projectStore.getByUidNoAcl(uid);
  }

  @Override
  public List<Project> getAllProjects() {
    return projectStore.getAll();
  }

  @Override
  public List<Project> getProjectsByUid(Collection<String> uids) {
    return projectStore.getByUid(uids);
  }

  @Override
  public List<Project> getUserDataRead(User user) {
    if (user == null) {
      return Lists.newArrayList();
    }

    return user.isSuper() ? getAllProjects() : projectStore.getDataReadAll(user);
  }

  @Override
  public List<Project> getAllDataRead() {
    User user = currentUserService.getCurrentUser();

    return getUserDataRead(user);
  }

  @Override
  public List<Project> getAllDataWrite() {
    User user = currentUserService.getCurrentUser();

    return getUserDataWrite(user);
  }

  @Override
  public List<Project> getUserDataWrite(User user) {
    if (user == null) {
      return Lists.newArrayList();
    }

    return user.isSuper() ? getAllProjects() : projectStore.getDataWriteAll(user);
  }
//
//  @Override
//  public List<project> getCurrentUserProjects() {
//    User user = currentUserService.getCurrentUser();
//
//    if (user == null) {
//      return Lists.newArrayList();
//    }
//
//    if (user.isSuper()) {
//      return getAllProjects();
//    } else {
//      return Lists.newArrayList(user.getUserCredentials().getAllProjects());
//    }
//  }


}
