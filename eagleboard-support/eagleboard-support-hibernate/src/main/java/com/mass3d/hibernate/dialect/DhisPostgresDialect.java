package com.mass3d.hibernate.dialect;

import java.sql.Types;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect;
import org.hibernate.type.StandardBasicTypes;

public class DhisPostgresDialect
    extends PostgisPG95Dialect
{
    public DhisPostgresDialect()
    {
        registerColumnType( Types.JAVA_OBJECT, "jsonb" );
        registerHibernateType( Types.OTHER, "pg-uuid" );
        registerFunction( "jsonb_extract_path", new StandardSQLFunction( "jsonb_extract_path", StandardBasicTypes.STRING ) );
        registerFunction( "jsonb_extract_path_text", new StandardSQLFunction( "jsonb_extract_path_text", StandardBasicTypes.STRING ) );
    }
}