package com.mass3d.common;

public enum DimensionType
{
    DATA_X( 0 ),
    PROGRAM_DATA_ELEMENT,
    PROGRAM_ATTRIBUTE,
    PROGRAM_INDICATOR,
    DATA_COLLAPSED,
    CATEGORY_OPTION_COMBO( 1 ),
    ATTRIBUTE_OPTION_COMBO( 2 ),
    PERIOD,
    ORGANISATION_UNIT,
    CATEGORY_OPTION_GROUP_SET,
    DATA_ELEMENT_GROUP_SET,
    ORGANISATION_UNIT_GROUP_SET,
    ORGANISATION_UNIT_GROUP,
    CATEGORY,
    OPTION_GROUP_SET,
    VALIDATION_RULE,
    STATIC,
    ORGANISATION_UNIT_LEVEL;
    
    private static final int LAST_ORDER = 999;
    
    private int order = LAST_ORDER;
    
    DimensionType()
    {
    }
    
    DimensionType( int order )
    {
        this.order = order;
    }
    
    public int getOrder()
    {
        return order;
    }
}
