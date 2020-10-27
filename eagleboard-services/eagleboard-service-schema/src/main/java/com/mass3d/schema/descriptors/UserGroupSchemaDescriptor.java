package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.user.UserGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class UserGroupSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "userGroup";

  public static final String PLURAL = "userGroups";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(UserGroup.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(102);

    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PUBLIC, Lists.newArrayList("F_USERGROUP_PUBLIC_ADD")));
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PRIVATE, Lists.newArrayList("F_USERGROUP_PRIVATE_ADD")));
    schema.getAuthorities()
        .add(new Authority(AuthorityType.DELETE, Lists.newArrayList("F_USERGROUP_DELETE")));

    return schema;
  }
}
