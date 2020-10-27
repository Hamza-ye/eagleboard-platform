package com.mass3d.document;

import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.user.User;

public interface DocumentStore
    extends IdentifiableObjectStore<Document> {

  long getCountByUser(User user);
}