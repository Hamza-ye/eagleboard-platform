package com.mass3d.expression;

import com.mass3d.hibernate.EnumUserType;

public class OperatorUserType
    extends EnumUserType<Operator>
{
    public OperatorUserType()
    {
        super( Operator.class );
    }
}