package com.mass3d.common;

import com.mass3d.hibernate.EnumUserType;

public class DataDimensionTypeUserType
    extends EnumUserType<DataDimensionType>
{
    public DataDimensionTypeUserType()
    {
        super( DataDimensionType.class );
    }
}
