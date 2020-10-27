package com.mass3d.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.user.User;

public interface DimensionService {

  /**
   * Returns items which the current user has data read access to.
   *
   * @param uid the dimension identifier.
   * @return a list of {@link DimensionalItemObject}, empty if none.
   */
  List<DimensionalItemObject> getCanReadDimensionItems( String uid );

  <T extends IdentifiableObject> List<T> getCanReadObjects( List<T> objects );

  <T extends IdentifiableObject> List<T> getCanReadObjects( User user, List<T> objects );

  DimensionType getDimensionType( String uid );

  List<DimensionalObject> getAllDimensions();

//  List<DimensionalObject> getDimensionConstraints();

  DimensionalObject getDimensionalObjectCopy( String uid, boolean filterCanRead );

//  void mergeAnalyticalObject( BaseAnalyticalObject object );

//  void mergeEventAnalyticalObject( EventAnalyticalObject object );

  /**
   * Gets a dimension item object which are among the data dimension item
   * objects. The composite dimensional items themselves will be transient
   * and the associated objects will be persistent.
   *
   * @param dimensionItem the dimension item identifier.
   * @return a dimensional item object.
   */
  DimensionalItemObject getDataDimensionalItemObject( String dimensionItem );

  /**
   * Gets a dimension item object which are among the data dimension item
   * objects. The composite dimensional items will be transient and the
   * associated objects will be persistent.
   *
   * @param idScheme the idScheme to identify the item.
   * @param dimensionItem the dimension item identifier.
   * @return a dimensional item object.
   */
  DimensionalItemObject getDataDimensionalItemObject( IdScheme idScheme, String dimensionItem );

  /**
   * Gets a dimension item object from a dimension item id.
   *
   * @param dimensionalItemId the dimension item identifier.
   * @return a dimensional item object.
   */
  DimensionalItemObject getDataDimensionalItemObject( DimensionalItemId dimensionalItemId );

  /**
   * Gets a set of dimension item objects from their ids.
   * In case of two {@see DimensionalItemId} having the same ids, this function always returns a
   * {@see DimensionalItemObject} corresponding to the {@see DimensionalItemId}
   * with the Period Offset property higher/lower than 0.
   *
   * @param itemIds a set of ids of the dimension item objects to get.
   * @return the set of dimension item objects built from the ids.
   */
  Set<DimensionalItemObject> getDataDimensionalItemObjects( Set<DimensionalItemId> itemIds );

  /**
   * Gets a map from dimension item ids to their dimension item objects.
   *
   * @param itemIds a set of ids of the dimension item objects to get.
   * @return a map from the item ids to the dimension item objects.
   */
  Map<DimensionalItemId, DimensionalItemObject> getDataDimensionalItemObjectMap( Set<DimensionalItemId> itemIds );

  /**
   * Gets a map from dimension item ids to their dimension item objects
   * without applying sharing settings.
   *
   * @param itemIds a set of ids of the dimension item objects to get.
   * @return a map from the item ids to the dimension item objects.
   */
  Map<DimensionalItemId, DimensionalItemObject> getNoAclDataDimensionalItemObjectMap( Set<DimensionalItemId> itemIds );
}
