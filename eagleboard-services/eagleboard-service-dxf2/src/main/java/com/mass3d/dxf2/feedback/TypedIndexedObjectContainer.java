package com.mass3d.dxf2.feedback;

import com.mass3d.common.IdentifiableObject;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * {@link IndexedObjectContainer}s for each object type.
 *
 */
public class TypedIndexedObjectContainer implements ObjectIndexProvider {

  private final Map<Class<? extends IdentifiableObject>, IndexedObjectContainer> typedIndexedObjectContainers = new HashMap<>();

  /**
   * Get the typed container for the specified object class.
   *
   * @param c the object class for which the container should be returned.
   * @return the container (if it does not exists, the method creates a container).
   */
  @Nonnull
  public IndexedObjectContainer getTypedContainer(@Nonnull Class<? extends IdentifiableObject> c) {
    return typedIndexedObjectContainers.computeIfAbsent(c, key -> new IndexedObjectContainer());
  }

  @Nonnull
  @Override
  public Integer mergeObjectIndex(@Nonnull IdentifiableObject object) {
    return getTypedContainer(object.getClass()).mergeObjectIndex(object);
  }

  /**
   * @param object the identifiable object that should be checked.
   * @return <code>true</code> if the object is included in the container, <code>false</code>
   * otherwise.
   */
  public boolean containsObject(@Nonnull IdentifiableObject object) {
    final IndexedObjectContainer indexedObjectContainer = typedIndexedObjectContainers
        .get(object.getClass());
    return indexedObjectContainer != null && indexedObjectContainer.containsObject(object);
  }

  /**
   * Adds an object to the corresponding indexed object container.
   *
   * @param identifiableObject the object that should be added to the container.
   */
  public void add(@Nonnull IdentifiableObject identifiableObject) {
    getTypedContainer(identifiableObject.getClass()).add(identifiableObject);
  }
}
