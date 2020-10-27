package com.mass3d.fileresource.hibernate;

import com.google.common.collect.ImmutableSet;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceDomain;
import com.mass3d.fileresource.FileResourceStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import java.util.List;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("com.mass3d.fileresource.FileResourceStore")
public class HibernateFileResourceStore
    extends HibernateIdentifiableObjectStore<FileResource>
    implements FileResourceStore {

  private static final Set<String> IMAGE_CONTENT_TYPES = new ImmutableSet.Builder<String>()
      .add("image/jpg")
      .add("image/png")
      .add("image/jpeg")
      .build();

  public HibernateFileResourceStore(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher, CurrentUserService currentUserService,
      AclService aclService) {
    super(sessionFactory, jdbcTemplate, publisher, FileResource.class, currentUserService,
        aclService, false);
  }

  @Override
  public List<FileResource> getExpiredFileResources(DateTime expires) {
    List<FileResource> results = getSession()
        .createNativeQuery("select fr.* " +
            "from fileresource fr " +
            "inner join (select dva.value " +
            "from datavalueaudit dva " +
            "where dva.created < :date " +
            "and dva.audittype in ('DELETE', 'UPDATE') " +
            "and dva.dataelementid in " +
            "(select dataelementid from dataelement where valuetype = 'FILE_RESOURCE')) dva " +
            "on dva.value = fr.uid " +
            "where fr.isassigned = true; ", FileResource.class)
        .setParameter("date", expires.toDate())
        .getResultList();

    return results;
  }

  @Override
  public List<FileResource> getAllUnProcessedImages() {
    return getQuery(
        "FROM FileResource fr WHERE fr.domain IN ( :domains ) AND fr.contentType IN ( :contentTypes ) AND hasMultipleStorageFiles = :hasMultipleStorageFiles")
        .setParameter("domains", FileResourceDomain.getDomainForMultipleImages())
        .setParameter("contentTypes", IMAGE_CONTENT_TYPES)
        .setParameter("hasMultipleStorageFiles", false)
        .setMaxResults(50).getResultList();
  }
}
