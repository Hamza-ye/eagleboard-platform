package com.mass3d.dxf2.metadata.merge;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Date;

public class Simple
{
    private String string;

    private Integer integer;

    private Date date;

    private Boolean bool;

    private int anInt;

    private float aFloat;

    public Simple()
    {
    }

    public Simple( String string, Integer integer, Date date, Boolean bool, int anInt, float aFloat )
    {
        this.string = string;
        this.integer = integer;
        this.date = date;
        this.bool = bool;
        this.anInt = anInt;
        this.aFloat = aFloat;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getString()
    {
        return string;
    }

    public void setString( String string )
    {
        this.string = string;
    }

    @JsonProperty
    @JacksonXmlProperty
    public Integer getInteger()
    {
        return integer;
    }

    public void setInteger( Integer integer )
    {
        this.integer = integer;
    }

    @JsonProperty
    @JacksonXmlProperty
    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    @JsonProperty
    @JacksonXmlProperty
    public Boolean getBool()
    {
        return bool;
    }

    public void setBool( Boolean bool )
    {
        this.bool = bool;
    }

    @JsonProperty
    @JacksonXmlProperty
    public int getAnInt()
    {
        return anInt;
    }

    public void setAnInt( int anInt )
    {
        this.anInt = anInt;
    }

    @JsonProperty
    @JacksonXmlProperty
    public float getaFloat()
    {
        return aFloat;
    }

    public void setaFloat( float aFloat )
    {
        this.aFloat = aFloat;
    }
}
