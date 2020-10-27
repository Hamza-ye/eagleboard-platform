package com.mass3d.system.util;

import com.google.common.collect.Sets;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import org.springframework.util.Assert;

/**
 * Utilities for SQL operations, compatible with PostgreSQL
 * and H2 database platforms.
 *
 */
public class SqlUtils
{
    public static final String QUOTE = "\"";
    public static final String SEPARATOR = ".";
    public static final String OPTION_SEP = ".";

    /**
     * Quotes the given relation (typically a column). Quotes part of
     * the given relation are encoded (replaced by double quotes that is).
     *
     * @param relation the relation (typically a column).
     * @return the quoted relation.
     */
    public static String quote( String relation )
    {
        String rel = relation.replaceAll( QUOTE, ( QUOTE + QUOTE ) );

        return QUOTE + rel + QUOTE;
    }

    /**
     * Quotes and qualifies the given relation (typically a column). Quotes part
     * of the given relation are encoded (replaced by double quotes that is). The
     * column name is qualified by the given alias.
     *
     * @param relation the relation (typically a column).
     * @param alias the alias.
     * @return the quoted relation.
     */
    public static String quote( String alias, String relation )
    {
        Assert.notNull( alias, "Alias must be specified" );

        return alias + SEPARATOR + quote( relation );
    }

    /**
     * Returns a string set for the given result set and column. Assumes
     * that the SQL type is an array of text values.
     *
     * @param rs the result set.
     * @param columnLabel the column label.
     * @return a string set.
     */
    public static Set<String> getArrayAsSet( ResultSet rs, String columnLabel )
        throws SQLException
    {
        Array sqlArray = rs.getArray( columnLabel );
        String[] array = (String[]) sqlArray.getArray();
        return Sets.newHashSet( array );
    }

    /**
     * Cast the given value to numeric (CAST(X) AS NUMERIC)
     * @param value a value
     * @return a string with the numeric cast statement
     */
    public static String castToNumber( String value )
    {
        return "CAST (" + value + ") AS NUMERIC";
    }

    public static String lower( String value )
    {
        return "lower(" + value + ")";
    }


}
