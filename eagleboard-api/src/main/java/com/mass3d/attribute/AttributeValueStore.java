package com.mass3d.attribute;

import java.util.List;
import com.mass3d.common.GenericStore;
import com.mass3d.common.IdentifiableObject;

public interface AttributeValueStore
    extends GenericStore<AttributeValue>
{
    List<AttributeValue> getAllByAttributes(List<Attribute> attributes);

    List<AttributeValue> getAllByAttribute(Attribute attribute);

    List<AttributeValue> getAllByAttributeAndValue(Attribute attribute, String value);

    /**
     * Is attribute value unique, the value must either not exist, or just exist in given object.
     *
     * @param object         Object
     * @param attributeValue AV to check for
     * @return true/false depending on uniqueness of AV
     */
    <T extends IdentifiableObject> boolean isAttributeValueUnique(T object,
        AttributeValue attributeValue);
}
