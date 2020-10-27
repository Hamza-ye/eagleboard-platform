package com.mass3d.hibernate.dialect;

import java.sql.Types;
import org.hibernate.dialect.H2Dialect;

public class DhisH2Dialect extends H2Dialect
{
    public DhisH2Dialect()
    {
        registerColumnType( Types.JAVA_OBJECT, "text" );
        registerColumnType( Types.JAVA_OBJECT, "jsonb" );
        registerColumnType( Types.OTHER, "uuid" );
    }

    @Override
    public String getDropSequenceString( String sequenceName )
    {
        // Adding the "if exists" clause to avoid warnings
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public boolean dropConstraints()
    {
        // No need to drop constraints before dropping tables, leads to error
        // messages
        return false;
    }
}
