package com.mass3d.common.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;

class LocalPeriod {

  private String id;

  private String name;

  LocalPeriod() {
  }

  @JsonProperty
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

public class JacksonPeriodDeserializer
    extends JsonDeserializer<Period> {

  @Override
  public Period deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
    LocalPeriod period = jp.readValueAs(LocalPeriod.class);

    return period.getId() == null ? null : PeriodType.getPeriodFromIsoString(period.getId());
  }
}
