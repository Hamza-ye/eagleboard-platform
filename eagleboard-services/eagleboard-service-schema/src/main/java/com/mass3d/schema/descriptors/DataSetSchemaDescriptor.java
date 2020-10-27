package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.dataset.DataSet;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class DataSetSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "dataSet";

  public static final String PLURAL = "dataSets";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(DataSet.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(1310);
    schema.setDataShareable(true);

    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PUBLIC, Lists.newArrayList("F_DATASET_PUBLIC_ADD")));
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PRIVATE, Lists.newArrayList("F_DATASET_PRIVATE_ADD")));
    schema.getAuthorities()
        .add(new Authority(AuthorityType.DELETE, Lists.newArrayList("F_DATASET_DELETE")));

    return schema;
  }
}
