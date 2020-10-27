package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mass3d.dataelement.DataElement;
import com.mass3d.indicator.Indicator;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//@Entity
//@Table(name = "datadimensionitem")
@JacksonXmlRootElement(localName = "dataDimensionItem", namespace = DxfNamespaces.DXF_2_0)
public class DataDimensionItem {

  public static final Set<Class<? extends IdentifiableObject>> DATA_DIMENSION_CLASSES = ImmutableSet.<Class<? extends IdentifiableObject>>builder()
      .
          add(Indicator.class).
          add(DataElement.class).
//        add( DataElementOperand.class ).
    add(ReportingRate.class).
//        add( ProgramIndicator.class ).
//        add( ProgramDataElementDimensionItem.class ).add( ProgramTrackedEntityAttributeDimensionItem.class ).add( ValidationRule.class ).
    build();

  public static final Map<DataDimensionItemType, Class<? extends NameableObject>> DATA_DIMENSION_TYPE_CLASS_MAP = ImmutableMap.<DataDimensionItemType, Class<? extends NameableObject>>builder()
      .
          put(DataDimensionItemType.INDICATOR, Indicator.class).
        put( DataDimensionItemType.DATA_ELEMENT, DataElement.class ).
//        put( DataDimensionItemType.DATA_ELEMENT_OPERAND, DataElementOperand.class ).
    put(DataDimensionItemType.REPORTING_RATE, ReportingRate.class).
//        put( DataDimensionItemType.PROGRAM_INDICATOR, ProgramIndicator.class ).put( DataDimensionItemType.PROGRAM_DATA_ELEMENT, ProgramDataElementDimensionItem.class ).
//        put( DataDimensionItemType.PROGRAM_ATTRIBUTE, ProgramTrackedEntityAttributeDimensionItem.class ).put( DataDimensionItemType.VALIDATION_RULE, ValidationRule.class).
    build();

  @Id
  @GeneratedValue(
      strategy = GenerationType.AUTO
  )
  @Column(name = "datadimensionitemid")
  private int id;

  // -------------------------------------------------------------------------
  // Eagle Board Data dimension objects
  // -------------------------------------------------------------------------
  @ManyToOne
  @JoinColumn(name = "datafieldid")
  private DataElement dataElement;

  // -------------------------------------------------------------------------
  // Data dimension objects
  // -------------------------------------------------------------------------

  @ManyToOne
  @JoinColumn(name = "indicatorid")
  private Indicator indicator;

//
//    private DataElementOperand dataElementOperand;

  private ReportingRate reportingRate;

//    private ProgramIndicator programIndicator;
//
//    private ProgramDataElementDimensionItem programDataElement;
//
//    private ProgramTrackedEntityAttributeDimensionItem programAttribute;

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  public DataDimensionItem() {
  }

