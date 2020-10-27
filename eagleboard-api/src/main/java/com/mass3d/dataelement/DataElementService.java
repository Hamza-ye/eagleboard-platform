package com.mass3d.dataelement;

import com.mass3d.common.ValueType;
import com.mass3d.period.PeriodType;
import java.util.Collection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Defines service functionality for DataElements.
 *
 * @version $Id: DataElementService.java 6289 2008-11-14 17:53:24Z larshelg $
 */
public interface DataElementService {

  String ID = DataElementService.class.getName();

  // -------------------------------------------------------------------------
  // DataElement
  // -------------------------------------------------------------------------

  /**
   * Adds a DataElement.
   *
   * @param dataElement the DataElement to add.
   * @return a generated unique id of the added DataElement.
   */
  long addDataElement(DataElement dataElement);

  /**
   * Updates a DataElement.
   *
   * @param dataElement the DataElement to update.
   */
  void updateDataElement(DataElement dataElement);

  /**
   * Deletes a DataElement. The DataElement is also removed from any
   * DataElementGroups it is a member of. It is not possible to delete a
   * DataElement with children.
   *
   * @param dataElement the DataElement to delete.
   * @throws HierarchyViolationException if the DataElement has children.
   */
  void deleteDataElement(DataElement dataElement);

  /**
   * Returns a DataElement.
   *
   * @param id the id of the DataElement to return.
   * @return the DataElement with the given id, or null if no match.
   */
  DataElement getDataElement(long id);

  /**
   * Returns the DataElement with the given UID.
   *
   * @param uid the UID.
   * @return the DataElement with the given UID, or null if no match.
   */
  DataElement getDataElement(String uid);

  /**
   * Returns the DataElement with the given code.
   *
   * @param code the code.
   * @return the DataElement with the given code, or null if no match.
   */
  DataElement getDataElementByCode(String code);

  /**
   * Returns all DataElements.
   *
   * @return a list of all DataElements, or an empty list if there
   * are no DataElements.
   */
  List<DataElement> getAllDataElements();

  /**
   * Returns all DataElements of a given type.
   *
   * @param valueType the value type restriction
   * @return a list of all DataElements with the given value type,
   * or an empty list if there are no DataElements.
   */
  List<DataElement> getAllDataElementsByValueType(ValueType valueType);

//  /**
//   * Returns all DataElements with the given domain type.
//   *
//   * @param domainType the DataElementDomainType.
//   * @return all DataElements with the given domainType.
//   */
//  List<DataElement> getDataElementsByDomainType( DataElementDomain domainType );


//  /**
//   * Returns all DataElements with the given category combo.
//   *
//   * @param categoryCombo the CategoryCombo.
//   * @return all DataElements with the given category combo.
//   */
//  List<DataElement> getDataElementByCategoryCombo( CategoryCombo categoryCombo );
//
//  /**
//   * Returns all DataElements which are not member of any DataElementGroups.
//   *
//   * @return all DataElements which are not member of any DataElementGroups.
//   */
//  List<DataElement> getDataElementsWithoutGroups();

  List<DataElement> getDataElementsByPeriodType(PeriodType periodType);

  /**
   * Returns all DataElements which are not assigned to any DataSets.
   *
   * @return all DataElements which are not assigned to any DataSets.
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

  /**
   * Returns all DataElements which zeroIsSignificant property is true or false.
   *
   * @param zeroIsSignificant whether zero is significant is true for this query.
   * @return a collection of DataElements.
   */
  List<DataElement> getDataElementsByZeroIsSignificant(boolean zeroIsSignificant);

  // -------------------------------------------------------------------------
  // DataElementGroup
  // -------------------------------------------------------------------------

  /**
   * Adds a DataElementGroup.
   *
   * @param dataElementGroup the DataElementGroup to add.
   * @return a generated unique id of the added DataElementGroup.
   */
  long addDataElementGroup( DataElementGroup dataElementGroup );

  /**
   * Updates a DataElementGroup.
   *
   * @param dataElementGroup the DataElementGroup to update.
   */
  void updateDataElementGroup( DataElementGroup dataElementGroup );

  /**
   * Deletes a DataElementGroup.
   *
   * @param dataElementGroup the DataElementGroup to delete.
   */
  void deleteDataElementGroup( DataElementGroup dataElementGroup );

  /**
   * Returns a DataElementGroup.
   *
   * @param id the id of the DataElementGroup to return.
   * @return the DataElementGroup with the given id, or null if no match.
   */
  DataElementGroup getDataElementGroup( long id );

  /**
   * Returns the data element groups with the given uids.
   *
   * @param uids the uid collection.
   * @return the data element groups with the given uids.
   */
  List<DataElementGroup> getDataElementGroupsByUid( Collection<String> uids );

  /**
   * Returns the DataElementGroup with the given UID.
   *
   * @param uid the UID of the DataElementGroup to return.
   * @return the DataElementGroup with the given UID, or null if no match.
   */
  DataElementGroup getDataElementGroup( String uid );

  /**
   * Returns a DataElementGroup with a given name.
   *
   * @param name the name of the DataElementGroup to return.
   * @return the DataElementGroup with the given name, or null if no match.
   */
  DataElementGroup getDataElementGroupByName( String name );

  /**
   * Returns all DataElementGroups.
   *
   * @return a collection of all DataElementGroups, or an empty collection if
   * no DataElementGroups exist.
   */
  List<DataElementGroup> getAllDataElementGroups();

  // -------------------------------------------------------------------------
  // DataElementGroupSet
  // -------------------------------------------------------------------------

  long addDataElementGroupSet( DataElementGroupSet groupSet );

  void updateDataElementGroupSet( DataElementGroupSet groupSet );

  void deleteDataElementGroupSet( DataElementGroupSet groupSet );

  DataElementGroupSet getDataElementGroupSet( long id );

  DataElementGroupSet getDataElementGroupSet( String uid );

  DataElementGroupSet getDataElementGroupSetByName( String name );

  List<DataElementGroupSet> getAllDataElementGroupSets();
}
