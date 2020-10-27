package com.mass3d.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mass3d.analytics.AggregationType;
import com.mass3d.dataelement.DataElementGroupSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DimensionalObject
    extends NameableObject {

  String DATA_X_DIM_ID = "dx"; // in, de, ds, do
  String DATA_COLLAPSED_DIM_ID = "dy"; // Collapsed event data dimensions
  String CATEGORYOPTIONCOMBO_DIM_ID = "co";
  String ATTRIBUTEOPTIONCOMBO_DIM_ID = "ao";
  String PERIOD_DIM_ID = "pe";
  String ORGUNIT_DIM_ID = "ou";
  String ORGUNIT_GROUP_DIM_ID = "oug"; // Used for org unit target
  String ITEM_DIM_ID = "item";

  String DIMENSION_SEP = "-";

  String LONGITUDE_DIM_ID = "longitude";
  String LATITUDE_DIM_ID = "latitude";

  String DIMENSION_NAME_SEP = ":";
  String OPTION_SEP = ";";
  String ITEM_SEP = "-";

  List<String> STATIC_DIMS = ImmutableList.<String>builder().add(
      LONGITUDE_DIM_ID, LATITUDE_DIM_ID).build();

  Map<String, String> PRETTY_NAMES = DimensionalObjectUtils.asMap(
      DATA_X_DIM_ID, "Data",
      CATEGORYOPTIONCOMBO_DIM_ID, "Data details",
      PERIOD_DIM_ID, "Period",
      ORGUNIT_DIM_ID, "Organisation unit");

  Set<Class<? extends IdentifiableObject>> DYNAMIC_DIMENSION_CLASSES = ImmutableSet.<Class<? extends IdentifiableObject>>builder().
//        add( Category.class ).
    add(DataElementGroupSet.class).
//        add( OrganisationUnitGroupSet.class ).
//        add( CategoryOptionGroupSet.class ).
    build();
//
//    Map<Class<? extends DimensionalObject>, Class<? extends DimensionalItemObject>> DIMENSION_CLASS_ITEM_CLASS_MAP = ImmutableMap.<Class<? extends DimensionalObject>, Class<? extends DimensionalItemObject>>builder().
//        put( Category.class, CategoryOption.class ).
//        put( DataElementGroupSet.class, DataElementGroup.class ).
//        put( OrganisationUnitGroupSet.class, OrganisationUnitGroup.class ).
//        put( CategoryOptionGroupSet.class, CategoryOptionGroup.class ).build();

  Set<ValueType> ARITHMETIC_VALUE_TYPES = ImmutableSet.<ValueType>builder().add(
      ValueType.BOOLEAN, ValueType.TRUE_ONLY, ValueType.NUMBER, ValueType.INTEGER,
      ValueType.INTEGER_POSITIVE, ValueType.INTEGER_NEGATIVE, ValueType.INTEGER_ZERO_OR_POSITIVE,
      ValueType.UNIT_INTERVAL, ValueType.PERCENTAGE).build();

  /**
   * Gets the dimension identifier.
   */
  String getDimension();

  /**
   * Gets the dimension type.
   */
  DimensionType getDimensionType();

  /**
   * Gets the dimension name, which corresponds to a column in the analytics tables, with fall back
   * to dimension.
   */
  String getDimensionName();

  /**
   * Dimension items.
   */
  List<DimensionalItemObject> getItems();

  /**
   * Indicates whether all available items in this dimension are included.
   */
  boolean isAllItems();

  /**
   * Indicates whether this dimension has any dimension items.
   */
  boolean hasItems();

//  /**
//   * Gets the legend set.
//   */
//  LegendSet getLegendSet();

//  /**
//   * Indicates whether this dimension has a legend set.
//   */
//  boolean hasLegendSet();

  /**
   * Gets the aggregation type.
   */
  AggregationType getAggregationType();

  /**
   * Gets the filter. Contains operator and filter. Applicable for events.
   */
  String getFilter();

  /**
   * Indicates the analytics type of this dimensional object.
   */
//  AnalyticsType getAnalyticsType();

  /**
   * Indicates whether this object should be handled as a data dimension. Persistent property.
   */
  boolean isDataDimension();

  /**
   * Indicates whether this dimension is fixed, meaning that the name of the dimension will be
   * returned as is for all dimension items in the response.
   */
  boolean isFixed();

  /**
   * Returns a unique key representing this dimension.
   */
  String getKey();
}
