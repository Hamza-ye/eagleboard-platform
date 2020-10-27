package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class UserRoleSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "userRole";

  public static final String PLURAL = "userRoles";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(UserAuthorityGroup.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(100);

    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PUBLIC, Lists.newArrayList("F_USERROLE_PUBLIC_ADD")));
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PRIVATE, Lists.newArrayList("F_USERROLE_PRIVATE_ADD")));
    schema.getAuthorities()
        .add(new Authority(AuthorityType.DELETE, Lists.newArrayList("F_USERROLE_DELETE")));

    return schema;
  }
}
