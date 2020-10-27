package com.mass3d.document;

import java.util.List;
import com.mass3d.user.User;

/**
 * @version $Id$
 */
public interface DocumentService {

  String ID = DocumentService.class.getName();
  String DIR = "documents";

  /**
   * Saves a Document.
   *
   * @param document the Document to save.
   * @return the generated identifier.
   */
  long saveDocument(Document document);

  /**
   * Retrieves the Document with the given identifier.
   *
   * @param id the identifier of the Document.
   * @return the Document.
   */
  Document getDocument(long id);

  /**
   * Retrieves the Document with the given identifier.
   *
   * @param uid the identifier of the Document.
   * @return the Document.
   */
  Document getDocument(String uid);

  /**
   * Deletes a Document.
   *
   * @param document the Document to delete.
   */
  void deleteDocument(Document document);

  /**
   * Used when removing a file reference from a Document.
   */
  void deleteFileFromDocument(Document document);

  /**
   * Retrieves all Documents.
   *
   * @return a Collection of Documents.
   */
  List<Document> getAllDocuments();

  /**
   * Retrieves the Document with the given name.
   *
   * @param name the name of the Document.
   * @return the Document.
   */
  List<Document> getDocumentByName(String name);

  List<Document> getDocumentsBetween(int first, int max);

  List<Document> getDocumentsBetweenByName(String name, int first, int max);

  int getDocumentCount();

  int getDocumentCountByName(String name);

  List<Document> getDocumentsByUid(List<String> uids);

  long getCountDocumentByUser(User user);
}
