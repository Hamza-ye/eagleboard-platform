package com.mass3d.dxf2.csv;

public enum CsvImportClass
{
    ORGANISATION_UNIT_GROUP_MEMBERSHIP,
    DATA_ELEMENT_GROUP_MEMBERSHIP,
    INDICATOR_GROUP_MEMBERSHIP,
    DATA_ELEMENT,
    DATA_ELEMENT_GROUP,
    CATEGORY_OPTION,
    CATEGORY,
    CATEGORY_COMBO,
    CATEGORY_OPTION_GROUP,
    ORGANISATION_UNIT,
    ORGANISATION_UNIT_GROUP,
    VALIDATION_RULE,
    OPTION_SET,
    OPTION_GROUP,
    OPTION_GROUP_SET,
    OPTION_GROUP_SET_MEMBERSHIP;

    public static boolean classExists( String classKey )
    {
        try
        {
            valueOf( classKey );
        }
        catch ( IllegalArgumentException e )
        {
            return false;
        }

        return true;
    }
}
