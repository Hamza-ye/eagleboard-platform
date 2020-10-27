package com.mass3d.dxf2.csv;

public class CsvImportOptions
{
    private CsvImportClass importClass;

    private boolean firstRowIsHeader;

    //---------------------------------------------------
    //  Constructor
    //---------------------------------------------------

    public CsvImportOptions()
    {
    }

    public CsvImportOptions( CsvImportClass importClass, boolean firstRowIsHeader )
    {
        this.importClass = importClass;
        this.firstRowIsHeader = firstRowIsHeader;
    }

    //---------------------------------------------------
    //  Getter & Setter
    //---------------------------------------------------

    public CsvImportClass getImportClass()
    {
        return importClass;
    }

    public CsvImportOptions setImportClass( CsvImportClass importClass )
    {
        this.importClass = importClass;
        return this;
    }

    public CsvImportOptions setFirstRowIsHeader( boolean firstRowIsHeader )
    {
        this.firstRowIsHeader = firstRowIsHeader;
        return this;
    }

    public boolean isFirstRowIsHeader()
    {
        return firstRowIsHeader;
    }
}
