package com.mass3d.document.impl;

import java.io.File;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mass3d.document.Document;
import com.mass3d.document.DocumentService;
import com.mass3d.document.DocumentStore;
import com.mass3d.external.location.LocationManager;
import com.mass3d.external.location.LocationManagerException;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceService;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version $Id$
 */
@Service( "com.mass3d.document.DocumentService" )
@Transactional
public class DefaultDocumentService
    implements DocumentService {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog(DefaultDocumentService.class);
  @Autowired
  private FileResourceService fileResourceService;
  @Autowired
  private LocationManager locationManager;
  @Autowired
  private DocumentStore documentStore;

  // -------------------------------------------------------------------------
  // DocumentService implementation
  // -------------------------------------------------------------------------

  @Override
  public long saveDocument(Document document) {
    documentStore.save(document);

    return document.getId();
  }

  @Override
  public Document getDocument(long id) {
    return documentStore.get(id);
  }

  @Override
  public Document getDocument(String uid) {
    return documentStore.getByUid(uid);
  }

  @Override
  public void deleteDocument(Document document) {

    // Remove files is !external
    if (!document.isExternal()) {
      if (document.getFileResource() != null) {
        deleteFileFromDocument(document);
        log.info("Document " + document.getUrl() + " successfully deleted");
      } else {
        try {
          File file = locationManager.getFileForReading(document.getUrl(), DocumentService.DIR);

          if (file.delete()) {
            log.info("Document " + document.getUrl() + " successfully deleted");
          } else {
            log.warn("Document " + document.getUrl() + " could not be deleted");
          }
        } catch (LocationManagerException ex) {
          log.warn("An error occured while deleting " + document.getUrl());
        }
      }
    }

    documentStore.delete(document);
  }

  @Override
  public void deleteFileFromDocument(Document document) {
    FileResource fileResource = document.getFileResource();

    // Remove reference to fileResource from document to avoid db constraint exception
    document.setFileResource(null);
    documentStore.save(document);

    // Delete file
    fileResourceService.deleteFileResource(fileResource.getUid());
  }

  @Override
  public List<Document> getAllDocuments() {
    return documentStore.getAll();
  }

  @Override
  public List<Document> getDocumentByName(String name) {
    return documentStore.getAllEqName(name);
  }

  @Override
  public int getDocumentCount() {
    return documentStore.getCount();
  }

  @Override
  public int getDocumentCountByName(String name) {
    return documentStore.getCountLikeName(name);
  }

  @Override
  public List<Document> getDocumentsBetween(int first, int max) {
    return documentStore.getAllOrderedName(first, max);
  }

  @Override
  public List<Document> getDocumentsBetweenByName(String name, int first, int max) {
    return documentStore.getAllLikeName(name, first, max);
  }

  @Override
  public List<Document> getDocumentsByUid(List<String> uids) {
    return documentStore.getByUid(uids);
  }

  @Override
  public long getCountDocumentByUser(User user) {
    return documentStore.getCountByUser(user);
  }
}