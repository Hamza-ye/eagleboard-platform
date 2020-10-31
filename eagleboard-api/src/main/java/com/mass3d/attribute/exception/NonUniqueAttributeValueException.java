package com.mass3d.attribute.exception;

import com.mass3d.attribute.AttributeValue;

public class NonUniqueAttributeValueException extends RuntimeException
{
    public NonUniqueAttributeValueException( AttributeValue attributeValue )
    {
        super( "Value " + attributeValue.getValue() + " already exists for attribute "
             + "(" + attributeValue.getAttribute() + ")" );
    }

    public NonUniqueAttributeValueException( AttributeValue attributeValue, String value )
    {
        super( "Value " + value + " already exists for attribute "
            + "(" + attributeValue.getAttribute() + ")" );
    }
}