  public static DataDimensionItem create(DimensionalItemObject object) {
    DataDimensionItem dimension = new DataDimensionItem();

    if (DataElement.class.isAssignableFrom(object.getClass())) {
      dimension.setDataElement((DataElement) object);
    } else if (Indicator.class.isAssignableFrom(object.getClass())) {
      dimension.setIndicator((Indicator) object);
    }
        else if ( DataElement.class.isAssignableFrom( object.getClass() ) )
        {
            dimension.setDataElement( (DataElement) object );
        }
//        else if ( DataElementOperand.class.isAssignableFrom( object.getClass() ) )
//        {
//            dimension.setDataElementOperand( (DataElementOperand) object );
//        }
    else if (ReportingRate.class.isAssignableFrom(object.getClass())) {
      dimension.setReportingRate((ReportingRate) object);
    }
//        else if ( ProgramIndicator.class.isAssignableFrom( object.getClass() ) )
//        {
//            dimension.setProgramIndicator( (ProgramIndicator) object );
//        }
//        else if ( ProgramDataElementDimensionItem.class.isAssignableFrom( object.getClass() ) )
//        {
//            dimension.setProgramDataElement( (ProgramDataElementDimensionItem) object );
//        }
//        else if ( ProgramTrackedEntityAttributeDimensionItem.class.isAssignableFrom( object.getClass() ) )
//        {
//            dimension.setProgramAttribute( (ProgramTrackedEntityAttributeDimensionItem) object );
//        }
    else {
      throw new IllegalArgumentException(
          "Not a valid data dimension: " + object.getClass().getSimpleName() + ", " + object);
    }

    return dimension;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public DimensionalItemObject getDimensionalItemObject() {
    if (dataElement != null) {
      return dataElement;
    } else if (indicator != null) {
      return indicator;
    }
//        else if ( dataElementOperand != null )
//        {
//            return dataElementOperand;
//        }
    else if (reportingRate != null) {
      return reportingRate;
    }
//        else if ( programIndicator != null )
//        {
//            return programIndicator;
//        }
//        else if ( programDataElement != null )
//        {
//            return programDataElement;
//        }
//        else if ( programAttribute != null )
//        {
//            return programAttribute;
//        }

    return null;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataDimensionItemType getDataDimensionItemType() {
    if (dataElement != null) {
      return DataDimensionItemType.DATA_ELEMENT;
    } else if (indicator != null) {
      return DataDimensionItemType.INDICATOR;
    }
//        else if ( dataElementOperand != null )
//        {
//            return DataDimensionItemType.DATA_ELEMENT_OPERAND;
//        }
    else if (reportingRate != null) {
      return DataDimensionItemType.REPORTING_RATE;
    }
//        else if ( programIndicator != null )
//        {
//            return DataDimensionItemType.PROGRAM_INDICATOR;
//        }
//        else if ( programDataElement != null )
//        {
//            return DataDimensionItemType.PROGRAM_DATA_ELEMENT;
//        }
//        else if ( programAttribute != null )
//        {
//            return DataDimensionItemType.PROGRAM_ATTRIBUTE;
//        }

    return null;
  }

  // -------------------------------------------------------------------------
  // Equals and hashCode
  // -------------------------------------------------------------------------

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getDimensionalItemObject() == null) ? 0
        : getDimensionalItemObject().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    DataDimensionItem other = (DataDimensionItem) obj;

    DimensionalItemObject object = getDimensionalItemObject();

    if (object == null) {
      return other.getDimensionalItemObject() == null;
    } else {
      return object.equals(other.getDimensionalItemObject());
    }

  }

  // -------------------------------------------------------------------------
  // EagleBoard Get and set methods
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // Get and set methods
  // -------------------------------------------------------------------------

  @JsonIgnore
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @JsonProperty
  @JsonSerialize(as = BaseNameableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Indicator getIndicator() {
    return indicator;
  }

  public void setIndicator(Indicator indicator) {
    this.indicator = indicator;
  }

    @JsonProperty
    @JsonSerialize( as = BaseNameableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }
//
//    @JsonProperty
//    @JsonSerialize( as = BaseNameableObject.class )
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public DataElementOperand getDataElementOperand()
//    {
//        return dataElementOperand;
//    }
//
//    public void setDataElementOperand( DataElementOperand dataElementOperand )
//    {
//        this.dataElementOperand = dataElementOperand;
//    }

  @JsonProperty
  @JsonSerialize(as = BaseNameableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public ReportingRate getReportingRate() {
    return reportingRate;
  }

  public void setReportingRate(ReportingRate reportingRate) {
    this.reportingRate = reportingRate;
  }

//    @JsonProperty
//    @JsonSerialize( as = BaseNameableObject.class )
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public ProgramIndicator getProgramIndicator()
//    {
//        return programIndicator;
//    }
//
//    public void setProgramIndicator( ProgramIndicator programIndicator )
//    {
//        this.programIndicator = programIndicator;
//    }
//
//    @JsonProperty
//    @JsonSerialize( as = BaseNameableObject.class )
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public ProgramDataElementDimensionItem getProgramDataElement()
//    {
//        return programDataElement;
//    }
//
//    public void setProgramDataElement( ProgramDataElementDimensionItem programDataElement )
//    {
//        this.programDataElement = programDataElement;
//    }
//
//    @JsonProperty
//    @JsonSerialize( as = BaseNameableObject.class )
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public ProgramTrackedEntityAttributeDimensionItem getProgramAttribute()
//    {
//        return programAttribute;
//    }
//
//    public void setProgramAttribute( ProgramTrackedEntityAttributeDimensionItem programAttribute )
//    {
//        this.programAttribute = programAttribute;
//    }
}
