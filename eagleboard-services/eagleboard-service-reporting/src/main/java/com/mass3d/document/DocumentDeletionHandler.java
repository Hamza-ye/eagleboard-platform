package com.mass3d.document;

import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceStorageStatus;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.document.DocumentDeletionHandler" )
public class DocumentDeletionHandler extends DeletionHandler {

  private DocumentService documentService;

  private JdbcTemplate jdbcTemplate;

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  // -------------------------------------------------------------------------
  // DeletionHandler implementation
  // -------------------------------------------------------------------------

  @Override
  public String getClassName() {
    return Document.class.getSimpleName();
  }

  public String allowDeleteUser(User user) {
    return documentService.getCountDocumentByUser(user) > 0 ? ERROR : null;
  }

  @Override
  public String allowDeleteFileResource(FileResource fileResource) {
    String sql = "SELECT COUNT(*) FROM document WHERE fileresource=" + fileResource.getId();

    int result = jdbcTemplate.queryForObject(sql, Integer.class);

    return result == 0 || fileResource.getStorageStatus() != FileResourceStorageStatus.STORED ? null
        : ERROR;
  }

  @Override
  public void deleteFileResource(FileResource fileResource) {
    String sql = "DELETE FROM document WHERE fileresource=" + fileResource.getId();

    jdbcTemplate.execute(sql);
  }
}