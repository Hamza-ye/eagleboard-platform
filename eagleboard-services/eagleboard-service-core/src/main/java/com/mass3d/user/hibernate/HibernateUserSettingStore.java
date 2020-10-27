package com.mass3d.user.hibernate;

import com.mass3d.user.User;
import com.mass3d.user.UserSetting;
import com.mass3d.user.UserSettingStore;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("com.mass3d.user.UserSettingStore")
public class HibernateUserSettingStore
    implements UserSettingStore {

  private static final boolean CACHEABLE = true;

  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private SessionFactory sessionFactory;

  public HibernateUserSettingStore(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  // -------------------------------------------------------------------------
  // UserSettingStore implementation
  // -------------------------------------------------------------------------

  @Override
  public void addUserSetting(UserSetting userSetting) {
    Session session = sessionFactory.getCurrentSession();

    session.save(userSetting);
  }

  @Override
  public void updateUserSetting(UserSetting userSetting) {
    Session session = sessionFactory.getCurrentSession();

    session.update(userSetting);
  }

  @Override
  @Transactional
  public UserSetting getUserSettingTx(User user, String name) {
    return getUserSetting(user, name);
  }

  @Override
  @SuppressWarnings("unchecked")
  public UserSetting getUserSetting(User user, String name) {
    Session session = sessionFactory.getCurrentSession();
    Query<UserSetting> query = session
        .createQuery("from UserSetting us where us.user = :user and us.name = :name");
    query.setParameter("user", user);
    query.setParameter("name", name);
    query.setCacheable(CACHEABLE);

    return query.uniqueResult();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<UserSetting> getAllUserSettings(User user) {
    Session session = sessionFactory.getCurrentSession();
    Query<UserSetting> query = session.createQuery("from UserSetting us where us.user = :user");
    query.setParameter("user", user);
    query.setCacheable(CACHEABLE);

    return query.list();
  }

  @Override
  public void deleteUserSetting(UserSetting userSetting) {
    Session session = sessionFactory.getCurrentSession();

    session.delete(userSetting);
  }

  @Override
  public void removeUserSettings(User user) {
    Session session = sessionFactory.getCurrentSession();

    String hql = "delete from UserSetting us where us.user = :user";

    session.createQuery(hql).setParameter("user", user).executeUpdate();
  }
}
