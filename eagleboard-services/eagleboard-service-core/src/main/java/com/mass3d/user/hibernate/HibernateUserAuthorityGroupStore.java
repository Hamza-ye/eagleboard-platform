package com.mass3d.user.hibernate;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataset.DataSet;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserAuthorityGroupStore;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("com.mass3d.user.UserAuthorityGroupStore")
public class HibernateUserAuthorityGroupStore
    extends HibernateIdentifiableObjectStore<UserAuthorityGroup>
    implements UserAuthorityGroupStore {

  public HibernateUserAuthorityGroupStore(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher, CurrentUserService currentUserService,
      AclService aclService) {
    super(sessionFactory, jdbcTemplate, publisher, UserAuthorityGroup.class, currentUserService,
        aclService, true);
  }

  @Override
  public int countDataSetUserAuthorityGroups(DataSet dataSet) {
    Query<Long> query = getTypedQuery(
        "select count(distinct c) from UserAuthorityGroup c where :dataSet in elements(c.dataSets)");
    query.setParameter("dataSet", dataSet);

    return query.getSingleResult().intValue();
  }
}
