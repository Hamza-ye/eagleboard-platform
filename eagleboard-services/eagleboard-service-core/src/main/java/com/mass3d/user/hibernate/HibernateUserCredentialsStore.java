package com.mass3d.user.hibernate;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserCredentialsStore;
import java.util.UUID;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("com.mass3d.user.UserCredentialsStore")
public class HibernateUserCredentialsStore
    extends HibernateIdentifiableObjectStore<UserCredentials>
    implements UserCredentialsStore {

  public HibernateUserCredentialsStore(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher, CurrentUserService currentUserService,
      AclService aclService) {
    super(sessionFactory, jdbcTemplate, publisher, UserCredentials.class, currentUserService,
        aclService, true);
  }

  @Override
  public UserCredentials getUserCredentialsByUsername(String username) {
    Query<UserCredentials> query = getQuery(
        "from UserCredentials uc where uc.username = :username");
    query.setParameter("username", username);
    return query.uniqueResult();
  }

  @Override
  public UserCredentials getUserCredentialsByOpenId(String openId) {
    Query<UserCredentials> query = getQuery("from UserCredentials uc where uc.openId = :openId");
    query.setParameter("openId", openId);
    return query.uniqueResult();
  }

  @Override
  public UserCredentials getUserCredentialsByLdapId(String ldapId) {
    Query<UserCredentials> query = getQuery("from UserCredentials uc where uc.ldapId = :ldapId");
    query.setParameter("ldapId", ldapId);
    return query.uniqueResult();
  }

  @Override
  public UserCredentials getUserCredentialsByUuid(UUID uuid) {
    Query<UserCredentials> query = getQuery("from UserCredentials uc where uc.uuid = :uuid");
    query.setParameter("uuid", uuid);
    return query.uniqueResult();
  }
}
