package com.mass3d.fileresource.hibernate;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.fileresource.ExternalFileResource;
import com.mass3d.fileresource.ExternalFileResourceStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("com.mass3d.fileresource.ExternalFileResourceStore")
public class HibernateExternalFileResourceStore
    extends HibernateIdentifiableObjectStore<ExternalFileResource>
    implements ExternalFileResourceStore {

  public HibernateExternalFileResourceStore(SessionFactory sessionFactory,
      JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher, CurrentUserService currentUserService,
      AclService aclService) {
    super(sessionFactory, jdbcTemplate, publisher, ExternalFileResource.class, currentUserService,
        aclService, false);
  }

  @Override
  public ExternalFileResource getExternalFileResourceByAccessToken(String accessToken) {
    return getQuery("from ExternalFileResource where accessToken = :accessToken")
        .setParameter("accessToken", accessToken).uniqueResult();
  }
}
