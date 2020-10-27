package com.mass3d.common;

import static org.apache.commons.lang3.EnumUtils.isValidEnum;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import static com.mass3d.common.DimensionItemType.DATA_ELEMENT;
import static com.mass3d.common.DimensionItemType.DATA_ELEMENT_OPERAND;

/**
 * Holds the DimensionItemType of a DimensionalItemObject, and the identifier strings of the objects
 * that that make up the DimensionalItemObject
 *
 */
public class DimensionalItemId {
  // -------------------------------------------------------------------------
  // Properties
  // -------------------------------------------------------------------------

  /**
   * The type of DimensionalItemObject whose ids we have
   */
  private DimensionItemType dimensionItemType;

  /**
   * The first id for the DimensionalItemObject
   */
  private String id0;

  /**
   * The second id (if any) for the DimensionalItemObject
   */
  private String id1;

  /**
   * The third id (if any) for the DimensionalItemObject
   */
  private String id2;

  /**
   * The period offset
   */
  private Integer periodOffset = 0;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DimensionalItemId( DimensionItemType dimensionItemType, String id0 )
  {
    this.dimensionItemType = dimensionItemType;
    this.id0 = id0;
  }

  public DimensionalItemId( DimensionItemType dimensionItemType, String id0, Integer periodOffset )
  {
    this.dimensionItemType = dimensionItemType;
    this.id0 = id0;
    this.periodOffset = periodOffset;
  }

  public DimensionalItemId( DimensionItemType dimensionItemType, String id0, String id1 )
  {
    this.dimensionItemType = dimensionItemType;
    this.id0 = id0;
    this.id1 = id1;
  }

  public DimensionalItemId( DimensionItemType dimensionItemType, String id0, String id1, String id2 )
  {
    this.dimensionItemType = dimensionItemType;
    this.id0 = id0;
    this.id1 = id1;
    this.id2 = id2;
  }

  public DimensionalItemId( DimensionItemType dimensionItemType, String id0, String id1, String id2,
      int periodOffset )
  {
    this.dimensionItemType = dimensionItemType;
    this.id0 = id0;
    this.id1 = id1;
    this.id2 = id2;
    this.periodOffset = periodOffset;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public boolean hasValidIds()
  {
    switch ( dimensionItemType )
    {
      case DATA_ELEMENT:
      case INDICATOR:
      case PROGRAM_INDICATOR:
        return id0 != null && id1 == null && id2 == null;

      case DATA_ELEMENT_OPERAND:
        return id0 != null && ( id1 != null || id2 != null );

      case REPORTING_RATE:
        return id0 != null && id1 != null && id2 == null
            && isValidEnum( ReportingRateMetric.class, id1 );

      case PROGRAM_DATA_ELEMENT:
      case PROGRAM_ATTRIBUTE:
        return id0 != null && id1 != null && id2 == null;

      default:
        return false;
    }
  }

  public boolean isDataElementOrOperand()
  {
    return dimensionItemType == DATA_ELEMENT
        || dimensionItemType == DATA_ELEMENT_OPERAND;
  }

  // -------------------------------------------------------------------------
  // hashCode, equals and toString
  // -------------------------------------------------------------------------

  @Override
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }

    if ( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    DimensionalItemId that = (DimensionalItemId) o;

    return Objects.equals( this.dimensionItemType, that.dimensionItemType )
        && Objects.equals( this.id0, that.id0 )
        && Objects.equals( this.id1, that.id1 )
        && Objects.equals( this.id2, that.id2 )
        && this.periodOffset.equals( that.periodOffset );
  }

  @Override
  public int hashCode()
  {
    int result = dimensionItemType.hashCode();

    result = 31 * result + (id0 == null ? 0 : id0.hashCode());
    result = 31 * result + (id1 == null ? 0 : id1.hashCode());
    result = 31 * result + (id2 == null ? 0 : id2.hashCode());
    result = 31 * result + periodOffset;

    return result;
  }

  @Override
  public String toString()
  {
    return MoreObjects.toStringHelper( this )
        .add( "dimensionItemType", dimensionItemType )
        .add( "id0", id0 )
        .add( "id1", id1 )
        .add( "id2", id2 )
        .add( "periodOffset", periodOffset )
        .toString();
  }

  // -------------------------------------------------------------------------
  // Getters
  // -------------------------------------------------------------------------

  public DimensionItemType getDimensionItemType()
  {
    return dimensionItemType;
  }

  public String getId0()
  {
    return id0;
  }

  public String getId1()
  {
    return id1;
  }

  public String getId2()
  {
    return id2;
  }

  public Integer getPeriodOffset()
  {
    return periodOffset;
  }
}
