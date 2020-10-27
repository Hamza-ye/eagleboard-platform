package com.mass3d.userkeyjsonvalue.hibernate;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.userkeyjsonvalue.UserKeyJsonValue;
import com.mass3d.userkeyjsonvalue.UserKeyJsonValueStore;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("com.mass3d.userkeyjsonvalue.UserKeyJsonValueStore")
public class HibernateUserKeyJsonValueStore
    extends HibernateIdentifiableObjectStore<UserKeyJsonValue>
    implements UserKeyJsonValueStore {

  public HibernateUserKeyJsonValueStore(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher, CurrentUserService currentUserService,
      AclService aclService) {
    super(sessionFactory, jdbcTemplate, publisher, UserKeyJsonValue.class, currentUserService,
        aclService, true);
  }

  @Override
  public UserKeyJsonValue getUserKeyJsonValue(User user, String namespace, String key) {
    CriteriaBuilder builder = getCriteriaBuilder();

    return getSingleResult(builder, newJpaParameters()
        .addPredicate(root -> builder.equal(root.get("user"), user))
        .addPredicate(root -> builder.equal(root.get("namespace"), namespace))
        .addPredicate(root -> builder.equal(root.get("key"), key)));
  }

  @Override
  public List<String> getNamespacesByUser(User user) {
    CriteriaBuilder builder = getCriteriaBuilder();

    return getList(builder, newJpaParameters()
        .addPredicate(root -> builder.equal(root.get("user"), user)))
        .stream().map(UserKeyJsonValue::getNamespace).distinct().collect(Collectors.toList());
  }

  @Override
  public List<String> getKeysByUserAndNamespace(User user, String namespace) {
    return (getUserKeyJsonValueByUserAndNamespace(user, namespace)).stream()
        .map(UserKeyJsonValue::getKey)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserKeyJsonValue> getUserKeyJsonValueByUserAndNamespace(User user, String namespace) {
    CriteriaBuilder builder = getCriteriaBuilder();

    return getList(builder, newJpaParameters()
        .addPredicate(root -> builder.equal(root.get("user"), user))
        .addPredicate(root -> builder.equal(root.get("namespace"), namespace)));
  }
}
