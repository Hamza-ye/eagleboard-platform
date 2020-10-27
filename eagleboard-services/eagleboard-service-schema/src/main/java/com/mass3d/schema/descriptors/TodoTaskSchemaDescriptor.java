package com.mass3d.schema.descriptors;

import com.mass3d.todotask.TodoTask;
import com.google.common.collect.Lists;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class TodoTaskSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "todoTask";

  public static final String PLURAL = "todoTasks";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(TodoTask.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(1100);

//        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_TODOTASK_ADD" ) ) );
//        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_TODOTASK_DELETE" ) ) );
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PUBLIC, Lists.newArrayList("F_TODOTASK_PUBLIC_ADD")));
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PRIVATE, Lists.newArrayList("F_TODOTASK_PRIVATE_ADD")));
    schema.getAuthorities()
        .add(new Authority(AuthorityType.DELETE, Lists.newArrayList("F_TODOTASK_DELETE")));

    return schema;
  }
}
