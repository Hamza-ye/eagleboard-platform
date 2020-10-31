package com.mass3d.attribute.exception;

import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.attribute.Attribute;

public class MissingMandatoryAttributeValueException extends Exception
{
    private final List<Attribute> attributes;

    public MissingMandatoryAttributeValueException( List<Attribute> attributes )
    {
        super( String.valueOf( attributes.stream()
            .map( att -> "Attribute " + att.getDisplayName() + " (" + att.getUid() + ")" )
            .collect( Collectors.toList() ) ) );

        this.attributes = attributes;
    }

    public List<Attribute> getAttributes()
    {
        return attributes;
    }
}
