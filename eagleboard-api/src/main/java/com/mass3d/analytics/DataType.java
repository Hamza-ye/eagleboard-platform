package com.mass3d.analytics;

import com.mass3d.common.ValueType;

public enum DataType
{
    NUMERIC, BOOLEAN, TEXT;
    
    public static DataType fromValueType( ValueType valueType )
    {
        if ( ValueType.NUMERIC_TYPES.contains( valueType ) )
        {
            return DataType.NUMERIC;
        }
        else if ( ValueType.BOOLEAN_TYPES.contains( valueType ) )
        {
            return DataType.BOOLEAN;
        }
        else
        {        
            return DataType.TEXT;
        }
    }
}
