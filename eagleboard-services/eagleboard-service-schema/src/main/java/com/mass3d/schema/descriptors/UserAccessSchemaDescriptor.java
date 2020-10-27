package com.mass3d.schema.descriptors;

import com.mass3d.user.UserAccess;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class UserAccessSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "userAccess";

  public static final String PLURAL = "userAccesses";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    return new Schema(UserAccess.class, SINGULAR, PLURAL);
  }
}
