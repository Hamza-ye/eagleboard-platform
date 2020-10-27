package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.user.User;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class UserSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "user";

  public static final String PLURAL = "users";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(User.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(101);

    schema.getAuthorities().add(new Authority(AuthorityType.CREATE,
        Lists.newArrayList("F_USER_ADD", "F_USER_ADD_WITHIN_MANAGED_GROUP")));
    schema.getAuthorities().add(new Authority(AuthorityType.DELETE,
        Lists.newArrayList("F_USER_DELETE", "F_USER_DELETE_WITHIN_MANAGED_GROUP")));

    return schema;
  }
}
