package com.mass3d.common.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import com.mass3d.period.PeriodType;

public class JacksonPeriodTypeSerializer
    extends JsonSerializer<PeriodType> {

  @Override
  public void serialize(PeriodType value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    if (value != null) {
      jgen.writeString(value.getName());
    }
  }
}
