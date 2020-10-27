package com.mass3d.schema.descriptors;

import com.mass3d.user.UserGroupAccess;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class UserGroupAccessSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "userGroupAccess";

  public static final String PLURAL = "userGroupAccesses";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    return new Schema(UserGroupAccess.class, SINGULAR, PLURAL);
  }
}
