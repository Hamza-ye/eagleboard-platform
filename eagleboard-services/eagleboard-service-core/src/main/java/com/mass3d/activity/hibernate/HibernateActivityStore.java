package com.mass3d.activity.hibernate;

import com.mass3d.activity.Activity;
import com.mass3d.activity.ActivityStore;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.deletedobject.DeletedObjectService;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("com.mass3d.activity.ActivityStore")
public class HibernateActivityStore
    extends HibernateIdentifiableObjectStore<Activity>
    implements ActivityStore {

  // -------------------------------------------------------------------------
  // activity
  // -------------------------------------------------------------------------
  public HibernateActivityStore(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher, CurrentUserService currentUserService,
      AclService aclService) {
    super(sessionFactory, jdbcTemplate, publisher, Activity.class, currentUserService,
        aclService, false);
  }

  @Override
  public List<Activity> getActivitysWithoutProjects() {
    String hql = "from activity d where size(d.projects) = 0";

    return getQuery(hql).setCacheable(true).list();
  }

  @Override
  public List<Activity> getActivitysWithProjects() {
    String hql = "from activity d where size(d.projects) > 0";

    return getQuery(hql).setCacheable(true).list();
  }
}
