package com.mass3d.project.hibernate;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.deletedobject.DeletedObjectService;
import com.mass3d.project.Project;
import com.mass3d.project.ProjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("com.mass3d.project.ProjectStore")
public class HibernateProjectStore
    extends HibernateIdentifiableObjectStore<Project>
    implements ProjectStore {

  public HibernateProjectStore(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher, CurrentUserService currentUserService,
      AclService aclService) {
    super(sessionFactory, jdbcTemplate, publisher, Project.class, currentUserService,
        aclService, false);
  }

}
