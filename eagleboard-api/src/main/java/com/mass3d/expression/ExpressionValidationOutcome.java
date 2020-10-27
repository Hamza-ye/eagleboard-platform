package com.mass3d.expression;

public enum ExpressionValidationOutcome
{
    VALID( "valid" ), 
    EXPRESSION_IS_EMPTY( "expression_is_empty"),
    DIMENSIONAL_ITEM_OBJECT_DOES_NOT_EXIST( "dimensional_item_object_does_not_exist" ),
    CONSTANT_DOES_NOT_EXIST( "constant_does_not_exist"),
    ORG_UNIT_GROUP_DOES_NOT_EXIST( "org_unit_group_does_not_exist"),
    EXPRESSION_IS_NOT_WELL_FORMED( "expression_is_not_well_formed");
    
    private final String key;
    
    ExpressionValidationOutcome( String key )
    {
        this.key = key;
    }
    
    public boolean isValid()
    {
        return this == VALID;
    }
    
    public String getKey()
    {
        return key;
    }
}
