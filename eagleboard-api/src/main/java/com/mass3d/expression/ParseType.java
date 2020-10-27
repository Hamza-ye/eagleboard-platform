package com.mass3d.expression;

import static com.mass3d.analytics.DataType.*;
import static com.mass3d.analytics.DataType.BOOLEAN;
import static com.mass3d.analytics.DataType.NUMERIC;

import com.mass3d.analytics.DataType;

/**
 * Type of expression that can be parsed by the Expression Service
 *
 */
public enum ParseType
{
    INDICATOR_EXPRESSION( NUMERIC ),
    VALIDATION_RULE_EXPRESSION( NUMERIC ),
    PREDICTOR_EXPRESSION( NUMERIC ),
    PREDICTOR_SKIP_TEST( BOOLEAN ),
    SIMPLE_TEST( BOOLEAN );

    private DataType dataType;

    ParseType( DataType dataType )
    {
        this.dataType = dataType;
    }

    public DataType getDataType()
    {
        return dataType;
    }
}
