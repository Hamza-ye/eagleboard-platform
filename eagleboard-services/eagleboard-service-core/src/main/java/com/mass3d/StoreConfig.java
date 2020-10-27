package com.mass3d;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.deletedobject.DeletedObjectService;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserGroupAccess;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

//@Configuration( "coreStoreConfig" )
public class StoreConfig {

//  @Autowired
//  private SessionFactory sessionFactory;
//
//  @Autowired
//  private JdbcTemplate jdbcTemplate;
//
//  @Autowired
//  private DeletedObjectService deletedObjectService;
//
//  @Autowired
//  private CurrentUserService currentUserService;
//
//  @Autowired
//  private AclService aclService;
//
//  @Bean( "com.mass3d.user.UserGroupStore" )
//  public HibernateIdentifiableObjectStore<UserGroup> userGroupStore()
//  {
//    return new HibernateIdentifiableObjectStore<>( sessionFactory,
//        jdbcTemplate, deletedObjectService, UserGroup.class, currentUserService, aclService, true );
//  }
//
//  @Bean( "com.mass3d.user.UserGroupAccessStore" )
//  public HibernateGenericStore<UserGroupAccess> userGroupAccessStore()
//  {
//    return new HibernateGenericStore<UserGroupAccess>( sessionFactory, jdbcTemplate,
//        UserGroupAccess.class, true );
//  }
//
//  @Bean( "com.mass3d.user.UserAccessStore" )
//  public HibernateGenericStore<UserAccess> userAccessStore()
//  {
//    return new HibernateGenericStore<UserAccess>( sessionFactory, jdbcTemplate,
//        UserAccess.class, true );
//  }
}
