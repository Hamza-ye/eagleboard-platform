package com.mass3d.schema.descriptors;

import com.mass3d.keyjsonvalue.KeyJsonValue;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class KeyJsonValueSchemaDescriptor implements SchemaDescriptor {

  public static final String SINGULAR = "dataStore";

  public static final String PLURAL = "dataStores";

  public static final String API_ENDPOINT = "/" + SINGULAR;

  @Override
  public Schema getSchema() {
    Schema schema = new Schema(KeyJsonValue.class, SINGULAR, PLURAL);
    schema.setRelativeApiEndpoint(API_ENDPOINT);
    schema.setOrder(9060);

    return schema;
  }
}
