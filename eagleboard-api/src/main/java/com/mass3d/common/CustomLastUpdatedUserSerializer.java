package com.mass3d.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.mass3d.user.User;
import java.io.IOException;

public class CustomLastUpdatedUserSerializer extends JsonSerializer<User> {

  @Override
  public void serialize(User user, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    ToXmlGenerator xmlGenerator = null;
    if (jsonGenerator instanceof ToXmlGenerator) {
      xmlGenerator = (ToXmlGenerator) jsonGenerator;
    }

    jsonGenerator.writeStartObject();
    if (xmlGenerator != null) {
      xmlGenerator.setNextIsAttribute(true);
      xmlGenerator.setNextName(null);
    }
    jsonGenerator.writeStringField("id", user.getUid());
    jsonGenerator.writeStringField("name", user.getDisplayName());
    jsonGenerator.writeEndObject();
  }
}