package com.mass3d.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataset.DataSetElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomListSerializer extends StdSerializer<Set<DataSetElement>> {

  public CustomListSerializer() {
    this(null);
  }

  public CustomListSerializer(Class<Set<DataSetElement>> t) {
    super(t);
  }

  @Override
  public void serialize(
      Set<DataSetElement> items,
      JsonGenerator generator,
      SerializerProvider provider)
      throws IOException, JsonProcessingException {

    List<Integer> ids = new ArrayList<>();
    for (DataSetElement item : items) {
      ids.add(item.getId());
    }
    generator.writeObject(ids);
  }
}
