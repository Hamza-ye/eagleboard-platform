package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.activity.Activity;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ActivitySchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "activity";

  public static final String PLURAL = "activities";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(Activity.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(1100);

//        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_TODOTASK_ADD" ) ) );
//        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_TODOTASK_DELETE" ) ) );
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PUBLIC, Lists.newArrayList("F_ACTIVITY_PUBLIC_ADD")));
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PRIVATE, Lists.newArrayList("F_ACTIVITY_PRIVATE_ADD")));
    schema.getAuthorities()
        .add(new Authority(AuthorityType.DELETE, Lists.newArrayList("F_ACTIVITY_DELETE")));

    return schema;
  }
}
