package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.dataelement.DataElement;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class DataElementSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "dataElement";

  public static final String PLURAL = "dataElements";

  public static final String API_ENDPOINT = "/" + PLURAL;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(DataElement.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(1200);

    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PUBLIC, Lists.newArrayList("F_DATAELEMENT_PUBLIC_ADD")));
    schema.getAuthorities().add(
        new Authority(AuthorityType.CREATE_PRIVATE, Lists.newArrayList("F_DATAELEMENT_PRIVATE_ADD")));
    schema.getAuthorities()
        .add(new Authority(AuthorityType.DELETE, Lists.newArrayList("F_DATAELEMENT_DELETE")));

    return schema;
  }
}
