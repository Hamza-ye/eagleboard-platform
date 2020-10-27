package com.mass3d.query;

import com.google.common.base.MoreObjects;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Simple class for caching of object type. Mainly for usage in speeding up Operator type lookup.
 *
 */
public class Type
{
    private boolean isString;

    private boolean isChar;

    private boolean isByte;

    private boolean isNumber;

    private boolean isInteger;

    private boolean isFloat;

    private boolean isDouble;

    private boolean isBoolean;

    private boolean isEnum;

    private boolean isDate;

    private boolean isCollection;

    private boolean isList;

    private boolean isSet;

    private boolean isNull;

    public Type( Object object )
    {
        isNull = object == null;
        isString = String.class.isInstance( object );
        isChar = Character.class.isInstance( object );
        isByte = Byte.class.isInstance( object );
        isNumber = Number.class.isInstance( object );
        isInteger = Integer.class.isInstance( object );
        isFloat = Float.class.isInstance( object );
        isDouble = Double.class.isInstance( object );
        isBoolean = Boolean.class.isInstance( object );
        isEnum = Enum.class.isInstance( object );
        isDate = Date.class.isInstance( object );
        isCollection = Collection.class.isInstance( object );
        isList = List.class.isInstance( object );
        isSet = Set.class.isInstance( object );
    }

    public boolean isNull()
    {
        return isNull;
    }

    public boolean isString()
    {
        return isString;
    }

    public boolean isChar()
    {
        return isChar;
    }

    public boolean isByte()
    {
        return isByte;
    }

    public boolean isNumber()
    {
        return isNumber;
    }

    public boolean isInteger()
    {
        return isInteger;
    }

    public boolean isFloat()
    {
        return isFloat;
    }

    public boolean isDouble()
    {
        return isDouble;
    }

    public boolean isBoolean()
    {
        return isBoolean;
    }

    public boolean isEnum()
    {
        return isEnum;
    }

    public boolean isDate()
    {
        return isDate;
    }

    public boolean isCollection()
    {
        return isCollection;
    }

    public boolean isList()
    {
        return isList;
    }

    public boolean isSet()
    {
        return isSet;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "isString", isString )
            .add( "isChar", isChar )
            .add( "isByte", isByte )
            .add( "isNumber", isNumber )
            .add( "isInteger", isInteger )
            .add( "isFloat", isFloat )
            .add( "isDouble", isDouble )
            .add( "isBoolean", isBoolean )
            .add( "isEnum", isEnum )
            .add( "isDate", isDate )
            .add( "isCollection", isCollection )
            .add( "isList", isList )
            .add( "isSet", isSet )
            .add( "isNull", isNull )
            .toString();
    }
}
