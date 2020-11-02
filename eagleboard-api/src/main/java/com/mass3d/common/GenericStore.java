package com.mass3d.common;

import java.util.List;
import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeValue;

public interface GenericStore<T>
{
    /**
     * Class of the object for this store.
     */
    Class<T> getClazz();

    /**
     * Saves the given object instance, with clear sharing set to true.
     *
     * @param object the object instance.
     */
    void save(T object);

    /**
     * Updates the given object instance.
     *
     * @param object the object instance.
     */
    void update(T object);

    /**
     * Removes the given object instance.
     *
     * @param object the object instance to delete.
     */
    void delete(T object);

    /**
     * Retrieves the object with the given identifier. This method will first
     * look in the current Session, then hit the database if not existing.
     *
     * @param id the object identifier.
     * @return the object identified by the given identifier.
     */
    T get(long id);

    long countAllValuesByAttributes(List<Attribute> attributes);

    List<AttributeValue> getAttributeValueByAttribute(Attribute attribute);

    /**
     * Gets the count of objects.
     *
     * @return the count of objects.
     */
    int getCount();

    /**
     * Retrieves a List of all objects.
     *
     * @return a List of all objects.
     */
    List<T> getAll();

    List<T> getByAttribute(Attribute attribute);

    List<T> getByAttributeAndValue(Attribute attribute, String value);

    List<T> getAllByAttributes(List<Attribute> attributes);

    List<AttributeValue> getAllValuesByAttributes(List<Attribute> attributes);

    List<AttributeValue> getAttributeValueByAttributeAndValue(Attribute attribute, String value);

    List<T> getByAttributeValue(AttributeValue attributeValue);

    <P extends IdentifiableObject> boolean isAttributeValueUnique(P object,
        AttributeValue attributeValue);

    <P extends IdentifiableObject> boolean isAttributeValueUnique(P object, Attribute attribute,
        String value);

    List<T> getAllByAttributeAndValues(Attribute attribute, List<String> values);

}
