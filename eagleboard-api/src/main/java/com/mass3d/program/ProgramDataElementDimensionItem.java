package com.mass3d.program;

import static com.mass3d.common.DimensionalObjectUtils.COMPOSITE_DIM_OBJECT_PLAIN_SEP;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.analytics.AggregationType;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdScheme;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;

@JacksonXmlRootElement(localName = "programDataElement", namespace = DxfNamespaces.DXF_2_0)
public class ProgramDataElementDimensionItem
    extends BaseDimensionalItemObject implements EmbeddedObject {

  private Program program;

  private DataElement dataElement;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public ProgramDataElementDimensionItem() {
  }

  public ProgramDataElementDimensionItem(Program program, DataElement dataElement) {
    this.program = program;
    this.dataElement = dataElement;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  @Override
  public String getName() {
    return program.getDisplayName() + " " + dataElement.getDisplayName();
  }

  @Override
  public String getShortName() {
    return program.getDisplayShortName() + " " + dataElement.getDisplayShortName();
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public ValueType getValueType() {
    return dataElement.getValueType();
  }

  @Override
  public String toString() {
    return "{" +
        "\"class\":\"" + getClass() + "\", " +
        "\"id\":\"" + id + "\", " +
        "\"uid\":\"" + uid + "\", " +
        "\"program\":" + program + ", " +
        "\"dataElement\":" + dataElement + ", " +
        "\"created\":\"" + created + "\", " +
        "\"lastUpdated\":\"" + lastUpdated + "\" " +
        "}";
  }

  // -------------------------------------------------------------------------
  // DimensionalItemObject
  // -------------------------------------------------------------------------

  @Override
  public String getDimensionItem() {
    return program.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + dataElement.getUid();
  }

  @Override
  public String getDimensionItem(IdScheme idScheme) {
    return program.getPropertyValue(idScheme) + COMPOSITE_DIM_OBJECT_PLAIN_SEP + dataElement
        .getPropertyValue(idScheme);
  }

  @Override
  public DimensionItemType getDimensionItemType() {
    return DimensionItemType.PROGRAM_DATA_ELEMENT;
  }

  //    @Override
//    public List<LegendSet> getLegendSets()
//    {
//        return dataElement.getLegendSets();
//    }
//
  @Override
  public AggregationType getAggregationType() {
    return dataElement.getAggregationType();
  }

  // -------------------------------------------------------------------------
  // Get and set methods
  // -------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Program getProgram() {
    return program;
  }

  public void setProgram(Program program) {
    this.program = program;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataElement getDataElement() {
    return dataElement;
  }

  public void setDataElement(DataElement dataElement) {
    this.dataElement = dataElement;
  }
}
