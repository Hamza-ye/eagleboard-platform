package com.mass3d.dataelement;

import com.mass3d.common.GenericDimensionalObjectStore;
import com.mass3d.common.ValueType;
import java.util.List;

public interface DataElementStore
    extends GenericDimensionalObjectStore<DataElement> {

  String ID = DataElementStore.class.getName();

  // -------------------------------------------------------------------------
  // DataElement
  // -------------------------------------------------------------------------

//  /**
//   * Returns all DataElements with the given category combo.
//   *
//   * @param categoryCombo the CategoryCombo.
//   * @return all DataElements with the given category combo.
//   */
//  List<DataElement> getDataElementByCategoryCombo( CategoryCombo categoryCombo );

  /**
   * Returns all DataElement which zeroIsSignificant property is true or false
   *
   * @param zeroIsSignificant is zeroIsSignificant property
   * @return a collection of all DataElement
   */
  List<DataElement> getDataElementsByZeroIsSignificant(boolean zeroIsSignificant);

//  /**
//   * Returns all DataElements of the given domain type.
//   *
//   * @param domainType the domain type.
//   * @return all DataElements of the given domain type.
//   */
//  List<DataElement> getDataElementsByDomainType( DataElementDomain domainType );

  /**
   * Returns all DataElements of the given value type.
   *
   * @param valueType the value type.
   * @return all DataElements of the given value type.
   */
  List<DataElement> getDataElementsByValueType(ValueType valueType);

  /**
   * Returns all DataElements which are not member of any DataElementGroups.
   *
   * @return all DataElements which are not member of any DataElementGroups.
   */
  List<DataElement> getDataElementsWithoutGroups();

  /**
   * Returns all DataElements which are not assigned to any ElementSets.
   *
   * @return all DataElements which are not assigned to any ElementSets.
   */
  List<DataElement> getDataElementsWithoutDataSets();

  /**
   * Returns all DataElements which are assigned to at least one DataSet.
   *
   * @return all DataElements which are assigned to at least one DataSet.
   */
  List<DataElement> getDataElementsWithDataSets();

  /**
   * Returns all DataElements which have the given aggregation level assigned.
   *
   * @param aggregationLevel the aggregation level.
   * @return all DataElements which have the given aggregation level assigned.
   */
  List<DataElement> getDataElementsByAggregationLevel(int aggregationLevel);
}
