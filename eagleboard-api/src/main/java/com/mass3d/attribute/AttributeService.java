package com.mass3d.attribute;

import java.util.List;
import java.util.Set;
import com.mass3d.attribute.exception.NonUniqueAttributeValueException;
import com.mass3d.common.IdentifiableObject;

public interface AttributeService
{
    String ID = AttributeService.class.getName();

    // -------------------------------------------------------------------------
    // Attribute
    // -------------------------------------------------------------------------

    /**
     * Adds an attribute.
     *
     * @param attribute the attribute.
     */
    void addAttribute(Attribute attribute);

    /**
     * Deletes an attribute.
     *
     * @param attribute the attribute.
     */
    void deleteAttribute(Attribute attribute);

    /**
     * Invalidate cached attribute
     * @param attributeUid
     */
    void invalidateCachedAttribute(String attributeUid);

    /**
     * Gets the attribute with the given id.
     *
     * @param id the attribute id.
     * @return the attribute with the given id.
     */
    Attribute getAttribute(long id);

    /**
     * Gets the attribute with the given uid.
     *
     * @param uid the attribute uid.
     * @return the attribute with the given uid.
     */
    Attribute getAttribute(String uid);

    /**
     * Gets the attribute with the given name.
     *
     * @param name the name.
     * @return the attribute with the given name.
     */
    Attribute getAttributeByName(String name);

    /**
     * Gets the attribute with the given code.
     *
     * @param code the code.
     * @return the attribute with the given code.
     */
    Attribute getAttributeByCode(String code);

    /**
     * Gets all attributes.
     *
     * @return a set of all attributes.
     */
    List<Attribute> getAllAttributes();

    List<Attribute> getAttributes(Class<?> klass);

    List<Attribute> getMandatoryAttributes(Class<?> klass);

    List<Attribute> getUniqueAttributes(Class<?> klass);

    // -------------------------------------------------------------------------
    // AttributeValue
    // -------------------------------------------------------------------------

    /**
     * Adds an attribute value.
     *
     * @param attributeValue the attribute value.
     */
    <T extends IdentifiableObject> void addAttributeValue(T object, AttributeValue attributeValue) throws NonUniqueAttributeValueException;

    /**
     * Deletes an attribute value.
     *
     * @param object the object which the attributeValue belongs to.
     * @param attributeValue the attribute value.
     */
    <T extends IdentifiableObject> void deleteAttributeValue(T object,
        AttributeValue attributeValue);

    /**
     * Deletes a Set of attribute values.
     *
     * @param object the object which the attributeValue belongs to.
     * @param attributeValues the Set of attribute values.
     */
    <T extends IdentifiableObject> void deleteAttributeValues(T object,
        Set<AttributeValue> attributeValues);

    <T extends IdentifiableObject> void generateAttributes(List<T> entityList);
}
