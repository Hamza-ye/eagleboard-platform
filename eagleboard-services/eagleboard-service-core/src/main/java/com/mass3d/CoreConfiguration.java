package com.mass3d;

import com.mass3d.common.GenericStore;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserGroupAccess;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@Configuration
//@ComponentScan(basePackages = {"com.mass3d"})
//@ImportResource({"classpath*:META-INF/mass3d/beans.xml"})
public class CoreConfiguration {

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Bean
//  HibernateGenericStore<UserAccess> userAccessStore(SessionFactory sessionFactory) {
//    HibernateGenericStore<UserAccess> hibernateGenericStore = new HibernateGenericStore<UserAccess>();
//    hibernateGenericStore.setClazz(UserAccess.class);
//    hibernateGenericStore.setSessionFactory(sessionFactory);
//    return hibernateGenericStore;
//  }
//
//  @Bean
//  HibernateGenericStore<UserGroupAccess> userGroupAccessStore(SessionFactory sessionFactory) {
//    HibernateGenericStore<UserGroupAccess> hibernateGenericStore = new HibernateGenericStore<UserGroupAccess>();
//    hibernateGenericStore.setClazz(UserGroupAccess.class);
//    hibernateGenericStore.setSessionFactory(sessionFactory);
//    return hibernateGenericStore;
//  }
//
//  @Bean
//  IdentifiableObjectStore<UserGroup> userGroupStore(SessionFactory sessionFactory) {
//    HibernateIdentifiableObjectStore<UserGroup> hibernateIdentifiableObjectStore = new HibernateIdentifiableObjectStore<UserGroup>();
//    hibernateIdentifiableObjectStore.setClazz(UserGroup.class);
//    hibernateIdentifiableObjectStore.setSessionFactory(sessionFactory);
//    return hibernateIdentifiableObjectStore;
//  }
}
