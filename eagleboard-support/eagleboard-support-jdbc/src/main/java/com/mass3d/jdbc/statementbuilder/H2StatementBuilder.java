package com.mass3d.jdbc.statementbuilder;

public class H2StatementBuilder
    extends AbstractStatementBuilder
{
    @Override
    public String getDoubleColumnType()
    {
        return "double";
    }

    @Override
    public String getColumnQuote()
    {
        return "\"";
    }

    @Override
    public String getVacuum( String table )
    {
        return null;
    }

    @Override
    public String getAnalyze( String table )
    {
        return null;
    }

    @Override
    public String getTableOptions( boolean autoVacuum )
    {
        return "";
    }

    @Override
    public String getRegexpMatch()
    {
        return "regexp";
    }

    @Override
    public String getRegexpWordStart() //TODO test
    {
        return "[[:<:]]";
    }

    @Override
    public String getRegexpWordEnd()
    {
        return "[[:>:]]";
    }

    @Override
    public String position( String substring, String string )
    {
        return ("POSITION(" + substring + ", " + string + ")");
    }

    @Override
    public String getRandom( int n )
    {
        return "cast(trunc(" + n + "*random(),0) as int)";
    }

    @Override
    public String getCharAt( String str, String n )
    {
        return "substring(" + str + "," + n + ",1)";
    }

    @Override
    public String getAddDate( String dateField, int days )
    {
        return "DATEADD('DAY'," + days + "," + dateField + ")";
    }

    @Override
    public String getDaysBetweenDates( String fromColumn, String toColumn )
    {
        return ("DATEDIFF('DAY', " + toColumn + ", " + fromColumn + ")");
    }

    @Override
    public String getNumberOfColumnsInPrimaryKey( String table )
    {
        return "select 0 as c"; //TODO fix
    }
}
